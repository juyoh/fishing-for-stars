package net.juyoh.ffs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.juyoh.ffs.network.ModPackets;
import net.juyoh.ffs.network.OpenFishingScreenS2CPayload;
import net.juyoh.ffs.particle.FishNotifierParticle;
import net.juyoh.ffs.particle.ModParticles;

public class FishingForStarsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(OpenFishingScreenS2CPayload.ID, OpenFishingScreenS2CPayload::apply);
		ParticleFactoryRegistry.getInstance().register(ModParticles.FISH_NOTIFIER_PARTICLE, FishNotifierParticle.Factory::new);
	}
}