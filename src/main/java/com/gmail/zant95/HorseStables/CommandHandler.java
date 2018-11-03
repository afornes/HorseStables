package com.gmail.zant95.HorseStables;

import com.gmail.zant95.HorseStables.Locale.Messages;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import com.gmail.zant95.HorseStables.Storage.DiskStorage;
import com.gmail.zant95.HorseStables.Storage.MemStorage;
import com.gmail.zant95.HorseStables.Utils.CIMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mule;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.util.ArrayList;

public final class CommandHandler
		implements CommandExecutor
{
	HorseStables plugin;

	public CommandHandler(HorseStables instance)
	{
		this.plugin = instance;
	}

	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("horsespawn")) {
			if (!sender.hasPermission("horsestables.spawn")) {
				sender.sendMessage(Messages.NO_PERMISSION);
			} else if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.ONLY_A_PLAYER);
			} else if (args.length == 3)
			{
				if (HorseSearch.horseExists(args[0], args[1], args[2])) {
					HorseManage.loadCustomHorse((Player)sender, args[0], args[1], args[2]);
				} else {
					sender.sendMessage(Messages.NOT_FOUND);
				}
			}
			else {
				sender.sendMessage(Messages.BAD_SYNTAX);
			}
		}
		if (command.getName().equalsIgnoreCase("horsedelete"))
		{
			if (!sender.hasPermission("horsestables.delete")) {
				sender.sendMessage(Messages.NO_PERMISSION);
			} else if (args.length == 3)
			{
				if (HorseSearch.horseExists(args[0], args[1], args[2])) {
					HorseManage.deleteCustomHorse(sender, args[0], args[1], args[2]);
				} else {
					sender.sendMessage(Messages.NOT_FOUND);
				}
			}
			else {
				sender.sendMessage(Messages.BAD_SYNTAX);
			}
		}
		else if (command.getName().equalsIgnoreCase("horsetoggle"))
		{
			if (!sender.hasPermission("horsestables.toggle"))
			{
				sender.sendMessage(Messages.NO_PERMISSION);
			}
			else if (args.length == 3)
			{
				if (!Pattern.compile("[-\\p{Alnum}]+").matcher(args[0]).matches())
				{
					sender.sendMessage(Messages.ONLY_ALPHANUMERIC);
					return true;
				}
				if (!Bukkit.getServer().getOfflinePlayer(args[1]).isOnline())
				{
					sender.sendMessage(Messages.DISCONNECTED_PLAYER);
					return true;
				}
				if (!Pattern.compile("1[0-5]|[0-9]").matcher(args[2]).matches())
				{
					sender.sendMessage(Messages.SLOT_NUMBER_BETWEEN);
					return true;
				}
				Player player = Bukkit.getServer().getPlayer(args[1]);
				if (HorseSearch.horseExists(args[0], args[1], args[2]))
				{
					if (player.isInsideVehicle())
					{
						if ((player.getVehicle() instanceof AbstractHorse)) {
							player.sendMessage(Messages.OCCUPIED_SLOT);
						} else {
							player.sendMessage(Messages.LEAVE_VEHICLE);
						}
						return true;
					}
					HorseManage.loadCustomHorse(player, args[0], args[1], args[2]);
				}
				else if ((player.isInsideVehicle()) && ((player.getVehicle() instanceof AbstractHorse)))
				{
					HorseManage.saveCustomHorse(player, args[0], args[1], args[2]);
				}
				else
				{
					player.sendMessage(Messages.NOT_HORSE);
				}
			}
			else
			{
				sender.sendMessage(Messages.BAD_SYNTAX);
			}
		}
		else if (command.getName().equalsIgnoreCase("horselist")) {
			if (!sender.hasPermission("horsestables.list")) {
				sender.sendMessage(Messages.NO_PERMISSION);
			} else if (args.length == 2)
			{
				if (!sender.hasPermission("horsestables.list.other")) {
					sender.sendMessage(Messages.NO_PERMISSION);
				} else if (args[0].equalsIgnoreCase("stable")) {
					HorseSearch.asyncSearchAndPrintStable(sender, args[1]);
				} else if (args[0].equalsIgnoreCase("player")) {
					HorseSearch.asyncSearchAndPrintPlayer(sender, args[1]);
				} else if (args[0].equalsIgnoreCase("tamer")) {
					HorseSearch.asyncSearchAndPrintTamer(sender, args[1]);
				} else {
					sender.sendMessage(Messages.BAD_SYNTAX);
				}
			}
			else if ((args.length == 0) && ((sender instanceof Player))) {
				HorseSearch.asyncSearchAndPrintPlayer(sender, sender.getName());
			} else if ((args.length == 1) && (args[0].equalsIgnoreCase("tamed"))) {
				HorseSearch.asyncSearchAndPrintTamer(sender, sender.getName());
			} else {
				sender.sendMessage(Messages.BAD_SYNTAX);
			}
		}
		return true;
	}
}

