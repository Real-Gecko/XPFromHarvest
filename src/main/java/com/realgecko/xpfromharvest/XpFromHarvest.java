package com.realgecko.xpfromharvest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XpFromHarvest.MODID, name = XpFromHarvest.NAME, version = XpFromHarvest.VERSION, acceptableRemoteVersions = "*")
public class XpFromHarvest {
    public static final String MODID = "xpfromharvest";
    public static final String NAME = "XP From Harvest";
    public static final String VERSION = "1.1.0";

    private BlockBreakHandler blockBreakHandler;
    private RightClickHandler rightClickHandler;

    @Mod.Instance("xpfromharvest")
    public static XpFromHarvest instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        blockBreakHandler = new BlockBreakHandler();
        rightClickHandler = new RightClickHandler();

        MinecraftForge.EVENT_BUS.register(blockBreakHandler);

        if (ModConfig.simpleHarvest)
            MinecraftForge.EVENT_BUS.register(rightClickHandler);
    }

    public void ConfigUpdated() {
        if (ModConfig.simpleHarvest)
            MinecraftForge.EVENT_BUS.register(rightClickHandler);
        else
            MinecraftForge.EVENT_BUS.unregister(rightClickHandler);
    }
}
