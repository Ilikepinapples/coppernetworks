package entity0.coppernetworks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class copperNetworkItemPowerClass {
    public static final Codec<entity0.coppernetworks.copperNetworkItemPowerClass> CODEC = RecordCodecBuilder.create((instance -> instance.group(
                Codec.INT.fieldOf("networkID").forGetter(copperNetworkItemPowerClass::getNetworkID)//,
        ).apply(instance, entity0.coppernetworks.copperNetworkItemPowerClass::new)));


        public int getNetworkID() {return id;}
        public int id;
        public long networkMaxPower = ((long) NetworkerClass.Networks.get(id).get(1));



        public boolean canConsume(long consumePower) {return consumePower <= NetworkerClass.getPower(id);}
        public boolean canGenerate(long genPower) {return genPower + NetworkerClass.getPower(id) <= networkMaxPower;}
        public void generate(long genPower) {NetworkerClass.setPower((NetworkerClass.getPower(id) + genPower), id);}
        public void consume(long consumePower) {NetworkerClass.setPower((NetworkerClass.getPower(id) - consumePower), id);}
        public void setNetworkmaxPower() {NetworkerClass.setPower(networkMaxPower, id);}
        public void cleanupNetwork() {
            id = 0;
        }
    public copperNetworkItemPowerClass (int id) {
        this.id = id;
    }

}
