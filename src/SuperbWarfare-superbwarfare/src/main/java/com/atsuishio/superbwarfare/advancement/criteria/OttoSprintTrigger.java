package com.atsuishio.superbwarfare.advancement.criteria;

import com.atsuishio.superbwarfare.Mod;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class OttoSprintTrigger extends SimpleCriterionTrigger<OttoSprintTrigger.TriggerInstance> {

    public static final ResourceLocation ID = Mod.loc("otto_sprint");

    @Override
    @ParametersAreNonnullByDefault
    protected @NotNull TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pDeserializationContext) {
        return new TriggerInstance(pPredicate);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer, instance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ContextAwarePredicate pPlayer) {
            super(ID, pPlayer);
        }

        public static OttoSprintTrigger.TriggerInstance get() {
            return new OttoSprintTrigger.TriggerInstance(ContextAwarePredicate.ANY);
        }

    }
}
