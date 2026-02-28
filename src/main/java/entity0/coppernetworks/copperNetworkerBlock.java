package entity0.coppernetworks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class copperNetworkerBlock extends BlockWithEntity {
    protected copperNetworkerBlock(Settings settings) {
        super(settings);
    }

    public static int getLuminance(BlockState state) {
        return 5;
    }
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(copperNetworkerBlock::new);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperNetworkerBlockEntity(pos, state);
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            BlockEntity ent = world.getBlockEntity(pos);
            if (ent instanceof CopperNetworkerBlockEntity) {
                ((CopperNetworkerBlockEntity) ent).placed((ServerWorld) world, pos);
            }
        }
    }



    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity ent = world.getBlockEntity(pos);
            if (ent instanceof CopperNetworkerBlockEntity) {
                ((CopperNetworkerBlockEntity) ent).broken((ServerWorld) world);
            }
        }
        return super.onBreak(world, pos, state, player);
    }
}
