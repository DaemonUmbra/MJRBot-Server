package com.mjr.mjrbot.bots.bases.dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TwitchData {

	public boolean giveAwayActive = false;

	private List<String> moderators = new ArrayList<String>();
	private List<String> viewers = new ArrayList<String>();
	private List<String> followers = new ArrayList<String>();
	private List<String> subscribers = new ArrayList<String>();
	private List<String> vips = new ArrayList<String>();
	public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
	public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
	public HashMap<String, Integer> bankHeistEnteredUsers = new HashMap<String, Integer>();
	public List<String> linkPermitedUsers = new ArrayList<String>();
	public List<String> giveawayEnteredUsers = new ArrayList<String>();

	public List<String> getModerators() {
		return moderators;
	}

	public void setModerators(List<String> moderators) {
		this.moderators = moderators;
	}

	public void addModerator(String moderator) {
		if (!this.moderators.contains(moderator.toLowerCase()))
			this.moderators.add(moderator.toLowerCase());
	}

	public void removeModerator(String moderator) {
		this.moderators.remove(moderator.toLowerCase());
	}

	public List<String> getViewers() {
		return viewers;
	}

	public void setViewers(List<String> viewers) {
		this.viewers = viewers;
	}

	public void addViewer(String viewer) {
		if (!this.viewers.contains(viewer.toLowerCase()))
			this.viewers.add(viewer.toLowerCase());
	}

	public void removeViewer(String viewer) {
		this.viewers.remove(viewer.toLowerCase());
	}

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

	public List<String> getVips() {
		return vips;
	}

	public void setVips(List<String> vips) {
		this.vips = vips;
	}

	public void addVip(String vip) {
		if (!this.vips.contains(vip.toLowerCase()))
			this.vips.add(vip.toLowerCase());
	}

	public void removeVip(String vip) {
		this.vips.remove(vip.toLowerCase());
	}
}
