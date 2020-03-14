package kafka.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 
 * @author Murilo Lima
 *
 */
public class ConfigLoader {

	private static ConfigLoader instance = null;
	private static Properties config = null;

	private ConfigLoader() {

	}

	public static ConfigLoader getInstance() {
		if (instance == null)
			instance = new ConfigLoader();

		return instance;
	}

	public Properties loadConfig() throws IOException {

		if (config != null)
			return config;

		String configFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\kafka\\cluster\\config.properties";

		if (!Files.exists(Paths.get(configFilePath)))
			throw new IOException(configFilePath + "not found.");

		final Properties cfg = new Properties();
		try (InputStream inputStream = new FileInputStream(configFilePath)) {
			cfg.load(inputStream);
		}
		return cfg;
	}

}
