package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;

public class Archer extends Mob{
	private int xa, ya;
	private int lvl;
	private int randomWalkTime = 0;
	private int bonus = 0;
	private int attackTime = 0;
	private boolean isDamage = false;
	
	public Archer(int lvl){
		this.lvl = lvl;
		x = random.nextInt(64 * 16);
		y = random.nextInt(64 * 16);
		health = maxHealth = lvl * 10;
		
		inventory.items.add(new ToolItem(ToolType.bow, random.nextInt(lvl)));
		inventory.items.add(new ResourceItem(Resource.arrow, random.nextInt(lvl * lvl * 10)));
	}
	
	public void tick() {
		super.tick();
		
	//	boolean isAiming = false;

		
		
		for(int i = 0; i < inventory.items.size(); i++){
			Item item = inventory.items.get(i);
			
			if(health < maxHealth / 2){
				if(item instanceof ResourceItem && ((ResourceItem)item).resource == Resource.apple){
					heal(maxHealth / 2);
					inventory.items.remove(item);
				}
			}

			if (item instanceof ToolItem) {
				//Will render later
				ToolItem tool = (ToolItem)item;
				
				if(tool.type == ToolType.bow){
					activeItem = tool;
					
					if (level.player != null && randomWalkTime == 0) {
						int xd = level.player.x - x;
						int yd = level.player.y - y;
						
						if(attackTime <= 0){
							if(dir == 0 && xd == 0){
								shoot();
							}
							if(dir == 1 && xd == 0){
								shoot();
							}
							if(dir == 2 && yd == 0){
								shoot();
							}
							
							if(dir == 3 && yd == 0){
								shoot();
							}
							attackTime = 120;
						}else{
							attackTime--;
						}
						
						if (xd * xd + yd * yd < 70 * 70) {
							xa = 0;
							ya = 0;
						
							
							
							if(xd == 0 || yd == 0){
								if(xd < 0) dir = 2;
								if(xd > 0) dir = 3;
								if(yd < 0) dir = 1;
								if(yd > 0) dir = 0;
								
								//isAiming = true;
								//System.out.println(dir+" - "+xd+" : "+yd);
							}else{
								if(Math.abs(xd) < Math.abs(yd)){
									if (xd < 0) xa = -1;
									if (xd > 0) xa = +1;
								}else{
									if (yd < 0) ya = -1;
									if (yd > 0) ya = +1;
								}
							}
							
							/*if(xd != 0 && yd != 0){
								if (xd < 0) xa = -1;
								if (xd > 0) xa = +1;
								if (yd < 0) ya = -1;
								if (yd > 0) ya = +1;
							}else{
								if(xd == 0){
									if(yd < 0) dir = 3;
									if(yd > 0) dir = 2;
								}
								if(yd == 0){
									if(xd < 0) dir = 1;
									if(xd > 0) dir = 0;
								}
							}*/
							
							
						}
					}
					/*level.add(new Arrow(this, dir, arrow));
					arrow.count--;
					
					if(arrow.isDepleted()){
						inventory.items.remove(arrow);
					}*/
				}
			}
		}
		
		//if(!isAiming){
			int speed = tickTime & 1;
			if (!move(xa * speed, ya * speed) || random.nextInt(200) == 0) {
				randomWalkTime = 60;
				xa = (random.nextInt(3) - 1) * random.nextInt(2);
				ya = (random.nextInt(3) - 1) * random.nextInt(2);
			}
			if (randomWalkTime > 0) randomWalkTime--;
		//}
	}

	public void shoot(){
	//	System.out.println("Shoot");
		ResourceItem arrow = inventory.findResource(Resource.arrow);
		
		if(arrow != null){			
			if(arrow.isDepleted()){
				inventory.items.remove(arrow);
			}else{
				level.add(new Arrow(this, dir, arrow));
				arrow.count--;
			}
		}
	}
	
	public void render(Screen screen) {
		//X valus of the tile on the icons png
		int xt = 8;
		//Y values of the tile on the icons png
		int yt = 20;

		int flip1 = (walkDist >> 3) & 1;
		int flip2 = (walkDist >> 3) & 1;

		if (dir == 1) {
			//Sets xt to the next sprite across
			xt += 2;
		}
		if (dir > 1) {

			flip1 = 0;
			//always been 0-1 if odd/even, for some reason allows for faster sprite flipping.
			
			flip2 = ((walkDist >> 4) & 1);
			//System.out.println(flip2+" : "+this);
			if (dir == 2) {
				flip1 = 1;
			}
			
			//Step animation every 8 steps
			// x/2^n
			xt += 4 + ((walkDist >> 3) & 1) * 2;
		}

		int xo = x - 8;
		int yo = y - 11;

		int col = Color.get(-1, 000, 550, 333);
		if (lvl == 2) col = Color.get(-1, 100, 550, 500);
		if (lvl == 3) col = Color.get(-1, 111, 550, 030);
		if (lvl == 4) col = Color.get(-1, 000, 500, 004);
		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}

		
		//Render all four 32 bit sprites
		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
		screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
		screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
		
		if(isDamage){
			//Will render attack item here later
			//attackItem.renderIcon(screen, x + 8 + 4, y + 4);
			isDamage = false;
		}
	}
	
	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, lvl + 1 + bonus, dir);
			if(bonus > 0){
				isDamage = true;
			}
		}
	}

	protected void die() {
		super.die();

		if (level.player != null) {
			level.player.score += 200 * lvl;
		}

	}
}
