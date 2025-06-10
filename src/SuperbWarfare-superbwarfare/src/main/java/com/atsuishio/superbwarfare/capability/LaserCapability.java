package com.atsuishio.superbwarfare.capability;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaserCapability {

    public static ResourceLocation ID = Mod.loc("laser_capability");

    public interface ILaserCapability extends INBTSerializable<CompoundTag> {

        void init(LaserHandler handler);

        void start();

        void tick();

        void stop();

        void end();

    }

    public static class LaserCapabilityImpl implements ILaserCapability {

        public LaserHandler laserHandler;

        @Override
        public void init(LaserHandler handler) {
            this.laserHandler = handler;
        }

        @Override
        public void start() {
            this.laserHandler.start();
        }

        @Override
        public void tick() {
        }

        @Override
        public void stop() {
            if (this.laserHandler != null) {
                this.laserHandler.stop();
            }
        }

        @Override
        public void end() {
            if (this.laserHandler != null) {
                this.laserHandler.end();
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            if (this.laserHandler != null) {
                tag.put("Laser", this.laserHandler.writeNBT());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("Laser") && this.laserHandler != null) {
                this.laserHandler.readNBT(nbt.getCompound("Laser"));
            }
        }
    }

    public static class LaserCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        private final LazyOptional<LaserCapabilityImpl> instance = LazyOptional.of(LaserCapabilityImpl::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ModCapabilities.LASER_CAPABILITY.orEmpty(cap, instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
