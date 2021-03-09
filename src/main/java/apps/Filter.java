package apps;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;

import kafka.config.ConfigLoader;

public class Filter {

	public static void main(String[] args) throws IOException {

		final Properties props = ConfigLoader.getInstance()
				.loadConfig();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-filter");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()
				.getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()
				.getClass());

		final StreamsBuilder builder = new StreamsBuilder();

		builder.stream("mrl-test")
				.filter((key, value) -> StringUtils.contains(value.toString(), "NASTRIPES"))
				.to("dsna-test-filter-output");

		final Topology topology = builder.build();
		System.out.println(topology.describe());
		
		final KafkaStreams streams = new KafkaStreams(topology, props);

		// add a shutdown hook with a countdown latch to capture a user interrupt and close the client upon terminating this program
		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime()
				.addShutdownHook(new Thread("streams-shutdown-hook") {
					@Override
					public void run() {
						streams.close();
						latch.countDown();
					}
				});

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			System.exit(1);
		}
		System.exit(0);

	}

}
