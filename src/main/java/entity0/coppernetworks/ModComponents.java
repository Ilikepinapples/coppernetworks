package entity0.coppernetworks;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static void initialize() {
        CopperNetworks.LOGGER.info("Initiating components for Item Access");
    }

    public static final ComponentType<copperNetworkItemPowerClass> COPPER_POWER_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(CopperNetworks.MOD_ID, "copper_power"),
            ComponentType.<copperNetworkItemPowerClass>builder().codec(null).build()
                    //copperNetworkItemPowerClass.CODEC).build()
    );
}
