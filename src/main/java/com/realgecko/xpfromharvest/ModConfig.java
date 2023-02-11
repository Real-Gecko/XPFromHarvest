package com.realgecko.xpfromharvest;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    private static final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec configSpec;

    public static ForgeConfigSpec.IntValue chance;
    public static ForgeConfigSpec.IntValue xpAmount;
    public static ForgeConfigSpec.BooleanValue simpleHarvest;
    //public static ForgeConfigSpec.ConfigValue<List<? extends String>> crops;
    public static ForgeConfigSpec.BooleanValue curiosity;
    
    public static Map<String, CropData> cropMap;

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
        /*crops = configBuilder
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
                );*/
        curiosity = configBuilder
                .comment("Curiosity Mode: sneak + right click with on block to get info in chat")
                .define("Curiosity", false);
        configSpec = configBuilder.build();
        
        cropMap = new HashMap<String, CropData>();
    	cropMap.put("minecraft:potatoes", new CropData(7));
    	cropMap.put("minecraft:carrots", new CropData(7));
    	cropMap.put("minecraft:wheat", new CropData(7));
    	cropMap.put("minecraft:beetroots", new CropData(3));
    	cropMap.put("minecraft:nether_wart", new CropData(3));
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
        
        loadCrops(FMLPaths.CONFIGDIR.get().resolve(XPFromHarvest.MODID + "/crops.json"));
    }
    
    private static void loadCrops(Path path) {
    	GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();

		try {
			File file = path.toFile();
			File parent = file.getParentFile();
			
			if (parent != null && !parent.exists() && !parent.mkdirs()) {
			    throw new IllegalStateException("Couldn't create directory: " + parent);
			}
			
	    	if (file.createNewFile()) {
    			FileWriter writer = new FileWriter(file);
    			writer.write(gson.toJson(cropMap));
    			writer.close();
    			
    			XPFromHarvest.LOGGER.info("Created new crop configuration!");
	    	}
	    	else {
    			Reader reader = Files.newReader(file, Charset.defaultCharset());
    			Type type = new TypeToken<HashMap<String, CropData>>() {}.getType();
    			
    			cropMap = gson.fromJson(reader, type);
    			XPFromHarvest.LOGGER.info("Loaded crop configuration!");
	    	}
		}
		catch (Exception e) {
	    	XPFromHarvest.LOGGER.error("Error while loading crop configuration:" + e.getMessage());
	    	e.printStackTrace();
		}
    }
}
