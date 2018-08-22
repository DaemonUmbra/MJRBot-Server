package com.mjr.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.defaultCommands.AccountLifeCommand;
import com.mjr.commands.defaultCommands.AddCommand;
import com.mjr.commands.defaultCommands.AddPointsCommand;
import com.mjr.commands.defaultCommands.AnswerCommand;
import com.mjr.commands.defaultCommands.BankHeistCommand;
import com.mjr.commands.defaultCommands.BuyRankCommand;
import com.mjr.commands.defaultCommands.ChangeCommandPermission;
import com.mjr.commands.defaultCommands.ChangeCommandResponse;
import com.mjr.commands.defaultCommands.ChangeCommandState;
import com.mjr.commands.defaultCommands.CommandsListCommand;
import com.mjr.commands.defaultCommands.DiceCommand;
import com.mjr.commands.defaultCommands.DisconnectCommand;
import com.mjr.commands.defaultCommands.EnterCommand;
import com.mjr.commands.defaultCommands.FollowTimeCommand;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.commands.defaultCommands.MathsCommand;
import com.mjr.commands.defaultCommands.PermitCommand;
import com.mjr.commands.defaultCommands.PingCommand;
import com.mjr.commands.defaultCommands.PlacebetCommand;
import com.mjr.commands.defaultCommands.PointsCheckCommand;
import com.mjr.commands.defaultCommands.PointsCommand;
import com.mjr.commands.defaultCommands.PointsLeaderboardCommands;
import com.mjr.commands.defaultCommands.QuoteCommand;
import com.mjr.commands.defaultCommands.RaceCommand;
import com.mjr.commands.defaultCommands.RankCheckCommand;
import com.mjr.commands.defaultCommands.RankCommand;
import com.mjr.commands.defaultCommands.ReconnectCommand;
import com.mjr.commands.defaultCommands.RemoveCommand;
import com.mjr.commands.defaultCommands.RemovePointsCommand;
import com.mjr.commands.defaultCommands.RemoveRankCommand;
import com.mjr.commands.defaultCommands.SetPointsCommand;
import com.mjr.commands.defaultCommands.SetRankCommand;
import com.mjr.commands.defaultCommands.SpinCommand;
import com.mjr.commands.defaultCommands.StopBotCommand;
import com.mjr.commands.defaultCommands.UptimeCommand;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;

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
	commands.put("!stopBot", new StopBotCommand());
    }

    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message)
	    throws FileNotFoundException, IOException {
	args = message.split(" ");

	// Check if known default command
	if (commands.containsKey(args[0].toLowerCase())) {
	    Command command = commands.get(args[0].toLowerCase());
	    if (Permissions.hasPermission(bot, type, channel, sender, command.getPermissionLevel())) {
		if (type == BotType.Twitch) {
		    TwitchBot twitchBot = ((TwitchBot) bot);

		    if (!twitchBot.usersCooldowns.containsKey(sender.toLowerCase())) {
			twitchBot.usersCooldowns.put(sender.toLowerCase(), 0);
		    }
		}

		if (type == BotType.Mixer) {
		    MixerBot mixerBot = ((MixerBot) bot);

		    if (!mixerBot.usersCooldowns.containsKey(sender.toLowerCase())) {
			mixerBot.usersCooldowns.put(sender.toLowerCase(), 0);
		    }
		}
		boolean allowed = false;
		if (command.hasCooldown()) {
		    if (type == BotType.Twitch) {
			TwitchBot twitchBot = ((TwitchBot) bot);
			if (twitchBot.usersCooldowns.containsKey(sender.toLowerCase())) {
			    if (PermissionLevel
				    .getTierValueByName(Permissions.getPermissionLevel(bot, type, channel, sender.toLowerCase())) > 0) {
				allowed = true;
			    } else if (twitchBot.usersCooldowns.get(sender.toLowerCase()) == 0) {
				allowed = true;
				twitchBot.usersCooldowns.remove(sender.toLowerCase());
				twitchBot.usersCooldowns.put(sender.toLowerCase(),
					Integer.parseInt(Config.getSetting("CommandsCooldownAmount", channel)));
				if (MJRBot.userCooldownTickThread.isAlive() == false)
				    MJRBot.userCooldownTickThread.start();

			    } else
				allowed = false;
			}
		    } else if (type == BotType.Mixer) {
			MixerBot mixerBot = ((MixerBot) bot);
			if (mixerBot.usersCooldowns.containsKey(sender.toLowerCase())) {
			    if (PermissionLevel
				    .getTierValueByName(Permissions.getPermissionLevel(bot, type, channel, sender.toLowerCase())) > 0) {
				allowed = true;
			    } else if (mixerBot.usersCooldowns.get(sender.toLowerCase()) == 0) {
				allowed = true;
				mixerBot.usersCooldowns.remove(sender.toLowerCase());
				mixerBot.usersCooldowns.put(sender.toLowerCase(),
					Integer.parseInt(Config.getSetting("CommandsCooldownAmount", channel)));
				if (MJRBot.userCooldownTickThread.isAlive() == false)
				    MJRBot.userCooldownTickThread.start();

			    } else
				allowed = false;
			}
		    }
		} else
		    allowed = true;
		if (allowed) {
		    command.onCommand(type, bot, channel, sender, login, hostname, message, args);
		    EventLog.addEvent(channel, sender, "Used the command " + args[0].toLowerCase(), EventType.Commands);
		    AnalyticsData.addNumOfCommandsUsed(1);
		}
	    }
	} else if (args[0].startsWith("!")) { // Check for known custom command
	    CustomCommands.getCommand(bot, type, channel, args[0], sender);
	}
    }
}