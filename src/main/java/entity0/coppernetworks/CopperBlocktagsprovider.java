package entity0.coppernetworks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class CopperBlocktagsprovider extends FabricTagProvider<Block> {
    public CopperBlocktagsprovider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }
    public static final TagKey<Block> CONDUCTIVEITEMS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(CopperNetworks.MOD_ID, "conductiveitems"));
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(CONDUCTIVEITEMS)
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.COPPER_BULB)
                .add(Blocks.COPPER_DOOR)
                .add(Blocks.COPPER_GRATE)
                .add(Blocks.COPPER_TRAPDOOR)
                .add(Blocks.CHISELED_COPPER)
                .add(Blocks.CUT_COPPER)
                .add(Blocks.CUT_COPPER_SLAB)
                .add(Blocks.CUT_COPPER_STAIRS)
                .add(CopperBlockanblockEntities.NETWORKER_BLOCK)
                .add(CopperBlockanblockEntities.STORAGE_BLOCK);
    }
}
