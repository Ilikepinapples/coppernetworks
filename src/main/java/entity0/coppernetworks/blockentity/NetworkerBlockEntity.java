package entity0.coppernetworks.blockentity;

import entity0.coppernetworks.CopperNetworks;
import entity0.coppernetworks.NetworkerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.logging.Logger;

public class NetworkerBlockEntity extends BlockEntity {
    long timePlaced;
    boolean placed;
    long power;
    public NetworkerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.COPPER_NETWORKER_BLOCKENTITY, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, NetworkerBlockEntity blockEntity) {
        CopperNetworks.LOGGER.info(String.valueOf(world.getBlockState(pos.down())));
        CopperNetworks.LOGGER.info(String.valueOf(state));

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
