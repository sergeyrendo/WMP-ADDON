package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Mod.MODID);

    public static final RegistryObject<EntityDataSerializer<IntList>> INT_LIST_SERIALIZER = REGISTRY.register("int_list_serializer",
            () -> EntityDataSerializer.simple(FriendlyByteBuf::writeIntIdList, FriendlyByteBuf::readIntIdList));
}
