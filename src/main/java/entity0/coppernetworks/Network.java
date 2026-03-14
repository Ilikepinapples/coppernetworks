package entity0.coppernetworks;

import entity0.coppernetworks.API.CopperPowerAPI;
import entity0.coppernetworks.API.CopperStorageAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
    public long power;
    public Set<BlockPos> blocksinnet;
    public Set<BlockPos> corepos;// this becomes null when you scan for some rerason
    public UUID networkuuid;
    public ServerWorld world;
    public long powercapacity;
    public Set<BlockPos> storagePos;
    public Set<BlockPos> poweredPos;


    public Network (Set<BlockPos> coreposition, UUID uuidofnetwork, long netpower, ServerWorld worldNet, Set<BlockPos> blockinnetwork, long powercapacity, Set<BlockPos> storagePos, Set<BlockPos> poweredPos) {
        this.corepos = coreposition;
        this.networkuuid = uuidofnetwork;
        this.power = netpower;
        this.world = worldNet;
        this.blocksinnet = blockinnetwork;
        this.powercapacity = powercapacity;
        this.storagePos = storagePos;
        this.poweredPos = poweredPos;
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
    //TODO could make this scan for block in other nets and not other networkers
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
        if (world.getBlockState(initialpos).getBlock() instanceof CopperStorageAPI copstorage) {
            powercapacity = powercapacity + copstorage.getstoragevalue();
            storagePos.add(initialpos);
        }
        if (world.getBlockEntity(initialpos) instanceof CopperPowerAPI copPower) {
            copPower.setnetUUID(networkuuid);
            poweredPos.add(initialpos);
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






                if ((!blocksinnet.contains(pos.up()) && world.getBlockState(pos.up()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.up());
                    ScanfromNext.add(pos.up());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.up(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.up());
                }
                if ((!blocksinnet.contains(pos.down()) && world.getBlockState(pos.down()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.down());
                    ScanfromNext.add(pos.down());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.down(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.down());
                }
                if ((!blocksinnet.contains(pos.north()) && world.getBlockState(pos.north()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.north(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.north());
                }
                if ((!blocksinnet.contains(pos.south()) && world.getBlockState(pos.south()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.south(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.south());
                }
                if ((!blocksinnet.contains(pos.east()) && world.getBlockState(pos.east()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.east(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.east());
                }
                if ((!blocksinnet.contains(pos.west()) && world.getBlockState(pos.west()).getBlock() instanceof CopperStorageAPI copstorage)) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.west(), world), networkuuid);
                    powercapacity = powercapacity + copstorage.getstoragevalue();
                    storagePos.add(pos.west());
                }




                if ((!blocksinnet.contains(pos.up()) && world.getBlockEntity(pos.up()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.up());
                    ScanfromNext.add(pos.up());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.up(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.up());
                }
                if ((!blocksinnet.contains(pos.down()) && world.getBlockEntity(pos.down()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.down());
                    ScanfromNext.add(pos.down());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.down(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.down());
                }
                if ((!blocksinnet.contains(pos.north()) && world.getBlockEntity(pos.north()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.north());
                    ScanfromNext.add(pos.north());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.north(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.north());
                }
                if ((!blocksinnet.contains(pos.south()) && world.getBlockEntity(pos.south()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.south());
                    ScanfromNext.add(pos.south());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.south(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.south());
                }
                if ((!blocksinnet.contains(pos.east()) && world.getBlockEntity(pos.east()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.east());
                    ScanfromNext.add(pos.east());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.east(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.east());
                }
                if ((!blocksinnet.contains(pos.west()) && world.getBlockEntity(pos.west()) instanceof CopperPowerAPI copPower)) {
                    blocksinnet.add(pos.west());
                    ScanfromNext.add(pos.west());
                    Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos.west(), world), networkuuid);
                    copPower.setnetUUID(networkuuid);
                    poweredPos.add(pos.west());
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

    public void scantoremovehanging(BlockPos initialpos, BlockState initalblockstate) {
        Set<BlockPos> suredelete = new HashSet<>();
        blocksinnet.remove(initialpos);
        Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(initialpos, world), networkuuid);
        //caN'T use intial pos here because the blockstate already gone
        if (storagePos.contains(initialpos) && initalblockstate.getBlock() instanceof CopperStorageAPI copstorpos) {
            powercapacity = powercapacity - copstorpos.getstoragevalue();
            storagePos.remove(initialpos);
        }
        if (poweredPos.contains(initialpos)) { //this hosuld actually be a block entity anyway but also its being broken so no need to clear
           poweredPos.remove(initialpos);
        }
        if (corepos.contains(initialpos)) {
            corepos.remove(initialpos);
        }
        suredelete.add(initialpos);


        Set<BlockPos> upSet = new HashSet<>();
        Set<BlockPos> coreupSet = new HashSet<>();
        Set<BlockPos> upstorageset = new HashSet<>();
        Set<BlockPos> upPowerset = new HashSet<>();
        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.up(), corep);
            upSet.addAll(scan);
            if (scan.contains(corep)) {
                coreupSet.add(corep);
            }
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    upstorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    upPowerset.add(posofPower);
                }
            }

        }
        Set<BlockPos> downPowerset = new HashSet<>();

        Set<BlockPos> northPowerset = new HashSet<>();

        Set<BlockPos> southPowerset = new HashSet<>();

        Set<BlockPos> eastPowerset = new HashSet<>();

        Set<BlockPos> westPowerset = new HashSet<>();

        Set<BlockPos> downSet = new HashSet<>();
        Set<BlockPos> coredownSet = new HashSet<>();
        Set<BlockPos> downstorageset = new HashSet<>();

        Set<BlockPos> northstorageset = new HashSet<>();
        Set<BlockPos> southstorageset = new HashSet<>();
        Set<BlockPos> eaststorageset = new HashSet<>();
        Set<BlockPos> weststorageset = new HashSet<>();

        for (BlockPos corep : corepos) {
            Set<BlockPos> scan = scantocore(initialpos.down(), corep);
            downSet.addAll(scan);
            if (scan.contains(corep)) {
                coredownSet.add(corep);
            }
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    downstorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    downPowerset.add(posofPower);
                }
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
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    northstorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    northPowerset.add(posofPower);
                }
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
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    southstorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    southPowerset.add(posofPower);
                }
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
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    eaststorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    eastPowerset.add(posofPower);
                }
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
            for (BlockPos posofstorage : storagePos) {
                if (scan.contains(posofstorage)) {
                    weststorageset.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (scan.contains(posofPower)) {
                    westPowerset.add(posofPower);
                }
            }
        }



        Set<BlockPos> removeFromStoragePos = new HashSet<>();
        Set<BlockPos> removeFromPowerPos = new HashSet<>();
        if (upSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(upSet);
            for (BlockPos posofstorage : storagePos) {
                if (upSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (upSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }

        }
        if (downSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(downSet);
            for (BlockPos posofstorage : storagePos) {
                if (downSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (downSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }


        }
        if (northSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(northSet);
            for (BlockPos posofstorage : storagePos) {
                if (northSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (northSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }


        }
        if (southSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(southSet);
            for (BlockPos posofstorage : storagePos) {
                if (southSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (southSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }


        }
        if (eastSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(eastSet);
            for (BlockPos posofstorage : storagePos) {
                if (eastSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (eastSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }


        }
        if (westSet.stream().noneMatch(corepos::contains)) {
            suredelete.addAll(westSet);
            for (BlockPos posofstorage : storagePos) {
                if (westSet.contains(posofstorage)) {
                    removeFromStoragePos.add(posofstorage);
                }
            }
            for (BlockPos posofPower : poweredPos) {
                if (westSet.contains(posofPower)) {
                    removeFromPowerPos.add(posofPower);
                }
            }
        }

        for (BlockPos storpos : removeFromStoragePos) {
            if (world.getBlockState(storpos).getBlock()  instanceof CopperStorageAPI copstorpos) {
                powercapacity = powercapacity - copstorpos.getstoragevalue();
            }
        }
        for (BlockPos powerpos : removeFromPowerPos) {
            if (world.getBlockEntity(powerpos)  instanceof CopperPowerAPI copPowBE) {
                copPowBE.setnetUUID(null); //is this wise?
            }
        }
        storagePos.removeAll(removeFromStoragePos);
        poweredPos.removeAll(removeFromPowerPos);

        blocksinnet.removeAll(suredelete);
        for (BlockPos pos : suredelete) {
            Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
        }


        //splitting logic
        List<Set<BlockPos>> PoweredBlocksPresent = new ArrayList<>();
        List<Set<BlockPos>> storagestobewithin = new ArrayList<>();
        List<Set<BlockPos>> netstomake = new ArrayList<>();
        List<Set<BlockPos>> blockstomakeitwith = new ArrayList<>();
        boolean addthis = true;
            if (!coreupSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(coreupSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(coreupSet);
                    blockstomakeitwith.add(upSet);
                    storagestobewithin.add(upstorageset);
                    PoweredBlocksPresent.add(upPowerset);
                }
            }
        addthis = true;
            if (!coredownSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(coredownSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(coredownSet);
                    blockstomakeitwith.add(downSet);
                    storagestobewithin.add(downstorageset);
                    PoweredBlocksPresent.add(downPowerset);
                }


            }
        addthis = true;
            if (!corenorthSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(corenorthSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(corenorthSet);
                    blockstomakeitwith.add(northSet);
                    storagestobewithin.add(northstorageset);
                    PoweredBlocksPresent.add(northPowerset);
                }


            }
        addthis = true;
            if (!coresouthSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(coresouthSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(coresouthSet);
                    blockstomakeitwith.add(southSet);
                    storagestobewithin.add(southstorageset);
                    PoweredBlocksPresent.add(southPowerset);
                }


            }
        addthis = true;
            if (!coreeastSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(coreeastSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(coreeastSet);
                    blockstomakeitwith.add(eastSet);
                    storagestobewithin.add(eaststorageset);
                    PoweredBlocksPresent.add(eastPowerset);
                }


            }
        addthis = true;
            if (!corewestSet.isEmpty()) {
                for (Set<BlockPos> coresetsalreadypresent : netstomake) {
                    if (coresetsalreadypresent.equals(corewestSet)) {
                        addthis = false;
                    }
                }
                if (addthis) {
                    netstomake.add(corewestSet);
                    blockstomakeitwith.add(westSet);
                    storagestobewithin.add(weststorageset);
                    PoweredBlocksPresent.add(westPowerset);
                }

            }

            if (netstomake.size() > 1) { //what if up and dwon share scanners?
                netstomake.remove(0);//index 0 remains as the original net
                blockstomakeitwith.remove(0);
                storagestobewithin.remove(0);
                PoweredBlocksPresent.remove(0);
                Set<BlockPos> postoremovefromstoragepos = new HashSet<>();
                for(BlockPos posstorage : storagePos) {
                    for (Set<BlockPos> blocktomakeset : storagestobewithin) {
                        if (blocktomakeset.contains(posstorage)) {
                            if (world.getBlockState(posstorage).getBlock()  instanceof CopperStorageAPI copstorpos) {
                                powercapacity = powercapacity - copstorpos.getstoragevalue();
                                blocksinnet.remove(posstorage);
                                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(posstorage, world), networkuuid); //TODO is this nescessarry?
                                postoremovefromstoragepos.add(posstorage);
                            }
                        }
                    }
                }
                Set<BlockPos> postoremovefrompowerpos = new HashSet<>();
                for(BlockPos pospower : poweredPos) {
                    for (Set<BlockPos> blocktomakepowerset : PoweredBlocksPresent) {
                        if (blocktomakepowerset.contains(pospower)) {
                            if (world.getBlockEntity(pospower)  instanceof CopperPowerAPI copPowpos) {
                                blocksinnet.remove(pospower);
                                Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pospower, world), networkuuid);//TODO is this nescessarry?
                                postoremovefrompowerpos.add(pospower);
                                copPowpos.setnetUUID(null);
                            }
                        }
                    }
                }
                poweredPos.removeAll(postoremovefrompowerpos);
                storagePos.removeAll(postoremovefromstoragepos);
                for (Set<BlockPos> blockuss : blockstomakeitwith) {
                    blocksinnet.removeAll(blockuss);
                    for (BlockPos pos : blockuss) {
                        Networks.getOrCreateNetworks(world.getServer()).removeInterestAllAround(new PosWorld(pos, world), networkuuid);
                    }

                }
                Networks.getOrCreateNetworks(world.getServer()).splitnet(networkuuid, netstomake, blockstomakeitwith, world, storagestobewithin, PoweredBlocksPresent);



            }

            for (BlockPos pos : blocksinnet) {
                Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(pos, world), networkuuid);
            }



    }

    /**
     * this will return a set either containing the core (the search halts after that point) or not containing the core, in the case it doesn't contain the core this set can then be removed from the network
     */
    private Set<BlockPos> scantocore(BlockPos initialpos, BlockPos coreposs) {
        Set<BlockPos> searchedhanging = new HashSet<BlockPos>();
        Set<BlockPos> toscanfrom = new HashSet<BlockPos>();
        toscanfrom.add(initialpos);
        searchedhanging.add(initialpos);
        if (initialpos.equals(coreposs)) {
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
                    if (pos.up().equals(coreposs)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.down()) && !searchedhanging.contains(pos.down())) {
                    searchedhanging.add(pos.down());
                    ScanfromNext.add(pos.down());
                    if (pos.down().equals(coreposs)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.north()) && !searchedhanging.contains(pos.north())) {
                    searchedhanging.add(pos.north());
                    ScanfromNext.add(pos.north());
                    if (pos.north().equals(coreposs)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.south()) && !searchedhanging.contains(pos.south())) {
                    searchedhanging.add(pos.south());
                    ScanfromNext.add(pos.south());
                    if (pos.south().equals(coreposs)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.east()) && !searchedhanging.contains(pos.east())) {
                    searchedhanging.add(pos.east());
                    ScanfromNext.add(pos.east());
                    if (pos.east().equals(coreposs)) {
                        return searchedhanging;
                    }
                }
                if (blocksinnet.contains(pos.west()) && !searchedhanging.contains(pos.west())) {
                    searchedhanging.add(pos.west());
                    ScanfromNext.add(pos.west());
                    if (pos.west().equals(coreposs)) {
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


