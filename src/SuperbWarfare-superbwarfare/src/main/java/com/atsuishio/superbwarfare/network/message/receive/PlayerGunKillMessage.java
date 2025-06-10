package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlayerGunKillMessage(int attackerId, int targetId, boolean headshot, ResourceKey<DamageType> damageType) {

    public static void encode(PlayerGunKillMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.attackerId);
        buffer.writeInt(message.targetId);
        buffer.writeBoolean(message.headshot);
        buffer.writeResourceKey(message.damageType);
    }

    public static PlayerGunKillMessage decode(FriendlyByteBuf buffer) {
        int attackerId = buffer.readInt();
        int targetId = buffer.readInt();
        boolean headshot = buffer.readBoolean();
        ResourceKey<DamageType> damageType = buffer.readResourceKey(Registries.DAMAGE_TYPE);
        return new PlayerGunKillMessage(attackerId, targetId, headshot, damageType);
    }

    public static void handler(PlayerGunKillMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                Player player = level.getEntity(message.attackerId) instanceof Player ? (Player) level.getEntity(message.attackerId) : null;
                Entity target = level.getEntity(message.targetId);

                if (player != null && target != null) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePlayerKillMessage(player, target, message.headshot, message.damageType, ctx));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
