package entity0.coppernetworks;

import com.mojang.datafixers.types.templates.Check;
import com.mojang.serialization.MapCodec;
import entity0.coppernetworks.blockentity.ModBlockEntity;
import entity0.coppernetworks.blockentity.NetworkerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NetworkerBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty STORAGE = BooleanProperty.of("storage");
    public NetworkerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STORAGE, false));
    }
    public static int getLuminance(BlockState currentBlockState) {
        return currentBlockState.get(NetworkerBlock.STORAGE) ? 0 : 5;
    }
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetworkerBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntity.COPPER_NETWORKER_BLOCKENTITY, NetworkerBlockEntity::tick);
    }
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> validateTicker(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker
    ) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STORAGE);
    }
}

//placer.sendMessage(Text.of(String.valueOf(world.getTime())));
//make it glow if networkerker and not storage
// make pillar block with top
//possibly give animation when in certain states