package net.juyoh.ffs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.juyoh.ffs.network.ModPackets;
import net.juyoh.ffs.particle.ModParticles;
import net.juyoh.ffs.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FishingForStars implements ModInitializer {
	public static final String MOD_ID = "fishing-for-stars";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Item SONAR_BOBBER = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "sonar_bobber"), new Item(new Item.Settings().maxCount(1).maxDamage(32).rarity(Rarity.RARE)) {
		@Override
		public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
			tooltip.add(Text.translatable("item.modifiers.offhand").formatted(Formatting.GRAY));
			tooltip.add(Text.translatable("description.ffs.sonar_bobber").formatted(Formatting.DARK_GRAY));
			if (MinecraftClient.getInstance().options.advancedItemTooltips) {
				//spacin
				tooltip.add(Text.literal(""));
			};
			super.appendTooltip(stack, context, tooltip, type);
		}
	});

	public static Map<UUID, ItemStack> promisedFish = new HashMap<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModPackets.registerC2Spackets();
		ModSounds.registerSounds();
		ModParticles.registerParticles();
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
				.register((itemGroup) -> itemGroup.add(SONAR_BOBBER));

	}
}