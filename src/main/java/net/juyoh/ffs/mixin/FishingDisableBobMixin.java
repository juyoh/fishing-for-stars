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
public class FishingDisableBobMixin {

	@Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;setVelocity(DDD)V"), method = "onTrackedDataSet", cancellable = true)
	private void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {

		ci.cancel();
	}
}