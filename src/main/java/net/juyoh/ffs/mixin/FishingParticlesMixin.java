package net.juyoh.ffs.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingParticlesMixin {
	@Shadow
	private int fishTravelCountdown;

	@Inject(at = @At(value = "INVOKE", shift= At.Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"), method = "tickFishingLogic", cancellable = true)
	private void init(BlockPos pos, CallbackInfo ci) {
		if (this.fishTravelCountdown > 0) {
			ci.cancel();
		}
	}
}