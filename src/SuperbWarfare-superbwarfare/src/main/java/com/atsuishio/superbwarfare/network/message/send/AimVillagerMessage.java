package com.atsuishio.superbwarfare.network.message.send;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AimVillagerMessage {

    private final int villagerId;

    public AimVillagerMessage(int villagerId) {
        this.villagerId = villagerId;
    }

    public static void encode(AimVillagerMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.villagerId);
    }

    public static AimVillagerMessage decode(FriendlyByteBuf buffer) {
        return new AimVillagerMessage(buffer.readInt());
    }

    public static void handler(AimVillagerMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var sender = context.get().getSender();
            if (sender == null) return;

            Entity entity = sender.level().getEntity(message.villagerId);
            if (entity instanceof AbstractVillager abstractVillager) {
                if (entity instanceof Villager villager) {
                    villager.getGossips().add(sender.getUUID(), GossipType.MINOR_NEGATIVE, 10);
                }
                abstractVillager.getBrain().setActiveActivityIfPossible(Activity.PANIC);
            }

        });
        context.get().setPacketHandled(true);
    }
}
