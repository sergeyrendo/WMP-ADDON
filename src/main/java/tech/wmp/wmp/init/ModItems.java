package tech.wmp.wmp.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import tech.wmp.wmp.WMP;

public class ModItems {
    public static final DeferredRegister<Item> REGISTRY = 
            DeferredRegister.create(ForgeRegistries.ITEMS, WMP.MOD_ID);

    public static final RegistryObject<Item> ICON_SPAWN_ITEM = REGISTRY.register("icon_spawn_item",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}