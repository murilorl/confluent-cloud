package apps;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import kafka.config.ConfigLoader;

public class Pipe {

	public static void main(String[] args) throws IOException {

		final Properties props = ConfigLoader.getInstance().loadConfig();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		final StreamsBuilder builder = new StreamsBuilder();

		// create a source stream from a Kafka topic
		KStream<String, String> source = builder.stream("mrl-test");

		// stream is to write it into another Kafka topic
		source.to("dsna-test-pipe-output");

		// inspect what kind of topology is created from this builder
		final Topology topology = builder.build();

		// print its description to standard output as:
		System.out.println(topology.describe());

		// construct the Streams client with the two components we have just constructed above
		final KafkaStreams streams = new KafkaStreams(topology, props);

		// add a shutdown hook with a countdown latch to capture a user interrupt and close the client upon terminating this program
		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
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
