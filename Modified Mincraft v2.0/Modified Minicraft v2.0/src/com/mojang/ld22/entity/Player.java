package com.mojang.ld22.entity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.ld22.Game;
import com.mojang.ld22.InputHandler;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.PowerGloveItem;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolArmor;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.screen.EscMenu;
import com.mojang.ld22.screen.InventoryMenu;
import com.mojang.ld22.sound.Sound;

public class Player extends Mob {
	private InputHandler input;
	private int attackTime, attackDir;
	
	public List<Item> favorite = new ArrayList<Item>();

	public Game game;
	public Item attackItem;
	public int stamina;
	public int staminaRecharge;
	public int staminaRechargeDelay;
	public int score;
	public int maxStamina = 10;
	private int onStairDelay;
	private int toolCho = random.nextInt(6);
	public int invulnerableTime = 0;

	public Player(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
		x = 24;
		y = 24;
		stamina = maxStamina;
		
		//rmor = 24;
		//Adds a power glove and workbench to the player when they spawn

		inventory.add(new FurnitureItem(new Workbench()));
		inventory.add(new PowerGloveItem());
		
		if(toolCho == 0){
			inventory.add(new ToolItem(ToolType.sword, random.nextInt(1)));
		}
		if(toolCho == 1){
			inventory.add(new ToolItem(ToolType.axe, random.nextInt(1)));
		}
		if(toolCho == 2){
			inventory.add(new ToolItem(ToolType.hoe, random.nextInt(1)));
		}
		if(toolCho == 3){
			inventory.add(new ToolItem(ToolType.shovel, random.nextInt(1)));
		}
		if(toolCho == 4){
			inventory.add(new ToolItem(ToolType.pickaxe, random.nextInt(1)));
		}
		if(toolCho == 5){
			inventory.add(new ResourceItem(Resource.arrow, 5));
			inventory.add(new ToolItem(ToolType.bow, random.nextInt(1)));
		}
		
		//inventory.add(new ResourceItem(Resource.wood, 100));
		/*inventory.add(new ResourceItem(Resource.slime, 60));
		
		inventory.add(new ToolItem(ToolType.wand, 0));
		inventory.add(new ToolItem(ToolType.wand, 1));
		inventory.add(new ToolItem(ToolType.wand, 2));
		inventory.add(new ToolItem(ToolType.wand, 3));
		inventory.add(new ToolItem(ToolType.wand, 4));*/
		/*inventory.add(new ResourceItem(Resource.arrow, 60));
		inventory.add(new ToolItem(ToolType.bow, 4));
		inventory.add(new ToolItem(ToolType.pickaxe, 4));*/
		/*inventory.add(new ToolArmor(ToolType.helmet, 1));
		inventory.add(new ToolArmor(ToolType.chestplate, 1));
		inventory.add(new ToolArmor(ToolType.greaves, 1));
		inventory.add(new ToolArmor(ToolType.boots, 1));
		inventory.add(new ToolArmor(ToolType.gloves, 1));*/
	}

	int time = 0;
	
