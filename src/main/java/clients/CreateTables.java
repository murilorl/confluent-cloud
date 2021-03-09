package clients;

public class CreateTables {

	private void run() {

		// Attach shutdown handler to catch Control-C.
		Runtime.getRuntime().addShutdownHook(new Thread("create-tables-shutdown-hook") {

		});
	}

	public static void main(String[] args) {
		new CreateTables().run();
	}

}
