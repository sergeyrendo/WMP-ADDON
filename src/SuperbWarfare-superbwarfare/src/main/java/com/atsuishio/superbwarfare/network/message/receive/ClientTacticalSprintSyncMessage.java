package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientTacticalSprintSyncMessage {

    public boolean flag;

    public ClientTacticalSprintSyncMessage(boolean flag) {
        this.flag = flag;
    }

    public static void encode(ClientTacticalSprintSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.flag);
    }

    public static ClientTacticalSprintSyncMessage decode(FriendlyByteBuf buffer) {
        return new ClientTacticalSprintSyncMessage(buffer.readBoolean());
    }

    public static void handler(ClientTacticalSprintSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleClientTacticalSprintSync(message.flag, ctx)));
        ctx.get().setPacketHandled(true);
    }
}
