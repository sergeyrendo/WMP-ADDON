package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SimulationDistanceMessage {
    public int distance;

    public SimulationDistanceMessage(int distance) {
        this.distance = distance;
    }

    public static void encode(SimulationDistanceMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.distance);
    }

    public static SimulationDistanceMessage decode(FriendlyByteBuf buffer) {
        return new SimulationDistanceMessage(buffer.readInt());
    }

    public static void handle(SimulationDistanceMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleSimulationDistanceMessage(message.distance, context)));
        context.get().setPacketHandled(true);
    }
}
