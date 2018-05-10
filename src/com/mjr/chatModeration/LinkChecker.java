package com.mjr.chatModeration;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.mjr.Permissions;
import com.mjr.Permissions.Permission;

public class LinkChecker {
    public static boolean Allowed = false;
    public static boolean Link = false;
    public static String PermitedUsers = "";

    public static void CheckLink(String message, String sender) {
	String TempMessage = "";
	if (message.startsWith("www.") || message.startsWith("http://www.")) {
	    if (message.startsWith("http://www.")) {
		TempMessage = message;
	    } else if (message.startsWith("www.")) {
		TempMessage = "http://" + message;
	    }
	} else if (!message.startsWith("www.") && !message.startsWith("http://www.") && message.contains("www.")
		|| message.contains("http://www.")) {
	    if (message.contains("www.")) {
		message = message.substring(message.indexOf("www."));
		if (message.contains(" ")) {
		    message = message.substring(0, message.indexOf(' '));
		}
		TempMessage = "http://" + message;
	    } else if (message.contains("http://www.")) {
		message = message.substring(message.indexOf("http://www."));
		if (message.contains(" ")) {
		    message = message.substring(0, message.indexOf(' '));
		}
		TempMessage = message;
	    }
	} else if (!message.startsWith("www.") || !message.startsWith("http://www.") || !message.contains("www.")
		|| !message.contains("http://www.")) {
	    if (message.contains(".com")) {
		message = message.split("\\.")[0];
		message = message + ".com";
	    } else if (message.contains(".co.uk")) {
		message = message.split("\\.")[0];
		message = message + ".co.uk";
	    } else if (message.contains(".org")) {
		message = message.split("\\.")[0];
		message = message + ".org";
	    } else if (message.contains(".net")) {
		message = message.split("\\.")[0];
		message = message + ".net";
	    } else if (message.contains(".tv")) {
		message = message.split("\\.")[0];
		message = message + ".tv";
	    } else if (message.contains(".uk")) {
		message = message.split("\\.")[0];
		message = message + ".uk";
	    }
	    if (message.contains(" ")) {
		message = message.substring(message.indexOf(' ') + 1);
	    }
	    TempMessage = "http://www." + message;
	}

	try {
	    URL myURL = new URL(TempMessage);
	    URLConnection myURLConnection = myURL.openConnection();
	    myURLConnection.connect();
	    Link = true;
	    if (Permissions.hasPermission(sender, Permissions.Permission.Moderator.getName())) {
		Allowed = true;
	    } else if (PermitedUsers.contains(sender)) {
		Allowed = true;
		PermitedUsers = PermitedUsers.replace(sender.toLowerCase() + ", ", "");
	    } else {
		Allowed = false;
	    }

	} catch (IOException e) {
	    Link = false;
	}
    }

    public static boolean isAllowed() {
	if (Allowed) {
	    return true;
	}
	return false;
    }
}
