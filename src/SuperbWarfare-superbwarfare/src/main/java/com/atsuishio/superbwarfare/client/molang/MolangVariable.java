package com.atsuishio.superbwarfare.client.molang;

import software.bernie.geckolib.core.molang.MolangParser;

import java.util.function.DoubleSupplier;

public class MolangVariable {

    public static final String SBW_SYSTEM_TIME = "query.sbw_system_time";
    public static final String SBW_IS_EMPTY = "query.sbw_is_empty";

    public static void register() {
        register(SBW_SYSTEM_TIME, () -> 0);
        register(SBW_IS_EMPTY, () -> 0);
    }

    private static void register(String name, DoubleSupplier supplier) {
        MolangParser.INSTANCE.setMemoizedValue(name, supplier);
    }
}
