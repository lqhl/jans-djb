package jans.common;

/**
 * Provides miscellaneous library routines.
 * @author lqhl
 */
public final class Lib {
	/** Debug flags specified on the command line. */
	private static boolean debugFlags[];

	/**
	 * Create and return a new instance of the named class, using the
	 * constructor that takes no arguments.
	 * 
	 * @param className
	 *            the name of the class to instantiate.
	 * @return a new instance of the class.
	 */
	@SuppressWarnings("unchecked")
	public static Object constructObject(String className) {
		try {
			Class[] param_types = new Class[0];
			Object[] params = new Object[0];
			return loadClass(className).getConstructor(param_types)
					.newInstance(params);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Load and return the named class, terminating program on any error.
	 * 
	 * @param className
	 *            the name of the class to load.
	 * @return the loaded class.
	 */
	@SuppressWarnings("unchecked")
	public static Class loadClass(String className) {
		try {
			return ClassLoader.getSystemClassLoader().loadClass(className);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Enable all the debug flags in <i>flagsString</i>.
	 * 
	 * @param flagsString
	 *            the flags to enable.
	 */
	public static void enableDebugFlags(String flagsString) {
		if (debugFlags == null)
			debugFlags = new boolean[0x80];

		char[] newFlags = flagsString.toCharArray();
		for (int i = 0; i < newFlags.length; i++) {
			char c = newFlags[i];
			if (c >= 0 && c < 0x80)
				debugFlags[(int) c] = true;
		}
	}

	/**
	 * Tests if <i>flag</i> was enabled on the command line.
	 * 
	 * @param flag
	 *            the debug flag to test.
	 * 
	 * @return <tt>true</tt> if this flag was enabled on the command line.
	 */
	public static boolean test(char flag) {
		if (debugFlags == null)
			return false;
		else if (debugFlags[(int) '+'])
			return true;
		else if (flag >= 0 && flag < 0x80 && debugFlags[(int) flag])
			return true;
		else
			return false;
	}

	/**
	 * Print <i>message</i> if <i>flag</i> was enabled on the command line. To
	 * specify which flags to enable.
	 * 
	 * @param flag
	 *            the debug flag that must be set to print this message.
	 * @param message
	 *            the debug message.
	 */
	public static void debug(char flag, String message) {
		if (test(flag))
			System.out.println(message);
	}

	/**
	 * Asserts that <i>expression</i> is <tt>true</tt>. If not, then
	 * exits with an error message.
	 * 
	 * @param expression
	 *            the expression to assert.
	 */
	public static void assertTrue(boolean expression) {
		if (!expression)
			throw new AssertionFailureError();
	}

	/**
	 * Asserts that <i>expression</i> is <tt>true</tt>. If not, then
	 * exits with the specified error message.
	 * 
	 * @param expression
	 *            the expression to assert.
	 * @param message
	 *            the error message.
	 */
	public static void assertTrue(boolean expression, String message) {
		if (!expression)
			throw new AssertionFailureError(message);
	}

	/**
	 * Asserts that this call is never made. Same as <tt>assertTrue(false)</tt>.
	 */
	public static void assertNotReached() {
		assertTrue(false);
	}

	/**
	 * Asserts that this call is never made, with the specified error message.
	 * Same as <tt>assertTrue(false, message)</tt>.
	 * 
	 * @param message
	 *            the error message.
	 */
	public static void assertNotReached(String message) {
		assertTrue(false, message);
	}
}

/**
 * Thrown when an assertion fails.
 */
class AssertionFailureError extends Error {
	private static final long serialVersionUID = 1611733617808283523L;

	AssertionFailureError() {
		super();
	}

	AssertionFailureError(String message) {
		super(message);
	}
}