package jans.common;

import java.util.Properties;
import java.io.FileInputStream;

public final class Config {
	/**
	 * Load configuration information from the specified file. Must be called
	 * 
	 * @param fileName
	 *            the name of the file containing the configuration to use.
	 */
	public static void load(String fileName) {
		configFile = fileName;

		try {
			props.load(new FileInputStream(fileName));
		} catch (Exception e) {
			System.err.println("Load Configuration File " + fileName + " Error!");
			System.exit(1);
		}
	}

	private static void configError(String message) {
		System.err.println("Error in " + configFile + ": " + message);
		System.exit(1);
	}

	/**
	 * Get the value of a key in <tt>tenet.conf</tt>.
	 * 
	 * @param key
	 *            the key to look up.
	 * @return the value of the specified key, or <tt>null</tt> if it is not
	 *         present.
	 */
	public static String getString(String key) {
		return props.getProperty(key);
	}

	/**
	 * Get the value of a key in <tt>tenet.conf</tt>, returning the specified
	 * default if the key does not exist.
	 * 
	 * @param key
	 *            the key to look up.
	 * @param defaultValue
	 *            the value to return if the key does not exist.
	 * @return the value of the specified key, or <tt>defaultValue</tt> if it is
	 *         not present.
	 */
	public static String getString(String key, String defaultValue) {
		String result = getString(key);
		return (result == null) ? defaultValue : result;
	}

	/**
	 * Get the value of an integer key in <tt>tenet.conf</tt>.
	 * 
	 * @param key
	 *            the key to look up.
	 * @return the value of the specified key.
	 */
	public static int getInteger(String key) {
		try {
			return Integer.parseInt(props.getProperty(key));
		} catch (NumberFormatException e) {
			configError("missing int " + key);
			return 0;
		}
	}

	/**
	 * Get the value of an integer key in <tt>tenet.conf</tt>, returning the
	 * specified default if the key does not exist.
	 * 
	 * @param key
	 *            the key to look up.
	 * @param defaultValue
	 *            the value to return if the key does not exist.
	 * @return the value of the specified key, or <tt>defaultValue</tt> if the
	 *         key does not exist.
	 */
	public static int getInteger(String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Get the value of a double key in <tt>tenet.conf</tt>.
	 * 
	 * @param key
	 *            the key to look up.
	 * @return the value of the specified key.
	 */
	public static double getDouble(String key) {
		try {
			return Double.parseDouble(props.getProperty(key));
		} catch (NumberFormatException e) {
			configError("missing double " + key);
			return 0;
		}
	}

	/**
	 * Get the value of a double key in <tt>tenet.conf</tt>, returning the
	 * specified default if the key does not exist.
	 * 
	 * @param key
	 *            the key to look up.
	 * @param defaultValue
	 *            the value to return if the key does not exist.
	 * @return the value of the specified key, or <tt>defaultValue</tt> if the
	 *         key does not exist.
	 */
	public static double getDouble(String key, double defaultValue) {
		try {
			return Double.parseDouble(props.getProperty(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Get the value of a boolean key in <tt>tenet.conf</tt>.
	 * 
	 * @param key
	 *            the key to look up.
	 * @return the value of the specified key.
	 */
	public static boolean getBoolean(String key) {
		String value = props.getProperty(key);

		if ("1".equals(value) || value != null
				&& value.toLowerCase().equals("true")) {
			return true;
		} else if (value != null
				&& (value.equals("0") || value.toLowerCase().equals("false"))) {
			return false;
		} else {
			configError("missing boolean " + key);
			return false;
		}
	}

	/**
	 * Get the value of a boolean key in <tt>tenet.conf</tt>, returning the
	 * specified default if the key does not exist.
	 * 
	 * @param key
	 *            the key to look up.
	 * @param defaultValue
	 *            the value to return if the key does not exist.
	 * @return the value of the specified key, or <tt>defaultValue</tt> if the
	 *         key does not exist.
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = props.getProperty(key);

		if ("1".equals(value) || value != null
				&& value.toLowerCase().equals("true")) {
			return true;
		} else if (value != null
				&& (value.equals("0") || value.toLowerCase().equals("false"))) {
			return false;
		} else {
			return defaultValue;
		}
	}

	private static String configFile;
	private static Properties props = new Properties();
}