	public void tick() {
		super.tick();

		if (invulnerableTime > 0) invulnerableTime--;
		Tile onTile = level.getTile(x >> 4, y >> 4);
		if (onTile == Tile.stairsDown || onTile == Tile.stairsUp) {
			if (onStairDelay == 0) {
				changeLevel((onTile == Tile.stairsUp) ? 1 : -1);
				onStairDelay = 10;
				return;
			}
			onStairDelay = 10;
		} else {
			if (onStairDelay > 0) onStairDelay--;
		}
		
		if (stamina <= 0 && staminaRechargeDelay == 0 && staminaRecharge == 0) {
			staminaRechargeDelay = 40;
		}

		if (staminaRechargeDelay > 0) {
			staminaRechargeDelay--;
		}

		if (staminaRechargeDelay == 0) {
			staminaRecharge++;
			if (isSwimming()) {
				staminaRecharge = 0;
			}
			while (staminaRecharge > 10) {
				staminaRecharge -= 10;
				if (stamina < maxStamina) stamina += 2;
			}
		}
		//assuming xa and ya are the same as velX and velY
		int xa = 0;
		int ya = 0;
		//the 3rd .down means pressed 
		if (input.up.down) ya--;
		if (input.down.down) ya++;
		if (input.left.down) xa--;
		if (input.right.down) xa++;
		
		/*if(input.attack.down) {
			if (time <= 0) {
				level.add(new Arrow(this, -1, 0));
				time = 100;
			}
		}*/
		
		if(time > 0)
			time--;
		
		if (isSwimming() && tickTime % 60 == 0) {
			if (stamina > 0) {
				stamina--;
			} else {
				hurt(this, 1, dir ^ 1);
			}
		}

		if (staminaRechargeDelay % 2 == 0) {
			move(xa, ya);
		}
		
		//System.out.println(armor);
		
		if (input.attack.clicked) {
			if (stamina == 0) {

			} else {
				stamina--;
				staminaRecharge = 0;
				attack();
			}
		}
		if (input.menu.clicked) {
			game.setMenu(new InventoryMenu(this));
		}
		if(input.interact.clicked){
			use();
		}
		if (input.escape.clicked) {
			if (!use()) {
				game.setMenu(new EscMenu(this));
			}
		}
		//Will fix later
		if(input.favor.clicked){
			if(game.getMenu() == null){
				if(!favorite.isEmpty()){
					if(activeItem != null){
						inventory.items.add(0, activeItem);
					}
					
					Item item = favorite.get(0);
					
					inventory.items.remove(item);
					
					activeItem = item;
				}
			}
		}
		
		if(input.unequip.clicked){
			if (activeItem != null) {
				//adds the active item to the inventory
				inventory.items.add(0, activeItem);
				//makes the active item null
				activeItem = null;
			}
		}
		if (attackTime > 0) attackTime--;

	}

	private boolean use() {		
		int yo = -2;
		if (dir == 0 && use(x - 8, y + 4 + yo, x + 8, y + 12 + yo)) return true;
		if (dir == 1 && use(x - 8, y - 12 + yo, x + 8, y - 4 + yo)) return true;
		if (dir == 3 && use(x + 4, y - 8 + yo, x + 12, y + 8 + yo)) return true;
		if (dir == 2 && use(x - 12, y - 8 + yo, x - 4, y + 8 + yo)) return true;

		int xt = x >> 4;
		int yt = (y + yo) >> 4;
		int r = 12;
		if (attackDir == 0) yt = (y + r + yo) >> 4;
		if (attackDir == 1) yt = (y - r + yo) >> 4;
		if (attackDir == 2) xt = (x - r) >> 4;
		if (attackDir == 3) xt = (x + r) >> 4;

		if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h) {
			if (level.getTile(xt, yt).use(level, xt, yt, this, attackDir)) return true;
		}

