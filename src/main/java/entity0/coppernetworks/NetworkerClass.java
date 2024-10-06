package entity0.coppernetworks;

import entity0.coppernetworks.blockentity.NetworkerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.client.RunArgs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.*;

import static entity0.coppernetworks.NetworkerBlock.STORAGE;

public class NetworkerClass {
    public static Map<Integer, List<Object>> Networks = new HashMap<>();
    public static Map<Integer, Long> Power = new HashMap<>();
    public static void addNetwork(List<Object> worldAndBlockposList, int id, long power) {
        Networks.put(id, worldAndBlockposList);
        Power.put(id, power);
    }
    public static long getPower(int id) {
        if (!(Power.get(id) == null)) {
            return Power.get(id);
        } else {
            return 0L;
        }

    }
    public static void setPower(long power, int id) {
        Power.put(id,power);
    }
    public static void removeNetwork(int id) {
        Power.remove(id);
        Networks.remove(id);
    }
    public static void tickNetworks(int id, BlockPos pos, NetworkerBlockEntity blockEntity, BlockState state) {
        List<Object> worldAndBlockposList = Networks.get(id);
        World world;
        List<BlockPos> networkBlocks;
        if (!(worldAndBlockposList == null)) {
            world = (World) worldAndBlockposList.get(0);
            networkBlocks = (List<BlockPos>) worldAndBlockposList.get(2);
        long power = getPower(id);

        for (BlockPos networkpos : networkBlocks) {
            if ((!networkpos.up().equals(pos)) && ((world.getBlockEntity(networkpos.up())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.up()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.up(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if ((!networkpos.down().equals(pos)) && ((world.getBlockEntity(networkpos.down())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.down()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.down(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if ((!networkpos.north().equals(pos)) && ((world.getBlockEntity(networkpos.north())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.north()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.north(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if ((!networkpos.south().equals(pos)) && ((world.getBlockEntity(networkpos.south())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.south()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.south(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if ((!networkpos.east().equals(pos)) && ((world.getBlockEntity(networkpos.east())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.east()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.east(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if ((!networkpos.west().equals(pos)) && ((world.getBlockEntity(networkpos.west())) instanceof NetworkerBlockEntity networkerBlockEntity) && (!world.getBlockState(networkpos.west()).get(STORAGE)) && networkerBlockEntity.timePlaced >= blockEntity.timePlaced) {
                world.setBlockState(networkpos.west(), state.with(STORAGE, true));
                power = power + power;
                setPower(0, networkerBlockEntity.hashCode());
                networkerBlockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.up()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.up()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.up()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.up())).ifPresent(oxistate -> world.setBlockState(networkpos.up(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.up(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.down()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.down()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.down()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.down())).ifPresent(oxistate -> world.setBlockState(networkpos.down(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.down(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.north()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.north()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.north()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.north())).ifPresent(oxistate -> world.setBlockState(networkpos.north(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.north(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.south()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.south()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.south()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.south())).ifPresent(oxistate -> world.setBlockState(networkpos.south(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.south(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.east()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.east()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.east()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.east())).ifPresent(oxistate -> world.setBlockState(networkpos.east(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.east(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            if (getPower(blockEntity.hashCode()) >= 1 && world.getBlockState(networkpos.west()).getBlock() instanceof Oxidizable && (world.getBlockState(networkpos.west()).getBlock() != Oxidizable.getUnaffectedOxidationBlock(world.getBlockState(networkpos.west()).getBlock()))) {
                Oxidizable.getDecreasedOxidationState(world.getBlockState(networkpos.west())).ifPresent(oxistate -> world.setBlockState(networkpos.west(), oxistate));
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, networkpos.west(), -1);
                setPower(getPower(id) - 1, id);
                blockEntity.markDirty();
            }
            /////////////////////////////////////
            if (world.getBlockEntity(networkpos.up()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.up())).copperNetworkAPI().id = blockEntity.hashCode();
            }
            if (world.getBlockEntity(networkpos.down()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.down())).copperNetworkAPI().id = blockEntity.hashCode();
            }
            if (world.getBlockEntity(networkpos.north()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.north())).copperNetworkAPI().id = blockEntity.hashCode();
            }
            if (world.getBlockEntity(networkpos.south()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.south())).copperNetworkAPI().id = blockEntity.hashCode();
            }
            if (world.getBlockEntity(networkpos.east()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.east())).copperNetworkAPI().id = blockEntity.hashCode();
            }
            if (world.getBlockEntity(networkpos.west()) instanceof copperNetworkPowerAPI) {
                ((copperNetworkPowerAPI) world.getBlockEntity(networkpos.west())).copperNetworkAPI().id = blockEntity.hashCode();

            }
            BlockPos corner1 = networkpos.up(2).north(1).west(1);
            BlockPos corner2 = networkpos.down(1).south(2).east(2);
            for (Entity entityStoodOn : world.getOtherEntities(null, new Box(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ()))) {
                if (entityStoodOn instanceof LivingEntity) {
                    for (ItemStack itemStack : ((LivingEntity) entityStoodOn).getEquippedItems()) {
                        if (itemStack.getItem() instanceof copperNetworkPowerItemAPI) {
                            ((copperNetworkPowerItemAPI) itemStack.getItem()).copperNetworkAPI(itemStack).id = blockEntity.hashCode(); //This needs to change to work correctly -- does it? seems to work
                        }
                    }
                }
                if (entityStoodOn instanceof ItemFrameEntity) {
                    ItemStack itemStack = ((ItemFrameEntity) entityStoodOn).getHeldItemStack();
                    if (itemStack.getItem() instanceof copperNetworkPowerItemAPI) {
                        ((copperNetworkPowerItemAPI) itemStack.getItem()).copperNetworkAPI(itemStack).id = blockEntity.hashCode(); //This needs to change to work correctly -- does it? seems to work
                    }
                }
            }
        }
        }
    }



    public static List<Object> scan(World world, BlockPos pos) {
        List<BlockPos> overallblocks = new ArrayList<BlockPos>();
        List<BlockPos> scan = new ArrayList<BlockPos>();
        long storage = 0;
        //List<BlockPos> contained = new ArrayList<BlockPos>();- this would mean contained blocks aren't taken into account when detecting blocks connected to the network, not sure if this would reduce or create lag as it reduces the load after scanning is completed but adds another list and more steps durng scanning (that is already an intensive process and more checks are likely to make this even more intensive)
        overallblocks.add(pos);
        scan.add(pos);
        while (true) {
            List<BlockPos> previousscan = new ArrayList<>(scan);
            scan.clear();
            //change all of these is of copper_block to is of groupodsomekind that I could declare elsewhere so things like slabs and stairs work without me having to write 32billion if statements
            for (BlockPos position : previousscan) {
                //if (world.getBlockState(position.up()).isOf(Blocks.COPPER_BLOCK) && world.getBlockState(position.down()).isOf(Blocks.COPPER_BLOCK) && world.getBlockState(position.north()).isOf(Blocks.COPPER_BLOCK) && world.getBlockState(position.south()).isOf(Blocks.COPPER_BLOCK) && world.getBlockState(position.east()).isOf(Blocks.COPPER_BLOCK) && world.getBlockState(position.west()).isOf(Blocks.COPPER_BLOCK)) {
                //contained.add(position); - this would mean contained blocks aren't taken into account when detecting blocks connected to the network, not sure if this would reduce or create lag as it reduces the load after scanning is completed but adds another list and more steps durng scanning (that is already an intensive process and more checks are likely to make this even more intensive)
                //don't scan from this block
                //continue; - this would make the scan ignore things encased in copper, removes the lag created by big cubes but also ignores things encased in copper
                // optomise this entire scanning process and the overallblocks iterating through process after
                //}
                if ((!overallblocks.contains(position.up())) && world.getBlockState(position.up()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.up());
                    overallblocks.add(position.up());
                }
                if ((!overallblocks.contains(position.down())) && world.getBlockState(position.down()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.down());
                    overallblocks.add(position.down());
                }
                if ((!overallblocks.contains(position.north())) && world.getBlockState(position.north()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.north());
                    overallblocks.add(position.north());
                }
                if ((!overallblocks.contains(position.south())) && world.getBlockState(position.south()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.south());
                    overallblocks.add(position.south());
                }
                if ((!overallblocks.contains(position.east())) && world.getBlockState(position.east()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.east());
                    overallblocks.add(position.east());
                }
                if ((!overallblocks.contains(position.west())) && world.getBlockState(position.west()).isIn(CopperNetworks.ModBlockTags.CONDUCTIVE)) {
                    scan.add(position.west());
                    overallblocks.add(position.west());
                }
                if ((!overallblocks.contains(position.up())) && ((world.getBlockEntity(position.up())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.up()).get(STORAGE))) {
                    scan.add(position.up());
                    overallblocks.add(position.up());
                    storage++;
                }
                if ((!overallblocks.contains(position.down())) && ((world.getBlockEntity(position.down())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.down()).get(STORAGE))) {
                    scan.add(position.down());
                    overallblocks.add(position.down());
                    storage++;
                }
                if ((!overallblocks.contains(position.north())) && ((world.getBlockEntity(position.north())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.north()).get(STORAGE))) {
                    scan.add(position.north());
                    overallblocks.add(position.north());
                    storage++;
                }
                if ((!overallblocks.contains(position.south())) && ((world.getBlockEntity(position.south())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.south()).get(STORAGE))) {
                    scan.add(position.south());
                    overallblocks.add(position.south());
                    storage++;
                }
                if ((!overallblocks.contains(position.east())) && ((world.getBlockEntity(position.east())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.east()).get(STORAGE))) {
                    scan.add(position.east());
                    overallblocks.add(position.east());
                    storage++;
                }
                if ((!overallblocks.contains(position.west())) && ((world.getBlockEntity(position.west())) instanceof NetworkerBlockEntity networkerBlockEntity) && (world.getBlockState(position.west()).get(STORAGE))) {
                    scan.add(position.west());
                    overallblocks.add(position.west());
                    storage++;
                }

            }

            if (scan.isEmpty()) {
                break;
            }
        }
        long maxPower = 100000 + (storage * 50000);
        List<Object> worldAndBlockposList = new ArrayList<Object>();
        worldAndBlockposList.add(world);
        worldAndBlockposList.add(maxPower);
        worldAndBlockposList.add(overallblocks);
        return worldAndBlockposList;
    }
}
