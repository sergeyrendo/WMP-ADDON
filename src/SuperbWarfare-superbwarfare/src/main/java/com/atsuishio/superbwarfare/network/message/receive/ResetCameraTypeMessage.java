package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetCameraTypeMessage {

    private final int type;

    public ResetCameraTypeMessage(int type) {
        this.type = type;
    }

    public static void encode(ResetCameraTypeMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static ResetCameraTypeMessage decode(FriendlyByteBuf buffer) {
        return new ResetCameraTypeMessage(buffer.readInt());
    }

    public static void handler(ResetCameraTypeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleResetCameraType(ctx)));
        ctx.get().setPacketHandled(true);
    }
}
