package tech.wmp.wmp.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig {
    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, builder.build());
    }
} 