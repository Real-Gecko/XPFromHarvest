package com.realgecko.xpfromharvest;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(XPFromHarvest.MODID)
public class XPFromHarvest {
    public static final String MODID = "xpfromharvest";

    private BlockBreakHandler blockBreakHandler;
    private SimpleHarvestHandler simpleHarvestHandler;
    private CuriosityHandler curiosityHandler;

    public XPFromHarvest(IEventBus modEventBus, ModContainer modContainer) {
        // Initialize handlers
        blockBreakHandler = new BlockBreakHandler();
        simpleHarvestHandler = new SimpleHarvestHandler();
        curiosityHandler = new CuriosityHandler();

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(blockBreakHandler);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        if (Config.simpleHarvest)
            NeoForge.EVENT_BUS.register(simpleHarvestHandler);

        if (Config.curiosityMode)
            NeoForge.EVENT_BUS.register(curiosityHandler);
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
