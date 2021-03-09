package clients.producers;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import kafka.config.ConfigLoader;
import model.vendor.Vendor;
import model.vendor.VendorCompany;
import util.JsonUtil;

public class VendorProducer {

	private static final String[] EVENT_TYPE = StringUtils.split("New,Change,Deletion", ',');

	private static final String TOPIC_VENDOR = "tp-lfa1";
	private static final String TOPIC_VENDOR_COMPANY = "tp-lfb1";

	Logger logger = Logger.getLogger(VendorProducer.class);

	public static void main(String[] args) {

		int numberOfMessages = 1;
		int numberOfCompanies = 1;
		String sourceSystemId;
		String sourceSystemName;

		if (args.length > 3) {
			if (!StringUtils.isNumeric(args[0]))
				throw new NumberFormatException(
						String.format("Number of messages parameter [%s] is not a valid number", args[0]));
			else
				numberOfMessages = Integer.valueOf(args[0]);

			if (StringUtils.isEmpty(args[1]))
				throw new IllegalArgumentException("Parameter #2 (Number of Companies) is required.");
			else
				numberOfCompanies = Integer.valueOf(args[1]);

			if (StringUtils.isEmpty(args[2]))
				throw new IllegalArgumentException("Parameter #2 (SourceSystemId) is required.");
			else
				sourceSystemId = args[2];

			if (StringUtils.isEmpty(args[3]))
				throw new IllegalArgumentException("Parameter #3 (SourceSystemName) is required.");
			else
				sourceSystemName = args[3];

		} else
			throw new IllegalArgumentException("Not enough arguments provided.");

		new VendorProducer().run(numberOfMessages, numberOfCompanies, sourceSystemId, sourceSystemName);

	}

	private void run(int numberOfMessages, int numberOfCompanies, String sourceSystemId, String sourceSystemName) {

		try {

			final Properties props = ConfigLoader.getInstance().loadConfig();

			// Add additional properties.
			props.put(ProducerConfig.ACKS_CONFIG, "all");
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
					"org.apache.kafka.common.serialization.StringSerializer");
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
					"org.apache.kafka.common.serialization.StringSerializer");
			props.put("client.id", sourceSystemName);

			Producer<String, String> producer = new KafkaProducer<>(props);

			// Produce sample data

			for (int i = 0; i < numberOfMessages; i++) {

				Vendor vendor = new Vendor();

				vendor.setSourceSystem(sourceSystemName);
				vendor.setSourceSystemId(sourceSystemId);
				vendor.setAccountNumber(RandomStringUtils.randomNumeric(10));
				vendor.setIndustryKey(RandomStringUtils.randomNumeric(4));
				vendor.setCreatedDate(new Date());
				vendor.setVendorAccountGroup(RandomStringUtils.randomNumeric(4));
				vendor.setName1(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 36)));

				String jsonString = JsonUtil.toJsonString(vendor);
				logger.info(String.format("New %s sample object created. Object:\r\n%s\n", Vendor.class.getName(),
						jsonString));

				producer.send(new ProducerRecord<String, String>(TOPIC_VENDOR,
						vendor.getSourceSystem() + vendor.getAccountNumber(), jsonString), new Callback() {

							@Override
							public void onCompletion(RecordMetadata m, Exception e) {
								if (e != null) {
									logger.error(e);
								} else {
									logger.info(
											String.format("Published record to topic %s partition [%d] @ offset %d%n",
													m.topic(), m.partition(), m.offset()));
									// System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n",
									// m.topic(), m.partition(), m.offset());
								}
							}
						});

				VendorCompany vendorCompany = null;
				for (int j = 0; j < numberOfCompanies; j++) {
					vendorCompany = new VendorCompany();
					vendorCompany.setSourceSystem(sourceSystemName);
					vendorCompany.setSourceSystemId(sourceSystemId);
					vendorCompany.setAccountNumber(vendor.getAccountNumber());
					vendorCompany.setCompanyCode(RandomStringUtils.randomNumeric(4));
					vendorCompany.setPostingBlock("");
					vendorCompany.setCreatedDate(new Date());
					vendorCompany.setPersonnelNumber(RandomStringUtils.randomNumeric(8));

					jsonString = JsonUtil.toJsonString(vendorCompany);
					logger.info(String.format("New %s sample object created. Object:\r\n%s\n",
							VendorCompany.class.getName(), jsonString));

					producer.send(
							new ProducerRecord<String, String>(TOPIC_VENDOR_COMPANY, vendor.getSourceSystem()
									+ vendor.getAccountNumber() + vendorCompany.getCompanyCode(), jsonString),
							new Callback() {

								@Override
								public void onCompletion(RecordMetadata m, Exception e) {
									if (e != null) {
										logger.error(e);
									} else {
										logger.info(String.format(
												"Published record to topic %s partition [%d] @ offset %d%n", m.topic(),
												m.partition(), m.offset()));
										// System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n",
										// m.topic(), m.partition(), m.offset());
									}
								}
							});

					// Wait time between producing new messages.
					if (numberOfCompanies > 1 && i != numberOfCompanies) {
						try {
							Thread.sleep(ThreadLocalRandom.current().nextInt(2, 5) * 1000L);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}

				// Wait time between producing new messages.
				if (numberOfMessages > 1 && i != numberOfMessages) {
					try {
						Thread.sleep(ThreadLocalRandom.current().nextInt(2, 5) * 1000L);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

			}

			producer.close();

		} catch (IOException e) {

			logger.error(e);

		}

	}

}