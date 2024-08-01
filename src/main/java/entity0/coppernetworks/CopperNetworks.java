package entity0.coppernetworks;

import entity0.coppernetworks.blockentity.ModBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopperNetworks implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "coppernetworks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModBlocks.initializeCategory();
		LOGGER.info("Copper shall now attempt to network.");
		ModBlockEntity.registerBlockEntities();
	}
	public class ModBlockTags {
		public static final TagKey<Block> CONDUCTIVE = TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "conductive"));
	}
}