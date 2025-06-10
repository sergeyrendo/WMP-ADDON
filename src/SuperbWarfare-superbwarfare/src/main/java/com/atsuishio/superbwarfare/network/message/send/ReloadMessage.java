package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.ReloadType;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadMessage {

    private final int type;

    public ReloadMessage(int type) {
        this.type = type;
    }

    public static ReloadMessage decode(FriendlyByteBuf buffer) {
        return new ReloadMessage(buffer.readInt());
    }

    public static void encode(ReloadMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ReloadMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (type != 0) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem gunItem)) return;

        var data = GunData.from(stack);
        if (data.useBackpackAmmo()) return;

        if (!player.isSpectator()
                && stack.getItem() instanceof GunItem
                && !GunData.from(stack).charging()
                && GunData.from(stack).reload.time() == 0
                && GunData.from(stack).bolt.actionTimer.get() == 0
                && !GunData.from(stack).reloading()
        ) {
            var reloadTypes = data.reloadTypes();
            boolean canSingleReload = reloadTypes.contains(ReloadType.ITERATIVE);
            boolean canReload = reloadTypes.contains(ReloadType.MAGAZINE) && !reloadTypes.contains(ReloadType.CLIP);
            boolean clipLoad = data.ammo.get() == 0 && reloadTypes.contains(ReloadType.CLIP);

            // 检查备弹
            if (!data.hasBackupAmmo(player)) return;

            if (canReload || clipLoad) {
                int magazine = data.magazine();

                if (gunItem.isOpenBolt(stack)) {
                    if (gunItem.hasBulletInBarrel(stack)) {
                        if (data.ammo.get() < magazine + 1) {
                            data.reload.reloadStarter.markStart();
                        }
                    } else {
                        if (data.ammo.get() < magazine) {
                            data.reload.reloadStarter.markStart();
                        }
                    }
                } else if (data.ammo.get() < magazine) {
                    data.reload.reloadStarter.markStart();
                }
                return;
            }

            if (canSingleReload && data.ammo.get() < data.magazine()) {
                data.reload.singleReloadStarter.markStart();
            }
        }
    }
}
