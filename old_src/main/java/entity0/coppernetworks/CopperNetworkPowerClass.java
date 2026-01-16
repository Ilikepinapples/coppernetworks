package entity0.coppernetworks;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class CopperNetworkPowerClass {
    public int id;
    public long getNetworkMaxPower (int id) {
        if (!(NetworkerClass.Networks.get(id) == null)) {
            return (long) NetworkerClass.Networks.get(id).get(1);
        }
        else {return 0L;}
    }


    public boolean canConsume(long consumePower) {return consumePower <= NetworkerClass.getPower(id);}
    public boolean canGenerate(long genPower) {return genPower + NetworkerClass.getPower(id) <= getNetworkMaxPower(id);}
    public void generate(long genPower) {NetworkerClass.setPower(NetworkerClass.getPower(id) + genPower, id);}
    public void consume(long consumePower) {NetworkerClass.setPower(NetworkerClass.getPower(id) - consumePower, id);}
    public void setNetworkmaxPower() {NetworkerClass.setPower(getNetworkMaxPower(id), id);}
    public void cleanupNetwork() {
        id = 0;
    }

}
