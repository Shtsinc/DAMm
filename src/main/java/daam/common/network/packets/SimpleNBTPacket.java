package daam.common.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class SimpleNBTPacket extends SimplePacket {

    protected CompoundTag compound;

    public SimpleNBTPacket() {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(compound);
    }



}
