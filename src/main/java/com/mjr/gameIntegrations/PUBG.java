package com.mjr.gameIntegrations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mjr.MJRBot;
import com.mjr.storage.ConfigMain;

public class PUBG {

	/*
	 * Format for variables
	 * 
	 * Stats: %pubg_STAT(pathform:user:type)%
	 * 
	 */

	public static String replaceVariablesWithData(String variable) {
		if (variable.startsWith("%pubg_STATNAMEHERE") || variable.startsWith("%pubg_("))
			throw new IndexOutOfBoundsException("Invalid stat name!");
		String stat = variable.substring(variable.indexOf("%pubg_") + 6);
		stat = stat.substring(0, stat.indexOf("("));

		String temp = variable.substring(variable.indexOf(stat));
		temp = temp.substring(temp.indexOf("(") + 1);
		temp = temp.substring(0, temp.indexOf(")"));

		String[] pubgParts = temp.split(":");
		if (pubgParts.length != 3)
			throw new IndexOutOfBoundsException("Missing args in pubg stats variable!");

		if (!pubgParts[0].equalsIgnoreCase("steam"))
			throw new IndexOutOfBoundsException("Invalid pathform!");

		else if (pubgParts.length == 3 && pubgParts[0].equalsIgnoreCase("steam")) {
			if (!pubgParts[2].equalsIgnoreCase("duo") && !pubgParts[2].equalsIgnoreCase("duo-fpp") && !pubgParts[2].equalsIgnoreCase("solo") && !pubgParts[2].equalsIgnoreCase("solo-fpp") && !pubgParts[2].equalsIgnoreCase("squad")
					&& !pubgParts[2].equalsIgnoreCase("squad-fpp"))
				throw new IndexOutOfBoundsException("Invalid game mode type!");
			return getProfileAllStat(stat, pubgParts[0], getPlayerID(pubgParts[1]), pubgParts[2]);
		}
		return null;
	}

	public static String getPlayerID(String user) {
		try { // TODO Create/Change to a HTTPConnectMethod
			String result = "";
			URL url;
			url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=" + user);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + ConfigMain.getSetting("PUBGToken"));
			conn.setRequestProperty("Accept", "application/vnd.api+json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			result = result.substring(result.indexOf("account.") + 8);
			result = result.substring(0, result.indexOf(",") - 1);
			return result;
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
			return null;
		}
	}

	public static String getProfileAllStat(String stat, String pathform, String user, String type) {
		if (user == null)
			return "ERROR";
		try {
			String result = "";
			URL url; // TODO Create/Change to a HTTPConnectMethod
			url = new URL("https://api.pubg.com/shards/" + pathform + "/players/" + user + "/seasons/lifetime");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + ConfigMain.getSetting("PUBGToken"));
			conn.setRequestProperty("Accept", "application/vnd.api+json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			result = result.substring(result.indexOf("\"" + type + "\""));
			result = result.substring(result.indexOf("\"" + stat + "\""));
			return result.substring(result.indexOf(':') + 1, result.indexOf(',')).replaceAll("}", "");
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
			return null;
		}
	}
}
