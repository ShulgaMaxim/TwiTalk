package ru.usu.twitalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Data {

	private static Data instance;

	public Set<String> friends;
	public Map<String, Long> infAbFriends;
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
		infAbFriends = new HashMap<String, Long>();
		contactsWithMsgs = new HashMap<String, ArrayList<String>>();
		infAbOAuthUser = new HashMap<String, String>();
	}

}