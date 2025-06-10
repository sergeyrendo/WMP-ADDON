package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModMenuTypes;
import com.atsuishio.superbwarfare.network.message.receive.DogTagEditorMessage;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE)
public class DogTagEditorMenu extends AbstractContainerMenu {

    protected final Container container;
    protected final ContainerLevelAccess access;
    @Nullable
    private String itemName;

    public ItemStack stack;

    public DogTagEditorMenu(int pContainerId) {
        this(pContainerId, new SimpleContainer(0), ContainerLevelAccess.NULL, ItemStack.EMPTY);
    }

    public DogTagEditorMenu(int pContainerId, ContainerLevelAccess access, ItemStack stack) {
        this(pContainerId, new SimpleContainer(0), access, stack);
    }

    public DogTagEditorMenu(int pContainerId, Container container, ContainerLevelAccess pContainerLevelAccess, ItemStack stack) {
        super(ModMenuTypes.DOG_TAG_EDITOR_MENU.get(), pContainerId);

        checkContainerSize(container, 0);

        this.container = container;
        this.access = pContainerLevelAccess;
        this.stack = stack;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.isAlive();
    }

    public boolean setItemName(String pItemName) {
        String s = validateName(pItemName);
        if (s != null && !s.equals(this.itemName)) {
            this.itemName = s;
            if (!this.stack.isEmpty()) {
                if (Util.isBlank(s)) {
                    this.stack.resetHoverName();
                } else {
                    this.stack.setHoverName(Component.literal(s));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String validateName(String pItemName) {
        String s = SharedConstants.filterText(pItemName);
        return s.length() <= 30 ? s : null;
    }

    @SubscribeEvent
    public static void onContainerOpened(PlayerContainerEvent.Open event) {
        if (event.getContainer() instanceof DogTagEditorMenu menu && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ItemStack itemStack = serverPlayer.getItemInHand(serverPlayer.getUsedItemHand());
            if (itemStack.is(ModItems.DOG_TAG.get())) {
                Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new DogTagEditorMessage(menu.containerId, itemStack));
            }
        }
    }

    public void finishEdit(short[][] colors, String name) {
        if (this.stack.isEmpty()) return;

        CompoundTag colorsTag = new CompoundTag();
        for (int i = 0; i < colors.length; i++) {
            int[] color = new int[colors[i].length];
            for (int j = 0; j < colors[i].length; j++) {
                color[j] = colors[i][j];
            }
            colorsTag.putIntArray("Color" + i, color);
        }
        this.stack.getOrCreateTag().put("Colors", colorsTag);

        if (!name.isEmpty()) {
            this.stack.setHoverName(Component.literal(name));
        }
    }
}
