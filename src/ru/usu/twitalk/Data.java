package ru.usu.twitalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ru.usu.twitalk.twitter.User;

public class Data {

	private static Data instance;

	//set friends
	public Set<String> friends;
	//info about frends
	public Set<User> users;
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
		friends = new TreeSet<String>();
		users = new HashSet<User>();
		contactsWithMsgs = new HashMap<String, ArrayList<String>>();
		infAbOAuthUser = new HashMap<String, String>();
	}

}	