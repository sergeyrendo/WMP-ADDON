package tech.wmp.wmp.init;

import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import tech.wmp.wmp.WMP;

@SuppressWarnings("unused")
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WMP.MOD_ID);

    // Общая вкладка со всеми предметами мода
    public static final RegistryObject<CreativeModeTab> VEHICLES = TABS.register("wmp", () -> CreativeModeTab.builder()
            // .icon(() -> new ItemStack(ModItems.ICON_SPAWN_ITEM.get()))
            .title(Component.translatable("item_group.wmp.wmp"))
            .displayItems((parameters, output) -> {
                output.accept(ContainerBlockItem.createInstance(ModEntities.HUMVEELVL1.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.HUMVEELVL1_1.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.HUMVEELVL2.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.HUMVEELVL3.get()));

            })
            .build());
    
    /**
     * Event for adding items to creative tabs
     */
    @Mod.EventBusSubscriber(modid = WMP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        @SubscribeEvent
        public static void register(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == ModTabs.VEHICLES.getKey()) {
            }
        }
    }
}
