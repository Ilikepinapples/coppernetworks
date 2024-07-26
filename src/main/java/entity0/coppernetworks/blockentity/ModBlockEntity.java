package entity0.coppernetworks.blockentity;

import entity0.coppernetworks.CopperNetworks;
import entity0.coppernetworks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlockEntity {
    public static final BlockEntityType<NetworkerBlockEntity> COPPER_NETWORKER_BLOCKENTITY = Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(CopperNetworks.MOD_ID, "copper_networker_blockentity"),
                    BlockEntityType.Builder.create(NetworkerBlockEntity::new, ModBlocks.COPPER_NETWORKER).build()
            );
    public static void registerBlockEntities() {
        CopperNetworks.LOGGER.info("Registering the block for" + CopperNetworks.MOD_ID);
    }

}
