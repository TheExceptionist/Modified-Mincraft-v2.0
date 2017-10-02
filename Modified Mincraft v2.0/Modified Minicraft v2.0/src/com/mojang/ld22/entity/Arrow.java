package com.mojang.ld22.entity;

import java.util.List;

import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;

public class Arrow extends Entity{
	private int lifeTime;
	public double xa, ya;
	public double xx, yy;
	private int time;
	private Mob owner;
	private int dir;
	private ToolItem bow;
	private ResourceItem arrow;
	private int type;
	
	public Arrow(Mob owner, double xa, double ya) {
		this.owner = owner;
		xx = this.x = owner.x;
		yy = this.y = owner.y;
		
		if(owner.activeItem != null && owner.activeItem instanceof ToolItem){
			ToolItem item = (ToolItem)owner.activeItem;
			if(item.type == ToolType.bow){
				bow = item;
			}
		}
		
		xr = 0;
		yr = 0;

		this.xa = xa;
		this.ya = ya;

		lifeTime = 60 * 10 + random.nextInt(30);
	}
	
	public Arrow(Mob owner, int dir, ResourceItem arrow) {
		this.owner = owner;
		this.dir = dir;
		this.arrow = arrow;
		this.bow = (ToolItem)owner.activeItem;
		xx = this.x = owner.x;
		yy = this.y = owner.y;
		xr = 0;
		yr = 0;

		if (dir == 2) xa = -1;
		if (dir == 3) xa = 1;
		if (dir == 1) ya = -1;
		if (dir == 0) ya = 1;

		lifeTime = 60 * 10 + random.nextInt(30);
	}

	public void tick() {		
		time++;
		if (time >= lifeTime) {
			remove();
			return;
		}	
		
		xx += (xa * 5);
		yy += (ya * 5);
		x = (int) xx;
		y = (int) yy;

		if (xa < 0) dir = 2;
		if (xa > 0) dir = 3;
		if (ya < 0) dir = 1;
		if (ya > 0) dir = 0;
		
		int xt = x >> 4;
		int yt = y >> 4;
		
		boolean pass = level.getTile(xt, yt).mayPass(level, x, y, this);
		
		if(!pass){
			remove();
		}
		
		List<Entity> toHit = level.getEntities(x, y, x, y);
		
		for (int i = 0; i < toHit.size(); i++) {
			Entity e = toHit.get(i);
			if (e instanceof Mob && e != owner) {
				getHit();
				
				if(arrow != null && bow != null){
					e.hurt(owner, bow.getAttackDamageBonus(e) + arrow.getAttackDamageBonus(e), ((Mob) e).dir ^ 1);
				}else{
					e.hurt(owner, 1, ((Mob) e).dir ^ 1);					
				}
				
				System.out.println(e+" - "+bow+" : "+arrow);
			}
		}
	}
	
	public void getHit(){
		level.add(new SmashParticle(x, y));
	}
	

	public boolean isBlockableBy(Mob mob) {
		return false;
	}

	public void render(Screen screen) {
		if (time >= lifeTime - 6 * 20) {
			if (time / 6 % 2 == 0) return;
		}

		if(dir == 0){
			screen.render(x - 4, y - 4 - 2, 4 + 13 * 32, Color.get(-1, 000, 333, 555), 0);
			screen.render(x - 4, y - 4 - 2, 4 + 13 * 32, Color.get(-1, 333, 555, 000), 0);
		}
		if(dir == 1){
			screen.render(x - 4, y - 4 - 2, 3 + 12 * 32, Color.get(-1, 000, 333, 555), 0);
			screen.render(x - 4, y - 4 - 2, 3 + 12 * 32, Color.get(-1, 333, 555, 000), 0);
		}
		if(dir == 2){
			screen.render(x - 4, y - 4 - 2, 3 + 13 * 32, Color.get(-1, 000, 333, 555), 0);
			screen.render(x - 4, y - 4 - 2, 3 + 13 * 32, Color.get(-1, 333, 555, 000), 0);
		}
		if(dir == 3){
			screen.render(x - 4, y - 4 - 2, 4 + 12 * 32, Color.get(-1, 000, 333, 555), 0);
			screen.render(x - 4, y - 4 - 2, 4 + 12 * 32, Color.get(-1, 333, 555, 000), 0);
		}
		//screen.render(x - 4, y - 4 - 2, xt + yt * 16, Color.get(-1, 555, 555, 555), random.nextInt(4));
		//screen.render(x - 4, y - 4 + 2, xt + yt * 16, Color.get(-1, 000, 000, 000), random.nextInt(4));
	}
	
	public void remove(){
		super.remove();
		
		if(random.nextInt(2) == 0){
			level.add(new ItemEntity(new ResourceItem(Resource.arrow), x, y));
		}
		
		level.add(new SmashParticle(x, y));
	}
	
	public boolean canSwim() {
		return true;
	}
}
