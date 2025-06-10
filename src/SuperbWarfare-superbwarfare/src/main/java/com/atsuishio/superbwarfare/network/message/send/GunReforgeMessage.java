package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.ReforgingTableMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunReforgeMessage {

    public int type;

    public GunReforgeMessage(int type) {
        this.type = type;
    }

    public static void encode(GunReforgeMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static GunReforgeMessage decode(FriendlyByteBuf buffer) {
        return new GunReforgeMessage(buffer.readInt());
    }

    public static void handler(GunReforgeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }

            AbstractContainerMenu abstractcontainermenu = player.containerMenu;
            if (abstractcontainermenu instanceof ReforgingTableMenu menu) {
                if (!menu.stillValid(player)) {
                    return;
                }
                menu.generateResult();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
