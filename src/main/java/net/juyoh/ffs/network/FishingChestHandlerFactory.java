package net.juyoh.ffs.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FishingChestHandlerFactory implements NamedScreenHandlerFactory {
    SimpleInventory inventory;
    FishingChestHandlerFactory(SimpleInventory inventory) {
        this.inventory = inventory;
    }
    @Override
    public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, inventory, 3) {};
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("text.ffs.fish_chest");
    }
}
