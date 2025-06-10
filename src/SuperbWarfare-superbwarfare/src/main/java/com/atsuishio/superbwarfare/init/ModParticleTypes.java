package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Mod.MODID);

    public static final RegistryObject<SimpleParticleType> FIRE_STAR = REGISTRY.register("fire_star", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BULLET_HOLE = REGISTRY.register("bullet_hole", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CUSTOM_CLOUD = REGISTRY.register("custom_cloud", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CUSTOM_SMOKE = REGISTRY.register("custom_smoke", () -> new SimpleParticleType(false));
}

