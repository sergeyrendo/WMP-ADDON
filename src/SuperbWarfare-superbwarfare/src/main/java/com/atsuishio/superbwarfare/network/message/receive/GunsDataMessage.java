package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.data.gun.DefaultGunData;
import com.atsuishio.superbwarfare.tools.BufferSerializer;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class GunsDataMessage {

    public final List<DefaultGunData> data;

    private GunsDataMessage(List<DefaultGunData> data) {
        this.data = data;
    }

    public static GunsDataMessage create() {
        return new GunsDataMessage(GunsTool.gunsData.values().stream().toList());
    }

    public static void encode(GunsDataMessage message, FriendlyByteBuf buf) {
        var obj = message.data;

        buf.writeVarInt(obj.size());
        for (var data : obj) {
            buf.writeBytes(BufferSerializer.serialize(data).copy());
        }
    }

    public static GunsDataMessage decode(FriendlyByteBuf buffer) {
        var size = buffer.readVarInt();
        var list = new ArrayList<DefaultGunData>();
        for (var i = 0; i < size; i++) {
            list.add(BufferSerializer.deserialize(buffer, new DefaultGunData()));
        }
        return new GunsDataMessage(list);
    }

    public static void handler(GunsDataMessage message) {
        GunsTool.gunsData.clear();

        for (var entry : message.data) {
            if (GunsTool.gunsData.containsKey(entry.id)) continue;
            GunsTool.gunsData.put(entry.id, entry);
        }
    }
}
