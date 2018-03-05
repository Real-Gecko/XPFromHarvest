package com.realgecko.xpfromharvest;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {

    public final static Config instance = new Config();
    int chance;

    void load(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        chance = config.getInt(
                "chance",
                Configuration.CATEGORY_GENERAL,
                100,
                1,
                100,
                "Chance in % of XP orb spawning after harvest done"
        ) - 1;
        if (config.hasChanged()) config.save();
    }
}
