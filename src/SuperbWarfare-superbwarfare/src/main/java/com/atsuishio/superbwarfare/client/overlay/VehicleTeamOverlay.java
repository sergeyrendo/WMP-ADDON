package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class VehicleTeamOverlay implements IGuiOverlay {

    public static final String ID = Mod.MODID + "_vehicle_team";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (!DisplayConfig.VEHICLE_INFO.get()) return;

        Minecraft mc = gui.getMinecraft();
        Player player = mc.player;
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = guiGraphics.pose();
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked"))
            return;

        boolean lookAtEntity = false;

        double entityRange = 0;
        Entity lookingEntity = TraceTool.camerafFindLookingEntity(player, cameraPos, 512, partialTick);

        if (lookingEntity instanceof VehicleEntity) {
            lookAtEntity = true;
            entityRange = player.distanceTo(lookingEntity);
        }

        if (lookAtEntity) {
            poseStack.pushPose();
            poseStack.scale(0.8f, 0.8f, 1);
            var font = gui.getMinecraft().font;

            if (lookingEntity.getFirstPassenger() instanceof Player player1) {
                guiGraphics.drawString(font,
                        Component.literal(player1.getDisplayName().getString() + (player1.getTeam() == null ? "" : " <" + (player1.getTeam().getName()) + ">")),
                        screenWidth / 2 + 90, screenHeight / 2 - 4, player1.getTeamColor(), false);
                guiGraphics.drawString(font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "m")),
                        screenWidth / 2 + 90, screenHeight / 2 + 5, player1.getTeamColor(), false);
            } else {
                guiGraphics.drawString(font,
                        Component.literal(lookingEntity.getDisplayName().getString() + " " + FormatTool.format1D(entityRange, "M")),
                        screenWidth / 2 + 90, screenHeight / 2 + 5, -1, false);
            }
            poseStack.popPose();
        }
    }
}
