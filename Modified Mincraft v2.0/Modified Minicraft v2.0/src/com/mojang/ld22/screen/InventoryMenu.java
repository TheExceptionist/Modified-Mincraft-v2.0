package com.mojang.ld22.screen;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolArmor;
import com.mojang.ld22.item.ToolType;

public class InventoryMenu extends Menu {
	private Player player;
	private int selected = 0;

	public InventoryMenu(Player player) {
		this.player = player;
	}

	public void tick() {
		if (input.menu.clicked)
			game.setMenu(null);

		if (input.up.clicked)
			selected--;
		if (input.down.clicked)
			selected++;

		int len = player.inventory.items.size();
		if (len == 0)
			selected = 0;
		if (selected < 0)
			selected += len;
		if (selected >= len)
			selected -= len;

		
		//Finsih Later
		if(input.favor.clicked && len > 0){
			Item item = player.inventory.items.get(selected);
			
			if(player.favorite.isEmpty()){
				player.favorite.add(item);
				
			}else{
				player.favorite.remove(0);
				player.favorite.add(item);
			}
		}
		
		if ((input.attack.clicked || input.menu.clicked) && len > 0) {
			// Made it so the inventory item is equal to null only if something
			// is selected
			Item item = player.inventory.items.remove(selected);
			//Figured out a problem when the add the active item, it threw the list off, so to fix this
			//I had to remove the item first
			
			// System.out.println(selected+" "+item+" "+player.inventory.items.get(selected));
			if(item instanceof ToolArmor){
				ToolArmor itemArmor = (ToolArmor)item;
				
				if(itemArmor.type == ToolType.helmet){
					if(player.helmet != null){
						player.inventory.items.add(0, player.helmet);
						player.armor -= player.helmet.getArmor();
						
					}
					
					player.helmet = itemArmor;
					player.armor += player.helmet.getArmor();
				}
				if(itemArmor.type == ToolType.chestplate){
					if(player.chestplate != null){
						player.inventory.items.add(0, player.chestplate);
						player.armor -= player.chestplate.getArmor();
					}

					player.chestplate = itemArmor;
					player.armor += player.chestplate.getArmor();
				}
				if(itemArmor.type == ToolType.greaves){
					if(player.greaves != null){
						player.inventory.items.add(0, player.greaves);
						player.armor -= player.greaves.getArmor();
					}
					
					player.greaves = itemArmor;
					player.armor += player.greaves.getArmor();
				}
				if(itemArmor.type == ToolType.boots){
					if(player.boots != null){
						player.inventory.items.add(0, player.boots);
						player.armor -= player.boots.getArmor();
					}
					
					player.boots = itemArmor;
					player.armor += player.boots.getArmor();
				}
				if(itemArmor.type == ToolType.gloves){
					if(player.gloves != null){
						player.inventory.items.add(0, player.gloves);
						player.armor -= player.gloves.getArmor();
					}
					
					player.gloves = itemArmor;
					player.armor += player.gloves.getArmor();
				}
			}else{
				if (player.activeItem != null) {
					// adds the active item to the inventory
					player.inventory.items.add(0, player.activeItem);
				}
				
				player.activeItem = item;
			}
			game.setMenu(null);
		}
		
		if(input.escape.clicked) game.setMenu(null);
	}

	public void render(Screen screen) {
		Font.renderFrame(screen, "inventory", 1, 1, 16, 11);
		renderItemList(screen, 1, 1, 16, 11, player.inventory.items, selected);
	}
}