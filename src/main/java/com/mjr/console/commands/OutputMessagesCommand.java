package com.mjr.console.commands;

import com.mjr.console.ConsoleCommand;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;

public class OutputMessagesCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("chat")) {
				ConsoleUtil.setShowChatMessages(!ConsoleUtil.isShowChatMessages());
				System.out.println("Output setting: Chat has been set to " + ConsoleUtil.isShowChatMessages());
			} else if (args[0].equalsIgnoreCase("chatbot")) {
				ConsoleUtil.setShowChatBotMessages(!ConsoleUtil.isShowChatBotMessages());
				System.out.println("Output setting: ChatBot has been set to " + ConsoleUtil.isShowChatBotMessages());
			} else if (args[0].equalsIgnoreCase("bot")) {
				ConsoleUtil.setShowBotMessages(!ConsoleUtil.isShowBotMessages());
				System.out.println("Output setting: Bot has been set to " + ConsoleUtil.isShowBotMessages());
			} else if (args[0].equalsIgnoreCase("showall")) {
				ConsoleUtil.setShowChatMessages(true);
				ConsoleUtil.setShowChatBotMessages(true);
				ConsoleUtil.setShowBotMessages(true);
				System.out.println("Output setting: Chat has been set to " + ConsoleUtil.isShowChatMessages());
				System.out.println("Output setting: ChatBot has been set to " + ConsoleUtil.isShowChatMessages());
				System.out.println("Output setting: Bot has been set to " + ConsoleUtil.isShowChatMessages());
			} else if (args[0].equalsIgnoreCase("hideall")) {
				ConsoleUtil.setShowChatMessages(false);
				ConsoleUtil.setShowChatBotMessages(false);
				ConsoleUtil.setShowBotMessages(false);
				System.out.println("Output setting: Chat has been set to " + ConsoleUtil.isShowChatMessages());
				System.out.println("Output setting: ChatBot has been set to " + ConsoleUtil.isShowChatMessages());
				System.out.println("Output setting: Bot has been set to " + ConsoleUtil.isShowChatMessages());
			} else {
				System.out.println("Invalid type, Use Chat, Chatbot, Bot OR showall, hideall");
				return;
			}
			ConsoleUtil.refreshConsoleMessages();
			System.out.println("Showing: " + (ConsoleUtil.isShowChatMessages() ? " Chat " : "")+ (ConsoleUtil.isShowChatBotMessages() ? " ChatBot " : "")+ (ConsoleUtil.isShowBotMessages() ? " Bot " : "")+ (ConsoleUtil.isShowErrorMessages() ? " Error " : "") + " outputs");
		} else
			System.out.println("Invalid syntax, Use toggle output " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Disable/Enable " + MessageType.Chat + ", " + MessageType.ChatBot + ", " + MessageType.Bot + " outputs to the console!";
	}

	@Override
	public String getParametersDescription() {
		return "<type>";
	}

}
