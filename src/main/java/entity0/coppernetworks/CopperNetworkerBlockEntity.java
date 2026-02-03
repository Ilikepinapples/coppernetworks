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
    UUID netUUID;
    public void placed(ServerWorld world, BlockPos pos) {
        netUUID = Networks.getOrCreateNetworks(world.getServer()).createNetwork(pos, 0L, world);
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

    public void broken(ServerWorld world) {
        Networks.getOrCreateNetworks(world.getServer()).removeNetwork(netUUID);
    }
}
