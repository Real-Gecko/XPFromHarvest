package com.realgecko.xpfromharvest;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class OptionsHolder
{
	public static class Common
	{
		private static final double chance = 80;
		private static final int xpAmount = 3;
		private static final boolean defaultSimple = true;

		public final ConfigValue<Double> Chance;
		public final ConfigValue<Integer> XpAmount;
		public final ConfigValue<Boolean> Simple;


		public Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("category1");
			this.Chance = builder.comment("% Chance of get xp ")
					.worldRestart()
					.defineInRange("% Chance ", chance, 1, 100);			
			this.XpAmount = builder.comment("XpAmount get for each crop")
					.define("XpAmount", xpAmount);			
			this.Simple = builder.comment("Enables Simple Harvest whith right click")
					.define("Simple Harvest", defaultSimple);
			
			builder.pop();
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static //constructor
	{
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = commonSpecPair.getLeft();
		COMMON_SPEC = commonSpecPair.getRight();
	}
}
