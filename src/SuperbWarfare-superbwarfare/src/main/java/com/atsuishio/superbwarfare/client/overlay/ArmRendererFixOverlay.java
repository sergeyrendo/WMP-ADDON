package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class ArmRendererFixOverlay implements IGuiOverlay {

    public static final String ID = Mod.MODID + "_arm_fix";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player entity = gui.getMinecraft().player;
        if (entity != null) {
            InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, screenWidth / 2 - 114514, screenHeight / 2 + 22, 1, 0f, 0, entity);
        }
    }
}
