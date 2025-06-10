package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeTool {

    public static boolean isGunDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)
                || source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE)
                || source.is(ModDamageTypes.SHOCK) || source.is(ModDamageTypes.BURN)
                || source.is(ModDamageTypes.LASER) || source.is(ModDamageTypes.LASER_HEADSHOT);
    }

    public static boolean isGunDamage(ResourceKey<DamageType> damageType) {
        return damageType == ModDamageTypes.GUN_FIRE || damageType == ModDamageTypes.GUN_FIRE_HEADSHOT
                || damageType == ModDamageTypes.GUN_FIRE_ABSOLUTE || damageType == ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE;
    }

    public static boolean isExplosionDamage(DamageSource source) {
        return source.is(ModDamageTypes.CUSTOM_EXPLOSION) || source.is(ModDamageTypes.PROJECTILE_BOOM);
    }

    public static boolean isHeadshotDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE);
    }

    public static boolean isGunFireDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)
                || source.is(ModDamageTypes.SHOCK) || source.is(ModDamageTypes.BURN)
                || source.is(ModDamageTypes.LASER) || source.is(ModDamageTypes.LASER_HEADSHOT);
    }

    public static boolean isModDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE)
                || source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)
                || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.SHOCK)
                || source.is(ModDamageTypes.PROJECTILE_BOOM) || source.is(ModDamageTypes.CANNON_FIRE)
                || source.is(ModDamageTypes.BURN)
                || source.is(ModDamageTypes.LASER) || source.is(ModDamageTypes.LASER_HEADSHOT);
    }

    public static boolean isCompatGunDamage(ResourceKey<DamageType> damageType) {
        return isGunDamage(damageType)
                || damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("tacz", "bullet"))
                || damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("tacz", "bullet_void"))
                || damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("tacz", "bullet_ignore_armor"))
                || damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("tacz", "bullet_void_ignore_armor"));
    }
}
