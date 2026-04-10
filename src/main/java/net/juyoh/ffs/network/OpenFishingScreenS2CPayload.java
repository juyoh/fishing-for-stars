package net.juyoh.ffs.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.screen.FishingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.stream.Stream;

public record OpenFishingScreenS2CPayload(Identifier item) implements CustomPayload {
    public static final CustomPayload.Id<OpenFishingScreenS2CPayload> ID = new CustomPayload.Id<>(Identifier.of(FishingForStars.MOD_ID, "open_screen"));
    public static final PacketCodec<RegistryByteBuf, OpenFishingScreenS2CPayload> CODEC = PacketCodec.of(OpenFishingScreenS2CPayload::write, OpenFishingScreenS2CPayload::read);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static OpenFishingScreenS2CPayload read(RegistryByteBuf buf){
        return new OpenFishingScreenS2CPayload(buf.readIdentifier());
    }
    public void write(RegistryByteBuf buf){
        buf.writeIdentifier(item);
    }

    public static void apply(OpenFishingScreenS2CPayload packet, ClientPlayNetworking.Context context){
        ItemStack stack = new ItemStack(Registries.ITEM.get(packet.item));

        context.client().setScreen(new FishingScreen(stack));
    }
}
