package net.juyoh.ffs.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.sound.ModSounds;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
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
