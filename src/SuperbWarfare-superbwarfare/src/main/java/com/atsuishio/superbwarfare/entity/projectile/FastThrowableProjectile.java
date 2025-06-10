package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.message.receive.ClientMotionSyncMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class FastThrowableProjectile extends ThrowableItemProjectile implements CustomSyncMotionEntity, IEntityAdditionalSpawnData {

    public static Consumer<FastThrowableProjectile> flySound = projectile -> {
    };
    public static Consumer<FastThrowableProjectile> nearFlySound = projectile -> {
    };

    private boolean isFastMoving = false;

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, @Nullable LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pLevel);
        this.setOwner(pShooter);
        if (pShooter != null) {
            this.setPos(pShooter.getX(), pShooter.getEyeY() - (double) 0.1F, pShooter.getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isFastMoving && this.isFastMoving() && this.level().isClientSide) {
            flySound.accept(this);
            nearFlySound.accept(this);
        }
        this.isFastMoving = this.isFastMoving();

        Vec3 vec3 = this.getDeltaMovement();
        float friction;
        if (this.isInWater()) {
            friction = 0.8F;
        } else {
            friction = 0.99F;
        }

        // 撤销重力影响
        vec3 = vec3.add(0, this.getGravity(), 0);
        // 重新计算动量
        this.setDeltaMovement(vec3.scale(1 / friction));

        // 重新应用重力
        Vec3 vec31 = this.getDeltaMovement();
        this.setDeltaMovement(vec31.x, vec31.y - (double) this.getGravity(), vec31.z);

        // 同步动量
        this.syncMotion();
    }

    @Override
    public void syncMotion() {
        if (this.level().isClientSide) return;
        if (!shouldSyncMotion()) return;

        if (this.tickCount % this.getType().updateInterval() == 0) {
            Mod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ClientMotionSyncMessage(this));
        }
    }

    public boolean isFastMoving() {
        return this.getDeltaMovement().lengthSqr() >= 6.25;
    }

    public boolean shouldSyncMotion() {
        return false;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        var motion = this.getDeltaMovement();
        buffer.writeFloat((float) motion.x);
        buffer.writeFloat((float) motion.y);
        buffer.writeFloat((float) motion.z);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.setDeltaMovement(additionalData.readFloat(), additionalData.readFloat(), additionalData.readFloat());
    }

    @NotNull
    public SoundEvent getCloseSound() {
        return SoundEvents.EMPTY;
    }

    @NotNull
    public SoundEvent getSound() {
        return SoundEvents.EMPTY;
    }

    public float getVolume() {
        return 0.5f;
    }
}
