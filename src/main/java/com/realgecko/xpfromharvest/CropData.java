package com.realgecko.xpfromharvest;

import java.util.Arrays;
import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CropData {
	public CropData(int age) {
		this.age = age;
	}
	public CropData(int age, Boolean rightClick) {
		this.age = age;
		this.rightClick = rightClick;
	}
	
	public Integer age;
	public Boolean rightClick;
	public Boolean forceFortune;
	public List<String> dropBlacklist;
	
	public int getAge() {
		return age != null ? age : -1;
	}
	
	public boolean getRightClick() {
		return rightClick != null ? rightClick : false;
	}
	
	public boolean getForceFortune() {
		return forceFortune != null ? forceFortune : false;
	}
	
	public List<String> getDropBlacklist() {
		return dropBlacklist != null ? dropBlacklist : Arrays.asList();
	}
	
	public static int getAgeFromBlockState(BlockState state) {
		int age = -1;
		for (Property<?> prop : state.getProperties())
			if (prop.getName().equals("age"))
				age = (Integer)state.getValue(prop);
		return age;
	}
}
