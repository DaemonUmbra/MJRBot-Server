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
			return result.substring(result.indexOf(':') + 1, result.indexOf(','));
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
			return result.substring(result.indexOf(':') + 1, result.indexOf(','));
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
			return null;
		}
	}
}
