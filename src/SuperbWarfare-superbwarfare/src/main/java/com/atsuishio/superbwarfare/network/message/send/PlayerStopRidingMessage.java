package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerStopRidingMessage {

    private final int type;

    public PlayerStopRidingMessage(int type) {
        this.type = type;
    }

    public static void encode(PlayerStopRidingMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static PlayerStopRidingMessage decode(FriendlyByteBuf buffer) {
        return new PlayerStopRidingMessage(buffer.readInt());
    }

    public static void handler(PlayerStopRidingMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            var vehicle = player.getVehicle();
            if (!(vehicle instanceof VehicleEntity)) return;

            player.stopRiding();
            player.setJumping(false);
        });
        ctx.get().setPacketHandled(true);
    }
}
