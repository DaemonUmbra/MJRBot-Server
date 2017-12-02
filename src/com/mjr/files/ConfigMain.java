package com.mjr.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mjr.ConsoleUtli;

public class ConfigMain {
    public static String filename = "Settings.properties";
    public static File file = new File("/home/" + File.separator + "MJRBot" + File.separator + filename);
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load() throws IOException {
	if (!file.exists()) {
	    file.getParentFile().mkdirs();
	    file.createNewFile();
	    iStream = new FileInputStream(file);
	    properties.load(iStream);
	    properties.store(new FileOutputStream(file), null);
	    setSetting("TwitchUsername", "");
	    setSetting("TwitchPassword", "");
	    setSetting("MixerUsername", "");
	    setSetting("MixerPassword", "");
	}
	FileReader reader = new FileReader(file);
	properties.load(reader);
    }

    public static String getSetting(String setting) {
	return properties.getProperty(setting);
    }

    @SuppressWarnings("deprecation")
    public static void setSetting(String setting, String value) {
	if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
	    if (value == "true")
		ConsoleUtli.TextToConsole(setting + " has been has enabled!", "Bot", null);
	    else
		ConsoleUtli.TextToConsole(setting + " has been has disabled!", "Bot", null);

	    properties.setProperty(setting, value);
	    try {
		properties.save(new FileOutputStream(file), null);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    properties.setProperty(setting, value);
	    try {
		properties.store(new FileOutputStream(file), null);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

    }

}
