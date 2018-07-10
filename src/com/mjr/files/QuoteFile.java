package com.mjr.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("resource") 
public class QuoteFile { // TODO: Add Database Link?

    public static String getQuote(String channelName, File file, int number) {
	String token1 = "";
	Scanner inFile1 = null;
	try {
	    inFile1 = new Scanner(file).useDelimiter(";\\s*");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	List<String> temps = new ArrayList<String>();
	while (inFile1.hasNext()) {
	    token1 = inFile1.next();
	    temps.add(token1);
	}
	inFile1.close();
	String[] tempsArray = temps.toArray(new String[0]);

	if (number < tempsArray.length)
	    return tempsArray[number];
	else
	    return null;
    }

    public static String getRandomQuote(String channelName, File file) {
	String token1 = "";
	Scanner inFile1 = null;
	try {
	    inFile1 = new Scanner(file).useDelimiter(";\\s*");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	List<String> temps = new ArrayList<String>();
	while (inFile1.hasNext()) {
	    token1 = inFile1.next();
	    temps.add(token1);
	}
	inFile1.close();
	String[] tempsArray = temps.toArray(new String[0]);
	Random rand = new Random();
	return tempsArray[rand.nextInt(tempsArray.length)];
    }

    public static void addQuote(String channelName, File file, String quote) {
	String fileTemp = file.getPath();
	Path filePath = Paths.get(fileTemp);
	if (!Files.exists(filePath)) {
	    try {
		Files.createFile(filePath);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	quote = quote.substring(11);
	quote = "'" + quote;
	quote = quote.replace(" @", "' ");
	try {
	    Files.write(filePath, ("\n" + quote.trim() + " " + Calendar.getInstance().get(Calendar.YEAR) + ";").getBytes(),
		    StandardOpenOption.APPEND);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
