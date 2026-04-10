package net.juyoh.ffs.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.juyoh.ffs.FishingForStars;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final SimpleParticleType FISH_NOTIFIER_PARTICLE =
            registerParticle("fish_notifier", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(FishingForStars.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        FishingForStars.LOGGER.info("Registering Particles for " + FishingForStars.MOD_ID);
    }
}
