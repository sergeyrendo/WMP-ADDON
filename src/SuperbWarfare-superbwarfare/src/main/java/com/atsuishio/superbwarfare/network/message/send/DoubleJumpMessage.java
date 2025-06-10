package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpMessage {

    private final int empty;

    public DoubleJumpMessage(int empty) {
        this.empty = empty;
    }

    public static DoubleJumpMessage decode(FriendlyByteBuf buffer) {
        return new DoubleJumpMessage(buffer.readInt());
    }

    public static void encode(DoubleJumpMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.empty);
    }

    public static void handler(DoubleJumpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                Level level = player.level();
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();
                level.playSound(null, BlockPos.containing(x, y, z), ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1);
            }
        });
        context.setPacketHandled(true);
    }
}
