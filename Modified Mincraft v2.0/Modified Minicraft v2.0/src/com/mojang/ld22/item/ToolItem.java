package com.mojang.ld22.item;

import java.util.Random;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class ToolItem extends Item {
	private Random random = new Random();

	public static final int MAX_LEVEL = 5;
	public static final String[] LEVEL_NAMES = { //
	"Wood", "Rock", "Iron", "Gold", "Gem"//
	};

	public static final int[] LEVEL_COLORS = {//
	Color.get(-1, 100, 321, 431),//
			Color.get(-1, 100, 321, 111),//
			Color.get(-1, 100, 321, 555),//
			Color.get(-1, 100, 321, 550),//
			Color.get(-1, 100, 321, 055),//
	};

	public ToolType type;
	public int level = 0;
	public int durablitily = 100;
	
	public int attr = 9;
	
	public final static int[] attributes = {
		0, //Rusted
		1, //Cracked
		2, //Uncommon
		3, //Fine
		4, //Lucky
		5, //Expert
		6, //Rare
		7, //exquisite
		8  //Legendary
	};
	
	public final static String[] attributes_names = {
			"Rusted",
			"Cracked",
			"Uncommon",
			"Fine",
			"Lucky",
			"Expert",
			"Rare",
			"Exquisite",
			"Legendary",
			""
		};
	
	public final static int[][] attributes_bonus = {
			{-3, -1},
			{-2, 0},
			{-1, 1},
			{0, 2},
			{1, 3},
			{2, 4},
			{3, 5},
			{4, 6},
			{5, 7},
			{0, 0}
		};

	public ToolItem(ToolType type, int level) {
		this.type = type;
		this.level = level;
		this.durablitily = ((level+1) * 200)  - (random.nextInt(100) - 50);
		
		randomizedAttributes();
	}
	
	protected void randomizedAttributes() {
		//for(int i = 0; i < attributes.length; i++){
			if(random.nextInt(2) == 0) return;
			
			int rand = random.nextInt(100);
			
			if(rand < 10){
				attr = attributes[7];
			}if(rand < 20){
				attr = attributes[6];
			}if(rand < 30){
				attr = attributes[5];
			}if(rand < 40){
				attr = attributes[4];
			}if(rand < 50){
				attr = attributes[3];
			}if(rand < 60){
				attr = attributes[2];
			}if(rand < 70){
				attr = attributes[1];
			}
			else{
				attr = attributes[0];
			}
			
			durablitily += attributes_bonus[attr][random.nextInt(attributes_bonus[attr].length)] * 10;
			return;
		//}
	}

	public int getColor() {
		return LEVEL_COLORS[level];
	}

	public int getSprite() {
		return type.sprite + 5 * 32;
	}

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
	}

	public void renderInventory(Screen screen, int x, int y) {
		//if(type != ToolType.bow || type != ToolType.arrow){
		screen.render(x, y, getSprite(), getColor(), 0);
		//}
		
		Font.draw(getName(attr), screen, x + 8, y, Color.get(-1, 555, 555, 555));
		
		//Fast way to do this
		if(y == 204 && durablitily > 0){
			Font.draw("Dura: "+durablitily, screen, x + 8, y + 8, Color.get(-1, 555, 555, 555));
		}
	}

	protected String getName(int attr2) {
		// TODO Auto-generated method stub
		if(attributes_names[attr2].length() > 3){
			return attributes_names[attr2].substring(0, 3)+" "+getName();
		}else{
			return getName();
		}
	}

	public String getName() {
		return LEVEL_NAMES[level] + " " + type.name;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean canAttack() {
		return true;
	}

	public int getAttackDamageBonus(Entity e) {
		if (type == ToolType.axe) {
			return (level + 1) * 2 + random.nextInt(4) + attributes_bonus[attr][random.nextInt(attributes_bonus[attr].length)];
		}
		if (type == ToolType.sword) {
			return (level + 1) * 3 + random.nextInt(2 + level * level * 2) + attributes_bonus[attr][random.nextInt(attributes_bonus[attr].length)];
		}
		
		if (type == ToolType.bow) {
			return (level + 1) * 2 + random.nextInt(2 + level * 2) + attributes_bonus[attr][random.nextInt(attributes_bonus[attr].length)];
		}
		
		return 1;
	}

	public boolean matches(Item item) {
		if (item instanceof ToolItem) {
			ToolItem other = (ToolItem) item;
			if (other.type != type) return false;
			if (other.level != level) return false;
			return true;
		}
		return false;
	}
	
	public boolean isDepleted() {
		return durablitily <= 0;
	}
	
	public boolean hasDurablitily(){
		return true;
	}

	public void addDurablitily(int i) {
		durablitily += i;
	}

	public void randomize() {

	}
}