package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import com.atsuishio.superbwarfare.config.client.VehicleControlConfig;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.AirEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.network.message.send.MouseMoveMessage;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.isFreeCam;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientMouseHandler {

    public static Vec2 posO = new Vec2(0, 0);
    public static Vec2 posN = new Vec2(0, 0);
    public static double lerpSpeedX = 0;
    public static double lerpSpeedY = 0;

    public static double speedX = 0;
    public static double speedY = 0;

    public static double freeCameraPitch = 0;
    public static double freeCameraYaw = 0;

    public static double custom3pDistance = 0;
    public static double custom3pDistanceLerp = 0;

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;


        if (player == null) {
            return;
        }

        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        posO = posN;
        posN = MouseMovementHandler.getMousePos();

        ItemStack stack = player.getMainHandItem();

        if (!notInGame() && stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
            if (drone != null) {
                speedX = drone.getMouseSensitivity() * (posN.x - posO.x);
                speedY = drone.getMouseSensitivity() * (posN.y - posO.y);

                lerpSpeedX = Mth.lerp(drone.getMouseSpeedX(), lerpSpeedX, speedX);
                lerpSpeedY = Mth.lerp(drone.getMouseSpeedY(), lerpSpeedY, speedY);

                com.atsuishio.superbwarfare.Mod.PACKET_HANDLER.sendToServer(new MouseMoveMessage(lerpSpeedX, lerpSpeedY));
            }
            return;
        }

        if (!notInGame() && player.getVehicle() instanceof VehicleEntity vehicle && player == vehicle.getFirstPassenger()) {

            int y = 1;

            if (vehicle instanceof AirEntity && VehicleControlConfig.INVERT_AIRCRAFT_CONTROL.get()) {
                y = -1;
            }

            speedX = vehicle.getMouseSensitivity() * (posN.x - posO.x);
            speedY = y * vehicle.getMouseSensitivity() * (posN.y - posO.y);

            lerpSpeedX = Mth.lerp(vehicle.getMouseSpeedX(), lerpSpeedX, speedX);
            lerpSpeedY = Mth.lerp(vehicle.getMouseSpeedY(), lerpSpeedY, speedY);

            double i = 0;

            if (vehicle.getRoll() < 0) {
                i = 1;
            } else if (vehicle.getRoll() > 0) {
                i = -1;
            }

            if (Mth.abs(vehicle.getRoll()) > 90) {
                i *= (1 - (Mth.abs(vehicle.getRoll()) - 90) / 90);
            }

            if (!isFreeCam(player)) {
                if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                    com.atsuishio.superbwarfare.Mod.PACKET_HANDLER.sendToServer(new MouseMoveMessage(
                            (1 - (Mth.abs(vehicle.getRoll()) / 90)) * lerpSpeedX + ((Mth.abs(vehicle.getRoll()) / 90)) * lerpSpeedY * i,
                            (1 - (Mth.abs(vehicle.getRoll()) / 90)) * lerpSpeedY + ((Mth.abs(vehicle.getRoll()) / 90)) * lerpSpeedX * (vehicle.getRoll() < 0 ? -1 : 1))
                    );
                } else {
                    com.atsuishio.superbwarfare.Mod.PACKET_HANDLER.sendToServer(new MouseMoveMessage(lerpSpeedX, lerpSpeedY));
                }
            } else {
                com.atsuishio.superbwarfare.Mod.PACKET_HANDLER.sendToServer(new MouseMoveMessage(0, 0));
            }
        }
    }

    @SubscribeEvent
    public static void handleClientTick(ViewportEvent.ComputeCameraAngles event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        float times = Minecraft.getInstance().getDeltaFrameTime();

        if (isFreeCam(player)) {
            freeCameraYaw -= 0.4f * times * lerpSpeedX;
            freeCameraPitch += 0.3f * times * lerpSpeedY;
        } else {
            freeCameraYaw = Mth.lerp(0.2 * times, freeCameraYaw, 0);
            freeCameraPitch = Mth.lerp(0.2 * times, freeCameraPitch, 0);
        }

        while (freeCameraYaw > 180F) {
            freeCameraYaw -= 360;
        }
        while (freeCameraYaw <= -180F) {
            freeCameraYaw += 360;
        }
        while (freeCameraPitch > 180F) {
            freeCameraPitch -= 360;
        }
        while (freeCameraPitch <= -180F) {
            freeCameraPitch += 360;
        }

        if (player.getVehicle() instanceof VehicleEntity vehicle && player == vehicle.getFirstPassenger() && vehicle instanceof AirEntity) {
            player.setYRot(player.getVehicle().getYRot());
            player.setYHeadRot(player.getYRot());
        }

        custom3pDistanceLerp = Mth.lerp(times, custom3pDistanceLerp, custom3pDistance);
    }
}
