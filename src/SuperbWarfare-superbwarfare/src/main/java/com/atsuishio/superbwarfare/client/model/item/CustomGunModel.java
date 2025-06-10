package com.atsuishio.superbwarfare.client.model.item;

import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.client.Minecraft;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.MolangQueries;
import software.bernie.geckolib.model.GeoModel;

public abstract class CustomGunModel<T extends GunItem & GeoAnimatable> extends GeoModel<T> {

    @Override
    public void applyMolangQueries(T animatable, double animTime) {
        MolangParser parser = MolangParser.INSTANCE;
        Minecraft mc = Minecraft.getInstance();

        parser.setMemoizedValue(MolangQueries.LIFE_TIME, () -> animTime / 20d);
        if (mc.level != null) {
            parser.setMemoizedValue(MolangQueries.ACTOR_COUNT, mc.level::getEntityCount);
            parser.setMemoizedValue(MolangQueries.TIME_OF_DAY, () -> mc.level.getDayTime() / 24000f);
            parser.setMemoizedValue(MolangQueries.MOON_PHASE, mc.level::getMoonPhase);
        }

        parser.setValue(MolangVariable.SBW_SYSTEM_TIME, System::currentTimeMillis);

        // GunData
        var player = mc.player;
        if (player == null) return;

        var stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;

        var data = GunData.from(stack);

        parser.setValue(MolangVariable.SBW_IS_EMPTY, () -> data.isEmpty.get() ? 1 : 0);
    }
}
