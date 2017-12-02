package com.mjr;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;
import com.mjr.mjrmixer.MJR_MixerBot;

public class MixerBot extends MJR_MixerBot {

    private final CommandManager commands = new CommandManager();

    @Override
    protected void onMessage(String sender, String message) {
	ConsoleUtli.TextToConsole(message, "Chat", sender);
	try {
	    commands.onCommand(this, MJRBot.getChannel(), sender, null, null, message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void onJoin(String sender) {
	MJRBot.getMixerBot().addViewer(sender);
    }

    @Override
    protected void onPart(String sender) {
	MJRBot.getMixerBot().removeViewer(sender);

    }

    public void joinChannel(String channel) throws InterruptedException, ExecutionException, IOException {
	ConfigMain.load();
	MJRBot.getMixerBot().setdebug(true);
	String response = JOptionPane.showInputDialog(null, "AuthCode (leave blank if you dont use two factor authentication):");
	MJRBot.getMixerBot().connect(channel, ConfigMain.getSetting("MixerUsername"), ConfigMain.getSetting("MixerPassword"), response);
	if (MJRBot.getMixerBot().isConnected() && MJRBot.getMixerBot().isAuthenticated()) {
	    // Load Config file
	    Config.load();
	    // Load PointsSystem
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		PointsSystem.load();
	    }
	    // Load Ranks
	    if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
		Ranks.load();
	    }
	    ConsoleUtli.TextToConsole("MJRBot is Connected & Authenticated to Mixer!", "Chat", null);
	    if (Config.getSetting("SilentJoin").equalsIgnoreCase("false"))
		MJRBot.getMixerBot().sendMessage(MJRBot.getMixerBot().getBotName() + " Connected!");
	} else
	    ConsoleUtli.TextToConsole("Theres been problem, connecting to Mixer, Please check settings are corrrect!", "Chat", null);
    }
}
