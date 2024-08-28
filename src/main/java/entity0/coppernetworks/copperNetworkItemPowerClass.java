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
        public long getNetworkMaxPower (int id) {
            if (!(NetworkerClass.Networks.get(id) == null)) {
                return (long) NetworkerClass.Networks.get(id).get(1);
            }
            else {return 0L;}
        }



        public boolean canConsume(long consumePower) {return consumePower <= NetworkerClass.getPower(id);}
        public boolean canGenerate(long genPower) {return genPower + NetworkerClass.getPower(id) <= getNetworkMaxPower(id);}
        public void generate(long genPower) {NetworkerClass.setPower((NetworkerClass.getPower(id) + genPower), id);}
        public void consume(long consumePower) {NetworkerClass.setPower((NetworkerClass.getPower(id) - consumePower), id);}
        public void setNetworkmaxPower() {NetworkerClass.setPower(getNetworkMaxPower(id), id);}
        public void cleanupNetwork() {
            id = 0;
        }
    public copperNetworkItemPowerClass (int id) {
        this.id = id;
    }

}
