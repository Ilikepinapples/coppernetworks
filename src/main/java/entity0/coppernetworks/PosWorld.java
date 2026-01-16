package entity0.coppernetworks;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.rmi.ServerError;
import java.util.Objects;

public class PosWorld {
    BlockPos pos;
    ServerWorld world;
    public PosWorld (BlockPos pos, ServerWorld world) {
        this.pos = pos;
        this.world = world;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PosWorld posWorld = (PosWorld) o;
        return Objects.equals(pos, posWorld.pos) && Objects.equals(world, posWorld.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, world);
    }
}
