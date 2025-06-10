package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Mod.MODID);

    public static final RegistryObject<MenuType<ReforgingTableMenu>> REFORGING_TABLE_MENU =
            REGISTRY.register("reforging_table_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new ReforgingTableMenu(windowId, inv)));
    public static final RegistryObject<MenuType<ChargingStationMenu>> CHARGING_STATION_MENU =
            REGISTRY.register("charging_station_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new ChargingStationMenu(windowId, inv)));
    public static final RegistryObject<MenuType<VehicleMenu>> VEHICLE_MENU =
            REGISTRY.register("vehicle_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new VehicleMenu(windowId, inv)));
    public static final RegistryObject<MenuType<FuMO25Menu>> FUMO_25_MENU =
            REGISTRY.register("fumo_25_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new FuMO25Menu(windowId, inv)));
    public static final RegistryObject<MenuType<DogTagEditorMenu>> DOG_TAG_EDITOR_MENU =
            REGISTRY.register("dog_tag_editor_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new DogTagEditorMenu(windowId)));
}
