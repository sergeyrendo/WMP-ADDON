package tech.wmp.wmp.entity;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArmedVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ThirdPersonCameraPosition;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
// import com.atsuishio.superbwarfare.entity.vehicle.weapon.CannonShellWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
// import com.atsuishio.superbwarfare.entity.vehicle.weapon.SwarmDroneWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.VehicleWeapon;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.mojang.math.Axis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import tech.wmp.wmp.init.ModSounds;

// Импортируем необходимые классы для атрибутов
// import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
// import net.minecraft.world.entity.ai.attributes.Attributes;
// import net.minecraft.world.entity.Mob;

public class humveelvl2Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, ArmedVehicleEntity, WeaponVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final EntityDataAccessor<Integer> HEAVY_AMMO = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CANNON_FIRE_TIME = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LOADED_MISSILE = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> MISSILE_COUNT = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SMOKE_DECOY = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GUN_FIRE_TIME = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> MG_AMMO = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LOADED_AMMO_TYPE = SynchedEntityData.defineId(humveelvl2Entity.class, EntityDataSerializers.INT);
    private VehicleWeapon[][] weapons = initWeapons();
    private int reloadCoolDown;
    private int selectedSeat;

    public humveelvl2Entity(EntityType<? extends humveelvl2Entity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.5f);
        // this.setStepHeight(1.5f);
        // Инициализация значений данных сущности
        if (!world.isClientSide()) {
            this.entityData.set(HEAVY_AMMO, 0); // Начальное значение для 
            this.entityData.set(CANNON_FIRE_TIME, 0);
            this.entityData.set(LOADED_MISSILE, 0);
            this.entityData.set(MISSILE_COUNT, 0);
            this.entityData.set(SMOKE_DECOY, 0);
            this.entityData.set(GUN_FIRE_TIME, 0);
        }
    }

    @SuppressWarnings("unchecked")
    public static humveelvl2Entity clientSpawn(PlayMessages.SpawnEntity packet, Level world) {
        EntityType<?> entityTypeFromPacket = BuiltInRegistries.ENTITY_TYPE.byId(packet.getTypeId());
        if (entityTypeFromPacket == null) {
            Mod.LOGGER.error("Failed to create entity from packet: Unknown entity type id: " + packet.getTypeId());
            return null; 
        }
        if (!(entityTypeFromPacket instanceof EntityType<?>)) {
             Mod.LOGGER.error("Retrieved EntityType is not an instance of EntityType<?> for id: " + packet.getTypeId());
             return null;
        }

        EntityType<humveelvl2Entity> castedEntityType = (EntityType<humveelvl2Entity>) entityTypeFromPacket;
        humveelvl2Entity entity = new humveelvl2Entity(castedEntityType, world);
        return entity;
    }

    @Override
    public VehicleWeapon[][] initWeapons() {
        return new VehicleWeapon[][]{
                new VehicleWeapon[]{

                },
                new VehicleWeapon[]{
                        // 机枪
                        new ProjectileWeapon()
                                .damage(VehicleConfig.HEAVY_MACHINE_GUN_DAMAGE.get())
                                .headShot(2)
                                .zoom(false)
                                .bypassArmorRate(0.4f)
                                .ammo(ModItems.HEAVY_AMMO.get())
                                .icon(Mod.loc("textures/screens/vehicle_weapon/gun_12_7mm.png"))
                                .sound1p(ModSounds.M2_1P.get())
                                .sound3p(ModSounds.M2_3P.get())
                                .sound3pFar(ModSounds.M2_FAR.get())
                                .sound3pVeryFar(ModSounds.M2_VERYFAR.get()),
                }
        };
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<humveelvl2Entity>(this, "movement", 0, this::idlePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return Mod.loc("textures/vehicle_icon/humvee_icon.png");
    }

    @Override
    public ThirdPersonCameraPosition getThirdPersonCameraPosition(int index) {
        return new ThirdPersonCameraPosition(2.75, 1, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOADED_AMMO_TYPE, 0);
        this.entityData.define(MG_AMMO, 0);
        this.entityData.define(HEAVY_AMMO, 0);
        this.entityData.define(CANNON_FIRE_TIME, 0);
        this.entityData.define(LOADED_MISSILE, 0);
        this.entityData.define(MISSILE_COUNT, 0);
        this.entityData.define(SMOKE_DECOY, 0);
        this.entityData.define(GUN_FIRE_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        // Добавьте здесь любые дополнительные данные для сохранения, если они есть
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        // Восстановите здесь любые дополнительные данные, если они есть
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound(com.atsuishio.superbwarfare.init.ModSounds.WHEEL_STEP.get(), (float) (getDeltaMovement().length() * 0.3), random.nextFloat() * 0.15f + 1.05f);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .multiply(0.2f)
                .multiply(1.5f, DamageTypes.ARROW)
                .multiply(1.5f, DamageTypes.TRIDENT)
                .multiply(2.5f, DamageTypes.MOB_ATTACK)
                .multiply(2f, DamageTypes.MOB_ATTACK_NO_AGGRO)
                .multiply(1.5f, DamageTypes.MOB_PROJECTILE)
                .multiply(12.5f, DamageTypes.LAVA)
                .multiply(6f, DamageTypes.EXPLOSION)
                .multiply(6f, DamageTypes.PLAYER_EXPLOSION)
                .multiply(2.4f, ModDamageTypes.CUSTOM_EXPLOSION)
                .multiply(2f, ModDamageTypes.PROJECTILE_BOOM)
                .multiply(0.75f, ModDamageTypes.MINE)
                .multiply(1.5f, ModDamageTypes.CANNON_FIRE)
                .multiply(0.25f, ModTags.DamageTypes.PROJECTILE)
                .multiply(0.85f, ModTags.DamageTypes.PROJECTILE_ABSOLUTE)
                .multiply(10f, ModDamageTypes.VEHICLE_STRIKE)
                .reduce(7);
    }

    public double getSubmergedHeight(Entity entity) {
        for (FluidType fluidType : ForgeRegistries.FLUID_TYPES.get().getValues()) {
            if (entity.level().getFluidState(entity.blockPosition()).getFluidType() == fluidType)
                return entity.getFluidTypeHeight(fluidType);
        }
        return 0;
    }

    public void updateTurretRotation(Player player) {
        if (player == getNthEntity(1)) {
            float yaw = player.getYHeadRot();
            float pitch = player.getXRot();
    
            // Изменено: исправлена инверсия, сохранена нормализация
            this.setTurretYRot(Mth.wrapDegrees(this.getYRot() - yaw));
            this.setTurretXRot(Mth.clamp(-pitch, -45f, 45f));
        }
    }


    @Override
    public void baseTick() {
        turretYRotO = this.getTurretYRot();
        turretXRotO = this.getTurretXRot();
        rudderRotO = this.getRudderRot();
        leftWheelRotO = this.getLeftWheelRot();
        rightWheelRotO = this.getRightWheelRot();

        super.baseTick();

        

        if (this.entityData.get(GUN_FIRE_TIME) > 0) {
            this.entityData.set(GUN_FIRE_TIME, this.entityData.get(GUN_FIRE_TIME) - 1);
        }


        // Логика обновления поворота турели
        Entity gunner = getNthEntity(1); // Получаем сущность на втором месте
        if (gunner instanceof Player) {
            updateTurretRotation((Player) gunner); // Вызываем наш метод
        }

        if (this.onGround()) {
            float f0 = 0.54f + 0.25f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.05 * this.getDeltaMovement().horizontalDistance())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f0, 0.85, f0));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.95, 0.99));
        }

        if (this.level() instanceof ServerLevel) {
            boolean hasCreativeAmmo = false;
            for (int i = 0; i < getMaxPassengers(); i++) {
                if (getNthEntity(i) instanceof Player pPlayer && InventoryTool.hasCreativeAmmoBox(pPlayer)) {
                    hasCreativeAmmo = true;
                }
            }

            // this.handleAmmo();

        }

        lowHealthWarning();
        this.terrainCompact(2.7f, 3.61f);
        inertiaRotate(1.25f);


        this.refreshDimensions();
    }

    @Override
    public boolean canCollideHardBlock() {
        return getDeltaMovement().horizontalDistance() > 0.09 || Mth.abs(this.entityData.get(POWER)) > 0.15;
    }

    @Override
    public void travel() {
        Entity passenger0 = this.getFirstPassenger();

        if (this.getEnergy() <= 0) return;

        if (passenger0 == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
            this.entityData.set(POWER, 0f);
        }

        if (forwardInputDown) {
            this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + (this.entityData.get(POWER) < 0 ? 0.014f : 0.0036f), 0.26f));
        }

        if (backInputDown) {
            this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - (this.entityData.get(POWER) > 0 ? 0.014f : 0.0036f), -0.15f));
        }

        if (rightInputDown) {
            this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.11f);
        } else if (this.leftInputDown) {
            this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.11f);
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.consumeEnergy(VehicleConfig.LAV_150_ENERGY_COST.get());
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * (upInputDown ? 0.5f : (rightInputDown || leftInputDown) ? 0.977f : 0.99f));
        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * (float) Math.max(0.76f - 0.1f * this.getDeltaMovement().horizontalDistance(), 0.3));

        float angle = (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1));
        double s0;

        if (Mth.abs(angle) < 90) {
            s0 = this.getDeltaMovement().horizontalDistance();
        } else {
            s0 = -this.getDeltaMovement().horizontalDistance();
        }

        this.setLeftWheelRot((float) ((this.getLeftWheelRot() - 1.25 * s0) - this.getDeltaMovement().horizontalDistance() * Mth.clamp(1.5f * this.entityData.get(DELTA_ROT), -5f, 5f)));
        this.setRightWheelRot((float) ((this.getRightWheelRot() - 1.25 * s0) + this.getDeltaMovement().horizontalDistance() * Mth.clamp(1.5f * this.entityData.get(DELTA_ROT), -5f, 5f)));

        this.setRudderRot(Mth.clamp(this.getRudderRot() - this.entityData.get(DELTA_ROT), -0.8f, 0.8f) * 0.75f);

        this.setYRot((float) (this.getYRot() - Math.max(10 * this.getDeltaMovement().horizontalDistance(), 0) * this.getRudderRot() * (this.entityData.get(POWER) > 0 ? 1 : -1)));
        if (onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(getViewVector(1).scale(this.entityData.get(POWER))));
        }
    }


    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.HUMVEE_ENGINE.get();
    }

    @Override
    public float getEngineSoundVolume() {
        return Math.max(Mth.abs(entityData.get(POWER)), Mth.abs(0.1f * this.entityData.get(DELTA_ROT))) * 2.5f;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        Matrix4f transform = getVehicleTransform(1);

        int i = this.getSeatIndex(passenger);
        Vector4f worldPosition;

        switch(i) {
            case 0: // Водитель (слева спереди)
                worldPosition = transformPosition(transform, 0.8f, 0.28f, 0.2f); 
                break;
            case 1: // Пассажир рядом с водителем
            worldPosition = transformPosition(transform, 0.0f, 1.5f, 0.0f); 
                break;
            case 2: // Пассажир сзади слева
            worldPosition = transformPosition(transform, 0.8f, 0.28f, -0.8f); 
                break;
            case 3: // Пассажир сзади справа
            worldPosition = transformPosition(transform, -0.8f, 0.28f, -0.8f); 
                break;
            case 4: // Пассажир рядом с водителем
            worldPosition = transformPosition(transform, -0.8f, 0.28f, 0.2f); 
                break;
            default:
                worldPosition = transformPosition(transform, 0, 1, 0);
                break;
        }

        passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
        callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
    }

    @Override
    public int getMaxPassengers() {
        return 5; // Водитель + 3 пассажира (типичная компоновка седана)
    }

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), getAttacker(), getAttacker()), 80f,
                    this.getX(), this.getY(), this.getZ(), 5f, ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP, true).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }

        explodePassengers();
        super.destroy();
    }


    public Matrix4f getTurretTransform(float ticks) {
        Matrix4f transformV = getVehicleTransform(ticks);

        Matrix4f transform = new Matrix4f();
        Vector4f worldPosition = transformPosition(transform, 0, 2.5f, 0); // Примерные координаты турели

        transformV.translate(worldPosition.x, worldPosition.y, worldPosition.z);
        transformV.rotate(Axis.YP.rotationDegrees(Mth.lerp(ticks, turretYRotO, getTurretYRot())));
        return transformV;
    }

    public Matrix4f getBarrelTransform(float ticks) {
        Matrix4f transformT = getTurretTransform(ticks);

        Matrix4f transform = new Matrix4f();
        Vector4f worldPosition = transformPosition(transform, 0.3625f, 0.293125f, 1.18095f);

        transformT.translate(worldPosition.x, worldPosition.y, worldPosition.z);

        float a = getTurretYaw(ticks);

        float r = (Mth.abs(a) - 90f) / 90f;

        float r2;

        if (Mth.abs(a) <= 90f) {
            r2 = a / 90f;
        } else {
            if (a < 0) {
                r2 = -(180f + a) / 90f;
            } else {
                r2 = (180f - a) / 90f;
            }
        }

        float x = Mth.lerp(ticks, turretXRotO, getTurretXRot());
        float xV = Mth.lerp(ticks, xRotO, getXRot());
        float z = Mth.lerp(ticks, prevRoll, getRoll());

        // Применяем вращение по оси X без инверсии
        transformT.rotate(Axis.XP.rotationDegrees(-(x + r * xV + r2 * z)));

        // Применяем смещение ствола относительно турели после всех вращений
        transformT.translate(0.3625f, 0.293125f, 1.18095f);

        return transformT;
    }


    public Vec3 getBarrelVector(float pPartialTicks) {
        Matrix4f transform = getBarrelTransform(pPartialTicks);
        Vector4f rootPosition = transformPosition(transform, 0, 0, 0);
        Vector4f targetPosition = transformPosition(transform, 0, 0, 1);
        return new Vec3(rootPosition.x, rootPosition.y, rootPosition.z).vectorTo(new Vec3(targetPosition.x, targetPosition.y, targetPosition.z));
    }




    @Override
    public void onPassengerTurned(Entity entity) {
        super.onPassengerTurned(entity); // Вызываем реализацию родительского класса
    }

    private PlayState idlePredicate(AnimationState<humveelvl2Entity> event) {
        if (Mth.abs((float)this.getDeltaMovement().horizontalDistanceSqr()) > 0.001 || Mth.abs(this.entityData.get(POWER)) > 0.05) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.lav.idle"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.lav.idle"));
    }
    
    // Реализация методов ArmedVehicleEntity - заглушки, так как оружия у нас больше нет
    
    @Override
    public int mainGunRpm(Player player) {
        return 470; // Нет оружия
    }

    @Override
    public boolean canShoot(Player player) {
        if (player == getNthEntity(1)) {
            return (this.entityData.get(MG_AMMO) > 0 || InventoryTool.hasCreativeAmmoBox(player)) && !cannotFire;
        }
        return false;
    }

    @Override
    public int getAmmoCount(Player player) {
        if (player == getNthEntity(1)) {
            return this.entityData.get(MG_AMMO);
        }
        return 0;
    }

    @Override
    public void vehicleShoot(Player player, int type) {
        boolean hasCreativeAmmo = false;
        for (int i = 0; i < getMaxPassengers() - 1; i++) {
            if (getNthEntity(i) instanceof Player pPlayer && InventoryTool.hasCreativeAmmoBox(pPlayer)) {
                hasCreativeAmmo = true;
            }
        }

        // if (type == 1) {
        //     if (this.cannotFire) return;
        //     if (this.entityData.get(HEAVY_AMMO) <= 0 && !hasCreativeAmmo) return;
    
        //     Matrix4f transform = getBarrelTransform(1);
        //     Vector4f worldPosition = transformPosition(transform, -0.8f, -0.20f, 0.5f);
    
        //     var projectile = (ProjectileWeapon) getWeapon(1);
        //     var projectileEntity = projectile.create(player).setGunItemId(this.getType().getDescriptionId() + ".2");
    
        //     projectileEntity.setPos(worldPosition.x - 1.1 * this.getDeltaMovement().x, worldPosition.y, worldPosition.z - 1.1 * this.getDeltaMovement().z);
        //     projectileEntity.shoot(getBarrelVector(1).x, getBarrelVector(1).y + 0.01f, getBarrelVector(1).z, 20, 0.3f);
    
        //     this.level().addFreshEntity(projectileEntity);
    
        //     if (!player.level().isClientSide) {
        //         playShootSound3p(player, 1, 4, 12, 24);
        //     }

        //     this.entityData.set(GUN_FIRE_TIME, 2);
        //     this.entityData.set(HEAT, this.entityData.get(HEAT) + 4);

        //     Level level = player.level();
        //     final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

        //     for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
        //         if (target instanceof ServerPlayer serverPlayer) {
        //             Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 4, 6, this.getX(), this.getEyeY(), this.getZ()));
        //         }
        //     }

        //     if (hasCreativeAmmo) return;

        //     ItemStack ammoBox = this.getItemStacks().stream().filter(stack -> {
        //         if (stack.is(ModItems.AMMO_BOX.get())) {
        //             return Ammo.HEAVY.get(stack) > 0;
        //         }
        //         return false;
        //     }).findFirst().orElse(ItemStack.EMPTY);

        //     if (!ammoBox.isEmpty()) {
        //         Ammo.HEAVY.add(ammoBox, -1);
        //     } else {
        //         consumeItem(getWeapon(1).ammo, 1);
        //     }
        // }

        if (type == 1) {
            if (this.cannotFire) return;
            Matrix4f transform = getBarrelTransform(1);
            Vector4f worldPosition = transformPosition(transform, -0.8f, -0.20f, 0.5f);

            var projectile = (ProjectileWeapon) getWeapon(1);
            var projectileEntity = projectile.create(player).setGunItemId(this.getType().getDescriptionId() + ".2");

            projectileEntity.setPos(worldPosition.x - 1.1 * this.getDeltaMovement().x, worldPosition.y, worldPosition.z - 1.1 * this.getDeltaMovement().z);
            projectileEntity.shoot(getBarrelVector(1).x, getBarrelVector(1).y + 0.01f, getBarrelVector(1).z, 20, 0.3f);

            this.level().addFreshEntity(projectileEntity);

            if (!player.level().isClientSide) {
                playShootSound3p(player, 1, 4, 12, 24);
            }

            this.entityData.set(GUN_FIRE_TIME, 2);
            this.entityData.set(HEAT, this.entityData.get(HEAT) + 4);

            Level level = player.level();
            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer serverPlayer) {
                    Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 4, 6, this.getX(), this.getEyeY(), this.getZ()));
                }
            }

            if (hasCreativeAmmo) return;

            ItemStack ammoBox = this.getItemStacks().stream().filter(stack -> {
                if (stack.is(ModItems.AMMO_BOX.get())) {
                    return Ammo.HEAVY.get(stack) > 0;
                }
                return false;
            }).findFirst().orElse(ItemStack.EMPTY);

            if (!ammoBox.isEmpty()) {
                Ammo.HEAVY.add(ammoBox, -1);
            } else {
                consumeItem(getWeapon(1).ammo, 1);
            }
        }

    }
    

    // private void handleAmmo() {
    //     if (!(this.getFirstPassenger() instanceof Player player)) return;
    
    //     if ((hasItem(ModItems.HEAVY_AMMO.get())
    //             || InventoryTool.hasCreativeAmmoBox(player))
    //             && this.reloadCoolDown <= 0 && this.getEntityData().get(HEAVY_AMMO) < 1) {
    //         this.entityData.set(HEAVY_AMMO, this.getEntityData().get(HEAVY_AMMO) + 1);
    //         this.reloadCoolDown = 160;
    //         if (!InventoryTool.hasCreativeAmmoBox(player)) {
    //             this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
    //         }
    //         this.level().playSound(null, this, ModSounds.M2_1P.get(), this.getSoundSource(), 1, 1);
    //     }
    
    //     this.entityData.set(AMMO, this.getEntityData().get(HEAVY_AMMO));
    // }

    // private void handleAmmo() {
    //     if (entityData.get(LOADED_AMMO_TYPE) == 1) {
    //     }

    //     boolean hasCreativeAmmo = false;
    //     for (int i = 0; i < getMaxPassengers(); i++) {
    //         if (getNthEntity(i) instanceof Player pPlayer && InventoryTool.hasCreativeAmmoBox(pPlayer)) {
    //             hasCreativeAmmo = true;
    //         }
    //     }

    //     if (hasCreativeAmmo) {
    //         this.entityData.set(AMMO, 9999);
    //         this.entityData.set(MG_AMMO, 9999);
    //     } else {
    //         this.entityData.set(AMMO, countItem(getWeapon(entityData.get(HEAVY_AMMO)).ammo));
    //         this.entityData.set(MG_AMMO, countItem(getWeapon(entityData.get(MG_AMMO)).ammo));
    //     }

    // }

    // private void handleAmmo() {
    //     if (getWeaponIndex(0) == 0 || getWeaponIndex(0) == 1) {
    //         entityData.set(LOADED_AMMO_TYPE, getWeaponIndex(0));
    //     }

    //     boolean hasCreativeAmmo = false;
    //     for (int i = 0; i < getMaxPassengers(); i++) {
    //         if (getNthEntity(i) instanceof Player pPlayer && InventoryTool.hasCreativeAmmoBox(pPlayer)) {
    //             hasCreativeAmmo = true;
    //         }
    //     }

    //     if (hasCreativeAmmo) {
    //         this.entityData.set(AMMO, 9999);
    //         this.entityData.set(MG_AMMO, 9999);
    //     } else {
    //         this.entityData.set(AMMO, countItem(getWeapon(0).ammo));
    //         this.entityData.set(MG_AMMO, countItem(getWeapon(1).ammo));
    //     }
    // }
    
    @Override
    public int zoomFov() {
        return 3; // Нет оптического прицела
    }
    
    @Override
    public int getWeaponHeat(Player player) {
        return 0; // Нет нагрева оружия
    }

    @Override
    public boolean hidePassenger(Entity entity) {
        // Пассажиры внутри автомобиля видны
        return false;
    }

    @Override
    public boolean hasDecoy() {
        return true;
    }

    @Override
    public double getSensitivity(double original, boolean zoom, int seatIndex, boolean isOnGround) {
        return 0.3; // Нормальная чувствительность для всех пассажиров
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public @Nullable Vec2 getCameraRotation(float partialTicks, Player player, boolean zoom, boolean isFirstPerson) {
        if (isFirstPerson) {
            return new Vec2(Mth.lerp(partialTicks, player.yHeadRotO, player.getYHeadRot()), 
                           Mth.lerp(partialTicks, player.xRotO, player.getXRot()));
        }
        return super.getCameraRotation(partialTicks, player, false, false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Vec3 getCameraPosition(float partialTicks, Player player, boolean zoom, boolean isFirstPerson) {
        if (isFirstPerson) {
            // В режиме от первого лица камера находится примерно на уровне глаз
            return new Vec3(Mth.lerp(partialTicks, player.xo, player.getX()), 
                           Mth.lerp(partialTicks, player.yo + player.getEyeHeight(), player.getEyeY()), 
                           Mth.lerp(partialTicks, player.zo, player.getZ()));
        }
        return super.getCameraPosition(partialTicks, player, false, false);
    }
}
