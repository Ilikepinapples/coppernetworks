package entity0.coppernetworks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class CopperNetworkerBlockEntity extends BlockEntity {
    public CopperNetworkerBlockEntity(BlockPos pos, BlockState state) {
        super(CopperBlockanblockEntities.NETWORKERBE, pos, state);
    }
    private UUID netUUID;
    public void SetUUID(UUID uuid) {
        netUUID = uuid;
        markDirty();
    }
    public void placed(ServerWorld world, BlockPos pos) {
        if (netUUID == null) {
            netUUID = Networks.getOrCreateNetworks(world.getServer()).createNetwork(pos, 0L, world);
        }
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            nbt.putUuid("uuid", netUUID);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        netUUID = nbt.getUuid("uuid");
    }
    public UUID getUUID () {
        return netUUID;
    }

    public void broken(ServerWorld world) {
        if (Networks.getOrCreateNetworks(world.getServer()).getNetwork(netUUID) != null) {
            //Networks.getOrCreateNetworks(world.getServer()).getNetwork(netUUID).scantoremovehanging(getPos());
            if (Networks.getOrCreateNetworks(world.getServer()).getNetwork(netUUID).corepos.size() <= 1) {
                Networks.getOrCreateNetworks(world.getServer()).removeNetwork(netUUID);
            }
        }
    }
}
