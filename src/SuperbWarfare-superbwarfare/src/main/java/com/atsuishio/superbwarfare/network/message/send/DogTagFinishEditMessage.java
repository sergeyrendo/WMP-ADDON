package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DogTagFinishEditMessage {

    private final short[][] colors;
    private final String name;

    public DogTagFinishEditMessage(short[][] colors, String name) {
        this.colors = colors;
        this.name = name;
    }

    public static void encode(DogTagFinishEditMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.colors.length);
        for (short[] color : message.colors) {
            buffer.writeVarInt(color.length);
            for (short c : color) {
                buffer.writeShort(c);
            }
        }
        buffer.writeUtf(message.name);
    }

    public static DogTagFinishEditMessage decode(FriendlyByteBuf buffer) {
        short[][] colors = new short[buffer.readVarInt()][];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new short[buffer.readVarInt()];
            for (int j = 0; j < colors[i].length; j++) {
                colors[i][j] = buffer.readShort();
            }
        }
        String name = buffer.readUtf();
        return new DogTagFinishEditMessage(colors, name);
    }

    public static void handler(DogTagFinishEditMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            if (serverPlayer == null) return;

            if (serverPlayer.containerMenu instanceof DogTagEditorMenu menu) {
                menu.finishEdit(message.colors, message.name);
            }
            serverPlayer.closeContainer();

        });
        ctx.get().setPacketHandled(true);
    }
}
