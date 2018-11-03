package com.gmail.zant95.HorseStables.Utils;

import java.util.TreeMap;

public final class CIMap<K, V>
		extends TreeMap<String, V>
{
	private static final long serialVersionUID = 3588405358786015678L;

	public final V put(String key, V value)
	{
		return (V)super.put(key.toLowerCase(), value);
	}

	public final V get(String key)
	{
		return (V)super.get(key.toLowerCase());
	}

	public final V remove(String key)
	{
		return (V)super.remove(key.toLowerCase());
	}

	public final boolean containsKey(String key)
	{
		return super.containsKey(key.toLowerCase());
	}
}
