package com.mjr.gameIntegrations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mjr.MJRBot;

public class CallOfDuty {

	/*
	 * Format for variables
	 * 
	 * Stats: %cod_STAT(game:pathform:user)%
	 * 
	 * Stats Per GameType: %cod_STAT(game:pathform:user:type)%
	 * 
	 */
	
	public static String replaceVariablesWithData(String variable) {
		if(variable.startsWith("%cod_STATNAMEHERE") || variable.startsWith("%cod_("))
			throw new IndexOutOfBoundsException("Invalid stat name!");
		String stat = variable.substring(variable.indexOf("%cod_") + 5);
		stat = stat.substring(0, stat.indexOf("("));

		String temp = variable.substring(variable.indexOf(stat));
		temp = temp.substring(temp.indexOf("(") + 1);
		temp = temp.substring(0, temp.indexOf(")"));

		String[] codParts = temp.split(":");
		if (codParts.length != 3 && codParts.length != 4)
			throw new IndexOutOfBoundsException("Missing args in cod stats variable!");

		if (!codParts[0].equalsIgnoreCase("bo4") && !codParts[0].equalsIgnoreCase("wwii"))
			throw new IndexOutOfBoundsException("Invalid game!");

		if (!codParts[1].equalsIgnoreCase("xbl") && !codParts[1].equalsIgnoreCase("psn") && !codParts[1].equalsIgnoreCase("battle") && !codParts[1].equalsIgnoreCase("steam"))
			throw new IndexOutOfBoundsException("Invalid pathform!");

		if (codParts.length == 3) {
			variable = getProfileAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"));
		}
		else if (codParts.length == 4 && codParts[0].equalsIgnoreCase("bo4")) {
			if (!codParts[3].equalsIgnoreCase("mp") && !codParts[3].equalsIgnoreCase("zombies") && !codParts[3].equalsIgnoreCase("blackout"))
				throw new IndexOutOfBoundsException("Invalid game mode type!");
			return getGameTypeAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"), codParts[3]);
		}
		return null;
	}

	public static String getProfileAllStat(String stat, String game, String pathform, String user) {
		try {
			String result = "";
			URL url;
			url = new URL("https://my.callofduty.com/api/papi-client/crm/cod/v2/title/" + game + "/platform/" + pathform + "/gamer/" + user + "/profile/");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			result = result.substring(result.indexOf("\""+stat + "\""));
			return result.substring(result.indexOf(':') + 1, result.indexOf(',')).replaceAll("}", "");
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
			return null;
		}
	}

	public static String getGameTypeAllStat(String stat, String game, String pathform, String user, String type) {
		try {
			String result = "";
			URL url;
			url = new URL("https://my.callofduty.com/api/papi-client/crm/cod/v2/title/" + game + "/platform/" + pathform + "/gamer/" + user + "/profile/?type=" + type);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			result = result.substring(result.indexOf("\""+stat + "\""));
			return result.substring(result.indexOf(':') + 1, result.indexOf(',')).replaceAll("}", "");
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
			return null;
		}
	}
}
