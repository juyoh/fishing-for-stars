package net.juyoh.ffs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.juyoh.ffs.network.ModPackets;
import net.juyoh.ffs.particle.ModParticles;
import net.juyoh.ffs.sound.ModSounds;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingForStars implements ModInitializer {
	public static final String MOD_ID = "fishing-for-stars";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Map<UUID, ItemStack> promisedFish = new HashMap<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModPackets.registerC2Spackets();
		ModSounds.registerSounds();
		ModParticles.registerParticles();
	}
}