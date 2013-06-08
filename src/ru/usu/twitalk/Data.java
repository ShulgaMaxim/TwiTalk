package ru.usu.twitalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Data {

	public static ArrayList<String> FOLLOWERS = new ArrayList<String>();
	public static Map<String,Long> infAbFollowers = new HashMap<String,Long>();
	public static String USER_NAME;
	public static String USER_TEXT;
	public static String USER_SCREEN_NAME;
	public static long ID_AUTH_USER;
	public static Map<String, ArrayList<String>> contactsWithMsgs = new TreeMap<String, ArrayList<String>>();

}
