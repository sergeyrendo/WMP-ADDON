package tech.wmp.wmp.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import tech.wmp.wmp.WMP;

/**
 * Класс для регистрации звуков, используемых в моде
 */
public class ModSounds {
    // Создаем регистр для звуков
    public static final DeferredRegister<SoundEvent> REGISTRY = 
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WMP.MOD_ID);
    
    public static final RegistryObject<SoundEvent> HUMVEE_ENGINE = register("humvee_engine");
    public static final RegistryObject<SoundEvent> M2_1P = register("m2_1p");
    public static final RegistryObject<SoundEvent> M2_3P = register("m2_3p");
    public static final RegistryObject<SoundEvent> M2_FAR = register("m2_far");
    public static final RegistryObject<SoundEvent> M2_VERYFAR = register("m2_veryfar");

    // для птура / tow
    public static final RegistryObject<SoundEvent> TOW_1P = register("tow_1p");
    public static final RegistryObject<SoundEvent> TOW_3P = register("tow_3p");
    public static final RegistryObject<SoundEvent> TOW_FAR = register("tow_far");
    public static final RegistryObject<SoundEvent> TOW_RELOAD = register("tow_reload");
    

    
    /**
     * Вспомогательный метод для регистрации звуков
     * @param name Название звука (без пространства имен)
     * @return RegistryObject для звукового события
     */
    private static RegistryObject<SoundEvent> register(String name) {
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WMP.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
} 