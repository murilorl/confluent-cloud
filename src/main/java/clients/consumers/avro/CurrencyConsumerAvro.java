package clients.consumers.avro;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import kafka.config.ConfigLoader;
import model.core.Currency;

/**
 * 
 * @author Murilo
 *
 */
public class CurrencyConsumerAvro {

	private final static String TOPIC = "currency";

	public static void main(final String[] args) throws Exception {

		final Properties props = ConfigLoader.getInstance().loadConfig();

		// Add additional properties.
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-avro-1");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		final Consumer<String, Currency> consumer = new KafkaConsumer<String, Currency>(props);
		consumer.subscribe(Arrays.asList(TOPIC));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

		try {
			while (true) {
				ConsumerRecords<String, Currency> records = consumer.poll(Duration.ofMillis(100));

				for (ConsumerRecord<String, Currency> record : records) {
					String key = record.key();
					Currency currency = record.value();

					if (key == null)
						System.out.printf(
								"Consumed record with no key defined and value %s; published to broker on %s%n",
								currency.toString(), dateFormat.format(new Date(currency.getValidUntil())));
					else
						System.out.printf("Consumed record with key %s and value %s; published to broker on %s%n", key,
								currency.toString(), dateFormat.format(new Date(currency.getValidUntil())));
				}
			}
		} finally {
			consumer.close();
		}
	}
}