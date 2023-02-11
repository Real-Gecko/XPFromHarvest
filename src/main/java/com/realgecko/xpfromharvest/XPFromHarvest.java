package com.realgecko.xpfromharvest;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(XPFromHarvest.MODID)
public class XPFromHarvest {
    public static final String MODID = "xpfromharvest";
    public static final Logger LOGGER = LogUtils.getLogger();

    private BlockBreakHandler blockBreakHandler;
    private SimpleHarvestHandler simpleHarvestHandler;
    private CuriosityHandler curiosityHandler;

    public XPFromHarvest() {
        blockBreakHandler = new BlockBreakHandler();
        simpleHarvestHandler = new SimpleHarvestHandler();
        curiosityHandler = new CuriosityHandler();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(blockBreakHandler);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        ModConfig.loadConfig();

        if (ModConfig.simpleHarvest.get())
            MinecraftForge.EVENT_BUS.register(simpleHarvestHandler);

        if (ModConfig.curiosity.get())
            MinecraftForge.EVENT_BUS.register(curiosityHandler);
    }

    // public void configChanged(ConfigChangedEvent event) {
    // LOGGER.info("configChanged");
    // if(event.getModID().equals(MODID)) {
    // if (ModConfig.simpleHarvest.get())
    // MinecraftForge.EVENT_BUS.register(simpleHarvestHandler);
    // else
    // MinecraftForge.EVENT_BUS.unregister(simpleHarvestHandler);
    //
    // if (ModConfig.curiosity.get())
    // MinecraftForge.EVENT_BUS.register(curiosityHandler);
    // else
    // MinecraftForge.EVENT_BUS.unregister(curiosityHandler);
    // }
    // }
}
