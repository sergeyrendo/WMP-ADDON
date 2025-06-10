package tech.wmp.wmp.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tech.wmp.wmp.WMP;
import tech.wmp.wmp.client.renderer.entity.humveelvl1Renderer;
import tech.wmp.wmp.client.renderer.entity.humveelvl1_1Renderer;
import tech.wmp.wmp.client.renderer.entity.humveelvl2Renderer;
import tech.wmp.wmp.client.renderer.entity.humveelvl3Renderer;

@Mod.EventBusSubscriber(modid = WMP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HUMVEELVL1.get(), humveelvl1Renderer::new);
        event.registerEntityRenderer(ModEntities.HUMVEELVL1_1.get(), humveelvl1_1Renderer::new);
        event.registerEntityRenderer(ModEntities.HUMVEELVL2.get(), humveelvl2Renderer::new);
        event.registerEntityRenderer(ModEntities.HUMVEELVL3.get(), humveelvl3Renderer::new);
    }

    /**
     * Регистрация всех рендереров сущностей
     * @deprecated Используйте метод registerEntityRenderers с аннотацией @SubscribeEvent
     */
    @Deprecated
    public static void register() {
        // Эта функция больше не используется
        // Все регистрации перенесены в метод registerEntityRenderers
    }
}

