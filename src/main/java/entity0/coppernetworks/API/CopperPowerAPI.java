package entity0.coppernetworks.API;

import entity0.coppernetworks.Network;
import entity0.coppernetworks.Networks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public interface CopperPowerAPI {
    public void setnetUUID (UUID netuuid);
    public UUID getnetUUID ();
    //reminder the server can be gotten from the serverworld
    //Is this actually any better than just using the item power implementation here too?
    default public boolean canGenerate(MinecraftServer server, Long power) {
        //Is this actually any better than just using the item power implementation here too?
        Network net = Networks.getOrCreateNetworks(server).getNetwork(getnetUUID());
        if (net != null) {
            return (net.powercapacity - net.getPower() >= power);
        }
        return false;
    }
    default public boolean canConsume(MinecraftServer server, long power) {
        Network net = Networks.getOrCreateNetworks(server).getNetwork(getnetUUID());
        if (net != null) {
            return (net.getPower() >= power);
        }
        return false;
    }
    default public void generate(MinecraftServer server, long power) {
        Network net = Networks.getOrCreateNetworks(server).getNetwork(getnetUUID());
        if (net != null) {
            net.setPower(net.getPower() + power);
        }
    }
    default public void consume(MinecraftServer server, long power) {
        Network net = Networks.getOrCreateNetworks(server).getNetwork(getnetUUID());
        if (net != null) {
            net.setPower(net.getPower() - power);
        }
    }
    default public boolean generateIfCan(MinecraftServer server, long power) {
        if (canGenerate(server, power)) {
            generate(server, power);
            return true;
        }
        return false;
    }
    default public boolean consumeIfCan(MinecraftServer server, long power) {
        if (canConsume(server, power)) {
            consume(server, power);
            return true;
        }
        return false;
    }
}
