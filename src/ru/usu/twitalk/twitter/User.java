package ru.usu.twitalk.twitter;

import java.util.ArrayList;

public class User {

	private String name;
	private long id;
	private String screen_name;
	private ArrayList<String> messages = new ArrayList<String>();
	
	public User(String name, long id, String screen_name) {
		this.id = id;
		this.screen_name = screen_name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getScreenName() {
		return this.screen_name;
	}
	
	public void addMsg(String msg) {
		if (!messages.contains(msg))
			messages.add(msg);
	}
	
	public ArrayList<String> getMsgs() {
		return messages;
	}
	
}
