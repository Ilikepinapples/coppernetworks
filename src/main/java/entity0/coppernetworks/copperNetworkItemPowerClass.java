package entity0.coppernetworks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class copperNetworkItemPowerClass {
    public static final Codec<entity0.coppernetworks.copperNetworkItemPowerClass> CODEC = RecordCodecBuilder.create((instance -> instance.group(
                Codec.LONG.fieldOf("networkMaxPower").forGetter(copperNetworkItemPowerClass::getNetworkMaxPower)//,
                //Codec.LONG_STREAM.xmap(LongStream::toArray, Arrays::stream).fieldOf("networkPower").forGetter(copperNetworkItemPowerClass::getNetworkPower)
        ).apply(instance, entity0.coppernetworks.copperNetworkItemPowerClass::new)));

        public long networkMaxPower;
        public long getNetworkMaxPower() {return networkMaxPower;}
        public long[] networkPower = new long[]{0};
        public long[] getNetworkPower() {return networkPower;}



        public boolean canConsume(long consumePower) {return consumePower <= networkPower[0];}
        public boolean canGenerate(long genPower) {return genPower + networkPower[0] <= networkMaxPower;}
        public void generate(long genPower) {networkPower[0] = networkPower[0] + genPower;}
        public void consume(long consumePower) {networkPower[0] = networkPower[0] - consumePower;}
        public void setNetworkmaxPower() {networkPower[0] = networkMaxPower;}
        public void cleanupNetwork() {
            networkPower = new long[]{0};
            networkMaxPower = 0;
        }
    public copperNetworkItemPowerClass (long networkMaxPower//, long[] networkPower) {
    ) {
        this.networkMaxPower = networkMaxPower;
        //this.networkPower = networkPower;
    }

}
