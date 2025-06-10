package tech.wmp.wmp.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import tech.wmp.wmp.WMP;
import tech.wmp.wmp.entity.humveelvl3Entity;

public class humveelvl3Model extends GeoModel<humveelvl3Entity> {

    @Override
    public ResourceLocation getAnimationResource(humveelvl3Entity animatable) {
        return new ResourceLocation(WMP.MOD_ID, "animations/lav.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(humveelvl3Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "geo/humveelvl3.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(humveelvl3Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "textures/entity/humvee.png");
    }
} 