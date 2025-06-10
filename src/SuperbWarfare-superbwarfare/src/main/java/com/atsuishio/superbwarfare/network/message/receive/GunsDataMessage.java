package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GunsDataMessage {

    public final Map<String, String> gunsData;

    private GunsDataMessage(Map<String, String> gunsData) {
        this.gunsData = gunsData;
    }

    private static final Gson GSON = new Gson();

    public static GunsDataMessage create() {
        var map = new HashMap<String, String>();
        for (var entry : GunsTool.gunsData.entrySet()) {
            map.put(entry.getKey(), GSON.toJson(entry.getValue()));
        }
        return new GunsDataMessage(map);
    }

    public static void encode(GunsDataMessage message, FriendlyByteBuf buffer) {
        for (var entry : message.gunsData.entrySet()) {
            buffer.writeUtf(entry.getKey());
            buffer.writeUtf(entry.getValue());
        }
    }

    public static GunsDataMessage decode(FriendlyByteBuf buffer) {
        var map = new HashMap<String, String>();
        while (buffer.isReadable()) {
            map.put(buffer.readUtf(), buffer.readUtf());
        }

        return new GunsDataMessage(map);
    }

    public static void handler(GunsDataMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleGunsDataMessage(message, ctx)));
        ctx.get().setPacketHandled(true);
    }
}
