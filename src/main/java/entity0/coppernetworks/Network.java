package entity0.coppernetworks;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.Console;
import java.util.*;

import static entity0.coppernetworks.CopperBlocktagsprovider.CONDUCTIVEITEMS;

public class Network {
    long power;
    Set<BlockPos> blocksinnet;
    Set<BlockPos> corepos;
    UUID networkuuid;
    ServerWorld world;
    long storageattached;



    public Network (Set<BlockPos> coreposition, UUID uuidofnetwork, long netpower, ServerWorld worldNet, Set<BlockPos> blockinnetwork, long storages) {
        this.corepos = coreposition;
        this.networkuuid = uuidofnetwork;
        this.power = netpower;
        this.world = worldNet;
        this.blocksinnet = blockinnetwork;
        this.storageattached = storages;

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
        if (world.getBlockState(initialpos).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {

            if (world.getBlockEntity(initialpos) instanceof CopperNetworkerBlockEntity consumenetnetworker) {
                UUID uuid = consumenetnetworker.getUUID();
                //chexking if your looking at yourself
                    if (uuid != null) {
                            Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(uuid).blocksinnet;
                            if (blockothernet != null) {
                                if ((blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode()))) {//logic also ensures it ownt trigger if it hits its own networker
                                    Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                    for (BlockPos corep : corepos) {
                                        if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                            subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                        }
                                    }
                                }
                            }
                    } else {
                        corepos.add(initialpos);
                        consumenetnetworker.SetUUID(networkuuid);//not the consume networker anymore
                    }
                }
        }
        blocksinnet.add(initialpos);
        Set<BlockPos> toscanfrom = new HashSet<BlockPos>();
        toscanfrom.add(initialpos);
        Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(initialpos, world), networkuuid);
        Set<BlockPos> ScanfromNext;
        Boolean finished = false;
        while (!finished) {
            ScanfromNext = new HashSet<>();

            for (BlockPos pos : toscanfrom) {
                if (!blocksinnet.contains(pos.up()) && world.getBlockState(pos.up()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.up());
                    ScanfromNext.add(pos.up());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.up(), world), networkuuid);
                    if (world.getBlockEntity(pos.up()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if ((blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode()))) {//logic also ensures it ownt trigger if it hits its own networker

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.down()) && world.getBlockState(pos.down()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.down());
                    ScanfromNext.add(pos.down());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.down(), world), networkuuid);
                    if (world.getBlockEntity(pos.down()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if (blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode())) {

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.north()) && world.getBlockState(pos.north()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.north(), world), networkuuid);
                    if (world.getBlockEntity(pos.north()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if (blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode())) {

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.south()) && world.getBlockState(pos.south()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.south(), world), networkuuid);
                    if (world.getBlockEntity(pos.south()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if (blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode())) {

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.east()) && world.getBlockState(pos.east()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.east(), world), networkuuid);
                    if (world.getBlockEntity(pos.east()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if (blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode())) {

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.west()) && world.getBlockState(pos.west()).equals(CopperBlockanblockEntities.NETWORKER_BLOCK.getDefaultState())) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.west(), world), networkuuid);
                    if (world.getBlockEntity(pos.west()) instanceof CopperNetworkerBlockEntity consumenetnetworker) {

                        Set<BlockPos> blockothernet = Networks.getOrCreateNetworks(world.getServer()).getNetwork(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID()).blocksinnet;
                        if (blockothernet != null) {
                            if (blockothernet.size() > blocksinnet.size() || (blockothernet.size() == blocksinnet.size() && ((CopperNetworkerBlockEntity) consumenetnetworker).getUUID().hashCode() > networkuuid.hashCode())) {

                                Networks.getOrCreateNetworks(world.getServer()).mergenetworks(((CopperNetworkerBlockEntity) consumenetnetworker).getUUID(), networkuuid);
                                for (BlockPos corep : corepos) {
                                    if (world.getBlockEntity(corep) instanceof CopperNetworkerBlockEntity subsumeNetworker) {
                                        subsumeNetworker.SetUUID(consumenetnetworker.getUUID());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (!blocksinnet.contains(pos.up()) && world.getBlockState(pos.up()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.up());
                    ScanfromNext.add(pos.up());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.up(), world), networkuuid);
                }
                if (!blocksinnet.contains(pos.down()) && world.getBlockState(pos.down()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.down());
                    ScanfromNext.add(pos.down());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.down(), world), networkuuid);
                }
                if (!blocksinnet.contains(pos.north()) && world.getBlockState(pos.north()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.north(), world), networkuuid);
                }
                if (!blocksinnet.contains(pos.south()) && world.getBlockState(pos.south()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.south(), world), networkuuid);
                }
                if (!blocksinnet.contains(pos.east()) && world.getBlockState(pos.east()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.east(), world), networkuuid);
                }
                if (!blocksinnet.contains(pos.west()) && world.getBlockState(pos.west()).isIn(CONDUCTIVEITEMS)) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.west(), world), networkuuid);
                }
            }
            toscanfrom = ScanfromNext;
            if (toscanfrom.isEmpty()) {
                finished = true;
            }
        }
    }


    public void scantoremovehanging(BlockPos initialpos) {
        Set<BlockPos> suredelete = new HashSet<>();
        blocksinnet.remove(initialpos);
        Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(initialpos, world), networkuuid);

        Set<BlockPos> upSet = new HashSet<>();
        Set<BlockPos> coreupSet = new HashSet<>();
        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.up(), corep);
            upSet.addAll(scan);
            if (scan.contains(corep)) {
                coreupSet.add(corep);
            }
        }
        Set<BlockPos> downSet = new HashSet<>();
        Set<BlockPos> coredownSet = new HashSet<>();
        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.down(), corep);
            downSet.addAll(scan);
            if (scan.contains(corep)) {
                coredownSet.add(corep);
            }
        }
        Set<BlockPos> northSet = new HashSet<>();
        Set<BlockPos> corenorthSet = new HashSet<>();

        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.north(), corep);
            northSet.addAll(scan);
            if (scan.contains(corep)) {
                corenorthSet.add(corep);
            }
        }
        Set<BlockPos> southSet = new HashSet<>();
        Set<BlockPos> coresouthSet = new HashSet<>();

        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.south(), corep);
            southSet.addAll(scan);
            if (scan.contains(corep)) {
                coresouthSet.add(corep);
            }
        }
        Set<BlockPos> eastSet = new HashSet<>();
        Set<BlockPos> coreeastSet = new HashSet<>();

        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.east(), corep);
            eastSet.addAll(scan);
            if (scan.contains(corep)) {
                coreeastSet.add(corep);
            }
        }
        Set<BlockPos> westSet = new HashSet<>();
        Set<BlockPos> corewestSet = new HashSet<>();

        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.west(), corep);
            westSet.addAll(scan);
            if (scan.contains(corep)) {
                corewestSet.add(corep);
            }
        }
        if (upSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(upSet);
        }
        if (downSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(downSet);
        }
        if (northSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(northSet);
        }
        if (southSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(southSet);
        }
        if (eastSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(eastSet);
        }
        if (westSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(westSet);
        }
        blocksinnet.removeAll(suredelete);
        for (BlockPos pos : suredelete) {
            Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
        }
        //splitting logic
        if (!(upSet.containsAll(corepos) || downSet.containsAll(corepos) || northSet.containsAll(corepos) || southSet.containsAll(corepos) || eastSet.containsAll(corepos) || westSet.containsAll(corepos))) {
            List<Set<BlockPos>> netstomake = new ArrayList<>();
            List<Set<BlockPos>> blockstomakeitwith = new ArrayList<>();
            if (!coreupSet.isEmpty()) {
                netstomake.add(coreupSet);
                blockstomakeitwith.add(upSet);
            }
            if (!coredownSet.isEmpty()) {
                netstomake.add(coredownSet);
                blockstomakeitwith.add(downSet);
            }
            if (!corenorthSet.isEmpty()) {
                netstomake.add(corenorthSet);
                blockstomakeitwith.add(northSet);
            }
            if (!coresouthSet.isEmpty()) {
                netstomake.add(coresouthSet);
                blockstomakeitwith.add(southSet);
            }
            if (!coreeastSet.isEmpty()) {
                netstomake.add(coreeastSet);
                blockstomakeitwith.add(eastSet);
            }
            if (!corewestSet.isEmpty()) {
                netstomake.add(corewestSet);
                blockstomakeitwith.add(westSet);
            }
            if (netstomake.size() > 1) {
                Networks.getOrCreateNetworks(world.getServer()).splitnet(networkuuid, netstomake, blockstomakeitwith, world);
                scantoremovehanging(initialpos);
            }
        }

            for (BlockPos pos : blocksinnet) {
                Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos, world), networkuuid);
            }
    }

    /**
     * this will return a set either containing the core (the search halts after that point) or not containing the core, in the case it doesn't contain the core this set can then be removed from the network
     */
    private Set<BlockPos> scantocore(BlockPos initialpos, BlockPos corepos) {
        Set<BlockPos> searchedhanging = new HashSet<BlockPos>();
        Set<BlockPos> toscanfrom = new HashSet<BlockPos>();
        toscanfrom.add(initialpos);
        searchedhanging.add(initialpos);
        if (initialpos.equals(corepos)) {
            return searchedhanging;
        }
        Set<BlockPos> ScanfromNext;
        Boolean finished = false;
        while (!finished) {
            ScanfromNext = new HashSet<>();

            for (BlockPos pos : toscanfrom) {
                if (blocksinnet.contains(pos.up()) && !searchedhanging.contains(pos.up())) {
                    searchedhanging.add(pos.up());
                    ScanfromNext.add(pos.up());
                    if (pos.up().equals(corepos)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.down()) && !searchedhanging.contains(pos.down())) {
                    searchedhanging.add(pos.down());
                    ScanfromNext.add(pos.down());
                    if (pos.down().equals(corepos)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.north()) && !searchedhanging.contains(pos.north())) {
                    searchedhanging.add(pos.north());
                    ScanfromNext.add(pos.north());
                    if (pos.north().equals(corepos)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.south()) && !searchedhanging.contains(pos.south())) {
                    searchedhanging.add(pos.south());
                    ScanfromNext.add(pos.south());
                    if (pos.south().equals(corepos)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.east()) && !searchedhanging.contains(pos.east())) {
                    searchedhanging.add(pos.east());
                    ScanfromNext.add(pos.east());
                    if (pos.east().equals(corepos)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.west()) && !searchedhanging.contains(pos.west())) {
                    searchedhanging.add(pos.west());
                    ScanfromNext.add(pos.west());
                    if (pos.west().equals(corepos)) {
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
