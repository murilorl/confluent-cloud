package clients.consumers.avro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import kafka.config.ConfigLoader;
import model.core.Currency;

/**
 * 
 * @author Murilo
 *
 */
public class CurrencyAvroStream {

	private final static String TOPIC = "currency";

	public static void main(String[] args) throws IOException {

		final Properties props = ConfigLoader.getInstance().loadConfig();

		// Add additional properties.
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-streams-avro-1");
		// Disable caching to print the aggregation value after each record
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		final Serde<Currency> dataRecordAvroSerde = new SpecificAvroSerde<>();
		final boolean isKeySerde = false;
		Map<String, Object> SRconfig = new HashMap<>();

		SRconfig.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
				props.getProperty(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG));
		SRconfig.put("schema.registry.basic.auth.user.info", props.getProperty("schema.registry.basic.auth.user.info"));
		SRconfig.put("basic.auth.credentials.source", props.getProperty("basic.auth.credentials.source"));
		dataRecordAvroSerde.configure(SRconfig, isKeySerde);

		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, Currency> records = builder.stream(TOPIC,
				Consumed.with(Serdes.String(), dataRecordAvroSerde));

		// Keep only the currencies that contains 'a' or 'e' (case insensitive) in the
		// currency key
		KStream<String, Currency> filteredCurrencies = records.filter((key, value) -> {
			return value.getCurrencyKey().matches("(?i).*[ae].*");
		});
		filteredCurrencies.print(Printed.<String, Currency>toSysOut().withLabel("Currency with 'a' or 'e' in the key"));
		filteredCurrencies.to("filtered_currency");

		KafkaStreams streams = new KafkaStreams(builder.build(), props);
		streams.start();

		// Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}

}
