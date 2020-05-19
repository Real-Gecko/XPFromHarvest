package com.realgecko.xpfromharvest;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Arrays;
import java.util.List;

public class ModConfig {
    private static final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec configSpec;

    public static ForgeConfigSpec.IntValue chance;
    public static ForgeConfigSpec.IntValue xpAmount;
    public static ForgeConfigSpec.BooleanValue simpleHarvest;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> crops;
    public static ForgeConfigSpec.BooleanValue curiosity;

    static {
        chance = configBuilder
                .comment("Chance in % of XP orb spawning after harvest done")
                .defineInRange("Chance", 100, 1, 100);
        xpAmount = configBuilder
                .comment("Amount of XP given")
                .defineInRange("XP Amount", 1, 1, Integer.MAX_VALUE);
        simpleHarvest = configBuilder
                .comment("Enable simple harvesting and replanting with right click")
                .define("Simple Harvest", false);
        crops = configBuilder
                .comment("List of crops to process with their ages")
                .defineList(
                        "Crops List",
                        Arrays.asList(
                                "Block{minecraft:potatoes}[age=7]",
                                "Block{minecraft:carrots}[age=7]",
                                "Block{minecraft:wheat}[age=7]",
                                "Block{minecraft:beetroots}[age=3]",
                                "Block{minecraft:nether_wart}[age=3]"
                        ),
                        o -> true
                );
        curiosity = configBuilder
                .comment("Curiosity Mode: sneak + right click with on block to get info in chat")
                .define("Curiosity", false);
        configSpec = configBuilder.build();
    }

    public static void loadConfig() {
        CommentedFileConfig fileConfig = CommentedFileConfig
                .builder(FMLPaths.CONFIGDIR.get().resolve("XPFromHarvest.toml"))
                .sync().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        configSpec.setConfig(fileConfig);
        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                configSpec
        );
    }
}
