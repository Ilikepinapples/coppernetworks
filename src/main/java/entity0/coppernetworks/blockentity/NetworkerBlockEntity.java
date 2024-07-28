package entity0.coppernetworks.blockentity;

import entity0.coppernetworks.CopperNetworks;
import entity0.coppernetworks.NetworkerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NetworkerBlockEntity extends BlockEntity {
    long timePlaced;
    boolean placed;
    long power;
    public NetworkerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.COPPER_NETWORKER_BLOCKENTITY, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, NetworkerBlockEntity blockEntity) {//also implement a check for being storage and don't do this if it is
        List<BlockPos> overallblocks = new ArrayList<BlockPos>();
        List<BlockPos> scan = new ArrayList<BlockPos>();
        overallblocks.add(pos);
        scan.add(pos);
        while (true) {
            List<BlockPos> previousscan = new ArrayList<>(scan);
            scan.clear();
            for (BlockPos position : previousscan) {
                if (world.getBlockState(position.up()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.up())) && (!scan.contains(position.up()))) {
                    scan.add(position.up());
                    overallblocks.add(position.up());
                }
                if (world.getBlockState(position.down()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.down())) && (!scan.contains(position.down()))) {
                    scan.add(position.down());
                    overallblocks.add(position.down());
                }
                if (world.getBlockState(position.north()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.north())) && (!scan.contains(position.north()))) {
                    scan.add(position.north());
                    overallblocks.add(position.north());
                }
                if (world.getBlockState(position.south()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.south())) && (!scan.contains(position.south()))) {
                    scan.add(position.south());
                    overallblocks.add(position.south());
                }
                if (world.getBlockState(position.east()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.east())) && (!scan.contains(position.east()))) {
                    scan.add(position.east());
                    overallblocks.add(position.east());
                }
                if (world.getBlockState(position.west()).isOf(Blocks.COPPER_BLOCK) && (!overallblocks.contains(position.west())) && (!scan.contains(position.west()))) {
                    scan.add(position.west());
                    overallblocks.add(position.west());
                }
            }

            if (scan.isEmpty()) {
                break;
            }

        }
        for (BlockPos networkpos : overallblocks) {
            if (world.getBlockState(networkpos.up()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.up(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.down()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.down(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.north()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.north(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.south()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.south(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.east()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.east(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.west()).isOf(Blocks.OXIDIZED_COPPER)) {world.setBlockState(networkpos.west(), Blocks.WEATHERED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.up()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.up(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.down()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.down(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.north()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.north(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.south()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.south(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.east()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.east(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.west()).isOf(Blocks.WEATHERED_COPPER)) {world.setBlockState(networkpos.west(), Blocks.EXPOSED_COPPER.getDefaultState());}
            if (world.getBlockState(networkpos.up()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.up(), Blocks.COPPER_BLOCK.getDefaultState());}
            if (world.getBlockState(networkpos.down()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.down(), Blocks.COPPER_BLOCK.getDefaultState());}
            if (world.getBlockState(networkpos.north()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.north(), Blocks.COPPER_BLOCK.getDefaultState());}
            if (world.getBlockState(networkpos.south()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.south(), Blocks.COPPER_BLOCK.getDefaultState());}
            if (world.getBlockState(networkpos.east()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.east(), Blocks.COPPER_BLOCK.getDefaultState());}
            if (world.getBlockState(networkpos.west()).isOf(Blocks.EXPOSED_COPPER)) {world.setBlockState(networkpos.west(), Blocks.COPPER_BLOCK.getDefaultState());}
        }
    }



    @Override
    public void setWorld(World world) {
        if (placed == false) {
            timePlaced = world.getTime();
            placed = true;
        }
        super.setWorld(world);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putLong("TimePlaced", timePlaced);
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
    }
    //        if (world.isClient()){
    //    return;
    //}
}
