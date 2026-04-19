package net.juyoh.ffs.mixin;

import net.juyoh.ffs.particle.ModParticles;
import net.juyoh.ffs.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingNotifyMixin {
	@Shadow
	private int hookCountdown;

	@Shadow
	@Final
	private static TrackedData<Boolean> CAUGHT_FISH;

	@Inject(at = @At(value = "INVOKE", shift= At.Shift.BEFORE, target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), method = "tickFishingLogic", cancellable = true)
	private void use(BlockPos pos, CallbackInfo ci) {
		Entity entity = ((Entity) (Object) this);
		this.hookCountdown = MathHelper.nextInt(entity.getRandom(), 20, 40);
		entity.getDataTracker().set(CAUGHT_FISH, true);
		entity.playSound(ModSounds.FISH_NOTIFY,  1.0F, 1.0F);

		((ServerWorld) entity.getEntityWorld()).spawnParticles(ModParticles.FISH_NOTIFIER_PARTICLE,
				entity.getX(),
				entity.getY() + 0.7,
				entity.getZ(), 1, 0, 0.2, 0, 0.2);
		ci.cancel();
	}
}