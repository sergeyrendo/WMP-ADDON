package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SeekConfig {

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SEEK_BLACKLIST;

    public static final List<? extends String> DEFAULT_SEEK_BLACKLIST = List.of(
            "minecraft:item",
            "minecraft:experience_orb",
            "minecraft:armor_stand",
            "minecraft:area_effect_cloud",
            "superbwarfare:claymore",
            "superbwarfare:c4",
            "touhou_little_maid:power_point",
            "evilcraft:vengeance_spirit",
            "mts:builder_rendering"
    );

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("seek");

        builder.comment("List of entities that can NOT be sought");
        SEEK_BLACKLIST = builder.defineList("seek_blacklist",
                DEFAULT_SEEK_BLACKLIST,
                e -> e instanceof String);

        builder.pop();
    }
}
