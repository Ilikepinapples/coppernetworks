package entity0.coppernetworks;



import entity0.coppernetworks.API.CopperPowerAPI;
import entity0.coppernetworks.API.CopperStorageAPI;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.packrat.NbtParsingRule;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class Networks {
    public static Networks netsObject;

    public HashMap<UUID, Network> NetworkMap;
    public Set<PosWorld> interestedblocks;
    public HashMap<PosWorld, Set<UUID>> interestNetworkMap;


//todo THIS save function can be optimised by instead only saving changes thus avoiding massive wait times when saving the world for big networks, also settign stuff to null after saving may be unewscessary so I could ad a check to see if that saving comes from only closing the world


    public void save(MinecraftServer server) {
        String netstring = NetworkString();
        try {
            if (Files.exists(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"))) {
                Files.delete(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"));
            }
            Files.write(Files.createFile(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper")), netstring.getBytes());
            netsObject = null;
        } catch (IOException e) {
        }
    }


    public Networks(HashMap<UUID, Network> netmap, Set<PosWorld> interestblock, HashMap<PosWorld, Set<UUID>> interestNetMap) {
        NetworkMap = netmap;
        interestedblocks = interestblock;
        interestNetworkMap = interestNetMap;


    }

    public Networks() {
        NetworkMap = new HashMap<UUID, Network>();
        interestedblocks = new HashSet<PosWorld>();
        interestNetworkMap = new HashMap<PosWorld, Set<UUID>>();

    }


    public void mergenetworks(UUID consumenet, UUID susbsumenet) {
        Network consume = NetworkMap.get(consumenet);
        Network subsume = NetworkMap.get(susbsumenet);
        if (consume != null && subsume != null) {
            consume.blocksinnet.addAll(subsume.blocksinnet);
            consume.power = (consume.getPower() + subsume.getPower());
            consume.corepos.addAll(subsume.corepos);
            //this needs all the powered and storage pos and the ascossiated values ? DOES IT ALREADY??? so turns out it scns those parts automatically which is probably cleaner anyway
            removeNetwork(susbsumenet);
        }

    }

    public void splitnet(UUID networkuuid, List<Set<BlockPos>> netstomake, List<Set<BlockPos>> blockstomakeitwith, ServerWorld world, List<Set<BlockPos>> storagestobewithin, List<Set<BlockPos>> PoweredBlocksPresent) {
        for (int i = 0; i < netstomake.size(); i++) {
            UUID uuid = createNetworknoscan(netstomake.get(i), 0, world, blockstomakeitwith.get(i), storagestobewithin.get(i), PoweredBlocksPresent.get(i));

            for (BlockPos pos : netstomake.get(i)) {
                if (world.getBlockEntity(pos) instanceof CopperNetworkerBlockEntity copperbe) {
                    copperbe.SetUUID(uuid);
                }

                getNetwork(networkuuid).corepos.remove(pos);
            }
        }
    }


    public UUID createNetwork(BlockPos coreposition, long netpower, ServerWorld world) {
        UUID uuid = UUID.randomUUID();
        Set<BlockPos> corepositions = new HashSet<>();
        corepositions.add(coreposition);
        Network net = new Network(corepositions, uuid, netpower, world, new HashSet<BlockPos>(), 1000L, new HashSet<BlockPos>(), new HashSet<>());
        net.scanfromtoadd(coreposition);
        NetworkMap.put(uuid, net);
        return uuid;
    }


    public UUID createNetworknoscan(Set<BlockPos> coreposition, long netpower, ServerWorld world, Set<BlockPos> blocksinnet, Set<BlockPos> storages, Set<BlockPos> PoweredBlocksPresent) {
        UUID uuid = UUID.randomUUID();
        long powercap = 1000L;
        for (BlockPos storage : storages) {
            if (world.getBlockEntity(storage) instanceof CopperStorageAPI copStorBE) {
                powercap = powercap + copStorBE.getstoragevalue();
            }
        }
        for (BlockPos storage : storages) {
            if (world.getBlockEntity(storage) instanceof CopperPowerAPI copPowBE) {
                copPowBE.setnetUUID(uuid);
            }
        }
        Network net = new Network(coreposition, uuid, netpower, world, blocksinnet, powercap, storages, PoweredBlocksPresent);
        NetworkMap.put(uuid, net);
        for (BlockPos positions : blocksinnet) {
            Networks.getOrCreateNetworks(world.getServer()).addInterestAllAround(new PosWorld(positions, world), uuid);
        }
        return uuid;
    }


    public Network getNetwork(UUID uuid) {
        return NetworkMap.get(uuid);
    }

    public void removeNetwork(UUID uuid) {
        if (uuid != null) {
            if (NetworkMap.containsKey(uuid)) {
                if (NetworkMap.get(uuid) != null) {
                    for (BlockPos posits : NetworkMap.get(uuid).blocksinnet) {
                        removeInterestAllAround(new PosWorld(posits, NetworkMap.get(uuid).world), uuid);
                    }
                    for (BlockPos positspow : NetworkMap.get(uuid).poweredPos) {
                        if (NetworkMap.get(uuid).world.getBlockEntity(positspow) instanceof CopperPowerAPI coppow) {
                            coppow.setnetUUID(null); //is this wise?
                        }
                    }
                }
            }
            NetworkMap.remove(uuid);
        }
    }
    //get or create probably isn't nescessary because a block with no id is gonna make a new network anyway and a block with an id is gonna connect to a network unless something has gone very wrong


    public void removeinterest(PosWorld posworld, UUID uuid) {
        Set<UUID> uuidset;
        uuidset = interestNetworkMap.get(posworld);
        if (!(uuidset == null)) {
            uuidset.remove(uuid);
            if (uuidset.isEmpty()) {
                interestedblocks.remove(posworld);
                interestNetworkMap.remove(posworld);
            }
        }
    }

    public void addinterest(PosWorld posworld, UUID uuid) {
        Set<UUID> uuidset;
        if (interestNetworkMap.containsKey(posworld)) {
            uuidset = interestNetworkMap.get(posworld);
            uuidset.add(uuid);
        } else {
            uuidset = new HashSet<UUID>();
            uuidset.add(uuid);
            interestNetworkMap.put(posworld, uuidset);
            interestedblocks.add(posworld);
        }
    }


    public boolean ispresentininterest(PosWorld posworld) {
        return (interestedblocks.contains(posworld));
    }

    public Set<UUID> getInterestedNetworks(PosWorld posworld) {
        return interestNetworkMap.get(posworld);
    }

    public void addInterestAllAround(PosWorld posworld, UUID uuid) {
        addinterest(posworld, uuid);
        addinterest(new PosWorld(posworld.pos.up(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.down(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.north(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.south(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.east(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.west(), posworld.world), uuid);
    }

    /**
     * this causes problems with blocks being removed from the network interest that should remain in it, while I could write something that checked the ffected surfaces of the network and readded interest i don't know if that solution is any more elegant or indeed performanent than just reasserting interest on all blocks in the ntwork after this is called
     **/
    public void removeInterestAllAround(PosWorld posworld, UUID uuid) {
        removeinterest(posworld, uuid);
        removeinterest(new PosWorld(posworld.pos.up(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.down(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.north(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.south(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.east(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.west(), posworld.world), uuid);
    }

    public String NetworkString() {
        String towrite = "";

        for (Map.Entry<UUID, Network> keyvalueset : NetworkMap.entrySet()) {
            towrite = towrite + keyvalueset.getKey().toString() + "L";
            for (BlockPos pos : keyvalueset.getValue().blocksinnet) {
                towrite = towrite + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "C";
            }
            if (towrite.lastIndexOf("L") != towrite.length() - 1) {
                towrite = towrite.substring(0, towrite.length() - 1);
            }
            towrite = towrite + "L";
            for (BlockPos corepos : keyvalueset.getValue().corepos) {
                towrite = towrite + corepos.getX() + "." + corepos.getY() + "." + corepos.getZ() + "&";
            }
            if (towrite.lastIndexOf("&") == towrite.length() - 1) {
                towrite = towrite.substring(0, towrite.length() - 1);
            }

            towrite = towrite + "L";
            for (BlockPos pos : keyvalueset.getValue().storagePos) {
                towrite = towrite + pos.getX() + "^" + pos.getY() + "^" + pos.getZ() + "C";
            }
            if (towrite.lastIndexOf("L") != towrite.length() - 1) {
                towrite = towrite.substring(0, towrite.length() - 1);
            }
            towrite = towrite + "L";
            for (BlockPos pos : keyvalueset.getValue().poweredPos) {
                towrite = towrite + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "C";
            }
            if (towrite.lastIndexOf("L") != towrite.length() - 1) {
                towrite = towrite.substring(0, towrite.length() - 1);
            }
            towrite = towrite + "L";


            towrite = towrite + keyvalueset.getValue().power + "," + keyvalueset.getValue().networkuuid + "," + keyvalueset.getValue().world.getRegistryKey().getValue() + "," + keyvalueset.getValue().powercapacity + "#";
        }

        if (!towrite.isEmpty()) {
            towrite = towrite.substring(0, towrite.length() - 1);
        }


        towrite = towrite + "|";

        for (PosWorld posworld : interestedblocks) {
            towrite = towrite + posworld.pos.getX() + "," + posworld.pos.getY() + "," + posworld.pos.getZ() + "," + posworld.world.getRegistryKey().getValue() + "#";
        }
        if (towrite.lastIndexOf("|") != towrite.length() - 1) {
            towrite = towrite.substring(0, towrite.length() - 1);
        }
        towrite = towrite + "|";

        for (Map.Entry<PosWorld, Set<UUID>> keyvalueset : interestNetworkMap.entrySet()) {
            towrite = towrite + keyvalueset.getKey().pos.getX() + "," + keyvalueset.getKey().pos.getY() + "," + keyvalueset.getKey().pos.getZ() + "," + keyvalueset.getKey().world.getRegistryKey().getValue() + "L";
            for (UUID uuid : keyvalueset.getValue()) {
                towrite = towrite + uuid + ",";
            }
            towrite = towrite.substring(0, towrite.length() - 1);
            towrite = towrite + "#";
        }
        if (towrite.lastIndexOf("|") != towrite.length() - 1) {
            towrite = towrite.substring(0, towrite.length() - 1);
        }


        return towrite;
    }

    public static Networks getOrCreateNetworks(MinecraftServer server) {
        //world.getServer().getWorld();
        if (netsObject == null) {
            if (Files.exists(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"))) {
                try {
                    String savedNBTNets = Files.readString(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"));
                    if (savedNBTNets.equals("||")) {
                        netsObject = new Networks();
                        return netsObject;
                    }
                    String[] splitNetData = savedNBTNets.split("\\|");
                    //try {
                    HashMap<UUID, Network> networkmapdata = new HashMap<UUID, Network>();
                    //fpr psme reason, whether data is being saved improperly or read imporperly this is trying to access things in the string that don't exist and crashing cuz of trying to put null to an integer
                    String[] networkmapdataStrings = splitNetData[0].split("#");
                    for (String networkmapdataString : networkmapdataStrings) {
                        String[] networkmapsplitdataString = networkmapdataString.split("L");
                        UUID uuidkey = UUID.fromString(networkmapsplitdataString[0]);
                        Set<BlockPos> blocksinnetdata = new HashSet<>();
                        for (String blocksinnetdatasplit : networkmapsplitdataString[1].split("C")) {
                            if (!blocksinnetdatasplit.isEmpty()) {
                                String[] blockposinnetsplit = blocksinnetdatasplit.split(",");
                                blocksinnetdata.add(new BlockPos(Integer.parseInt(blockposinnetsplit[0]), Integer.parseInt(blockposinnetsplit[1]), Integer.parseInt(blockposinnetsplit[2])));
                            }
                        }


                        Set<BlockPos> storagepos = new HashSet<>();
                        for (String blocksinnetdatasplit : networkmapsplitdataString[3].split("C")) {
                            if (!blocksinnetdatasplit.isEmpty()) {
                                String[] blockposinnetsplit = blocksinnetdatasplit.split("\\^");
                                storagepos.add(new BlockPos(Integer.parseInt(blockposinnetsplit[0]), Integer.parseInt(blockposinnetsplit[1]), Integer.parseInt(blockposinnetsplit[2])));
                            }
                        }
                        Set<BlockPos> powerpos = new HashSet<>();
                        for (String blocksinnetdatasplit : networkmapsplitdataString[4].split("C")) {
                            if (!blocksinnetdatasplit.isEmpty()) {
                                String[] blockposinnetsplit = blocksinnetdatasplit.split(",");
                                powerpos.add(new BlockPos(Integer.parseInt(blockposinnetsplit[0]), Integer.parseInt(blockposinnetsplit[1]), Integer.parseInt(blockposinnetsplit[2])));
                            }
                        }


                        String[] networkmapsplitdataStringpt3 = networkmapsplitdataString[5].split(",");

                        Set<BlockPos> corepos = new HashSet<>();
                        for (String coreposstring : networkmapsplitdataString[2].split("&")) {
                            String[] corepossplitstring = coreposstring.split("\\.");
                            corepos.add(new BlockPos(Integer.parseInt(corepossplitstring[0]), Integer.parseInt(corepossplitstring[1]), Integer.parseInt(corepossplitstring[2])));
                        }

                        Long power = Long.parseLong(networkmapsplitdataStringpt3[0]);
                        UUID networkUUID = UUID.fromString(networkmapsplitdataStringpt3[1]);
                        ServerWorld networkworld = server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(networkmapsplitdataStringpt3[2])));
                        long attachedstoragetonet = Long.parseLong(networkmapsplitdataStringpt3[3]);
                        networkmapdata.put(uuidkey, new Network(corepos, networkUUID, power, networkworld, blocksinnetdata, attachedstoragetonet, storagepos, powerpos));
                    }

                    Set<PosWorld> interestBlockdata = new HashSet<PosWorld>();
                    String[] interestBlockStrings = splitNetData[1].split("#");
                    for (String posworlds : interestBlockStrings) {
                        String[] posworlddata = posworlds.split(",");
                        PosWorld pworld = new PosWorld(new BlockPos(Integer.parseInt(posworlddata[0]), Integer.parseInt(posworlddata[1]), Integer.parseInt(posworlddata[2])), server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(posworlddata[3]))));
                        interestBlockdata.add(pworld);
                    }
                    HashMap<PosWorld, Set<UUID>> posworlduuiddata = new HashMap<>();
                    String[] posworlduuidset = splitNetData[2].split("#");
                    for (String posworlduuid : posworlduuidset) {
                        String[] posworlduuidsplit = posworlduuid.split("L");
                        String[] posworlddata = posworlduuidsplit[0].split(",");
                        PosWorld pworld = new PosWorld(new BlockPos(Integer.parseInt(posworlddata[0]), Integer.parseInt(posworlddata[1]), Integer.parseInt(posworlddata[2])), server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(posworlddata[3]))));
                        Set<UUID> uuidset = new HashSet<>();
                        String[] uuidsetdata = posworlduuidsplit[1].split(",");
                        for (String uuidstr : uuidsetdata) {
                            uuidset.add(UUID.fromString(uuidstr));
                        }
                        posworlduuiddata.put(pworld, uuidset);
                    }
                    netsObject = new Networks(networkmapdata, interestBlockdata, posworlduuiddata);
                    return netsObject;
                    //} catch (Exception q) {
                    //    CopperNetworks.LOGGER.info("error reading network data, new network data created.");
                    //    netsObject = new Networks();
                    //    String nbtnetstring = netsObject.NetworkString();
                    //    try {
                    //        if(Files.exists(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"))) {
                    //            Files.delete(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"));
                    //        }
                    //        Files.write(Files.createFile(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper")), nbtnetstring.getBytes());
                    //    } catch (IOException e) {}
                    //    return netsObject;
                    //}
                } catch (IOException e) {
                }

            } else {
                netsObject = new Networks();
                String nbtnetstring = netsObject.NetworkString();
                try {
                    if (Files.exists(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"))) {
                        Files.delete(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper"));
                    }
                    Files.write(Files.createFile(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper")), nbtnetstring.getBytes());
                } catch (IOException e) {
                }
            }
        }
        return netsObject;
    }
}
