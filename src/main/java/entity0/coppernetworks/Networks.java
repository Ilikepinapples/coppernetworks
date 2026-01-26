package entity0.coppernetworks;



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




    public void save (MinecraftServer server) {
        String netstring = NetworkString();
        try {
            Files.write(Files.createFile(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper")), netstring.getBytes());
            netsObject = null;
        } catch (IOException e) {}
    }


    public Networks (HashMap<UUID, Network> netmap, Set<PosWorld> interestblock, HashMap<PosWorld, Set<UUID>> interestNetMap) {
        NetworkMap = netmap;
        interestedblocks = interestblock;
        interestNetworkMap = interestNetMap;
    }
    public Networks () {
        NetworkMap = new HashMap<UUID, Network>();
        interestedblocks = new HashSet<PosWorld>();
        interestNetworkMap = new HashMap<PosWorld, Set<UUID>>();
    }




    public UUID createNetwork(BlockPos coreposition, long netpower, ServerWorld world) {
        UUID uuid = UUID.randomUUID();
        Network net = new Network(coreposition, uuid, netpower, world);
        NetworkMap.put(uuid, net);
        return uuid;
    }
    public  Network getNetwork(UUID uuid) {
        return NetworkMap.get(uuid);
    }
    //get or create probably isn't nescessary because a block with no id is gonna make a new network anyway and a block with an id is gonna connect to a network unless something has gone very wrong



    public void removeinterest (PosWorld posworld, UUID uuid) {
        Set<UUID> uuidset;
        uuidset = interestNetworkMap.get(posworld);
        uuidset.remove(uuid);
        if (uuidset.isEmpty()) {
            interestedblocks.remove(posworld);
            interestNetworkMap.remove(posworld);
        }
    }
    public void addinterest (PosWorld posworld, UUID uuid) {
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
        return(interestedblocks.contains(posworld));
    }

    public Set<UUID> getInterestedNetworks(PosWorld posworld) {
        return interestNetworkMap.get(posworld);
    }

    public void addInterestAllAround (PosWorld posworld, UUID uuid) {
        addinterest(posworld, uuid);
        addinterest(new PosWorld(posworld.pos.up(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.down(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.north(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.south(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.east(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.west(), posworld.world), uuid);
    }
    /** this causes problems with blocks being removed from the network interest that should remain in it, while I could write something that checked the ffected surfaces of the network and readded interest i don't know if that solution is any more elegant or indeed performanent than just reasserting interest on all blocks in the ntwork after this is called **/
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
            for (BlockPos pos :keyvalueset.getValue().blocksinnet) {
                towrite = towrite + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "C";
            }
            towrite = towrite.substring(0, towrite.length() -1) + "L";
            towrite = towrite + keyvalueset.getValue().corepos.getX() + "," + keyvalueset.getValue().corepos.getY() + "," + keyvalueset.getValue().corepos.getZ() + "," + keyvalueset.getValue().power  + "," + keyvalueset.getValue().networkuuid + "," + keyvalueset.getValue().world.getRegistryKey() + "#";
        }
        if (!towrite.equals("")) {
            towrite = towrite.substring(0, towrite.length() - 1);
        }
        towrite = towrite + "|";

        for (PosWorld posworld : interestedblocks) {
            towrite = towrite + posworld.pos.getX() + "," + posworld.pos.getY() + "," + posworld.pos.getZ() + "," + posworld.world.getRegistryKey().getValue() + "#";
        }
        if (!towrite.equals("|")) {
            towrite = towrite.substring(0, towrite.length() - 1);
        }
        towrite = towrite + "|";

        for (Map.Entry<PosWorld, Set<UUID>> keyvalueset: interestNetworkMap.entrySet()) {
            towrite = towrite + keyvalueset.getKey().pos.getX() + "," + keyvalueset.getKey().pos.getY() + "," + keyvalueset.getKey().pos.getZ() + "," + keyvalueset.getKey().world.getRegistryKey().toString() + "L";
            for (UUID uuid : keyvalueset.getValue()) {
                towrite = towrite + uuid + ",";
            }
            towrite = towrite.substring(0, towrite.length() -1);
            towrite = towrite + "#";
        }
        if (towrite != "||") {
            towrite = towrite.substring(0, towrite.length() - 1);
        }


        return towrite;
    }
    public static Networks getOrCreateNetworks (MinecraftServer server) {
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

                    HashMap<UUID, Network> networkmapdata = new HashMap<UUID, Network>();
                    String[] networkmapdataStrings = splitNetData[0].split("#");
                    for (String networkmapdataString : networkmapdataStrings) {
                        String[] networkmapsplitdataString = networkmapdataString.split("L");
                        UUID uuidkey = UUID.fromString(networkmapsplitdataString[0]);
                        Set<BlockPos> blocksinnetdata = new HashSet<>();
                        for (String blocksinnetdatasplit : networkmapsplitdataString[1].split("C")) {
                            String[] blockposinnetsplit = blocksinnetdatasplit.split(",");
                            blocksinnetdata.add(new BlockPos(Integer.getInteger(blockposinnetsplit[0]), Integer.getInteger(blockposinnetsplit[1]), Integer.getInteger(blockposinnetsplit[2])));
                        }
                        String[] networkmapsplitdataStringpt3 = networkmapsplitdataString[2].split(",");
                        BlockPos corepos = new BlockPos(Integer.getInteger(networkmapsplitdataStringpt3[0]),Integer.getInteger(networkmapsplitdataStringpt3[1]),Integer.getInteger(networkmapsplitdataStringpt3[2]));
                        Long power = Long.getLong(networkmapsplitdataStringpt3[3]);
                        UUID networkUUID = UUID.fromString(networkmapsplitdataStringpt3[4]);
                        ServerWorld networkworld = server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(networkmapsplitdataStringpt3[5])));
                        networkmapdata.put(uuidkey, new Network(corepos, networkUUID, power, networkworld));
                    }

                    Set<PosWorld> interestBlockdata = new HashSet<PosWorld>();
                    String[] interestBlockStrings = splitNetData[1].split("#");
                    for (String posworlds : interestBlockStrings) {
                       String[] posworlddata = posworlds.split(",");
                       PosWorld pworld = new PosWorld(new BlockPos(Integer.getInteger(posworlddata[0]), Integer.getInteger(posworlddata[1]), Integer.getInteger(posworlddata[2])), server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(posworlddata[3]))));
                       interestBlockdata.add(pworld);
                    }
                    HashMap<PosWorld, Set<UUID>> posworlduuiddata = new HashMap<>();
                    String[] posworlduuidset = splitNetData[2].split("#");
                    for (String posworlduuid : posworlduuidset) {
                        String[] posworlduuidsplit = posworlduuid.split("L");
                        String[] posworlddata = posworlduuidsplit[0].split(",");
                        PosWorld pworld = new PosWorld(new BlockPos(Integer.getInteger(posworlddata[0]), Integer.getInteger(posworlddata[1]), Integer.getInteger(posworlddata[2])), server.getWorld(RegistryKey.of(RegistryKey.ofRegistry(Identifier.ofVanilla("dimension")), Identifier.of(posworlddata[3]))));
                        Set<UUID> uuidset = new HashSet<>();
                        String[] uuidsetdata = posworlduuidsplit[1].split(",");
                        for (String uuidstr : uuidsetdata) {
                            uuidset.add(UUID.fromString(uuidstr));
                        }
                        posworlduuiddata.put(pworld, uuidset);
                    }
                    netsObject = new Networks(networkmapdata, interestBlockdata, posworlduuiddata);
                    return netsObject;
                } catch (IOException e) {}
            } else {
                netsObject = new Networks();
                String nbtnetstring = netsObject.NetworkString();
                try {
                    Files.write(Files.createFile(server.getSavePath(WorldSavePath.ROOT).resolve("NetworksCopper")), nbtnetstring.getBytes());
                } catch (IOException e) {}
            }
        }
        return netsObject;
    }


}
