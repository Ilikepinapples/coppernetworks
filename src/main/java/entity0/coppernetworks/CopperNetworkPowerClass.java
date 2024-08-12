package entity0.coppernetworks;


import net.minecraft.block.Block;

public class CopperNetworkPowerClass {
    public long networkMaxPower;
    public long[] networkPower = new long[]{};

    public boolean canConsume(long consumePower) {
        return consumePower <= networkPower[0];
    }
    public boolean canGenerate(long genPower) {
        return genPower + networkPower[0] <= networkMaxPower;
    }
    public void generate(long genPower) {
        networkPower[0] = networkPower[0] + genPower;
    }
    public void consume(long consumePower) {
        networkPower[0] = networkPower[0] - consumePower;
    }
    public void setNetworkmaxPower() {
        networkPower[0] = networkMaxPower;
    }
    //public void setupNetwork() {
    //    networkPower[0] = 0;
    //    networkMaxPower = 0;
    //}
    //public void cleanupNetwork() {
    //    networkPower[0] = 0;
    //    networkMaxPower = 0;
    //}

}
