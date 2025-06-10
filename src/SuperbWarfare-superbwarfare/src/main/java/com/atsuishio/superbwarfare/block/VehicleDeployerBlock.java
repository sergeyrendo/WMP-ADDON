package com.atsuishio.superbwarfare.block;

import com.atsuishio.superbwarfare.block.entity.VehicleDeployerBlockEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class VehicleDeployerBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public VehicleDeployerBlock() {
        super(Properties.of().sound(SoundType.METAL).strength(3.0f).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TRIGGERED);
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide
                || !(level.getBlockEntity(pos) instanceof VehicleDeployerBlockEntity blockEntity)
        ) return InteractionResult.SUCCESS;

        if (!player.isCreative()) return InteractionResult.FAIL;

        var stack = player.getItemInHand(hand);
        if (stack.getItem() != ModItems.CONTAINER.get()) {
            player.displayClientMessage(Component.translatable("des.superbwarfare.vehicle_deployer.fail").withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        blockEntity.writeEntityInfo(stack);
        player.displayClientMessage(Component.translatable("des.superbwarfare.vehicle_deployer.success").withStyle(ChatFormatting.GREEN), true);

        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean pMovedByPiston) {
        boolean charged = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean triggered = state.getValue(TRIGGERED);

        if (charged && !triggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.TRUE), 4);
            if (level.getBlockEntity(pos) instanceof VehicleDeployerBlockEntity blockEntity) {
                blockEntity.deploy(state);
            }
        } else if (!charged && triggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.FALSE), 4);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new VehicleDeployerBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
