package clients.producers.avro;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import kafka.config.ConfigLoader;
import model.core.Currency;

/**
 * 
 * @author Murilo Lima
 *
 */
public class CurrencyProducerAvro {

	private final static String TOPIC = "currency";

	public static void main(final String[] args) throws IOException {

		final Properties props = ConfigLoader.getInstance().loadConfig();

		// Add additional properties.
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

		Producer<String, Currency> producer = new KafkaProducer<String, Currency>(props);

		// Produce sample data
		final Long numMessages = 10L;
		for (Long i = 0L; i < numMessages; i++) {

			Currency currency = new Currency();
			currency.setSid(RandomStringUtils.randomAlphabetic(3));
			currency.setCurrencyKey(RandomStringUtils.randomAlphabetic(3));
			currency.setIsoCurrencyCode(RandomStringUtils.randomNumeric(4));
			currency.setValidUntil(System.currentTimeMillis());
			
			System.out.printf("Producing record: %s%n", currency.toString());
			
			// If a message key is necessary, use send(new ProducerRecord<String, Currency>(TOPIC, key, currency)
			producer.send(new ProducerRecord<String, Currency>(TOPIC, currency), new Callback() {
				@Override
				public void onCompletion(RecordMetadata m, Exception e) {
					if (e != null) {
						e.printStackTrace();
					} else {
						System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(),
								m.partition(), m.offset());
					}
				}
			});
		}

		producer.flush();

		System.out.printf("10 messages were produced to topic %s%n", TOPIC);

		producer.close();

	}

}
