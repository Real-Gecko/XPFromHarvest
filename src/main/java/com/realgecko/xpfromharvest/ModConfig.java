package com.realgecko.xpfromharvest;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = XpFromHarvest.MODID)
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

    @Mod.EventBusSubscriber
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(XpFromHarvest.MODID)) {
                ConfigManager.sync(XpFromHarvest.MODID, Config.Type.INSTANCE);
                XpFromHarvest.instance.ConfigUpdated();
            }
        }
    }
}
