package tech.wmp.wmp.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VehicleConfigWMP {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;
        
        public static final ForgeConfigSpec.IntValue HUMVEE_ENERGY_COST;
    
        static {
            BUILDER.push("Vehicle Configs for VVP");
    
            HUMVEE_ENERGY_COST = BUILDER
                    .comment("Energy cost for Humvee vehicle")
                    .defineInRange("humvee_energy_cost", 700, 1, 10000);
    
            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }