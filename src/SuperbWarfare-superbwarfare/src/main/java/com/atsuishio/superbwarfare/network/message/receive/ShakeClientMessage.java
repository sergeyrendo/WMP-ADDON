package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShakeClientMessage {

    public double time;
    public double radius;
    public double amplitude;
    public double x;
    public double y;
    public double z;

    public ShakeClientMessage(double time, double radius, double amplitude, double x, double y, double z) {
        this.time = time;
        this.radius = radius;
        this.amplitude = amplitude;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(ShakeClientMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.time);
        buffer.writeDouble(message.radius);
        buffer.writeDouble(message.amplitude);
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static ShakeClientMessage decode(FriendlyByteBuf buffer) {
        return new ShakeClientMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(ShakeClientMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientEventHandler.handleShakeClient(message.time, message.radius, message.amplitude, message.x, message.y, message.z, context)));
        context.get().setPacketHandled(true);
    }
}
