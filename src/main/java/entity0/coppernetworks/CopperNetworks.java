package entity0.coppernetworks;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

public class CopperNetworks implements ModInitializer {
	public static final String MOD_ID = "copper-networks";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");
		oxievent.EVENT.register((pos, world) -> {
			//not bothering to check if these are actually in the network after checking all interest areas is still useful because any partially oxidised block connected also are prevented fro oxidisng further kinda smoothing the transition
			if (Networks.ispresentininterest(new PosWorld(pos, world))) {
				Set<UUID> networks = Networks.getInterestedNetworks(new PosWorld(pos, world));
				for (UUID uuid : networks) {
					Network net = Networks.getNetwork(uuid);
					if (net.getPower() >= 1) {
						net.setPower(net.getPower() - 1);
						world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, -1);
						return ActionResult.FAIL; //if this is an area shared between networks its kinda arbitrary which one loses power for it (first registered one with enough energy)
					}
				}
			}
				return ActionResult.PASS;
		} );
		changeblockevent.EVENT.register((pos, world, old, newb) -> {
			if (Networks.ispresentininterest(new PosWorld(pos, world))) {
				//change from copper to conductive
				Set<UUID> networks = Networks.getInterestedNetworks(new PosWorld(pos, world));
				if (newb.getBlock() == Blocks.COPPER_BLOCK) {
					for (UUID uuid : networks) {
						Networks.getNetwork(uuid).scanfromtoadd(pos);
					}
				} else {
					for (UUID uuid : networks) {
						Network net = Networks.getNetwork(uuid);
						if (net.innet(pos)) {
							net.scantoremovehanging(pos);
						}
					}
				}
			}
			return ActionResult.PASS;
		});
	}
}