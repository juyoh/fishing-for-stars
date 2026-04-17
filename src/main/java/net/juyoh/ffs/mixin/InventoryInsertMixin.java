package net.juyoh.ffs.mixin;

import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.screen.FishingChestScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Slot.class)
public class InventoryInsertMixin {

	@Shadow
	public int id;

	@Inject(at = @At(value = "HEAD"), method = "canInsert", cancellable = true)
	private void init(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (MinecraftClient.getInstance().player.currentScreenHandler instanceof GenericContainerScreenHandler && Objects.equals(MinecraftClient.getInstance().currentScreen.getTitle(), Text.translatable("text.ffs.fish_chest"))) {
			if (this.id < 27) {
				cir.setReturnValue(false);
			}
		}
	}
}