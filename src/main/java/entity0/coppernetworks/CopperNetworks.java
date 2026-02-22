package entity0.coppernetworks;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

import static entity0.coppernetworks.CopperBlocktagsprovider.CONDUCTIVEITEMS;

public class CopperNetworks implements ModInitializer {
	public static final String MOD_ID = "copper-networks";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CopperBlockanblockEntities.initialise();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerLifecycleEvents.BEFORE_SAVE.register((server, fl, fo) -> {
			Networks.getOrCreateNetworks(server).save(server);

		});

		oxievent.EVENT.register((pos, world) -> {
			//not bothering to check if these are actually in the network after checking all interest areas is still useful because any partially oxidised block connected also are prevented fro oxidisng further kinda smoothing the transition
			if (Networks.getOrCreateNetworks(world.getServer()).ispresentininterest(new PosWorld(pos, world))) {
				Set<UUID> networks = Networks.getOrCreateNetworks(world.getServer()).getInterestedNetworks(new PosWorld(pos, world));
				for (UUID uuid : networks) {
					Network net = Networks.getOrCreateNetworks(world.getServer()).getNetwork(uuid);
					if (net != null && net.getPower() >= 1) {
						net.setPower(net.getPower() - 1);
						world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, -1);
						return ActionResult.FAIL; //if this is an area shared between networks its kinda arbitrary which one loses power for it (first registered one with enough energy)
					}
				}
			}
				return ActionResult.PASS;
		} );
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.add(CopperBlockanblockEntities.NETWORKER_BLOCK.asItem()));


		changeblockevent.EVENT.register((pos, world, old, newb) -> {
				if (Networks.getOrCreateNetworks(world.getServer()).ispresentininterest(new PosWorld(pos, world))) {
					Set<UUID> networks = Set.copyOf(Networks.getOrCreateNetworks(world.getServer()).getInterestedNetworks(new PosWorld(pos, world)));
					if (newb.isIn(CONDUCTIVEITEMS)) {
						for (UUID uuid : networks) {
							Network net = Networks.getOrCreateNetworks(world.getServer()).getNetwork(uuid);
							if (net != null) {
     								net.scanfromtoadd(pos);
							}
						}
					} else {
						//try {
						for (UUID uuid : networks) {
							Network net = Networks.getOrCreateNetworks(world.getServer()).getNetwork(uuid);
							if (net != null) {
								if (net.innet(pos)) {
									net.scantoremovehanging(pos);
								}
							}
						}
						//} catch (Exception ignored) {}
					}
			}
			return ActionResult.PASS;
		});
	}
}