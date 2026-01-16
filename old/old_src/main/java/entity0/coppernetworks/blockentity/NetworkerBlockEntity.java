package entity0.coppernetworks.blockentity;

import entity0.coppernetworks.NetworkerClass;
import entity0.coppernetworks.copperNetworkPowerAPI;
import entity0.coppernetworks.CopperNetworks;
import entity0.coppernetworks.copperNetworkPowerItemAPI;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.ArrayList;
import java.util.List;

import static entity0.coppernetworks.NetworkerBlock.STORAGE;

public class NetworkerBlockEntity extends BlockEntity {
    public long timePlaced;
    boolean placed;
    int id = this.hashCode();
    int cooldown;
    long power;
    World worlds = this.world;
    public NetworkerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.COPPER_NETWORKER_BLOCKENTITY, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, NetworkerBlockEntity blockEntity) {//also implement a check for being storage and don't do this if it is
        if (!world.isClient && (!state.get(STORAGE))) {
            if (blockEntity.cooldown <= 0) {
                NetworkerClass.addNetwork(NetworkerClass.scan(world, pos), blockEntity.id, NetworkerClass.getPower(blockEntity.id));
                blockEntity.cooldown = 100;
            } else {
                blockEntity.cooldown--;
            }
            NetworkerClass.tickNetworks(blockEntity.id, pos, blockEntity, state);

            blockEntity.markDirty();
        }
    }



// if nescessary put !world.isClient in an if statement on all of these to ensure executed on server
    @Override
    public void setWorld(World world) {
        if (!world.isClient()) {
            if (!placed) {
                timePlaced = world.getTime();
                placed = true;
            }
            markDirty();
        }
        super.setWorld(world);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putLong("TimePlaced", timePlaced);
        power = NetworkerClass.getPower(id);
        nbt.putLong("Power", power);
        nbt.putBoolean("placed", placed);
        super.writeNbt(nbt, wrapper);
    }
    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
            timePlaced = nbt.getLong("TimePlaced");
            placed = nbt.getBoolean("placed");
            power = nbt.getLong("Power");
            NetworkerClass.setPower(power, id);
    }
    //        if (world.isClient()){
    //    return;
    //}
}
//make this save power when dropped