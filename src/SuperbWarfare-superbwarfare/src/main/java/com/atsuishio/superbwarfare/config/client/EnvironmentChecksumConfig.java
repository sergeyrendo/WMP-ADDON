package com.atsuishio.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnvironmentChecksumConfig {

    public static ForgeConfigSpec.ConfigValue<String> ENVIRONMENT_CHECKSUM;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("checksum");

        builder.comment("System environment checksum, do not edit");
        ENVIRONMENT_CHECKSUM = builder.define("environment_checksum", "");

        builder.pop();
    }

}
