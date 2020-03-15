# Overview
Produces messages to Kafka cluster (Confluent Cloud) using the Java Producer and Consumer.

# Prerequisites

* Java 1.8 or higher to run the demo application
* Maven to compile the demo application
* Create a local file (e.g. at `$HOME/.confluent/java.config`) with configuration parameters to connect to your Kafka cluster, which can be on your local host, [Confluent Cloud](https://www.confluent.io/confluent-cloud/?utm_source=github&utm_medium=demo&utm_campaign=ch.examples_type.community_content.clients-ccloud), or any other cluster.  Follow [these detailed instructions](https://github.com/confluentinc/configuration-templates/tree/master/README.md) to properly create this file. 
* If you are running on Confluent Cloud, you must have access to a [Confluent Cloud](https://www.confluent.io/confluent-cloud/?utm_source=github&utm_medium=demo&utm_campaign=ch.examples_type.community_content.clients-ccloud) cluster

## Example 1: Currency Avro producer!
In this example, the producer writes Kafka data to a topic (`currency`) in Confluent Cloud.

### Prerequisites
* A topic called currency with a schema Avro that matches the one on /src/main/resources/avro/schemas/Currency.avsc

1. Run the producer? 

	```shell
	# Compile the Java code
	$ mvn clean package
	
	# Run the producer
	$ mvn exec:java -Dexec.mainClass="clients.producers.avro.CurrencyProducerAvro"
	```	
	
## Example 2: Currency Avro consumer!

### Prerequisites
* A topic called currency with a schema Avro that matches the one on /src/main/resources/avro/schemas/Currency.avsc	

1. Run the consumer?
 
	```shell
	# Compile the Java code
	$ mvn clean package
	
	# Run the producer
	$ mvn exec:java -Dexec.mainClass="clients.consumers.avro.CurrencyConsumerAvro"
	```	
	
Note: it take's some iterations for the consumer to start receiving records from the cluster. Initial polls return nothing .	