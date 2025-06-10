package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Predicate;

public class TargetDeployer extends Item {
    public TargetDeployer() {
        super(new Item.Properties());
    }


    private static final Predicate<Entity> IS_TARGET = e -> e instanceof TargetEntity;

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            BlockPos pos;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                pos = blockpos;
            } else {
                pos = blockpos.relative(direction);
            }

            // 禁止堆叠
            if (!level.getEntities(
                    (Entity) null,
                    ModEntities.TARGET.get().getAABB(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
                    IS_TARGET
            ).isEmpty()) {
                return InteractionResult.FAIL;
            }

            if (ModEntities.TARGET.get().spawn((ServerLevel) level, itemstack, pContext.getPlayer(), pos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, pos) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
                level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(pLevel instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!(pLevel.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
                // 禁止堆叠
                if (!pLevel.getEntities(
                        (Entity) null,
                        ModEntities.TARGET.get().getAABB(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5),
                        IS_TARGET
                ).isEmpty()) {
                    return InteractionResultHolder.fail(itemstack);
                }

                TargetEntity entity = ModEntities.TARGET.get().spawn((ServerLevel) pLevel, itemstack, pPlayer, blockpos, MobSpawnType.SPAWN_EGG, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
