package tech.wmp.wmp.init;

import com.atsuishio.superbwarfare.entity.projectile.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tech.wmp.wmp.entity.humveelvl1Entity;
import tech.wmp.wmp.entity.humveelvl1__1Entity;
import tech.wmp.wmp.entity.humveelvl2Entity;
import tech.wmp.wmp.entity.humveelvl3Entity;
import tech.wmp.wmp.WMP;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WMP.MOD_ID);

    public static final RegistryObject<EntityType<SmallCannonShellEntity>> SMALL_CANNON_SHELL = ENTITY_TYPES.register("small_cannon_shell",
                    () -> EntityType.Builder.<SmallCannonShellEntity>of(SmallCannonShellEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("small_cannon_shell"));
        
    public static final RegistryObject<EntityType<CannonShellEntity>> CANNON_SHELL = ENTITY_TYPES.register("cannon_shell",
                    () -> EntityType.Builder.<CannonShellEntity>of(CannonShellEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("cannon_shell"));
        
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = ENTITY_TYPES.register("projectile",
                    () -> EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("projectile"));
        
    public static final RegistryObject<EntityType<HeliRocketEntity>> HELI_ROCKET = ENTITY_TYPES.register("heli_rocket",
                    () -> EntityType.Builder.<HeliRocketEntity>of(HeliRocketEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("heli_rocket"));
        
    public static final RegistryObject<EntityType<Mk82Entity>> MK82 = ENTITY_TYPES.register("mk82",
                    () -> EntityType.Builder.<Mk82Entity>of(Mk82Entity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("mk82"));
        
    public static final RegistryObject<EntityType<Agm65Entity>> AGM65 = ENTITY_TYPES.register("agm65",
                    () -> EntityType.Builder.<Agm65Entity>of(Agm65Entity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("agm65"));
        
    public static final RegistryObject<EntityType<SwarmDroneEntity>> SWARM_DRONE = ENTITY_TYPES.register("swarm_drone",
                    () -> EntityType.Builder.<SwarmDroneEntity>of(SwarmDroneEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("swarm_drone"));

    public static final RegistryObject<EntityType<WgMissileEntity>> WGL_MISSILE = ENTITY_TYPES.register("wg_missile",
                            () -> EntityType.Builder.<WgMissileEntity>of(WgMissileEntity::new, MobCategory.MISC)
                                    .sized(0.5f, 0.5f)
                                    .build("wg_missile"));

    public static final RegistryObject<EntityType<humveelvl1Entity>> HUMVEELVL1 = ENTITY_TYPES.register("humveelvl1",
                        () -> EntityType.Builder.<humveelvl1Entity>of(humveelvl1Entity::new, MobCategory.MISC)
                                .setTrackingRange(64)
                                .setUpdateInterval(1)
                                .setCustomClientFactory(humveelvl1Entity::clientSpawn)
                                .fireImmune()
                                .sized(3.8f, 2.7f)
                                .build("humveelvl1"));

    public static final RegistryObject<EntityType<humveelvl1__1Entity>> HUMVEELVL1_1 = ENTITY_TYPES.register("humveelvl1_1",
                        () -> EntityType.Builder.<humveelvl1__1Entity>of(humveelvl1__1Entity::new, MobCategory.MISC)
                                .setTrackingRange(64)
                                .setUpdateInterval(1)
                                .setCustomClientFactory(humveelvl1__1Entity::clientSpawn)
                                .fireImmune()
                                .sized(3.8f, 2.7f)
                                .build("humveelvl1_1"));

    public static final RegistryObject<EntityType<humveelvl2Entity>> HUMVEELVL2 = ENTITY_TYPES.register("humveelvl2",
                        () -> EntityType.Builder.<humveelvl2Entity>of(humveelvl2Entity::new, MobCategory.MISC)
                                .setTrackingRange(64)
                                .setUpdateInterval(1)
                                .fireImmune()
                                .sized(3.8f, 2.7f)
                                .build("humveelvl2"));

    public static final RegistryObject<EntityType<humveelvl3Entity>> HUMVEELVL3 = ENTITY_TYPES.register("humveelvl3",
                        () -> EntityType.Builder.<humveelvl3Entity>of(humveelvl3Entity::new, MobCategory.MISC)
                                .setTrackingRange(64)
                                .setUpdateInterval(1)
                                .fireImmune()
                                .sized(3.8f, 2.7f)
                                .build("humveelvl3"));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(name, () -> entityTypeBuilder.build(name));
    }


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
