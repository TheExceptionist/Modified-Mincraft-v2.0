package com.mojang.ld22.entity;

import com.mojang.ld22.crafting.FurnitureRecipe;
import com.mojang.ld22.crafting.ResourceRecipe;
import com.mojang.ld22.crafting.ToolRecipe;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.screen.ContainerMenu;

public class Chest extends Furniture {
	public Inventory inventory = new Inventory();
	
	public Chest() {
		super("Chest");
		col = Color.get(-1, 110, 331, 552);
		sprite = 1;
	}
	
	public Chest(int x, int y) {
		super("Chest");
		col = Color.get(-1, 110, 331, 552);
		sprite = 1;
		this.x = x;
		this.y = y;
		System.out.println("Gen Chest Chest");
		while(inventory.items.isEmpty()){
			genItems();
		}
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new ContainerMenu(player, "Chest", inventory));
		return true;
	}
	
	private void genItems(){
		if(random.nextInt(100) <= 15){
			inventory.items.add(0, new FurnitureItem(new Lantern()));
		}
		
		if(random.nextInt(100) <= 10){
			inventory.items.add(0, new FurnitureItem(new Oven()));
		}
		
		if(random.nextInt(100) <= 25){
			inventory.items.add(0, new FurnitureItem(new Workbench()));
		}
		
		if(random.nextInt(100) <= 25){
			inventory.items.add(0, new FurnitureItem(new Chest()));
		}
		
		if(random.nextInt(100) <= 5){
			inventory.items.add(0, new FurnitureItem(new Anvil()));
		}
		//
		if(random.nextInt(100) <= 5){
			inventory.items.add(0, new ToolItem(ToolType.sword, random.nextInt(5)));
		}
		
		if(random.nextInt(100) <= 15){
			inventory.items.add(0, new ToolItem(ToolType.axe, random.nextInt(5)));
		}
		
		if(random.nextInt(100) <= 20){
			inventory.items.add(0, new ToolItem(ToolType.pickaxe, random.nextInt(5)));
		}
		
		if(random.nextInt(100) <= 25){
			inventory.items.add(0, new ToolItem(ToolType.hoe, random.nextInt(5)));
		}
		
		if(random.nextInt(100) <= 30){
			inventory.items.add(0, new ToolItem(ToolType.shovel, random.nextInt(5)));
		}
		//
		if(random.nextInt(100) <= 5){
			inventory.items.add(0, new ResourceItem(Resource.ironIngot));
		}
		
		if(random.nextInt(100) <= 15){
			inventory.items.add(0, new ResourceItem(Resource.goldIngot));
		}
		
		if(random.nextInt(100) <= 20){
			inventory.items.add(0, new ResourceItem(Resource.glass));
		}
		
		if(random.nextInt(100) <= 5){
			inventory.items.add(0, new ResourceItem(Resource.gem));
		}
		
		if(random.nextInt(100) <= 10){
			inventory.items.add(0, new ResourceItem(Resource.bread));
		}
	}
}