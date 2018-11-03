package com.gmail.zant95.HorseStables;

import com.gmail.zant95.HorseStables.Storage.DiskStorage;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class HorseStables
		extends JavaPlugin
{
	public void onEnable()
	{
		if (!getDataFolder().exists())
		{
			getLogger().info("Creating plugin folder...");
			new File(getDataFolder().toString()).mkdir();
		}
		DiskStorage.asyncLoadHorses();

		getCommand("horsespawn").setExecutor(new CommandHandler(this));
		getCommand("horsedelete").setExecutor(new CommandHandler(this));
		getCommand("horsetoggle").setExecutor(new CommandHandler(this));
		getCommand("horselist").setExecutor(new CommandHandler(this));

		getLogger().info("HorseStables enabled!");
	}

	public void onDisable()
	{
		getLogger().info("Goodbye HorseStables!");
	}
}