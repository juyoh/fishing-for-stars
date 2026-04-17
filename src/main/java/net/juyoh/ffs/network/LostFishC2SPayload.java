package net.juyoh.ffs.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.juyoh.ffs.FishingForStars;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LostFishC2SPayload() implements CustomPayload {
    public static final Id<LostFishC2SPayload> ID = new Id<>(Identifier.of(FishingForStars.MOD_ID, "lost_fish"));
    public static final PacketCodec<RegistryByteBuf, LostFishC2SPayload> CODEC = PacketCodec.of(LostFishC2SPayload::write, LostFishC2SPayload::read);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static LostFishC2SPayload read(RegistryByteBuf buf){
        return new LostFishC2SPayload();
    }
    public void write(RegistryByteBuf buf){
    }

    public static void apply(LostFishC2SPayload packet, ServerPlayNetworking.Context context){
        if (context.player() == null || context.player().fishHook == null) return;

        context.player().fishHook.discard();
        context.player().fishHook = null;
    }
}
