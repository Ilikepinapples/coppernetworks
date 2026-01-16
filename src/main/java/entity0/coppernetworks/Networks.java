package entity0.coppernetworks;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
//add mark dirty to everything
public class Networks extends PersistentState {
    //all of the stuff inaisw hwew is static for now while I work out persistence
    public static HashMap<UUID, Network> NetworkMap = new HashMap<>();

    public static UUID createNetwork(BlockPos coreposition, long netpower, ServerWorld world) {
        UUID uuid = UUID.randomUUID();
        Network net = new Network(coreposition, uuid, netpower, world);
        NetworkMap.put(uuid, net);
        return uuid;
    }
    public static Network getNetwork(UUID uuid) {
        return NetworkMap.get(uuid);
    }
    //get or create probably isn't nescessary because a block with no id is gonna make a new network anyway and a block with an id is gonna connect to a network unless something has gone very wrong

    public static Set<PosWorld> interestedblocks = new HashSet<PosWorld>();
    public static HashMap<PosWorld, Set<UUID>> interestNetworkMap = new HashMap<>();

    public static void removeinterest (PosWorld posworld, UUID uuid) {
        Set<UUID> uuidset;
        uuidset = interestNetworkMap.get(posworld);
        uuidset.remove(uuid);
        if (uuidset.isEmpty()) {
            interestedblocks.remove(posworld);
            interestNetworkMap.remove(posworld);
        }
    }
    public static void addinterest (PosWorld posworld, UUID uuid) {
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


    public static boolean ispresentininterest(PosWorld posworld) {
        return(interestedblocks.contains(posworld));
    }

    public static Set<UUID> getInterestedNetworks(PosWorld posworld) {
        return interestNetworkMap.get(posworld);
    }

    public static void addInterestAllAround (PosWorld posworld, UUID uuid) {
        addinterest(posworld, uuid);
        addinterest(new PosWorld(posworld.pos.up(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.down(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.north(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.south(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.east(), posworld.world), uuid);
        addinterest(new PosWorld(posworld.pos.west(), posworld.world), uuid);
    }
    /** this causes problems with blocks being removed from the network interest that should remain in it, while I could write something that checked the ffected surfaces of the network and readded interest i don't know if that solution is any more elegant or indeed performanent than just reasserting interest on all blocks in the ntwork after this is called **/
    public static void removeInterestAllAround(PosWorld posworld, UUID uuid) {
        removeinterest(posworld, uuid);
        removeinterest(new PosWorld(posworld.pos.up(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.down(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.north(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.south(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.east(), posworld.world), uuid);
        removeinterest(new PosWorld(posworld.pos.west(), posworld.world), uuid);
    }
//make sure that this actually deletes the removed UUIDS and networks. I assume the given argument of nbt is the current nbt of the networks which i don't actually need so
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbttowrite = new NbtCompound();
        NbtCompound networkmap = new NbtCompound();
        NetworkMap.forEach((uuid, network) -> {
            NbtCompound net = new NbtCompound();
            net.putIntArray("corepos", new int[]{network.corepos.getX(), network.corepos.getY(), network.corepos.getZ()});
            net.putString("world", network.world.getRegistryKey().toString());
            net.putLong("power", network.power);

            NbtCompound blockinnet = new NbtCompound();
            BlockPos[] posarray = (BlockPos[]) network.blocksinnet.toArray();
            for (int i = 0; i < posarray.length; i++) {
                blockinnet.putIntArray(String.valueOf(i), new int[]{posarray[i].getX(),posarray[i].getY(),posarray[i].getZ()});
            }
            net.put("blocksinnet", blockinnet);
            networkmap.put(uuid.toString(), networkmap);
        });

        NbtCompound blockininterest = new NbtCompound();
        PosWorld[] interestarray = (PosWorld[]) interestedblocks.toArray();
        for (int i = 0; i < interestarray.length; i++) {
            NbtCompound posworldnbt = new NbtCompound();
            posworldnbt.putIntArray("blockpos", new int[]{interestarray[i].pos.getX(),interestarray[i].pos.getY(),interestarray[i].pos.getZ()});
            posworldnbt.putString("world", interestarray[i].world.getRegistryKey().toString());
            blockininterest.put(String.valueOf(i), posworldnbt);
        }
        NbtCompound interestnetworkmap = new NbtCompound();
        interestNetworkMap.forEach((posworld, uuidset) -> {
            NbtCompound netuuidsets = new NbtCompound();
            UUID[] uuidaarray = (UUID[]) uuidset.toArray();
            for (int i = 0; i < uuidaarray.length; i++) {
                netuuidsets.putUuid(String.valueOf(i), uuidaarray[i]);
            }
            interestnetworkmap.put(posworld.world.getRegistryKey() + "," + posworld.pos.getX() + "," + posworld.pos.getY() + "," + posworld.pos.getZ(), netuuidsets);
        });
        nbttowrite.put("netmap", networkmap);
        nbttowrite.put("blockininterest", blockininterest);
        nbttowrite.put("interestmap", interestnetworkmap);
        return nbttowrite;
    }
}
