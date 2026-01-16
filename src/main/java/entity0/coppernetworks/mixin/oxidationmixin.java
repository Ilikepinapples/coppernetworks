package entity0.coppernetworks.mixin;

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

@Mixin(Degradable.class)
public interface oxidationmixin {
	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), method = "tickDegradation", cancellable = true)
	private void onOxidise(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {

		ActionResult result = oxievent.EVENT.invoker().interact(pos, world);

		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
	}
}