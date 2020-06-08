package clients.producers;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import kafka.config.ConfigLoader;
import model.material.Material;
import util.JsonUtil;

public class MaterialProducer {

	private static final String[] EVENT_TYPE = StringUtils.split("New,Change,Deletion", ',');

	private static final String TOPIC = "materials";

	Logger logger = Logger.getLogger(MaterialProducer.class);

	public static void main(String[] args) {

		int numberOfMessages = 1;
		String sourceSystemId;
		String sourceSystemName;

		if (args.length > 2) {
			if (!StringUtils.isNumeric(args[0]))
				throw new NumberFormatException(
						String.format("Number of messages parameter [%s] is not a valid number", args[0]));
			else
				numberOfMessages = Integer.valueOf(args[0]);

			if (StringUtils.isEmpty(args[1]))
				throw new IllegalArgumentException("Parameter #2 (SourceSystemId) is required.");
			else
				sourceSystemId = args[1];

			if (StringUtils.isEmpty(args[2]))
				throw new IllegalArgumentException("Parameter #3 (SourceSystemName) is required.");
			else
				sourceSystemName = args[2];

		} else
			throw new IllegalArgumentException("Not enough arguments provided.");

		new MaterialProducer().run(numberOfMessages, sourceSystemId, sourceSystemName);

	}

	private void run(int numberOfMessages, String sourceSystemId, String sourceSystemName) {

		try {

			final Properties props = ConfigLoader.getInstance().loadConfig();

			// Add additional properties.

			props.put(ProducerConfig.ACKS_CONFIG, "all");

			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,

					"org.apache.kafka.common.serialization.StringSerializer");

			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,

					"org.apache.kafka.common.serialization.StringSerializer");

			Producer<String, String> producer = new KafkaProducer<>(props);

			// Produce sample data

			for (int i = 0; i < numberOfMessages; i++) {

				Material material = new Material();

				material.setId(RandomStringUtils.randomNumeric(8));
				material.setSourceSystemId(sourceSystemId);
				material.setSourceSystemName(sourceSystemName);
				material.setMaterialNumber(RandomStringUtils.randomNumeric(4));

				material.setChangedBy(RandomStringUtils.randomAlphabetic(6));

				material.setIndustrySector("M");

				material.setMaterialType("HIBE");

				material.setEventType(EVENT_TYPE[ThreadLocalRandom.current().nextInt(EVENT_TYPE.length)]);

				String jsonString = JsonUtil.toJsonString(material);

				logger.info(String.format("New %s sample object created. Object:\r\n%s\n", Material.class.getName(),

						jsonString));

				producer.send(new ProducerRecord<String, String>(TOPIC, jsonString), new Callback() {

					@Override

					public void onCompletion(RecordMetadata m, Exception e) {

						if (e != null) {

							logger.error(e);

						} else {

							logger.info(String.format("Published record to topic %s partition [%d] @ offset %d%n",

									m.topic(), m.partition(), m.offset()));

							// System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n",

							// m.topic(), m.partition(), m.offset());

						}

					}

				});

				// Wait time between producing new messages.

				if (numberOfMessages > 1 && i != numberOfMessages) {

					try {

						Thread.sleep(ThreadLocalRandom.current().nextInt(2, 5) * 1000L);

					} catch (InterruptedException e1) {

						e1.printStackTrace();

					}

				}

			}

			// producer.flush();

			producer.close();

		} catch (IOException e) {

			logger.error(e);

		}

	}

}