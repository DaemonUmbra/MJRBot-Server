package com.mjr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPConnect {
    /**
     * Made by Turkey2349
     */
    public static String GetResponsefrom(String link) {
	BufferedReader reader = null;
	StringBuilder buffer = new StringBuilder();

	try {
	    reader = new BufferedReader(new InputStreamReader(new URL(link).openStream()));

	    int read;
	    char[] chars = new char[1024];
	    while ((read = reader.read(chars)) != -1)
		buffer.append(chars, 0, read);
	} catch (Exception e) {
	} finally {
	    try {
		if (reader != null)
		    reader.close();
	    } catch (IOException e) {
	    }
	}
	return buffer.toString();
    }
}