final class HorseManage
{
	static final void loadCustomHorse(Player player, String stableName, String playerName, String slotNumber)
	{
		AbstractHorse horse = null;
		try
		{
			Object[] serializedHorse = (Object[])((CIMap)((CIMap)MemStorage.horses.get(stableName)).get(playerName)).get(slotNumber);

			System.out.println(serializedHorse[21]);
			horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), (EntityType)serializedHorse[21]);
			if (serializedHorse[21].equals(EntityType.HORSE))
			{
				((Horse)horse).setColor((Horse.Color)serializedHorse[3]);
				((Horse)horse).setStyle((Horse.Style)serializedHorse[18]);
			}
			else if (serializedHorse[21].equals(EntityType.LLAMA))
			{
				((Llama)horse).setColor((Llama.Color)serializedHorse[3]);
				if (((Boolean)serializedHorse[2]).booleanValue()) {
					((Llama)horse).setCarryingChest(true);
				}
			}
			else if (serializedHorse[21].equals(EntityType.MULE))
			{
				if (((Boolean)serializedHorse[2]).booleanValue()) {
					((Mule)horse).setCarryingChest(true);
				}
			}
			else if ((serializedHorse[21].equals(EntityType.DONKEY)) &&
					(((Boolean)serializedHorse[2]).booleanValue()))
			{
				((Donkey)horse).setCarryingChest(true);
			}
			if (((horse instanceof ChestedHorse)) &&
					(((Boolean)serializedHorse[2]).booleanValue())) {
				((ChestedHorse)horse).setCarryingChest(true);
			}
			horse.setAge(((Integer)serializedHorse[0]).intValue());
			horse.setBreed(((Boolean)serializedHorse[1]).booleanValue());
			horse.setCustomName((String)serializedHorse[4]);
			horse.setCustomNameVisible(((Boolean)serializedHorse[5]).booleanValue());
			horse.setDomestication(((Integer)serializedHorse[6]).intValue());
			horse.setFireTicks(((Integer)serializedHorse[7]).intValue());
			horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(((Double)serializedHorse[11]).doubleValue());
			horse.setHealth(
					Math.min(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), ((Double)serializedHorse[8]).doubleValue() > 53.0D ? 53.0D : ((Double)serializedHorse[8]).doubleValue()));

			horse.setJumpStrength(((Double)serializedHorse[9]).doubleValue());
			horse.setMaxDomestication(((Integer)serializedHorse[10]).intValue());
			horse.setMaximumAir(((Integer)serializedHorse[12]).intValue());
			horse.setMaximumNoDamageTicks(((Integer)serializedHorse[13]).intValue());
			horse.setNoDamageTicks(((Integer)serializedHorse[14]).intValue());
			if (serializedHorse[15] != null) {
				horse.setOwner(Bukkit.getServer().getPlayer((String)serializedHorse[15]));
			}
			horse.setRemainingAir(((Integer)serializedHorse[16]).intValue());
			horse.setRemoveWhenFarAway(((Boolean)serializedHorse[17]).booleanValue());
			horse.setTamed(((Boolean)serializedHorse[19]).booleanValue());
			horse.setTicksLived(((Integer)serializedHorse[20]).intValue());

			horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(((Double)serializedHorse[22]).doubleValue());

			Inventory horseInventory = horse.getInventory();

			ItemStack[] deserializedHorseInventory = SerializeItemStackList.deserializeItemStackList((List)serializedHorse[23]);
			for (int i = 0; i < deserializedHorseInventory.length; i++) {
				try
				{
					horseInventory.setItem(i, deserializedHorseInventory[i]);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			horse.setPassenger(player);

			deleteCustomHorse(stableName, playerName, slotNumber);

			DiskStorage.asyncSaveHorses();

			player.sendMessage(Messages.LOADED_HORSE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (horse != null) {
				horse.remove();
			}
			player.sendMessage(Messages.UNEXPECTED);
		}
	}

	static final void saveCustomHorse(Player player, String stableName, String playerName, String slotNumber)
	{
		try
		{
			AbstractHorse horse = (AbstractHorse)player.getVehicle();
			PotionEffect effect;
			for (Iterator localIterator = horse.getActivePotionEffects().iterator(); localIterator.hasNext(); horse.removePotionEffect(effect.getType())) {
				effect = (PotionEffect)localIterator.next();
			}
			Object[] serializedHorse = new Object[24];
			serializedHorse[0] = Integer.valueOf(horse.getAge());
			serializedHorse[1] = Boolean.valueOf(horse.canBreed());
			serializedHorse[2] = Boolean.valueOf(false);
			serializedHorse[3] = null;
			serializedHorse[4] = horse.getCustomName();
			serializedHorse[5] = Boolean.valueOf(horse.isCustomNameVisible());
			serializedHorse[6] = Integer.valueOf(horse.getDomestication());
			serializedHorse[7] = Integer.valueOf(horse.getFireTicks());
			serializedHorse[8] = Double.valueOf(horse.getHealth());
			serializedHorse[9] = Double.valueOf(horse.getJumpStrength());
			serializedHorse[10] = Integer.valueOf(horse.getMaxDomestication());
			serializedHorse[11] = Double.valueOf(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			serializedHorse[12] = Integer.valueOf(horse.getMaximumAir());
			serializedHorse[13] = Integer.valueOf(horse.getMaximumNoDamageTicks());
			serializedHorse[14] = Integer.valueOf(horse.getNoDamageTicks());
			serializedHorse[15] = (horse.getOwner() != null ? horse.getOwner().getName() : null);
			serializedHorse[16] = Integer.valueOf(horse.getRemainingAir());
			serializedHorse[17] = Boolean.valueOf(horse.getRemoveWhenFarAway());
			serializedHorse[18] = null;
			serializedHorse[19] = Boolean.valueOf(horse.isTamed());
			serializedHorse[20] = Integer.valueOf(horse.getTicksLived());
			serializedHorse[21] = horse.getType();
			serializedHorse[22] = Double.valueOf(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());

			serializedHorse[23] = SerializeItemStackList.serializeItemStackList(horse.getInventory().getContents());
			if ((horse instanceof Horse))
			{
				serializedHorse[3] = ((Horse)horse).getColor();
				serializedHorse[18] = ((Horse)horse).getStyle();
			}
			else if ((horse instanceof Llama))
			{
				serializedHorse[3] = ((Llama)horse).getColor();
			}
			if ((horse instanceof ChestedHorse)) {
				serializedHorse[2] = Boolean.valueOf(((ChestedHorse)horse).isCarryingChest());
			}
			horse.getName();

			System.out.println("TIPO: " + serializedHorse[21]);

			CIMap<String, Object[]> slotStorage = new CIMap();
			slotStorage.put(slotNumber, serializedHorse);

			CIMap<String, CIMap<String, Object[]>> playerStorage = new CIMap();
			playerStorage.put(playerName, slotStorage);
			if (MemStorage.horses.containsKey(stableName))
			{
				if (((CIMap)MemStorage.horses.get(stableName)).containsKey(playerName)) {
					((CIMap)((CIMap)MemStorage.horses.get(stableName)).get(playerName)).putAll(slotStorage);
				} else {
					((CIMap)MemStorage.horses.get(stableName)).putAll(playerStorage);
				}
			}
			else {
				MemStorage.horses.put(stableName, playerStorage);
			}
			horse.remove();

			DiskStorage.asyncSaveHorses();

			player.sendMessage(Messages.SAVED_HORSE);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			player.sendMessage(Messages.UNEXPECTED);
		}
	}

	static final void deleteCustomHorse(CommandSender sender, String stableName, String playerName, String slotNumber)
	{
		try
		{
			deleteCustomHorse(stableName, playerName, slotNumber);

			sender.sendMessage(Messages.DELETED_HORSE);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			sender.sendMessage(Messages.UNEXPECTED);
		}
	}

	static final void deleteCustomHorse(String stableName, String playerName, String slotNumber)
			throws Exception
	{
		((CIMap)((CIMap)MemStorage.horses.get(stableName)).get(playerName)).remove(slotNumber);
		if (((CIMap)((CIMap)MemStorage.horses.get(stableName)).get(playerName)).isEmpty())
		{
			((CIMap)MemStorage.horses.get(stableName)).remove(playerName);
			if (((CIMap)MemStorage.horses.get(stableName)).isEmpty()) {
				MemStorage.horses.remove(stableName);
			}
		}
		DiskStorage.asyncSaveHorses();
	}
}

final class HorseSearch
{
	static final boolean horseExists(String stableName, String playerName, String slotNumber)
	{
		return (MemStorage.horses.containsKey(stableName)) && (((CIMap)MemStorage.horses.get(stableName)).containsKey(playerName)) && (((CIMap)((CIMap)MemStorage.horses.get(stableName)).get(playerName)).containsKey(slotNumber));
	}

	static final void asyncSearchAndPrintStable(final CommandSender sender, final String stableName)
	{
		sender.sendMessage(Messages.SEPARATOR);
		sender.sendMessage(Messages.STABLE_LIST_TITLE(stableName));

		Bukkit.getScheduler().runTaskAsynchronously(MemStorage.plugin, new Runnable()
		{
			public void run()
			{
				CIMap<String, CIMap<String, Object[]>> stableEntry = (CIMap)MemStorage.horses.get(stableName);

				ArrayList<String> chatMessageBuffer = new ArrayList();
				Iterator<Map.Entry<String, CIMap<String, Object[]>>> playerIterator;
				if (stableEntry != null)
				{
					playerIterator = stableEntry.entrySet().iterator();
					while (playerIterator.hasNext())
					{
						Map.Entry<String, CIMap<String, Object[]>> playerEntry = (Map.Entry)playerIterator.next();

						chatMessageBuffer.add(Messages.PLAYER_ENTRY((String)playerEntry.getKey()));

						Iterator<Map.Entry<String, Object[]>> slotIterator = ((CIMap)playerEntry.getValue()).entrySet().iterator();
						while (slotIterator.hasNext())
						{
							Map.Entry<String, Object[]> slotEntry = (Map.Entry)slotIterator.next();

							Object[] serializedHorse = (Object[])slotEntry.getValue();
							String horseType = Messages.HORSE_TYPE(serializedHorse[21]);
							String horseName = Messages.HORSE_NAME(serializedHorse[4]);

							chatMessageBuffer.add(Messages.HORSE_ENTRY((String)slotEntry.getKey(), horseType, horseName));
						}
					}
				}
				if (!chatMessageBuffer.isEmpty()) {
					for (String chatMessage : chatMessageBuffer) {
						sender.sendMessage(chatMessage);
					}
				} else {
					sender.sendMessage(Messages.NOT_FOUND);
				}
				sender.sendMessage(Messages.SEPARATOR);
			}
		});
	}

	static final void asyncSearchAndPrintPlayer(final CommandSender sender, final String playerName)
	{
		sender.sendMessage(Messages.SEPARATOR);
		sender.sendMessage(Messages.PLAYER_LIST_TITLE(playerName));

		Bukkit.getScheduler().runTaskAsynchronously(MemStorage.plugin, new Runnable()
		{
			public void run()
			{
				ArrayList<String> chatMessageBuffer = new ArrayList();

				Iterator<Map.Entry<String, CIMap<String, CIMap<String, Object[]>>>> stableIterator = MemStorage.horses.entrySet().iterator();
				Map.Entry<String, CIMap<String, CIMap<String, Object[]>>> stableEntry;
				while (stableIterator.hasNext())
				{
					stableEntry = (Map.Entry)stableIterator.next();
					if (((CIMap)stableEntry.getValue()).containsKey(playerName))
					{
						chatMessageBuffer.add(Messages.STABLE_ENTRY((String)stableEntry.getKey()));

						Iterator<Map.Entry<String, Object[]>> slotIterator = ((CIMap)((CIMap)stableEntry.getValue()).get(playerName)).entrySet().iterator();
						while (slotIterator.hasNext())
						{
							Map.Entry<String, Object[]> slotEntry = (Map.Entry)slotIterator.next();

							Object[] serializedHorse = (Object[])slotEntry.getValue();
							String horseType = Messages.HORSE_TYPE(serializedHorse[21]);
							String horseName = Messages.HORSE_NAME(serializedHorse[4]);

							chatMessageBuffer.add(Messages.HORSE_ENTRY((String)slotEntry.getKey(), horseType, horseName));
						}
					}
				}
				if (!chatMessageBuffer.isEmpty()) {
					for (String chatMessage : chatMessageBuffer) {
						sender.sendMessage(chatMessage);
					}
				} else {
					sender.sendMessage(Messages.NOT_FOUND);
				}
				sender.sendMessage(Messages.SEPARATOR);
			}
		});
	}

	static final void asyncSearchAndPrintTamer(final CommandSender sender, final String tamerName)
	{
		sender.sendMessage(Messages.SEPARATOR);
		sender.sendMessage(Messages.TAMER_LIST_TITLE(tamerName));

		Bukkit.getScheduler().runTaskAsynchronously(MemStorage.plugin, new Runnable()
		{
			public void run()
			{
				ArrayList<String> chatMessageBufferStable = new ArrayList();

				Iterator<Map.Entry<String, CIMap<String, CIMap<String, Object[]>>>> stableIterator = MemStorage.horses.entrySet().iterator();
				Map.Entry<String, CIMap<String, CIMap<String, Object[]>>> stableEntry;
				while (stableIterator.hasNext())
				{
					stableEntry = (Map.Entry)stableIterator.next();

					ArrayList<String> chatMessageBufferPlayer = new ArrayList();

					Iterator<Map.Entry<String, CIMap<String, Object[]>>> playerIterator = ((CIMap)stableEntry.getValue()).entrySet().iterator();
					while (playerIterator.hasNext())
					{
						Map.Entry<String, CIMap<String, Object[]>> playerEntry = (Map.Entry)playerIterator.next();

						ArrayList<String> chatMessageBufferSlot = new ArrayList();

						Iterator<Map.Entry<String, Object[]>> slotIterator = ((CIMap)playerEntry.getValue()).entrySet().iterator();
						while (slotIterator.hasNext())
						{
							Map.Entry<String, Object[]> slotEntry = (Map.Entry)slotIterator.next();

							Object[] serializedHorse = (Object[])slotEntry.getValue();
							String horseType = Messages.HORSE_TYPE(serializedHorse[21]);
							String horseName = Messages.HORSE_NAME(serializedHorse[4]);
							if ((serializedHorse[15] != null) && (serializedHorse[15].toString().equalsIgnoreCase(tamerName))) {
								chatMessageBufferSlot.add("    " + Messages.HORSE_ENTRY((String)slotEntry.getKey(), horseType, horseName));
							}
						}
						if (!chatMessageBufferSlot.isEmpty())
						{
							chatMessageBufferPlayer.add("    " + Messages.PLAYER_ENTRY((String)playerEntry.getKey()));
							chatMessageBufferPlayer.addAll(chatMessageBufferSlot);
						}
					}
					if (!chatMessageBufferPlayer.isEmpty())
					{
						chatMessageBufferStable.add(Messages.STABLE_ENTRY((String)stableEntry.getKey()));
						chatMessageBufferStable.addAll(chatMessageBufferPlayer);
					}
				}
				if (!chatMessageBufferStable.isEmpty()) {
					for (String chatMessage : chatMessageBufferStable) {
						sender.sendMessage(chatMessage);
					}
				} else {
					sender.sendMessage(Messages.NOT_FOUND);
				}
				sender.sendMessage(Messages.SEPARATOR);
			}
		});
	}
}