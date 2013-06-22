package ru.usu.twitalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.usu.twitalk.twitter.User;

public class Data {

	private static Data instance;

	//info about frends
	public Map<String, User> users;
	//info about OAuth user
	public Map<String, String> infAbOAuthUser;
	public Map<String, ArrayList<String>> contactsWithMsgs;

	public static final String USER_NAME = "name";
	public static final String USER_SCREEN_NAME = "screen_name";
	public static final String ID_AUTH_USER = "id_str";

	public static Data getInstance() {
		if (instance == null)
			instance = new Data();
		return instance;
	}

	private Data() {
		users = new HashMap<String, User>();
		contactsWithMsgs = new HashMap<String, ArrayList<String>>();
		infAbOAuthUser = new HashMap<String, String>();
	}

}	