package clients.producers;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import util.ProducerUtils;
import util.SapSystemsEnum;

public class VendorProducerEnrichedMain {

	Logger logger = Logger.getLogger(VendorProducerEnrichedMain.class);

	public static void main(String[] args) {

		long numberOfMessages = 1;
		int numberOfCompanies = 1;

		if (args.length > 1) {
			if (!StringUtils.isNumeric(args[0]))
				throw new NumberFormatException(
						String.format("Number of messages parameter [%s] is not a valid number", args[0]));
			else
				numberOfMessages = Long.valueOf(args[0]);

			if (StringUtils.isEmpty(args[1]))
				throw new IllegalArgumentException("Parameter #2 (Number of Companies) is required.");
			else
				numberOfCompanies = Integer.valueOf(args[1]);

		} else
			throw new IllegalArgumentException("Not enough arguments provided.");

		int numOfProducers = ProducerUtils.getNumberOfProducers(numberOfMessages);
		long messagesPerProducer = numberOfMessages / numOfProducers;

		for (int i = 1; i <= numOfProducers ; i++) {

			if (i == numOfProducers) {
				messagesPerProducer = messagesPerProducer
						+ (numberOfMessages - (messagesPerProducer * numOfProducers));
			}

			SapSystemsEnum[] sapSystems = SapSystemsEnum.values();
			SapSystemsEnum sapSystem = sapSystems[RandomUtils.nextInt(0, sapSystems.length)];

			VendorProducerEnriched vendorProducerEnriched = new VendorProducerEnriched(messagesPerProducer,
					numberOfCompanies, sapSystem.name(), sapSystem.getSystemId());
			try {
				vendorProducerEnriched.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}