package com.mjr.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.mjr.Permissions;
import com.mjr.commands.defaultCommands.AddCommand;
import com.mjr.commands.defaultCommands.AddPointsCommand;
import com.mjr.commands.defaultCommands.AnswerCommand;
import com.mjr.commands.defaultCommands.BankHeistCommand;
import com.mjr.commands.defaultCommands.BuyRankCommand;
import com.mjr.commands.defaultCommands.ChangeResponseCommand;
import com.mjr.commands.defaultCommands.ChangeStateCommand;
import com.mjr.commands.defaultCommands.CommandsListCommand;
import com.mjr.commands.defaultCommands.DiceCommand;
import com.mjr.commands.defaultCommands.DisconnectCommand;
import com.mjr.commands.defaultCommands.EnterCommand;
import com.mjr.commands.defaultCommands.GetRankCommand;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.commands.defaultCommands.MathsCommand;
import com.mjr.commands.defaultCommands.PermitCommand;
import com.mjr.commands.defaultCommands.PlacebetCommand;
import com.mjr.commands.defaultCommands.PointsCheckCommand;
import com.mjr.commands.defaultCommands.PointsCommand;
import com.mjr.commands.defaultCommands.QuoteCommand;
import com.mjr.commands.defaultCommands.RaceCommand;
import com.mjr.commands.defaultCommands.RankCommand;
import com.mjr.commands.defaultCommands.RemoveCommand;
import com.mjr.commands.defaultCommands.RemovePointsCommand;
import com.mjr.commands.defaultCommands.RemoveRankCheckCommand;
import com.mjr.commands.defaultCommands.SetPointsCommand;
import com.mjr.commands.defaultCommands.SetRankCommand;
import com.mjr.commands.defaultCommands.SpinCommand;
import com.mjr.commands.defaultCommands.UptimeCommand;

public class CommandManager {
    public String[] args;

    private static HashMap<String, Command> commands = new HashMap<String, Command>();

    public static void loadCommands() {
	commands.clear();
	commands.put("!quote", new QuoteCommand());
	commands.put("!points", new PointsCommand());
	commands.put("!rank", new RankCommand());
	commands.put("!commands", new CommandsListCommand());
	commands.put("!spin", new SpinCommand());
	commands.put("!answer", new AnswerCommand());
	commands.put("!buyrank", new BuyRankCommand());
	commands.put("!uptime", new UptimeCommand());
	commands.put("!maths", new MathsCommand());
	commands.put("!permit", new PermitCommand());
	commands.put("!addpoints", new AddPointsCommand());
	commands.put("!removepoints", new RemovePointsCommand());
	commands.put("!setpoints", new SetPointsCommand());
	commands.put("!pointscheck", new PointsCheckCommand());
	commands.put("!setrank", new SetRankCommand());
	commands.put("!removerank", new RemoveRankCheckCommand());
	commands.put("!getrank", new GetRankCommand());
	commands.put("!addcommand", new AddCommand());
	commands.put("!removecommand", new RemoveCommand());
	commands.put("!commandstate", new ChangeStateCommand());
	commands.put("!commandresponse", new ChangeResponseCommand());
	commands.put("!race", new RaceCommand());
	commands.put("!placebet", new PlacebetCommand());
	commands.put("!giveaway", new GiveAwayCommand());
	commands.put("!enter", new EnterCommand());
	commands.put("!heist", new BankHeistCommand());
	commands.put("!disconnect", new DisconnectCommand());
	commands.put("!dice", new DiceCommand());
	commands.put("!uptime", new UptimeCommand());
    }

    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message)
	    throws FileNotFoundException, IOException {
	args = message.split(" ");

	// Check if known default command
	if (commands.containsKey(args[0].toLowerCase())) {
	    Command command = commands.get(args[0].toLowerCase());
	    if (Permissions.hasPermission(sender, command.getPermissionLevel()))
		command.onCommand(bot, channel, sender, login, hostname, message, args);
	} else if (args[0].startsWith("!")) { // Check if its a known custom command
	    CustomCommands.getCommand(args[0], sender);
	}
    }
}