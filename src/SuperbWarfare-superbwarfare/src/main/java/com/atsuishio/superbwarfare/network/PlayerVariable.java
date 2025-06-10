package com.atsuishio.superbwarfare.network;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerVariable {

    private PlayerVariable old = null;

    public Map<Ammo, Integer> ammo = new HashMap<>();
    public boolean tacticalSprint = false;

    public void watch() {
        this.old = this.copy();
    }

    /**
     * 编辑并自动同步玩家变量
     */
    public static void modify(Entity entity, Consumer<PlayerVariable> consumer) {
        var cap = entity.getCapability(ModVariables.PLAYER_VARIABLE).orElse(new PlayerVariable());

        cap.watch();
        consumer.accept(cap);
        cap.sync(entity);
    }

    public void sync(Entity entity) {
        if (!entity.getCapability(ModVariables.PLAYER_VARIABLE).isPresent()) return;

        var newVariable = entity.getCapability(ModVariables.PLAYER_VARIABLE).resolve().get();
        if (old != null && old.equals(newVariable)) return;

        if (entity instanceof ServerPlayer player) {
            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new PlayerVariablesSyncMessage(entity.getId(), compareAndUpdate()));
        }
    }

    public Map<Byte, Integer> compareAndUpdate() {
        var map = new HashMap<Byte, Integer>();
        var old = this.old == null ? new PlayerVariable() : this.old;

        for (var type : Ammo.values()) {
            var oldCount = old.ammo.getOrDefault(type, 0);
            var newCount = type.get(this);

            if (oldCount != newCount) {
                map.put((byte) type.ordinal(), newCount);
            }
        }

        if (old.tacticalSprint != this.tacticalSprint) {
            map.put((byte) -1, this.tacticalSprint ? 1 : 0);
        }

        return map;
    }

    public PlayerVariable copy() {
        var clone = new PlayerVariable();

        for (var type : Ammo.values()) {
            type.set(clone, type.get(this));
        }

        clone.tacticalSprint = this.tacticalSprint;

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerVariable other)) return false;

        for (var type : Ammo.values()) {
            if (type.get(this) != type.get(other)) return false;
        }

        return tacticalSprint == other.tacticalSprint;
    }

    public Tag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (var type : Ammo.values()) {
            type.set(nbt, type.get(this));
        }

        nbt.putBoolean("TacticalSprint", tacticalSprint);
        return nbt;
    }

    public void readNBT(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;
        for (var type : Ammo.values()) {
            type.set(this, type.get(nbt));
        }

        tacticalSprint = nbt.getBoolean("TacticalSprint");
    }
}
