package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        GameplayConfig.init(builder);

        return builder.build();
    }

}
