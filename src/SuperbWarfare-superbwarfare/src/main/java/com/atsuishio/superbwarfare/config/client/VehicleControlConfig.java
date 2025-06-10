package com.atsuishio.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class VehicleControlConfig {

    public static ForgeConfigSpec.BooleanValue INVERT_AIRCRAFT_CONTROL;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("invert_aircraft_control");

        builder.comment("Set true to invert aircraft control");
        INVERT_AIRCRAFT_CONTROL = builder.define("invert_aircraft_control", false);

        builder.pop();
    }
}
