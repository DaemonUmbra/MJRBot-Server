package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.mjr.mjrbot.MJRBot;

public class FileBase {

	public static Properties load(String channelName, String fileName) {
		try {
			FileReader reader;
			reader = new FileReader(loadFile(channelName, fileName));

			Properties properties = new Properties();
			properties.load(reader);
			return properties;
		} catch (IOException e) {
			MJRBot.logErrorMessage(e);
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
			MJRBot.logErrorMessage(e);
		}
		return null;
	}

	public static Properties load(int channelID, String fileName) {
		try {
			FileReader reader;
			reader = new FileReader(loadFile(channelID, fileName));

			Properties properties = new Properties();
			properties.load(reader);
			return properties;
		} catch (IOException e) {
			MJRBot.logErrorMessage(e);
		}
		return null;
	}

	public static File loadFile(int channelID, String fileName) {
		try {
			File file = new File(MJRBot.filePath + channelID + File.separator + fileName);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			return file;
		} catch (IOException e) {
			MJRBot.logErrorMessage(e);
		}
		return null;
	}
}
