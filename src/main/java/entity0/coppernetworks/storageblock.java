package entity0.coppernetworks;

import entity0.coppernetworks.API.CopperStorageAPI;
import net.minecraft.block.Block;

public class storageblock extends Block implements CopperStorageAPI {
    public storageblock(Settings settings) {
        super(settings);
    }

    @Override
    public long getstoragevalue() {
        return 10000;
    }
}
