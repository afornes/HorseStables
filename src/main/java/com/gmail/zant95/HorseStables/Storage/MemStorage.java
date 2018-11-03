package com.gmail.zant95.HorseStables.Storage;

import com.gmail.zant95.HorseStables.Utils.CIMap;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class MemStorage
{
	public static boolean isOccupiedStorage = false;
	public static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("HorseStables");
	public static final File storageFolder = new File(plugin.getDataFolder() + File.separator + "storage");
	public static final CIMap<String, CIMap<String, CIMap<String, Object[]>>> horses = new CIMap();
}
