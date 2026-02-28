package entity0.coppernetworks;


import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class CopperBlockanblockEntities {
    public static void initialise() {}

private static <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                   BlockEntityType.BlockEntityFactory<? extends T> entityFactory, //why is this ? extends T
                                                                   Block... blocks) {
    Identifier id = Identifier.of( CopperNetworks.MOD_ID, name);
    return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.<T>create(entityFactory, blocks).build()); // why can this not be ? extends T also why does it have to be explicitly staed at all whaat the type is
}

public static Block register(Block block, String name) {
    Identifier id = Identifier.of(CopperNetworks.MOD_ID, name);
    BlockItem blockItem = new BlockItem(block, new Item.Settings());
    Registry.register(Registries.ITEM, id, blockItem);
    return Registry.register(Registries.BLOCK, id, block);
}

    public static final Block NETWORKER_BLOCK = register(new copperNetworkerBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER)
            .luminance(copperNetworkerBlock::getLuminance)
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(3.0F, 6.0F)), "networker_block");


    public static final Block STORAGE_BLOCK = register(new storageblock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER)
            .luminance(copperNetworkerBlock::getLuminance)
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(3.0F, 6.0F)), "storage_block");


    public static final BlockEntityType<CopperNetworkerBlockEntity> NETWORKERBE  = register("networker",CopperNetworkerBlockEntity::new, CopperBlockanblockEntities.NETWORKER_BLOCK);

}