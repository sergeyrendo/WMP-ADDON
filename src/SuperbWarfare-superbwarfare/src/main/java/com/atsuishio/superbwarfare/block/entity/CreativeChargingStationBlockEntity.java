package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.capability.energy.InfinityEnergyStorage;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Energy Data Slot Code based on @GoryMoon's Chargers
 */
public class CreativeChargingStationBlockEntity extends BlockEntity {

    public static final int CHARGE_RADIUS = 8;

    private LazyOptional<IEnergyStorage> energyHandler;

    public CreativeChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_CHARGING_STATION.get(), pos, state);
        this.energyHandler = LazyOptional.of(InfinityEnergyStorage::new);
    }

    public static void serverTick(CreativeChargingStationBlockEntity blockEntity) {
        if (blockEntity.level == null) return;

        blockEntity.energyHandler.ifPresent(handler -> {
            blockEntity.chargeEntity();
            blockEntity.chargeBlock();
        });
    }

    private void chargeEntity() {
        if (this.level == null) return;
        if (this.level.getGameTime() % 20 != 0) return;

        List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(CHARGE_RADIUS));
        entities.forEach(entity -> entity.getCapability(ForgeCapabilities.ENERGY).ifPresent(cap -> {
            if (cap.canReceive()) {
                cap.receiveEnergy(Integer.MAX_VALUE, false);
            }
        }));
    }

    private void chargeBlock() {
        if (this.level == null) return;

        for (Direction direction : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity == null
                    || !blockEntity.getCapability(ForgeCapabilities.ENERGY).isPresent()
                    || blockEntity instanceof CreativeChargingStationBlockEntity
            ) continue;

            blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                if (energy.canReceive() && energy.getEnergyStored() < energy.getMaxEnergyStored()) {
                    energy.receiveEnergy(Integer.MAX_VALUE, false);
                    blockEntity.setChanged();
                }
            });
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, energyHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.energyHandler = LazyOptional.of(InfinityEnergyStorage::new);
    }
}
