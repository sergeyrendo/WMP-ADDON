package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.tools.FormatTool;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Transcript extends Item {

    public static final String TAG_SCORES = "Scores";

    public Transcript() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.add(Component.translatable("des.superbwarfare.transcript").withStyle(ChatFormatting.GRAY));
        addScoresText(stack, tooltip);
    }

    public void addScoresText(ItemStack stack, List<Component> tooltip) {
        ListTag tags = stack.getOrCreateTag().getList(TAG_SCORES, Tag.TAG_COMPOUND);

        int total = 0;
        for (int i = 0; i < tags.size(); i++) {
            CompoundTag tag = tags.getCompound(i);

            int score = tag.getInt("Score");
            total += score;
            tooltip.add(Component.translatable("des.superbwarfare.transcript.score").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(score + " ").withStyle(score == 10 ? ChatFormatting.GOLD : ChatFormatting.WHITE))
                    .append(Component.translatable("des.superbwarfare.transcript.distance").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(FormatTool.format1D(tag.getDouble("Distance"), "m")).withStyle(ChatFormatting.WHITE)));
        }

        tooltip.add(Component.translatable("des.superbwarfare.transcript.total").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(total + " ").withStyle(total == 100 ? ChatFormatting.GOLD : ChatFormatting.WHITE)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isCrouching()) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            stack.getOrCreateTag().put(TAG_SCORES, new ListTag());
            return InteractionResultHolder.success(stack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
