package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeVehicleSeatMessage {

    private final int index;

    public ChangeVehicleSeatMessage(int index) {
        this.index = index;
    }

    public static void encode(ChangeVehicleSeatMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(message.index);
    }

    public static ChangeVehicleSeatMessage decode(FriendlyByteBuf byteBuf) {
        return new ChangeVehicleSeatMessage(byteBuf.readInt());
    }

    public static void handler(ChangeVehicleSeatMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null || !(player.getVehicle() instanceof VehicleEntity vehicle)) {
                return;
            }

            vehicle.changeSeat(player, message.index);
        });
        context.get().setPacketHandled(true);
    }
}
