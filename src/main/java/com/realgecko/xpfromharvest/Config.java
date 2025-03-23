package com.realgecko.xpfromharvest;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = XPFromHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec configSpec;

    private static final ForgeConfigSpec.IntValue CHANCE = configBuilder
            .comment("Chance in % of XP orb spawning after harvest done")
            .defineInRange("Chance", 100, 1, 100);
    private static final ForgeConfigSpec.IntValue XP_AMOUNT = configBuilder
            .comment("Amount of XP given")
            .defineInRange("XP Amount", 1, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue SIMPLE_HARVEST = configBuilder
            .comment("Enable simple harvesting and replanting with right click")
            .define("Simple Harvest", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CROP_LIST = configBuilder
            .comment("List of crops to process with their ages").defineList("Crops List",
                    List.of(
                            "Block{minecraft:potatoes}[age=7]",
                            "Block{minecraft:carrots}[age=7]",
                            "Block{minecraft:wheat}[age=7]",
                            "Block{minecraft:beetroots}[age=3]",
                            "Block{minecraft:nether_wart}[age=3]"),
                    o -> true);
    private static final ForgeConfigSpec.BooleanValue CURIOSITY_MODE = configBuilder
            .comment("Curiosity Mode: sneak + right click on block to get info in chat")
            .define("Curiosity Mode", false);

    static final ForgeConfigSpec SPEC = configBuilder.build();

    public static int chance;
    public static int xpAmount;
    public static boolean simpleHarvest;
    public static List<String> cropList;
    public static boolean curiosityMode;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        chance = CHANCE.get();
        xpAmount = XP_AMOUNT.get();
        simpleHarvest = SIMPLE_HARVEST.get();
        cropList = CROP_LIST.get().stream().map(itemName -> itemName).collect(Collectors.toList());
        curiosityMode = CURIOSITY_MODE.get();
    }
}
