package net.juyoh.ffs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.juyoh.ffs.network.ModPackets;
import net.juyoh.ffs.particle.FishNotifierParticle;
import net.juyoh.ffs.particle.ModParticles;
import net.juyoh.ffs.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishingForStarsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModPackets.registerS2Cpackets();
		ParticleFactoryRegistry.getInstance().register(ModParticles.FISH_NOTIFIER_PARTICLE, FishNotifierParticle.Factory::new);
	}
}