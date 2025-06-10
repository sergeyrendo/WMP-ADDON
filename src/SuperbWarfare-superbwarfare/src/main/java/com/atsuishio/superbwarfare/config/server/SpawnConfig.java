package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpawnConfig {

    public static ForgeConfigSpec.BooleanValue SPAWN_SENPAI;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("spawn");

        builder.comment("Set true to allow Senpai to spawn naturally");
        SPAWN_SENPAI = builder.define("spawn_senpai", false);

        builder.pop();
    }

}
