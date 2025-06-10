package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.data.vehicle.DefaultVehicleData;
import com.atsuishio.superbwarfare.data.vehicle.VehicleDataTool;
import com.atsuishio.superbwarfare.tools.BufferSerializer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public record VehiclesDataMessage(List<DefaultVehicleData> data) {

    public static void encode(VehiclesDataMessage message, FriendlyByteBuf buf) {
        var obj = message.data;

        buf.writeVarInt(obj.size());
        for (var data : obj) {
            buf.writeBytes(BufferSerializer.serialize(data).copy());
        }
    }

    public static VehiclesDataMessage decode(FriendlyByteBuf buffer) {
        var size = buffer.readVarInt();
        var list = new ArrayList<DefaultVehicleData>();
        for (var i = 0; i < size; i++) {
            list.add(BufferSerializer.deserialize(buffer, new DefaultVehicleData()));
        }
        return new VehiclesDataMessage(list);
    }

    public static VehiclesDataMessage create() {
        return new VehiclesDataMessage(VehicleDataTool.vehicleData.values().stream().toList());
    }

    public static void handler(final VehiclesDataMessage message) {
        VehicleDataTool.vehicleData.clear();

        for (var entry : message.data) {
            VehicleDataTool.vehicleData.put(entry.id, entry);
        }
    }
}
