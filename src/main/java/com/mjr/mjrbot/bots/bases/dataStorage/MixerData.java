package com.mjr.mjrbot.bots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MixerData {
	public boolean giveAwayActive = false;

	public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
	public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
	public HashMap<String, Integer> bankHeistEnteredUsers = new HashMap<String, Integer>();
	public List<String> linkPermitedUsers = new ArrayList<String>();
	public List<String> giveawayEnteredUsers = new ArrayList<String>();
	private List<String> subscribers = new ArrayList<String>();
	private List<String> followers = new ArrayList<String>();

	public List<String> getFollowers() {
		return followers;
	}

	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}

	public void addFollower(String follower) {
		if (!this.followers.contains(follower.toLowerCase()))
			this.followers.add(follower.toLowerCase());
	}

	public void removeFollower(String follower) {
		this.followers.remove(follower.toLowerCase());
	}

	public List<String> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<String> subscribers) {
		this.subscribers = subscribers;
	}

	public void addSubscriber(String subscriber) {
		if (!this.subscribers.contains(subscriber.toLowerCase()))
			this.subscribers.add(subscriber.toLowerCase());
	}

	public void removeSubscriber(String subscriber) {
		this.subscribers.remove(subscriber.toLowerCase());
	}
}
