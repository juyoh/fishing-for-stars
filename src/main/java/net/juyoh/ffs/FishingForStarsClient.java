package net.juyoh.ffs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.juyoh.ffs.network.ModPackets;
import net.juyoh.ffs.particle.FishNotifierParticle;
import net.juyoh.ffs.particle.ModParticles;

public class FishingForStarsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModPackets.registerS2Cpackets();
		ParticleFactoryRegistry.getInstance().register(ModParticles.FISH_NOTIFIER_PARTICLE, FishNotifierParticle.Factory::new);
	}
}