package com.realgecko.xpfromharvest;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = XPFromHarvest.MODID)
public class ModConfig {
    @Config.Comment("Chance in % of XP orb spawning after harvest done")
    @Config.Name("Chance")
    @Config.RangeInt(min = 1, max = 100)
    public static int chance = 100;

    @Config.Comment("Amount of XP given")
    @Config.Name("XP Amount")
    @Config.RangeInt(min = 1)
    public static int xpAmount = 1;

    @Config.Comment("Enable simple harvesting and replanting with right click")
    @Config.Name("Simple Harvest")
    public static boolean simpleHarvest = false;

    @Config.Comment("List of crops to process with their ages")
    @Config.Name("Crops List")
    public static String[] crops = {
            "minecraft:potatoes[age=7]",
            "minecraft:carrots[age=7]",
            "minecraft:wheat[age=7]",
            "minecraft:beetroots[age=3]",
            "minecraft:nether_wart[age=3]"
    };

    @Config.Comment("Curiosity Mode: sneak + right click with on block to get info in chat")
    @Config.Name("Curiosity")
    public static boolean curiosity = false;

    @Mod.EventBusSubscriber
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(XPFromHarvest.MODID)) {
                ConfigManager.sync(XPFromHarvest.MODID, Config.Type.INSTANCE);
                XPFromHarvest.instance.ConfigUpdated();
            }
        }
    }
}
