package com.realgecko.xpfromharvest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XPFromHarvest.MODID, name = XPFromHarvest.NAME, version = XPFromHarvest.VERSION, acceptableRemoteVersions = "*")
public class XPFromHarvest {
    public static final String MODID = "xpfromharvest";
    public static final String NAME = "XP From Harvest";
    public static final String VERSION = "1.2.0";

    private BlockBreakHandler blockBreakHandler;
    private SimpleHarvestHandler simpleHarvestHandler;
    private CuriosityHandler curiosityHandler;

    @Mod.Instance(MODID)
    public static XPFromHarvest instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        blockBreakHandler = new BlockBreakHandler();
        simpleHarvestHandler = new SimpleHarvestHandler();
        curiosityHandler = new CuriosityHandler();

        MinecraftForge.EVENT_BUS.register(blockBreakHandler);

        if (ModConfig.simpleHarvest)
            MinecraftForge.EVENT_BUS.register(simpleHarvestHandler);

        if (ModConfig.curiosity)
            MinecraftForge.EVENT_BUS.register(curiosityHandler);
    }

    public void ConfigUpdated() {
        if (ModConfig.simpleHarvest)
            MinecraftForge.EVENT_BUS.register(simpleHarvestHandler);
        else
            MinecraftForge.EVENT_BUS.unregister(simpleHarvestHandler);

        if (ModConfig.curiosity)
            MinecraftForge.EVENT_BUS.register(curiosityHandler);
        else
            MinecraftForge.EVENT_BUS.unregister(curiosityHandler);
    }
}