		return false;
	}
	
	private void attack() {
		walkDist += 8;
		attackDir = dir;
		attackItem = activeItem;
		boolean done = false;

		if (activeItem != null) {
			if(activeItem instanceof ToolItem){
				ToolItem item = (ToolItem)activeItem;
				
				ResourceItem arrow = inventory.findResource(Resource.arrow);
				
				if(item.type == ToolType.bow && arrow != null){
					level.add(new Arrow(this, dir, arrow));
					arrow.count--;
					
					Sound.bow.play();
					
					if(arrow.isDepleted()){
						inventory.items.remove(arrow);
					}
				}
			}
			
			
			attackTime = 10;
			int yo = -2;
			int range = 12;
			if (dir == 0 && interact(x - 8, y + 4 + yo, x + 8, y + range + yo)) done = true;
			if (dir == 1 && interact(x - 8, y - range + yo, x + 8, y - 4 + yo)) done = true;
			if (dir == 3 && interact(x + 4, y - 8 + yo, x + range, y + 8 + yo)) done = true;
			if (dir == 2 && interact(x - range, y - 8 + yo, x - 4, y + 8 + yo)) done = true;

			//System.out.println(dir);

			//0 down
			//1 up
			//3 left
			//2 right
			
			if (done){
				return;
			} 

			int xt = x >> 4;
			int yt = (y + yo) >> 4;
			int r = 12;
			if (attackDir == 0) yt = (y + r + yo) >> 4;
			if (attackDir == 1) yt = (y - r + yo) >> 4;
			if (attackDir == 2) xt = (x - r) >> 4;
			if (attackDir == 3) xt = (x + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h) {
				if(activeItem instanceof ToolItem){
					ToolItem item = (ToolItem) activeItem;
					ResourceItem slime = inventory.findResource(Resource.slime);
					boolean success = 75 +  (item.level * 5) > random.nextInt(100);
				
					if(!success && item.type == ToolType.wand){
						level.add(new TextParticle("Spell Failed!", x, y, Color.get(-1, 500, 500, 500)));
					}
					
					if(item.type == ToolType.wand && slime != null && success){
						if(item.level == 0) level.setTile(xt, yt, Tile.dirt, 0);
						if(item.level == 1) level.setTile(xt, yt, Tile.tree, 0);
						if(item.level == 2) level.setTile(xt, yt, Tile.rock, 0);
						if(item.level == 3) level.setTile(xt, yt, Tile.water, 0);
						if(item.level == 4) level.setTile(xt, yt, Tile.cactus, 0);
						slime.count--;
						
						level.add(new TextParticle("Spell Casted!", x, y, Color.get(-1, 005, 005, 005)));
						Sound.magic.play();
						
						if(slime.isDepleted()){
							inventory.items.remove(slime);
						}
					}
				} 
				if (activeItem.interactOn(level.getTile(xt, yt), level, xt, yt, this, attackDir)) {
					done = true;
					useItem(activeItem);
				} else {
					if (level.getTile(xt, yt).interact(level, xt, yt, this, activeItem, attackDir)) {
						done = true;
						useItem(activeItem);
					}
				}
				
				if (activeItem.isDepleted()) {
					activeItem = null;
				}
			}
		}

		if (done) return;

		if (activeItem == null || activeItem.canAttack()) {
			attackTime = 5;
			int yo = -2;
			int range = 20;
			if (dir == 0) hurt(x - 8, y + 4 + yo, x + 8, y + range + yo);
			if (dir == 1) hurt(x - 8, y - range + yo, x + 8, y - 4 + yo);
			if (dir == 3) hurt(x + 4, y - 8 + yo, x + range, y + 8 + yo);
			if (dir == 2) hurt(x - range, y - 8 + yo, x - 4, y + 8 + yo);

			int xt = x >> 4;
			int yt = (y + yo) >> 4;
			int r = 12;
			if (attackDir == 0) yt = (y + r + yo) >> 4;
			if (attackDir == 1) yt = (y - r + yo) >> 4;
			if (attackDir == 2) xt = (x - r) >> 4;
			if (attackDir == 3) xt = (x + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h) {
				level.getTile(xt, yt).hurt(level, xt, yt, this, random.nextInt(3) + 1, attackDir);
				useItem(activeItem);
			}
		}

	}

	private boolean use(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.use(this, attackDir)) return true;
		}
		return false;
	}

	private boolean interact(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.interact(this, activeItem, attackDir)) return true;
		}
		return false;
	}

	private void hurt(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) e.hurt(this, getAttackDamage(e), attackDir);
		}
	}

	private int getAttackDamage(Entity e) {
		int dmg = random.nextInt(3) + 1;
		if (attackItem != null) {
			dmg += attackItem.getAttackDamageBonus(e);
		}
		return dmg;
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 14;

		int flip1 = (walkDist >> 3) & 1;
		int flip2 = (walkDist >> 3) & 1;

		if (dir == 1) {
			xt += 2;
		}
		if (dir > 1) {
			flip1 = 0;
			flip2 = ((walkDist >> 4) & 1);
			if (dir == 2) {
				flip1 = 1;
			}
			xt += 4 + ((walkDist >> 3) & 1) * 2;
		}

		int xo = x - 8;
		int yo = y - 11;
		if (isSwimming()) {
			yo += 4;
			int waterColor = Color.get(-1, -1, 115, 335);
			if (tickTime / 8 % 2 == 0) {
				waterColor = Color.get(-1, 335, 5, 115);
			}
			screen.render(xo + 0, yo + 3, 5 + 13 * 32, waterColor, 0);
			screen.render(xo + 8, yo + 3, 5 + 13 * 32, waterColor, 1);
		}

		if (attackTime > 0 && attackDir == 1) {
			screen.render(xo + 0, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
			screen.render(xo + 8, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
			if (attackItem != null) {
				attackItem.renderIcon(screen, xo + 4, yo - 4);
			}
		}
		int col = Color.get(-1, 100, 220, 532);
		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}

		if (activeItem instanceof FurnitureItem) {
			yt += 2;
		}
		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
		if (!isSwimming()) {
			screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
			screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
		}

		if (attackTime > 0 && attackDir == 2) {
			screen.render(xo - 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
			screen.render(xo - 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
			if (attackItem != null) {
				attackItem.renderIcon(screen, xo - 4, yo + 4);
			}
		}
		if (attackTime > 0 && attackDir == 3) {
			screen.render(xo + 8 + 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
			screen.render(xo + 8 + 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
			if (attackItem != null) {
				attackItem.renderIcon(screen, xo + 8 + 4, yo + 4);
			}
		}
		if (attackTime > 0 && attackDir == 0) {
			screen.render(xo + 0, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
			screen.render(xo + 8, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
			if (attackItem != null) {
				attackItem.renderIcon(screen, xo + 4, yo + 8 + 4);
			}
		}

		if (activeItem instanceof FurnitureItem) {
			Furniture furniture = ((FurnitureItem) activeItem).furniture;
			furniture.x = x;
			furniture.y = yo;
			furniture.render(screen);

		}
	}

	public boolean canSwim() {
		return true;
	}

	public boolean findStartPos(Level level) {
		while (true) {
			int x = random.nextInt(level.w);
			int y = random.nextInt(level.h);
			//Player can spawn on grass and sand tiles now
			if (level.getTile(x, y) == Tile.grass || level.getTile(x, y) == Tile.sand) {
				this.x = x * 16 + 8;
				this.y = y * 16 + 8;
				return true;
			}
		}
	}

	public boolean payStamina(int cost) {
		if (cost > stamina) return false;
		stamina -= cost;
		return true;
	}

	public void changeLevel(int dir) {
		game.scheduleLevelChange(dir);
	}

	public int getLightRadius() {
		int r = 2;
		if (activeItem != null) {
			if (activeItem instanceof FurnitureItem) {
				int rr = ((FurnitureItem) activeItem).furniture.getLightRadius();
				if (rr > r) r = rr;
			}
		}
		return r;
	}

	protected void die() {
		super.die();
		Sound.playerDeath.play();
	}

	protected void touchedBy(Entity entity) {
		if (!(entity instanceof Player)) {
			entity.touchedBy(this);
		}
	}

	protected void doHurt(int damage, int attackDir) {
		if (hurtTime > 0 || invulnerableTime > 0) return;

		Sound.playerHurt.play();
		damage -= armor/2;
		
		if(damage < 0) damage = 0;
		
		health -= damage;
		level.add(new TextParticle("" + damage, x, y, Color.get(-1, 504, 504, 504)));
		
		
		if (helmet != null) {
			((ToolArmor)helmet).addDurablitily(-damage); 
			if (helmet.isDepleted()) {
				level.add(new TextParticle("" + helmet.getName()+" has broken!", x, y, Color.get(-1, 50, 50, 50)));
				helmet = null;
			}
		}
		if (chestplate != null){
			((ToolArmor)chestplate).addDurablitily(-damage);
			if (chestplate.isDepleted()) {
				level.add(new TextParticle("" + chestplate.getName()+" has broken!", x, y, Color.get(-1, 50, 50, 50)));
				chestplate = null;
			}
		}
		if (greaves != null){ 
			((ToolArmor)greaves).addDurablitily(-damage);
			if (greaves.isDepleted()) {
				level.add(new TextParticle("" + greaves.getName()+" has broken!", x, y, Color.get(-1, 50, 50, 50)));
				greaves = null;
			}
		}
		if (boots != null){ 
			((ToolArmor)boots).addDurablitily(-damage);
			if (boots.isDepleted()) {
				level.add(new TextParticle("" + boots.getName()+" has broken!", x, y, Color.get(-1, 50, 50, 50)));
				boots = null;
			}
		}
		if (gloves != null){ 
			((ToolArmor)gloves).addDurablitily(-damage);
			if (gloves.isDepleted()) {
				level.add(new TextParticle("" + gloves.getName()+" has broken!", x, y, Color.get(-1, 50, 50, 50)));
				gloves = null;
			}
		}
		
		
		
		if (attackDir == 0) yKnockback = +6;
		if (attackDir == 1) yKnockback = -6;
		if (attackDir == 2) xKnockback = -6;
		if (attackDir == 3) xKnockback = +6;
		hurtTime = 10;
		invulnerableTime = 30;
	}

	public void gameWon() {
		level.player.invulnerableTime = 60 * 5;
		game.won();
	}
}
