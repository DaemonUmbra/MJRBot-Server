package com.mjr.storage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.mjr.MJRBot;

public class FileBase {

    public static Properties load(String channelName, String fileName) {
	try {
	    FileReader reader;
	    reader = new FileReader(loadFile(channelName, fileName));

	    Properties properties = new Properties();
	    properties.load(reader);
	    return properties;
	} catch (IOException e) {
	    MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	}
	return null;
    }

    public static File loadFile(String channelName, String fileName) {
	try {
	    File file = new File(MJRBot.filePath + channelName + File.separator + fileName);
	    if (!file.exists()) {
		file.getParentFile().mkdirs();
		file.createNewFile();
	    }
	    return file;
	} catch (IOException e) {
	    MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	}
	return null;
    }
}
