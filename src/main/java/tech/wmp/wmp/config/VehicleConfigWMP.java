package tech.wmp.wmp.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VehicleConfigWMP {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;
    
        public static final ForgeConfigSpec.IntValue TYPHOON_MAX_ENERGY;
        public static final ForgeConfigSpec.IntValue TYPHOON_HP;
        public static final ForgeConfigSpec.IntValue TYPHOON_ENERGY_COST;
        public static final ForgeConfigSpec.DoubleValue TYPHOON_ENERGY_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue TYPHOON_SHOOT_COST;
    
        static {
            BUILDER.push("Vehicle Configs for VVP");
    
            TYPHOON_MAX_ENERGY = BUILDER
                    .comment("Maximum energy for Typhoon vehicle")
                    .defineInRange("typhoon_max_energy", 6500, 1, 10000000);
    
            TYPHOON_HP = BUILDER
                    .comment("Health points for Typhoon vehicle")
                    .defineInRange("typhoon_hp", 250, 1, 10000000);
    
            TYPHOON_ENERGY_COST = BUILDER
                    .comment("Energy cost for Typhoon vehicle")
                    .defineInRange("typhoon_energy_cost", 1, 1, 100);
    
            TYPHOON_ENERGY_MULTIPLIER = BUILDER
                    .comment("Energy multiplier for Typhoon vehicle")
                    .defineInRange("typhoon_energy_multiplier", 0.1, 0.01, 1.0);
    
            TYPHOON_SHOOT_COST = BUILDER
                    .comment("Shooting cost for Typhoon vehicle")
                    .defineInRange("typhoon_shoot_cost", 10, 1, 1000);
    
            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }