package net.juyoh.ffs.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingDisableBobMixin {

	@Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;setVelocity(DDD)V"), method = "onTrackedDataSet", cancellable = true)
	private void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {

		ci.cancel();
	}
}