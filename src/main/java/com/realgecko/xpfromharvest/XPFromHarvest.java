package com.realgecko.xpfromharvest;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.BlockState;
import net.minecraftforge.event.world.BlockEvent;

import net.minecraftforge.common.IPlantable;

import net.minecraftforge.fml.config.ModConfig ;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("xpfromharvest")
public class XPFromHarvest
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public XPFromHarvest() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OptionsHolder.COMMON_SPEC); 
                
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("XPFromHarvest Preinit");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("xpfromharvest", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
       /* LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
       */
    }

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {	
	        if (event.getWorld().isClientSide()) return;

	        BlockState state = event.getWorld().getBlockState(event.getPos());
	        Block block = state.getBlock();
	        BlockState defaultState = block.defaultBlockState();
	        boolean harvest = false;

	        if(!state.equals(defaultState) && block instanceof IPlantable) {
	            harvest = true;
	        }

	        double chance=OptionsHolder.COMMON.Chance.get();
	        int xpAmount=OptionsHolder.COMMON.XpAmount.get();
	        
	        if (harvest && (Math.random()*100 + 1) <= chance)
	        	block.popExperience((ServerWorld) event.getWorld(), event.getPos(),  xpAmount);
	}
	
	@SubscribeEvent
	public  void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {

        if (event.getWorld().isClientSide()) return;
        Boolean simple=OptionsHolder.COMMON.Simple.get();
        if (!simple) return;
        
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        BlockState defaultState = block.defaultBlockState();
       
        if(!state.equals(defaultState) && block instanceof IPlantable) { //if is in default state do not harvest, ex : grass
            handleHarvest(block, world, pos, state);
        }
    }
	
	 void handleHarvest(Block block, World world, BlockPos pos, BlockState state) {
        List<ItemStack> drops = NonNullList.create();
        drops=Block.getDrops(state, (ServerWorld) world, pos, null);
        if (drops.size()==1) return;

        boolean foundSeed = false;
        for (ItemStack drop : drops) {
        	//do not spawn the first seed
            if ((drop.getItem() instanceof IPlantable) && !foundSeed) {
                foundSeed = true;
            }else {
            	ItemEntity entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop);            
            	world.addFreshEntity(entityItem);
            }
        }
        
        double chance=OptionsHolder.COMMON.Chance.get();
        int xpAmount=OptionsHolder.COMMON.XpAmount.get();

        if (Math.random()*100 <= chance) {
        	ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), xpAmount);                        
            world.addFreshEntity(xpOrb);            
        }
        state = block.defaultBlockState();
        world.setBlockAndUpdate(pos, state);
    }
           
}
