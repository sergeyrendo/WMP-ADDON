package tech.wmp.wmp.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import tech.wmp.wmp.WMP;
import tech.wmp.wmp.entity.humveelvl2Entity;

public class humveelvl2Model extends GeoModel<humveelvl2Entity> {

    @Override
    public ResourceLocation getAnimationResource(humveelvl2Entity animatable) {
        return new ResourceLocation(WMP.MOD_ID, "animations/lav.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(humveelvl2Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "geo/humveelvl2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(humveelvl2Entity object) {
        return new ResourceLocation(WMP.MOD_ID, "textures/entity/humvee.png");
    }
} 