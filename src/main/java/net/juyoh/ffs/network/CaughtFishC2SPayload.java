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

public record CaughtFishC2SPayload(boolean caughtChest) implements CustomPayload {
    public static final Id<CaughtFishC2SPayload> ID = new Id<>(Identifier.of(FishingForStars.MOD_ID, "caught_fish"));
    public static final PacketCodec<RegistryByteBuf, CaughtFishC2SPayload> CODEC = PacketCodec.of(CaughtFishC2SPayload::write, CaughtFishC2SPayload::read);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static CaughtFishC2SPayload read(RegistryByteBuf buf){
        return new CaughtFishC2SPayload(buf.readBoolean());
    }
    public void write(RegistryByteBuf buf){
        buf.writeBoolean(caughtChest);
    }

    public static void apply(CaughtFishC2SPayload packet, ServerPlayNetworking.Context context){
        if (context.player() == null || context.player().fishHook == null) return;
        ServerPlayerEntity playerEntity = context.player();
        FishingBobberEntity bobber = context.player().fishHook;
        if (FishingForStars.promisedFish.containsKey(playerEntity.getUuid())) {
            //Fly fish towards player

            ItemStack itemStack = FishingForStars.promisedFish.get(playerEntity.getUuid());
            ItemEntity itemEntity = new ItemEntity(bobber.getWorld(), bobber.getX(), bobber.getY(), bobber.getZ(), itemStack);
            double d = playerEntity.getX() - bobber.getX();
            double e = playerEntity.getY() - bobber.getY();
            double f = playerEntity.getZ() - bobber.getZ();

            itemEntity.setVelocity(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
            bobber.getWorld().spawnEntity(itemEntity);
            playerEntity.getWorld().spawnEntity(new ExperienceOrbEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY() + (double)0.5F, playerEntity.getZ() + (double)0.5F, bobber.getRandom().nextInt(6) + 1));
            if (itemStack.isIn(ItemTags.FISHES)) {
                playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
            }

            FishingForStars.promisedFish.remove(context.player().getUuid());
        }
        if (packet.caughtChest) {
            playerEntity.getWorld().playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), ModSounds.TREASURE_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);

            SimpleInventory inventory = new SimpleInventory(27);
            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld)playerEntity.getWorld())).add(LootContextParameters.ORIGIN, bobber.getPos()).luck(playerEntity.getLuck()).build(LootContextTypes.CHEST);
            LootTable lootTable = playerEntity.getWorld().getServer().getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(FishingForStars.MOD_ID, "chests/fishing_chest")));
            lootTable.supplyInventory(inventory, lootContextParameterSet, bobber.getRandom().nextInt());

            playerEntity.openHandledScreen(new FishingChestHandlerFactory(inventory));
        }

        context.player().fishHook.discard();
        context.player().fishHook = null;
    }
}
