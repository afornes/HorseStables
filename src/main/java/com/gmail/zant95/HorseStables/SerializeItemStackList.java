package com.gmail.zant95.HorseStables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class SerializeItemStackList
{
	public static final List<HashMap<Map<String, Object>, Map<String, Object>>> serializeItemStackList(ItemStack[] itemStackList)
	{
		List<HashMap<Map<String, Object>, Map<String, Object>>> serializedItemStackList = new ArrayList();
		for (ItemStack itemStack : itemStackList)
		{
			HashMap<Map<String, Object>, Map<String, Object>> serializedMap = new HashMap();
			if (itemStack == null) {
				itemStack = new ItemStack(Material.AIR);
			}
			Map<String, Object> serializedItemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta().serialize() : null;

			itemStack.setItemMeta(null);
			Map<String, Object> serializedItemStack = itemStack.serialize();

			serializedMap.put(serializedItemStack, serializedItemMeta);
			serializedItemStackList.add(serializedMap);
		}
		return serializedItemStackList;
	}

	public static final ItemStack[] deserializeItemStackList(List<HashMap<Map<String, Object>, Map<String, Object>>> serializedItemStackList)
	{
		ItemStack[] itemStackList = new ItemStack[serializedItemStackList.size()];

		int i = 0;
		for (HashMap<Map<String, Object>, Map<String, Object>> serializedItemStackMap : serializedItemStackList)
		{
			Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack = (Map.Entry)serializedItemStackMap.entrySet().iterator().next();

			ItemStack itemStack = ItemStack.deserialize((Map)serializedItemStack.getKey());
			if (serializedItemStack.getValue() != null)
			{
				ItemMeta itemMeta = (ItemMeta)ConfigurationSerialization.deserializeObject((Map)serializedItemStack.getValue(), ConfigurationSerialization.getClassByAlias("ItemMeta"));
				itemStack.setItemMeta(itemMeta);
			}
			itemStackList[(i++)] = itemStack;
		}
		return itemStackList;
	}
}