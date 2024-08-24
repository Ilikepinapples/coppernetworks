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

    public static final ComponentType<CopperNetworkPowerClass> COPPER_POWER_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(CopperNetworks.MOD_ID, "Copper_Power"),
            ComponentType.<CopperNetworkPowerClass>builder().codec(CopperNetworkPowerClass.CODEC).build()
    );
}
