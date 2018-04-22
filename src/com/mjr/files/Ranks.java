package com.mjr.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.MJRBot;

public class Ranks {

    public static String[] ranks = { "gold", "sliver", "bronze", "none" };

    public static int GoldPrice = 5000;
    public static int SliverPrice = 3500;
    public static int BronzePrice = 2000;

    public static String filename = "UserRanks.properties";
    public static File file = new File(MJRBot.filePath + MJRBot.getTwitchBot().getChannel().substring(1) + File.separator + filename);
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load() throws IOException {
	if (!file.exists()) {
	    file.getParentFile().mkdirs();
	    file.createNewFile();
	}
	FileReader reader = new FileReader(file);
	properties.load(reader);
    }

    public static String getRank(String User) {
	String value = "";
	User = User.toLowerCase();
	value = properties.getProperty(User, value);
	return value;
    }

    @SuppressWarnings("deprecation")
    public static void setRank(String User, String Rank) {
	User = User.toLowerCase();
	Rank = Rank.toLowerCase();
	if (getRank(User) != Rank) {
	    properties.setProperty(User.toLowerCase(), Rank);
	    try {
		properties.save(new FileOutputStream(file), null);
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void removeRank(String User) {
	if (getRank(User) != "None") {
	    setRank(User, "None");
	}
    }

    public static int getRankPrice(String Rank) {
	Rank = Rank.toLowerCase();
	if (Rank.equalsIgnoreCase(ranks[0]))
	    return GoldPrice;
	else if (Rank.equalsIgnoreCase(ranks[1]))
	    return SliverPrice;
	else if (Rank.equalsIgnoreCase(ranks[2]))
	    return BronzePrice;
	return 0;
    }

    public static Boolean isOnList(String User) {
	User = User.toLowerCase();
	if (properties.getProperty(User) != null)
	    return true;
	else
	    return false;
    }

    public static Boolean hasRank(String User, String Rank) {
	User = User.toLowerCase();
	Rank = Rank.toLowerCase();
	if (getRank(User).equalsIgnoreCase(Rank))
	    return true;
	else
	    return false;
    }

    public static Boolean isValidRank(String Rank) {
	Rank = Rank.toLowerCase();
	if (Arrays.asList(ranks).contains(Rank))
	    return true;
	else
	    return false;
    }
}
