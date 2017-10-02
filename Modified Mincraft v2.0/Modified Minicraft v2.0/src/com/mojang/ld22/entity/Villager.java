package com.mojang.ld22.entity;

public class Villager extends Mob {
	public Villager(){
		x = random.nextInt(64 * 16);
		y = random.nextInt(64 * 16);
	}
}
