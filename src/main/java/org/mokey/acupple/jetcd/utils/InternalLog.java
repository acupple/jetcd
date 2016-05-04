package org.mokey.acupple.jetcd.utils;

public final class InternalLog {
	public static void log(String message) {
		System.out.println(message);
	}
	public static void error(String message) {
		System.out.println(message);
	}
	public static void error(String message, Exception error) {
		System.out.println(message);
		error.printStackTrace(System.out);
	}
}
