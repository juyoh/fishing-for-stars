package net.juyoh.ffs.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.screenhandler.Networking;
import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.network.OpenFishingScreenS2CPayload;
import net.juyoh.ffs.sound.ModSounds;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.packet.s2c.custom.DebugBrainCustomPayload;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FishingBobberEntity.class)
public abstract class FishingCatchMixin {

	@Shadow
	@Nullable
	public abstract PlayerEntity getPlayerOwner();

	@Shadow
	@Final
	private int luckBonus;

	@Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/loot/context/LootContextParameterSet$Builder;build(Lnet/minecraft/loot/context/LootContextType;)Lnet/minecraft/loot/context/LootContextParameterSet;"), method = "use", cancellable = true)
	private void use(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(1);
		cir.cancel();
		FishingBobberEntity entity = (FishingBobberEntity) (Object) this;

		LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld)entity.getWorld())).add(LootContextParameters.ORIGIN, entity.getPos()).add(LootContextParameters.TOOL, usedItem).add(LootContextParameters.THIS_ENTITY, entity).luck((float)this.luckBonus + this.getPlayerOwner().getLuck()).build(LootContextTypes.FISHING);
		LootTable lootTable = entity.getWorld().getServer().getReloadableRegistries().getLootTable(LootTables.FISHING_GAMEPLAY);
		List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);

		boolean hasRadar = getPlayerOwner().getOffHandStack().getItem() == FishingForStars.SONAR_BOBBER;

		FishingForStars.promisedFish.put(entity.getPlayerOwner().getUuid(), list.getFirst());

		boolean hasChest = entity.getRandom().nextFloat() < 0.5;

		int fishSpeed = 1;
		int fishLaziness = 1;

		if (list.getFirst().isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(FishingForStars.MOD_ID, "fishing_junk")))) {
			fishSpeed = 1;
			fishLaziness = 3;
		} else if (list.getFirst().isIn(ItemTags.FISHES)) {
			fishSpeed = 2;
			fishLaziness = 1;
		} else if (list.getFirst().isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(FishingForStars.MOD_ID, "fishing_treasure")))) {
			fishSpeed = 3;
			fishLaziness = 1;
		}

		ServerPlayNetworking.send((ServerPlayerEntity) entity.getPlayerOwner(),
				new OpenFishingScreenS2CPayload(hasRadar ? Registries.ITEM.getId(list.getFirst().getItem()) : Identifier.ofVanilla("air"), hasChest, fishSpeed, fishLaziness));

		Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)getPlayerOwner(), usedItem, entity, list);
		if (hasRadar) {
			getPlayerOwner().getOffHandStack().damage(1, getPlayerOwner(), EquipmentSlot.OFFHAND);
		}
	}
}