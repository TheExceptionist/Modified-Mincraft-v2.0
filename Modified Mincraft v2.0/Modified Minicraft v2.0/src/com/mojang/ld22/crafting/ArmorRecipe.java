package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.ToolArmor;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;

public class ArmorRecipe extends Recipe {
	private ToolType type;
	private int level;

	public ArmorRecipe(ToolType type, int level) {
		super(new ToolItem(type, level));
		this.type = type;
		this.level = level;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ToolArmor(type, level));
	}
}
