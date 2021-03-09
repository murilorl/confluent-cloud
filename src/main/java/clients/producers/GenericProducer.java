package clients.producers;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import kafka.config.ConfigLoader;

public class GenericProducer {

	Logger logger = Logger.getLogger(GenericProducer.class);

	@Parameter(names = { "--topic", "-t" })
	private String topic;

	@Parameter(names = { "--key", "-k" })
	private String key;

	@Parameter(names = { "--value", "-v" })
	private String value;

	public static void main(String... args) throws IOException {
		GenericProducer producer = new GenericProducer();
		JCommander.newBuilder().addObject(producer).build().parse(args);
		producer.run();

	}

	private void run() throws IOException {
		final Properties props = ConfigLoader.getInstance().loadConfig();

		// Add additional properties.
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put("client.id", "mrlima-generic-producer");
		props.put("reconnect.backoff.max.ms", 30000);

		Producer<String, String> producer = new KafkaProducer<>(props);
		producer.send(new ProducerRecord<String, String>(topic, key, value), new Callback() {

			@Override
			public void onCompletion(RecordMetadata m, Exception e) {
				if (e != null) {
					logger.error(e);
				} else {
					logger.info(String.format("Published record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset()));
				}
			}
		});

		producer.close();
	}

}
