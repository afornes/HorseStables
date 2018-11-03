package com.gmail.zant95.HorseStables.Locale;

import org.bukkit.ChatColor;

public final class Messages
{
	private static final String SUCESSPREFIX = "&6 - &2";
	private static final String FAILPREFIX = "&6 - &c";
	public static final String NO_PERMISSION = FORMAT("&6 - &cYou don't have permissions to use this command.");
	public static final String ONLY_A_PLAYER = FORMAT("&6 - &cThis command must be used by a player.");
	public static final String NOT_FOUND = FORMAT("&6 - &cNot found.");
	public static final String BAD_SYNTAX = FORMAT("&6 - &cError, bad syntax.");
	public static final String ONLY_ALPHANUMERIC = FORMAT("&6 - &cThe stable name can only contain alphanumeric characters.");
	public static final String DISCONNECTED_PLAYER = FORMAT("&6 - &cError, disconnected player.");
	public static final String SLOT_NUMBER_BETWEEN = FORMAT("&6 - &cThe slot number must be between 0 and 15.");
	public static final String OCCUPIED_SLOT = FORMAT("&6 - &cYou already have a horse in this slot.");
	public static final String LEAVE_VEHICLE = FORMAT("&6 - &cYou must leave this vehicle to do this.");
	public static final String NOT_HORSE = FORMAT("&6 - &cYou don't have a horse in this slot.");
	public static final String UNEXPECTED = FORMAT("&6 - &cUnexpected error.");
	public static final String LOADED_HORSE = FORMAT("&6 - &2Your horse has been successfully &6loaded&2.");
	public static final String SAVED_HORSE = FORMAT("&6 - &2Your horse has been successfully &6saved&2.");
	public static final String DELETED_HORSE = FORMAT("&6 - &2The selected horse has been successfully &6deleted&2.");
	public static final String SEPARATOR = FORMAT("&l=============================================");

	public static final String STABLE_LIST_TITLE(String stableName)
	{
		return FORMAT("&6 Stable &f" + stableName + " &6horses:");
	}

	public static final String PLAYER_LIST_TITLE(String playerName)
	{
		return FORMAT("&f " + playerName + "&6's horses:");
	}

	public static final String TAMER_LIST_TITLE(String tamerName)
	{
		return FORMAT("&f " + tamerName + "&6's tamed horses:");
	}

	public static final String HORSE_TYPE(Object horseType)
	{
		return FORMAT("[&f" + horseType + "&6]");
	}

	public static final String HORSE_NAME(Object horseName)
	{
		return FORMAT(horseName != null ? "[&f" + horseName + "&6]" : "");
	}

	public static final String HORSE_ENTRY(String slotNumber, String horseType, String horseName)
	{
		return FORMAT("&2    - &6Slot &f" + slotNumber + "&6 " + horseType + horseName + ".");
	}

	public static final String STABLE_ENTRY(String stableName)
	{
		return FORMAT("&6 - &2" + stableName + "&6:");
	}

	public static final String PLAYER_ENTRY(String playerName)
	{
		return FORMAT("&6 - &2" + playerName + "&6:");
	}

	private static final String FORMAT(String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
