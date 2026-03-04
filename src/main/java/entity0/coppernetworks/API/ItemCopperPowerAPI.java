package entity0.coppernetworks.API;

import entity0.coppernetworks.Network;
import entity0.coppernetworks.Networks;
import entity0.coppernetworks.PosWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.UUID;

public interface ItemCopperPowerAPI {
    default public Set<UUID> getNetworkUUIDS(MinecraftServer server, BlockPos posoftheitem, ServerWorld world) {
        return Networks.getOrCreateNetworks(server).getInterestedNetworks(new PosWorld(posoftheitem, world));
    }

    default public boolean canGenerate(MinecraftServer server, Long power, BlockPos posoftheitem, ServerWorld world) {
        Set<UUID> uuids = getNetworkUUIDS(server, posoftheitem, world);
        if (uuids != null) {
            UUID uuid = (UUID) uuids.toArray()[0]; //let the gods of the unordered set to array decide the network
            //null check unescessary as null is technically a valid key but nothing will ever be there so network becomes null anyway
            Network net = Networks.getOrCreateNetworks(server).getNetwork(uuid);
            if (net != null) {
                return (net.powercapacity - net.getPower() >= power);
            }
        }
            return false;
    }
    default public boolean canConsume(MinecraftServer server, long power, BlockPos posoftheitem, ServerWorld world) {
        Set<UUID> uuids = getNetworkUUIDS(server, posoftheitem, world);
        if (uuids != null) {
            UUID uuid = (UUID) uuids.toArray()[0]; //let the gods of the unordered set to array decide the network
            Network net = Networks.getOrCreateNetworks(server).getNetwork(uuid);
            if (net != null) {
                return (net.getPower() >= power);
            }
        }
            return false;
    }
    default public void generate(MinecraftServer server, long power, BlockPos posoftheitem, ServerWorld world) {
        Set<UUID> uuids = getNetworkUUIDS(server, posoftheitem, world);
        if (uuids != null) {
            UUID uuid = (UUID) uuids.toArray()[0]; //let the gods of the unordered set to array decide the network
            //null check unescessary as null is technically a valid key but nothing will ever be there so network becomes null anyway
            Network net = Networks.getOrCreateNetworks(server).getNetwork(uuid);
            if (net != null) {
                net.setPower(net.getPower() + power);
            }
        }
    }
    default public void consume(MinecraftServer server, long power, BlockPos posoftheitem, ServerWorld world) {
        Set<UUID> uuids = getNetworkUUIDS(server, posoftheitem, world);
        if (uuids != null) {
            UUID uuid = (UUID) uuids.toArray()[0]; //let the gods of the unordered set to array decide the network
            Network net = Networks.getOrCreateNetworks(server).getNetwork(uuid);
            if (net != null) {
                net.setPower(net.getPower() - power);
            }
        }
    }
    default public void generateIfCan(MinecraftServer server, long power, BlockPos posoftheitem, ServerWorld world) {
        if (canGenerate(server, power, posoftheitem, world)) {
            generate(server, power, posoftheitem, world);
        }
    }
    default public boolean consumeIfCan(MinecraftServer server, long power, BlockPos posoftheitem, ServerWorld world) {
        if (canConsume(server, power, posoftheitem, world)) {
            consume(server, power, posoftheitem, world);
            return true;
        }
        return false;
    }
}

