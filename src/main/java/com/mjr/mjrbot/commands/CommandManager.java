package com.mjr.mjrbot.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.defaultCommands.AccountLifeCommand;
import com.mjr.mjrbot.commands.defaultCommands.AddBadwordCommand;
import com.mjr.mjrbot.commands.defaultCommands.AddCommand;
import com.mjr.mjrbot.commands.defaultCommands.AddPointsCommand;
import com.mjr.mjrbot.commands.defaultCommands.AnswerCommand;
import com.mjr.mjrbot.commands.defaultCommands.BankHeistCommand;
import com.mjr.mjrbot.commands.defaultCommands.BuyRankCommand;
import com.mjr.mjrbot.commands.defaultCommands.ChangeCommandPermission;
import com.mjr.mjrbot.commands.defaultCommands.ChangeCommandResponse;
import com.mjr.mjrbot.commands.defaultCommands.ChangeCommandState;
import com.mjr.mjrbot.commands.defaultCommands.CommandsListCommand;
import com.mjr.mjrbot.commands.defaultCommands.DiceCommand;
import com.mjr.mjrbot.commands.defaultCommands.DisconnectCommand;
import com.mjr.mjrbot.commands.defaultCommands.EnterCommand;
import com.mjr.mjrbot.commands.defaultCommands.FollowTimeCommand;
import com.mjr.mjrbot.commands.defaultCommands.GiveAwayCommand;
import com.mjr.mjrbot.commands.defaultCommands.MathsCommand;
import com.mjr.mjrbot.commands.defaultCommands.PermitCommand;
import com.mjr.mjrbot.commands.defaultCommands.PingCommand;
import com.mjr.mjrbot.commands.defaultCommands.PlacebetCommand;
import com.mjr.mjrbot.commands.defaultCommands.PointsCheckCommand;
import com.mjr.mjrbot.commands.defaultCommands.PointsCommand;
import com.mjr.mjrbot.commands.defaultCommands.PointsLeaderboardCommands;
import com.mjr.mjrbot.commands.defaultCommands.QuoteCommand;
import com.mjr.mjrbot.commands.defaultCommands.RaceCommand;
import com.mjr.mjrbot.commands.defaultCommands.RankCheckCommand;
import com.mjr.mjrbot.commands.defaultCommands.RankCommand;
import com.mjr.mjrbot.commands.defaultCommands.ReconnectCommand;
import com.mjr.mjrbot.commands.defaultCommands.RemoveCommand;
import com.mjr.mjrbot.commands.defaultCommands.RemovePointsCommand;
import com.mjr.mjrbot.commands.defaultCommands.RemoveRankCommand;
import com.mjr.mjrbot.commands.defaultCommands.SetPointsCommand;
import com.mjr.mjrbot.commands.defaultCommands.SetRankCommand;
import com.mjr.mjrbot.commands.defaultCommands.SpinCommand;
import com.mjr.mjrbot.commands.defaultCommands.StopBotCommand;
import com.mjr.mjrbot.commands.defaultCommands.UptimeCommand;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.Permissions;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class CommandManager {
	public String[] args;

	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	public static void loadCommands() {
		commands.clear();
		// Points Commands
		commands.put("!points", new PointsCommand());
		commands.put("!addpoints", new AddPointsCommand());
		commands.put("!removepoints", new RemovePointsCommand());
		commands.put("!setpoints", new SetPointsCommand());
		commands.put("!pointscheck", new PointsCheckCommand());
		commands.put("!leaderboard", new PointsLeaderboardCommands());

		// Rank Commands
		commands.put("!rank", new RankCommand());
		commands.put("!buyrank", new BuyRankCommand());
		commands.put("!setrank", new SetRankCommand());
		commands.put("!removerank", new RemoveRankCommand());
		commands.put("!rankcheck", new RankCheckCommand());

		// Custom Commands
		commands.put("!addcommand", new AddCommand());
		commands.put("!removecommand", new RemoveCommand());
		commands.put("!commandstate", new ChangeCommandState());
		commands.put("!commandresponse", new ChangeCommandResponse());
		commands.put("!commandpermission", new ChangeCommandPermission());

		// Games Commands
		commands.put("!spin", new SpinCommand());
		commands.put("!maths", new MathsCommand());
		commands.put("!answer", new AnswerCommand());
		commands.put("!race", new RaceCommand());
		commands.put("!placebet", new PlacebetCommand());
		commands.put("!heist", new BankHeistCommand());
		commands.put("!dice", new DiceCommand());

		// Giveaway Commands
		commands.put("!giveaway", new GiveAwayCommand());
		commands.put("!enter", new EnterCommand());

		// Other Commands
		commands.put("!quote", new QuoteCommand());
		commands.put("!commands", new CommandsListCommand());
		commands.put("!disconnect", new DisconnectCommand());
		commands.put("!reconnect", new ReconnectCommand());
		commands.put("!uptime", new UptimeCommand());
		commands.put("!permit", new PermitCommand());
		commands.put("!accountlife", new AccountLifeCommand());
		commands.put("!followtime", new FollowTimeCommand());
		commands.put("!ping", new PingCommand());
		commands.put("!stopbot", new StopBotCommand());

		// Chat Moderation Commands
		commands.put("!addbadword", new AddBadwordCommand());
	}

	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message) throws FileNotFoundException, IOException {
		args = message.split(" ");

		// Check if known default command
		if (commands.containsKey(args[0].toLowerCase())) {
			Command command = commands.get(args[0].toLowerCase());
			if (canUseCommand(type, bot, sender, command)) {
				command.onCommand(type, bot, sender, login, hostname, message, args);
				EventLog.addEvent(type, bot, sender, "Used the command " + args[0].toLowerCase(), EventType.Commands);
				AnalyticsData.addNumOfCommandsUsed(1);
			}
		} else if (args[0].startsWith("!")) { // Check for known custom command
			CustomCommands.getCommand(type, bot, args[0], sender);
		}
	}

	public boolean canUseCommand(BotType type, Object bot, String sender, Command command) {
		if (!Permissions.hasPermission(bot, type, sender, command.getPermissionLevel()))
			return false;

		if (command.hasCooldown()) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (PermissionLevel.getTierValueByName(Permissions.getPermissionLevel(bot, type, sender.toLowerCase())) > 1) {
					return true;
				} else if (twitchBot.getTwitchData().usersCooldowns.get(sender.toLowerCase()) == 0) {
					twitchBot.getTwitchData().usersCooldowns.remove(sender.toLowerCase());
					twitchBot.getTwitchData().usersCooldowns.put(sender.toLowerCase(), Integer.parseInt(Config.getSetting("CommandsCooldownAmount", type, bot)));
					if (MJRBot.userCooldownTickThread.isAlive() == false)
						MJRBot.userCooldownTickThread.start();
					return true;

				} else
					return false;
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (PermissionLevel.getTierValueByName(Permissions.getPermissionLevel(bot, type, sender.toLowerCase())) > 0) {
					return true;
				} else if (mixerBot.getMixerData().usersCooldowns.get(sender.toLowerCase()) == 0) {
					mixerBot.getMixerData().usersCooldowns.remove(sender.toLowerCase());
					mixerBot.getMixerData().usersCooldowns.put(sender.toLowerCase(), Integer.parseInt(Config.getSetting("CommandsCooldownAmount", type, bot)));
					if (MJRBot.userCooldownTickThread.isAlive() == false)
						MJRBot.userCooldownTickThread.start();
					return true;
				} else
					return false;
			}
		} else
			return true;
		return false;
	}
}