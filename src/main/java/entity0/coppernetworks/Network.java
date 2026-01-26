package entity0.coppernetworks;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Network {
    long power;
    Set<BlockPos> blocksinnet = new HashSet<BlockPos>();
    BlockPos corepos;
    UUID networkuuid;
    ServerWorld world;
    public Network (BlockPos coreposition, UUID uuidofnetwork, long netpower, ServerWorld worldNet) {
        this.corepos = coreposition;
        this.networkuuid = uuidofnetwork;
        this.power = netpower;
        this.world = worldNet;
    }

    public long getPower() {
        return power;
    }
    public void setPower(long powerin) {
        power = powerin;
    }

    public boolean innet(BlockPos pos) {
        return blocksinnet.contains(pos);
    }
    public void scanfromtoadd (BlockPos initialpos) {
        blocksinnet.add(initialpos);
        //make this check if a block is not in the network and is conductive (change check for copper block to conductive)
        Set<BlockPos> toscanfrom = new HashSet<BlockPos>();
        toscanfrom.add(initialpos);
        Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(initialpos, world), networkuuid);
        Set<BlockPos> ScanfromNext;
        Boolean finished = false;
        while (!finished) {
            ScanfromNext = new HashSet<>();

            for (BlockPos pos : toscanfrom) {
                if (!blocksinnet.contains(pos.up()) && world.getBlockState(pos.up()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.up());
                    ScanfromNext.add(pos.up());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.up(),world), networkuuid);
                }
                if (!blocksinnet.contains(pos.down()) && world.getBlockState(pos.down()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.down());
                    ScanfromNext.add(pos.down());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.down(),world), networkuuid);
                }
                if (!blocksinnet.contains(pos.north()) && world.getBlockState(pos.north()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.north(),world), networkuuid);
                }
                if (!blocksinnet.contains(pos.south()) && world.getBlockState(pos.south()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.south(),world), networkuuid);
                }
                if (!blocksinnet.contains(pos.east()) && world.getBlockState(pos.east()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.east(),world), networkuuid);
                }
                if (!blocksinnet.contains(pos.west()) && world.getBlockState(pos.west()) == Blocks.COPPER_BLOCK.getDefaultState()) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.west(),world), networkuuid);
                }
            }
            toscanfrom = ScanfromNext;
            if (toscanfrom.isEmpty()) {
                finished = true;
            }
        }

    }
    public void scantoremovehanging(BlockPos initialpos) {
        Set <BlockPos> scan;
        scan = scantocore(initialpos.up());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(initialpos.up(), world), networkuuid);
        }
        scan = scantocore(initialpos.down());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            for (BlockPos pos : scan) {
                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
        }
        scan = scantocore(initialpos.north());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            for (BlockPos pos : scan) {
                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
        }
        scan = scantocore(initialpos.south());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            for (BlockPos pos : scan) {
                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
        }
        scan = scantocore(initialpos.east());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            for (BlockPos pos : scan) {
                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
        }
        scan = scantocore(initialpos.west());
        if (!scan.contains(corepos)) {
            blocksinnet.removeAll(scan);
            for (BlockPos pos : scan) {
                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
        }
        for (BlockPos pos : blocksinnet) {
            Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos, world), networkuuid);
        }
    }

    /**
     * this will return a set either containing the core (the search halts after that point) or not containing the core, in the case it doesn't contain the core this set can then be removed from the network
     */
    private Set<BlockPos> scantocore(BlockPos initialpos) {
        Set<BlockPos> searchedhanging = new HashSet<BlockPos>();
        searchedhanging.add(initialpos);
        Set<BlockPos> toscanfrom = new HashSet<BlockPos>();
        toscanfrom.add(initialpos);
        Set<BlockPos> ScanfromNext;
        Boolean finished = false;
        while (!finished) {
            ScanfromNext = new HashSet<>();

            for (BlockPos pos : toscanfrom) {
                if (blocksinnet.contains(pos.up())) {
                    searchedhanging.add(pos.up());
                    ScanfromNext.add(pos.up());
                    if (pos.up() == corepos) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.down())) {
                    searchedhanging.add(pos.down());
                    ScanfromNext.add(pos.down());
                    if (pos.down() == corepos) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.north())) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    if (pos.north() == corepos) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.south())) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    if (pos.south() == corepos) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.east())) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    if (pos.east() == corepos) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.west())) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    if (pos.west() == corepos) {
                        return searchedhanging;
                    }
                }
            }
            toscanfrom = ScanfromNext;
            if (toscanfrom.isEmpty()) {
                finished = true;
            }
        }
        return searchedhanging;
    }
}
