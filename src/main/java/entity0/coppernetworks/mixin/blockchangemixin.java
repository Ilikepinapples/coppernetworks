package entity0.coppernetworks.mixin;

import entity0.coppernetworks.changeblockevent;
import entity0.coppernetworks.oxievent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Degradable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.logging.Logger;

@Mixin(ServerWorld.class)
public class blockchangemixin {
    @Inject(at = @At(value = "HEAD"), method = "onBlockChanged", cancellable = true)
    private void blockchange(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
        ActionResult result = changeblockevent.EVENT.invoker().interact(pos, (ServerWorld) (Object) this, oldBlock, newBlock);
        //cancelling this doesn't do anything
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}