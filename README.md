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
	
## Example 1b: Material producer!
In this example, the producer will generate *n* material objects and write them to a topic (`material`) in Confluent Cloud.

### Prerequisites
* A topic called `material`

1. Run the producer? 

	```shell
	# Run the producer. Arguments:
		0 - Number of messages
		1 - System Id
		2 - System Name
	$ mvn exec:java -Dexec.mainClass="clients.producers.MaterialProducer" -Dexec.args="1 F2P CARS"
	```		
	
## Example 2: Currency Avro consumer!

### Prerequisites
* A topic called `currency` with a schema Avro that matches the one on /src/main/resources/avro/schemas/Currency.avsc	

1. Run the consumer?
 
	```shell
	# Compile the Java code
	$ mvn clean package
	
	# Run the producer
	$ mvn exec:java -Dexec.mainClass="clients.consumers.avro.CurrencyConsumerAvro"
	```	
	
Note: it take's some iterations for the consumer to start receiving records from the cluster. Initial polls return nothing.


## Example 3: Currency Avro stream!
This Kafka Streams application will process new messages published to topic `currency` keeping only the ones that contains 'a' or 'e' (case insensitive) in the field `CurrencyKey`. The filtered messages will be published to topic called `filtered_currency`.

### Prerequisites
* A topic called `currency` with a schema Avro that matches the one on /src/main/resources/avro/schemas/Currency.avsc	
* A topic called `filtered_currency` with a schema Avro that matches the one on /src/main/resources/avro/schemas/Currency.avsc

1. Run the stream app?
 
	```shell
	# Compile the Java code
	$ mvn clean package
	
	# Run the producer
	$ mvn exec:java -Dexec.mainClass="clients.consumers.avro.CurrencyAvroStream"
	```	
	
You should see log lines in the console similar to the ones below:

	```shell
	...
	[Currency with 'a' or 'e' in the key]: null, {"Sid": "wTK", "CurrencyKey": "zXa", "IsoCurrencyCode": "7850", "ValidUntil": 1584310163686}
	[Currency with 'a' or 'e' in the key]: null, {"Sid": "MGt", "CurrencyKey": "daH", "IsoCurrencyCode": "9733", "ValidUntil": 1584310166671}
	[Currency with 'a' or 'e' in the key]: null, {"Sid": "JYK", "CurrencyKey": "nAp", "IsoCurrencyCode": "6936", "ValidUntil": 1584310166682}
	[Currency with 'a' or 'e' in the key]: null, {"Sid": "Pfc", "CurrencyKey": "OAk", "IsoCurrencyCode": "6435", "ValidUntil": 1584310166687}
	```	
	
When you are done, press `<ctrl>-c`.