package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ProjectileConfig {

    public static ForgeConfigSpec.BooleanValue ALLOW_PROJECTILE_DESTROY_GLASS;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("projectile");

        builder.comment("Set true to allow projectiles to destroy glasses");
        ALLOW_PROJECTILE_DESTROY_GLASS = builder.define("allow_projectile_destroy_glass", false);

        builder.pop();
    }
}
