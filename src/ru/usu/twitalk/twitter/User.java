package ru.usu.twitalk.twitter;

public class User {

	private String name;
	private long id;
	private String screen_name;
	
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
	
}
