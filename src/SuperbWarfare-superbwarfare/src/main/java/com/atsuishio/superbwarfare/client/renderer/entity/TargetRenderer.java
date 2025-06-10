package com.atsuishio.superbwarfare.client.renderer.entity;

import com.atsuishio.superbwarfare.client.layer.TargetLayer;
import com.atsuishio.superbwarfare.client.model.entity.TargetModel;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TargetRenderer extends GeoEntityRenderer<TargetEntity> {
    public TargetRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TargetModel());
        this.shadowRadius = 0f;
        this.addRenderLayer(new TargetLayer(this));
    }

    @Override
    public RenderType getRenderType(TargetEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, TargetEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
                          float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(TargetEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public boolean shouldShowName(TargetEntity animatable) {
        return animatable.hasCustomName();
    }
}
