package entity0.coppernetworks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface oxievent {
    Event<oxievent> EVENT = EventFactory.createArrayBacked(oxievent.class, (listeners) -> (position, world) -> {
        for (oxievent listener : listeners) {
            ActionResult result = listener.interact(position, world);
            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return  ActionResult.PASS;
    });

    ActionResult interact(BlockPos position, ServerWorld world);
}
