package tech.wmp.wmp.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import tech.wmp.wmp.WMP;
import tech.wmp.wmp.entity.humveelvl1Entity;

public class humveelvl1Model extends GeoModel<humveelvl1Entity> {

    @Override
    public ResourceLocation getAnimationResource(humveelvl1Entity animatable) {
        return new ResourceLocation(WMP.MOD_ID, "animations/lav.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(humveelvl1Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "geo/humveelvl1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(humveelvl1Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "textures/entity/humvee.png");
    }
} 