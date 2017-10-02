package com.mojang.ld22.item;

import java.util.Random;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class ToolArmor extends ToolItem{
	private Random random = new Random();
	public int armorBonus = 1;
	
	public ToolArmor(ToolType type, int level) {
		super(type, level);
		
		int bonus = 0;
		
		if(type == ToolType.helmet){
			bonus = 3;
		}
		if(type == ToolType.chestplate){
			bonus = 5;
		}
		if(type == ToolType.greaves){
			bonus = 4;
		}
		if(type == ToolType.boots){
			bonus = 2;
		}
		if(type == ToolType.gloves){
			bonus = 1;
		}
		
		this.armorBonus = level * bonus;
		
		int bonus2 = ToolItem.attributes_bonus[attr][random.nextInt(ToolItem.attributes_bonus[attr].length)];
		
		if(bonus2 <= 0) bonus2 = 1;
		
		armorBonus += (bonus2);
	}
	
	protected void randomizedAttributes() {
		super.randomizedAttributes();
	}
	
	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, 2 + 12 * 32, getColor(), 0);
		Font.draw(getName(attr), screen, x + 8, y, Color.get(-1, 555, 555, 555));
	}
	
	public int getArmor() {
		return armorBonus;
	}

}
