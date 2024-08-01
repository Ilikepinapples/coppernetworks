package entity0.coppernetworks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static Block register(Block block, String name) {
        // Register the block and its item.
        Identifier id = Identifier.of(CopperNetworks.MOD_ID, name);
        BlockItem blockItem = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, id, blockItem);
        return Registry.register(Registries.BLOCK, id, block);
    }


    public static final Block COPPER_NETWORKER = register(
            new NetworkerBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.COPPER)
                    .luminance(NetworkerBlock::getLuminance)
            ), "copper_networker"
    );
    public static void initializeCategory() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((itemGroup) -> {
        itemGroup.add(ModBlocks.COPPER_NETWORKER.asItem());
    });
}



//need to add a new block class that does stuff and then make new coppernetworkblockinstead of block





}