package util;

public class ProducerUtils {

	public static int getNumberOfProducers(long numberOfMessages) {

		if (numberOfMessages <= 10)
			return 1;
		else if (numberOfMessages >= 10 && numberOfMessages < 100)
			return 2;
		else if (numberOfMessages >= 100 && numberOfMessages < 1000)
			return 4;
		else if (numberOfMessages >= 1000 && numberOfMessages < 10000)
			return 6;
		else if (numberOfMessages >= 10000 && numberOfMessages < 100000)
			return 8;
		else
			return 10;

	}

}
