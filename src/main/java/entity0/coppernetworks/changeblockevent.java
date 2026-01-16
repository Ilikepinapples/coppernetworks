package entity0.coppernetworks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface changeblockevent {
    Event<changeblockevent> EVENT = EventFactory.createArrayBacked(changeblockevent.class, (listeners) -> (position, world, old, newb) -> {
        for (changeblockevent listener : listeners) {
            ActionResult result = listener.interact(position, world, old, newb);
            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return  ActionResult.PASS;
    });

    ActionResult interact(BlockPos position, ServerWorld world, BlockState old, BlockState newb);
}
