package net.juyoh.ffs.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.juyoh.ffs.FishingForStars;

public class ModPackets {
    public static void registerC2Spackets() {
        PayloadTypeRegistry.playC2S().register(CaughtFishC2SPayload.ID, CaughtFishC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CaughtFishC2SPayload.ID, CaughtFishC2SPayload::apply);

        FishingForStars.LOGGER.info("Registering Client to Server packets for ffs");
    }

    public static void registerS2Cpackets() {
        PayloadTypeRegistry.playS2C().register(OpenFishingScreenS2CPayload.ID, OpenFishingScreenS2CPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(OpenFishingScreenS2CPayload.ID, OpenFishingScreenS2CPayload::apply);

        FishingForStars.LOGGER.info("Registering Server to Client packets for ffs");
    }
}
