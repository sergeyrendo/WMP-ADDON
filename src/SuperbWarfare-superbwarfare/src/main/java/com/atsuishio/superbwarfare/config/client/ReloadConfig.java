package com.atsuishio.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ReloadConfig {

    public static ForgeConfigSpec.BooleanValue LEFT_CLICK_RELOAD;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("reload");

        builder.comment("Set true if you want to reload guns when ammo is empty by clicking left button");
        LEFT_CLICK_RELOAD = builder.define("left_click_reload", true);

        builder.pop();
    }
}
