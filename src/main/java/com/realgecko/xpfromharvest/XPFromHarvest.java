package com.realgecko.xpfromharvest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(XPFromHarvest.MODID)
public class XPFromHarvest {
    public static final String MODID = "xpfromharvest";

    private BlockBreakHandler blockBreakHandler;
    private SimpleHarvestHandler simpleHarvestHandler;
    private CuriosityHandler curiosityHandler;

    public XPFromHarvest(FMLJavaModLoadingContext context) {
        // Initialize handlers
        blockBreakHandler = new BlockBreakHandler();
        simpleHarvestHandler = new SimpleHarvestHandler();
        curiosityHandler = new CuriosityHandler();

        context.getModEventBus().addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(blockBreakHandler);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        if (Config.simpleHarvest)
            MinecraftForge.EVENT_BUS.register(simpleHarvestHandler);

        if (Config.curiosityMode)
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
