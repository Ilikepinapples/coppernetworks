package entity0.coppernetworks;

import net.minecraft.item.ItemStack;

public interface copperNetworkPowerItemAPI {
        default copperNetworkItemPowerClass copperNetworkAPI(ItemStack stack) {
            if (!stack.contains(ModComponents.COPPER_POWER_COMPONENT)) {
                stack.set(ModComponents.COPPER_POWER_COMPONENT, getDefaultComponent());
            }
            return stack.get(ModComponents.COPPER_POWER_COMPONENT);
        }

    default copperNetworkItemPowerClass getDefaultComponent() {
        return new copperNetworkItemPowerClass(0);
    }
}
