package com.atsuishio.superbwarfare.network;

import com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay;
import com.atsuishio.superbwarfare.client.overlay.DroneHudOverlay;
import com.atsuishio.superbwarfare.client.screens.DogTagEditorScreen;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.config.client.KillMessageConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import com.atsuishio.superbwarfare.menu.EnergyMenu;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.network.message.receive.ClientMotionSyncMessage;
import com.atsuishio.superbwarfare.network.message.receive.ContainerDataMessage;
import com.atsuishio.superbwarfare.network.message.receive.RadarMenuOpenMessage;
import com.atsuishio.superbwarfare.tools.PlayerKillRecord;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handlePlayerKillMessage(Player attacker, Entity target, boolean headshot, ResourceKey<DamageType> damageType, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            if (KillMessageHandler.QUEUE.size() >= KillMessageConfig.KILL_MESSAGE_COUNT.get()) {
                KillMessageHandler.QUEUE.poll();
            }
            KillMessageHandler.QUEUE.offer(new PlayerKillRecord(attacker, target, attacker.getMainHandItem(), headshot, damageType));
        }
    }

    public static void handleClientIndicatorMessage(ClientIndicatorMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            switch (message.type) {
                case 1 -> CrossHairOverlay.HEAD_INDICATOR = message.value;
                case 2 -> CrossHairOverlay.KILL_INDICATOR = message.value;
                case 3 -> CrossHairOverlay.VEHICLE_INDICATOR = message.value;
                default -> CrossHairOverlay.HIT_INDICATOR = message.value;
            }
        }
    }

    public static void handleSimulationDistanceMessage(int distance, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            DroneHudOverlay.MAX_DISTANCE = distance * 16;
        }
    }

    public static void handleContainerDataMessage(int containerId, List<ContainerDataMessage.Pair> data, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.containerMenu.containerId == containerId) {
                data.forEach(p -> ((EnergyMenu) mc.player.containerMenu).setData(p.id, p.data));
            }
        }
    }

    public static void handleRadarMenuOpen(RadarMenuOpenMessage message, Supplier<NetworkEvent.Context> ctx) {
        FuMO25ScreenHelper.resetEntities();
        FuMO25ScreenHelper.pos = message.pos;
    }

    public static void handleRadarMenuClose() {
        FuMO25ScreenHelper.resetEntities();
        FuMO25ScreenHelper.pos = null;
    }

    public static void handleResetCameraType(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player == null) return;

            Minecraft.getInstance().options.setCameraType(Objects.requireNonNullElse(ClientEventHandler.lastCameraType, CameraType.FIRST_PERSON));
        }
    }

    public static void handleClientSyncMotion(ClientMotionSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            var level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(message.id);
            if (entity != null) {
                entity.lerpMotion(message.x, message.y, message.z);
            }
        }
    }

    public static void handleClientTacticalSprintSync(boolean flag, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            MiscConfig.ALLOW_TACTICAL_SPRINT.set(flag);
            MiscConfig.ALLOW_TACTICAL_SPRINT.save();
        }
    }

    public static void handleDogTagEditorMessage(int containerId, ItemStack stack, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.containerMenu.containerId == containerId) {
                ((DogTagEditorMenu) mc.player.containerMenu).stack = stack;
                if (mc.screen instanceof DogTagEditorScreen dogTagEditorScreen) {
                    dogTagEditorScreen.stack = stack;
                }
            }
        }
    }
}
