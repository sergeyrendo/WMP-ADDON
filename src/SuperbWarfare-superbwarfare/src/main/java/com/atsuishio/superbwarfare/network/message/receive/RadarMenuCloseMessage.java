package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RadarMenuCloseMessage {

    public int type;

    public RadarMenuCloseMessage(int type) {
        this.type = type;
    }

    public static void encode(RadarMenuCloseMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static RadarMenuCloseMessage decode(FriendlyByteBuf buffer) {
        return new RadarMenuCloseMessage(buffer.readInt());
    }

    public static void handler(RadarMenuCloseMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> ClientPacketHandler::handleRadarMenuClose));
        ctx.get().setPacketHandled(true);
    }
}
