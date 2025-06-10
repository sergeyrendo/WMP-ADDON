package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DogTagEditorMessage {

    private final int containerId;
    private final ItemStack stack;

    public DogTagEditorMessage(int containerId, ItemStack stack) {
        this.containerId = containerId;
        this.stack = stack;
    }

    public static DogTagEditorMessage decode(FriendlyByteBuf buf) {
        return new DogTagEditorMessage(buf.readUnsignedByte(), buf.readItem());
    }

    public static void encode(DogTagEditorMessage message, FriendlyByteBuf buf) {
        buf.writeByte(message.containerId);
        buf.writeItem(message.stack);
    }

    public static void handler(DogTagEditorMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleDogTagEditorMessage(message.containerId, message.stack, ctx)));
        ctx.get().setPacketHandled(true);
    }
}
