package net.juyoh.ffs.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.network.CaughtFishC2SPayload;
import net.juyoh.ffs.sound.ModSounds;
import net.juyoh.ffs.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class FishingScreen extends Screen {
    public static Identifier SPEECH_BG = Identifier.of(FishingForStars.MOD_ID, "textures/gui/speech_bg.png");
    public static Identifier BASE = Identifier.of(FishingForStars.MOD_ID, "textures/gui/base.png");
    public static Identifier ICONS = Identifier.of(FishingForStars.MOD_ID, "textures/gui/icons.png");

    ItemStack stack;

    int paddleSize = getPaddleSize(MinecraftClient.getInstance().player.experienceLevel);
    int paddleY = 0; //max is 568 - paddleSize
    int maxPaddleY = 568 - paddleSize;
    int fishY = 0; //max is 568 - fishSize ()
    int catchProgress = 10; //max is 140

    int tick = 0;
    int exitTimer = 30;
    boolean playing = true;

    boolean caughtFish = false;
    boolean caughtChest = false;

    public FishingScreen(ItemStack stack) {
        super(Text.translatable("text.juyoh.ffs.fishing"));
        this.stack = stack;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MinecraftClient client = MinecraftClient.getInstance();

        context.getMatrices().push();

        //speech bubble
        context.drawTexture(SPEECH_BG, (width - 53) / 2, (height - 159) / 2, 0, 0, 53, 159, 53, 159);
        //main rod
        int baseX = (width - 38) / 2;
        int baseY = (height - 150) / 2;
        context.drawTexture(BASE, baseX, baseY, 0, 0, 38, 150, 47, 150);
        //progress bar
        float progressF = (float) catchProgress / 140;
        context.fill(baseX + 32, baseY + 143 - catchProgress, baseX + 32 + 4, baseY + 146, ColorUtils.lerp(0xFFFF7800, 0xFF00D400, progressF));
        context.drawText(client.textRenderer, "catchprogressdivided: " + progressF, 0, 148, Colors.WHITE, true);

        //debug
        context.drawText(client.textRenderer, "paddleY: " + paddleY + " / " + maxPaddleY, 0, 0, Colors.WHITE, true);
        context.drawText(client.textRenderer, "fishY: " + fishY + " / " + maxPaddleY, 0, 32, Colors.WHITE, true);
        context.drawText(client.textRenderer, "catchProg: " + catchProgress + " / 140", 0, 64, Colors.WHITE, true);
        context.drawText(client.textRenderer, "isPlaying: " + playing, 0, 72, Colors.WHITE, true);
        context.drawText(client.textRenderer, "exitTimer: " + exitTimer + " / 30", 0, 104, Colors.WHITE, true);
        context.drawText(client.textRenderer, "tick: " + tick, 0, 136, Colors.WHITE, true);

        context.getMatrices().translate(0.5, 0.5, 0);

        client.getItemRenderer().renderItem(client.player, stack, ModelTransformationMode.GUI, false, context.getMatrices(), context.getVertexConsumers(),  client.world, 1, 1, 1);

        context.getMatrices().pop();
    }

    @Override
    public void tick() {
        super.tick();

        MinecraftClient client = MinecraftClient.getInstance();

        boolean isFishBehind =  isFishBehind();

        if (tick % 20 == 0 && tick < 260 && playing) {
            client.player.playSound(isFishBehind ? ModSounds.FISH_BEHIND : ModSounds.FISH_OFF, 1f, 1f);
        }
        if (playing) {
            catchProgress += isFishBehind ? 1 : -3;
            exitTimer = 30;
        }
        if (catchProgress <= 0) {
            //fish escapes
            playing = false;
        }
        if (catchProgress > 140 && playing) {
            //caught fish
            caughtFish = true;
            playing = false;
            client.player.playSound(ModSounds.FISH_FINISH_DING, 1f, 1f);
        }
        exitTimer--;
        if (exitTimer <= 0) {
            close();
        }
        tick++;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }
    boolean isFishBehind() {
        return fishY <= paddleY + paddleSize && fishY >= paddleY;
    }
    int getPaddleSize(int xpLevel) {
        int minSize = 96;
        xpLevel = Math.min(Math.max(xpLevel, 0), 30) / 3;
        return Math.min(minSize + (xpLevel * 8), 176);
    }

    @Override
    public void close() {
        if (exitTimer <= 0) {
            super.close();
            if (caughtFish) {
                caughtChest = true; //temp
                FishingForStars.LOGGER.info("send caught fish payload");
                ClientPlayNetworking.send(new CaughtFishC2SPayload(caughtChest));
            }
            return;
        }
        playing = false;
        MinecraftClient.getInstance().player.playSound(ModSounds.FISH_ESCAPE);
    }
}
