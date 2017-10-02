package com.mojang.ld22.item;

import java.util.Random;

public class ToolType {
	public static ToolType shovel = new ToolType("Shvl", 0);
	public static ToolType hoe = new ToolType("Hoe", 1);
	public static ToolType sword = new ToolType("Swrd", 2);
	public static ToolType pickaxe = new ToolType("Pick", 3);
	public static ToolType axe = new ToolType("Axe", 4);
	public static ToolType helmet = new ToolType("Helm", 5);
	public static ToolType chestplate = new ToolType("Plte", 7);
	public static ToolType greaves = new ToolType("Grev", 7);
	public static ToolType boots = new ToolType("Boot", 8);
	public static ToolType gloves = new ToolType("Glov", 9);
	public static ToolType wand = new ToolType("Wand", 6);
	public static ToolType bow = new ToolType("Bow", 5);

	public String name;
	public int sprite;
	
	private Random random = new Random();

	private ToolType(String name, int sprite) {
		this.name = name;
		this.sprite = sprite;
	}
}
