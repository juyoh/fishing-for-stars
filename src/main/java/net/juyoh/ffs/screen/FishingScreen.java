package net.juyoh.ffs.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.juyoh.ffs.FishingForStars;
import net.juyoh.ffs.network.CaughtFishC2SPayload;
import net.juyoh.ffs.network.LostFishC2SPayload;
import net.juyoh.ffs.sound.ModSounds;
import net.juyoh.ffs.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class FishingScreen extends Screen {
    public static Identifier SPEECH_BG = Identifier.of(FishingForStars.MOD_ID, "textures/gui/speech_bg.png");
    public static Identifier BASE = Identifier.of(FishingForStars.MOD_ID, "textures/gui/base.png");
    public static Identifier ICONS = Identifier.of(FishingForStars.MOD_ID, "textures/gui/icons.png");
    public static Identifier SONAR = Identifier.of(FishingForStars.MOD_ID, "textures/gui/sonar.png");

    ItemStack stack;

    int paddleSize = getPaddleSize(MinecraftClient.getInstance().player.experienceLevel);
    int maxPaddleY = 144 - paddleSize;
    float paddleY = maxPaddleY; //max is 142 - paddleSize
    float fishY = 133; //max is 142 - fishSize (9) = 133
    float catchProgress = 10; //max is 140

    float fishSpeed = -1;
    int fishMovementSpeed;
    int fishLaziness;
    float paddleSpeed = 0;

    public int tick = 0;
    int exitTimer = 30;
    boolean playing = true;

    boolean caughtFish = false;
    boolean hasChest;
    boolean caughtChest = false;
    float treasureProgress = 0; //max is 50
    float chestY = -100;

    public FishingScreen(ItemStack stack, boolean hasChest, int fishSpeed, int fishLaziness) {
        super(Text.translatable("text.ffs.fishing"));
        this.stack = stack;
        this.hasChest = hasChest;
        this.fishMovementSpeed = fishSpeed;
        this.fishLaziness = fishLaziness;
        paddleSize = getPaddleSize(MinecraftClient.getInstance().player.experienceLevel);
        if (hasChest) {
            chestY = MinecraftClient.getInstance().player.getRandom().nextFloat() * (144 - 9);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MinecraftClient client = MinecraftClient.getInstance();



        //speech bubble
        context.drawTexture(SPEECH_BG, (width - 53) / 2, (height - 159) / 2, 0, 0, 53, 159, 53, 159);
        //main rod
        int baseX = (width - 38) / 2;
        int baseY = (height - 150) / 2;
        context.drawTexture(BASE, baseX, baseY, 0, 0, 38, 150, 47, 150);

        //progress bar
        float progressF = catchProgress / 140;
        context.fill(baseX + 32, (int) (baseY + 143 - catchProgress), baseX + 32 + 4, baseY + 146, ColorUtils.lerp(0xFFFF7800, 0xFF00D400, progressF));
        context.drawText(client.textRenderer, "catchprogressdivided: " + progressF, 0, 148, Colors.WHITE, true);

        //fishing bar / paddle
        //old solid color
        //context.fill(baseX + 17, (int) (baseY + 3 + paddleY), baseX + 17 + 9, (int) (baseY + paddleY + paddleSize), 0xFF00D400);

        //top texture

        int renderPaddleY = (int) Math.max(0, paddleY);

        context.drawTexture(BASE, baseX + 17, (baseY + 3 + (renderPaddleY)), 38, 79, 9, 2, 47, 150);
        for (int i = 0; i < paddleSize - 4; i++) {
            //fill
            context.drawTexture(BASE, baseX + 17, (baseY + 5 + (renderPaddleY) + i), 38, 81, 9, 1, 47, 150);
        }
        //end
        context.drawTexture(BASE, baseX + 17, (baseY + (renderPaddleY) + paddleSize - 1), 38, 86, 9, 2, 47, 150);

        //fish 44px by 49px
        context.drawTexture(ICONS, baseX + 17, (int) (baseY + 3 + (fishY)), 0, 1, 9, 9, 22, 24);

        //treasure chest
        if (hasChest && tick > 40 && treasureProgress < 50) {
            context.drawTexture(ICONS, baseX + 17, (int) (baseY + 3 + (chestY)), 12F, 13F, 9, 11, (int) (22), (int) (24));
            if (treasureProgress > 0) {
                context.fill(baseX + 17, (int) (baseY + 3 + (chestY) + 8), (int) (baseX + 17 + ((treasureProgress / 50) * 9)), (int) (baseY + 3 + (chestY) + 10), Colors.WHITE);
            }
        } else if (hasChest && tick > 35 && treasureProgress < 50) {
            //chest appearing animation
            context.drawTexture(ICONS, baseX + 17, (int) (baseY + 3 + (chestY)), 12F, 13F, 9, 11, 22, 24);
        }

        //debug
        context.drawText(client.textRenderer, "paddleY: " + paddleY + " / " + maxPaddleY, 0, 0, Colors.WHITE, true);
        context.drawText(client.textRenderer, "fishY: " + fishY + " / " + maxPaddleY, 0, 32, Colors.WHITE, true);
        context.drawText(client.textRenderer, "catchProg: " + catchProgress + " / 140", 0, 64, Colors.WHITE, true);
        context.drawText(client.textRenderer, "isPlaying: " + playing, 0, 72, Colors.WHITE, true);
        context.drawText(client.textRenderer, "exitTimer: " + exitTimer + " / 30", 0, 104, Colors.WHITE, true);
        context.drawText(client.textRenderer, "tick: " + tick, 0, 136, Colors.WHITE, true);
        context.drawText(client.textRenderer, "item: " + stack.getItem().toString(), 0, 166, Colors.WHITE, true);
        context.drawText(client.textRenderer, "lmb: " + (isLeftClickDown() ? "down" : "not down"), 0, 172, Colors.WHITE, true);
        context.drawText(client.textRenderer, "delta: " + delta, 0, 192, Colors.WHITE, true);
        context.drawText(client.textRenderer, "paddle speed: " + paddleSpeed, 0, 208, Colors.WHITE, true);
        context.drawText(client.textRenderer, "paddle speed * delta: " + paddleSpeed * delta, 0, 224, Colors.WHITE, true);

        context.drawText(client.textRenderer, "smallenough: " + (Math.abs(paddleSpeed) < 1f ? "ye" : "no"), 0, 232, Colors.WHITE, true);
        context.drawText(client.textRenderer, "chestprogress: " + treasureProgress + " / 50", 0, 248, Colors.WHITE, true);

        context.drawText(client.textRenderer, "fishSpeed: " + fishMovementSpeed , 0, 258, Colors.WHITE, true);
        context.drawText(client.textRenderer, "fishLaziness: " + fishLaziness , 0, 268, Colors.WHITE, true);

        context.drawText(client.textRenderer, "lang: " + client.options.language , 0, 278, Colors.WHITE, true);

        //gameplay
        if (playing) {
            catchProgress += (isFishBehind() ? 1 : -1.4f) * delta;

            paddleSpeed += 0.1f; //gravity

            //fishSpeed *= 0.8F; //drag
            //paddleSpeed *= 0.8F;

            if (isLeftClickDown()) {
                paddleSpeed -= 0.2f;
            }
            if (paddleSpeed < -10f) {
                paddleSpeed = -10f;
            }
            if (paddleSpeed > 15f) {
                paddleSpeed = 15f;
            }


            //paddle bottom bounce
            if (paddleY > maxPaddleY) {
                paddleY = maxPaddleY;
                paddleSpeed = -paddleSpeed * 0.7f;
                if (-paddleSpeed < 2f) {
                    paddleSpeed = 0;
                } else {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_COPPER_BREAK, 0.6f, 1f);
                }
            }
            //paddle top bounce
            if (paddleY < 0) {
                paddleY = 0;
                paddleSpeed = -paddleSpeed * 0.7f;
                if (paddleSpeed < 1f) {
                    paddleSpeed = 0;
                } else {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_COPPER_BREAK, 0.6f, 1.2f);
                }
            }

            fishY += (fishSpeed * delta);
            paddleY += (paddleSpeed * delta);

            if (fishY > 133) {
                fishY = 133;
            }
            if (fishY < 0) {
                fishY = 0;
            }
        }

        //radar
        if (stack.getItem() != Items.AIR) {
            context.drawTexture(SONAR, baseX + 40 + 4, baseY + 7, 0, 0, 29, 24, 29, 24);
            context.drawItem(stack, baseX + 40 + 4 + 9, baseY + 7 + 4);
        }
        //tutorial
        //only show if player hasn't used fishing rod
        Identifier tutorialId = Identifier.of(FishingForStars.MOD_ID, "textures/gui/tutorial/" + getLanguageName() + ".png");
        if (client.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.FISH_CAUGHT)) <= 0) {

            context.drawTexture(tutorialId, baseX + 50, baseY + 44, 0, 0, 48, 69, 48, 69);
            context.drawText(client.textRenderer, "hasn't caught fish" , 0, 288, Colors.WHITE, true);
        }
        context.drawText(client.textRenderer, "realLangPath: " + tutorialId, 0, 298, Colors.WHITE, true);
        context.drawText(client.textRenderer, "fishCaught: " + client.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.FISH_CAUGHT)), 0, 308, Colors.WHITE, true);

        //spinning handle
        context.getMatrices().push();
        context.getMatrices().translate(baseX + 5, baseY + 129, 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tick * 2));
        context.drawTexture(BASE, 0, 0, 38, 88, 3, 8, 47, 150);
        context.getMatrices().pop();
        //frog
        //InventoryScreen.drawEntity(context, baseX, baseY, baseX + 40, baseY + 40, 48, 0,  0, fishY * 6,  new FrogEntity(EntityType.FROG, client.world));
    }

    @Override
    public void tick() {
        super.tick();

        MinecraftClient client = MinecraftClient.getInstance();

        boolean isFishBehind = isFishBehind();

        if (tick % 20 == 0 && playing) {
            client.player.playSound(isFishBehind ? ModSounds.FISH_BEHIND : ModSounds.FISH_OFF, 1f, 1f);
        }
        if (playing) {
            exitTimer = 30;
        }
        //higher fish laziness means the fish will move less
        if (client.player.getRandom().nextInt(fishLaziness * 20) == 0) {
            boolean up = client.player.getRandom().nextBoolean();
            //fish is at top, force down
            if (fishY < 12) {
                up = false;
            } else if (fishY > 110) {
                up = true;
            }
            fishSpeed += (client.player.getRandom().nextFloat() * fishMovementSpeed) * (up ? -0.6f : 0.6f);
        }
        if (catchProgress <= 0 && playing) {
            //fish escapes
            playing = false;
            MinecraftClient.getInstance().player.playSound(ModSounds.FISH_ESCAPE);
        }
        if (catchProgress > 140 && playing) {
            //caught fish
            caughtFish = true;
            playing = false;
            client.player.playSound(ModSounds.FISH_FINISH_DING, 1f, 1f);
        }
        if (isTreasureBehind() && treasureProgress < 50 && tick >= 40 && playing) {
            treasureProgress++;
        }
        if (treasureProgress == 50 && !caughtChest) {
            caughtChest = true;
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 0.9f);
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
    public boolean isFishBehind() {
        return fishY <= paddleY + paddleSize && fishY + 9 >= paddleY;
    }
    boolean isTreasureBehind() {
        return chestY <= paddleY + paddleSize && chestY + 7 >= paddleY;
    }
    int getPaddleSize(int xpLevel) {
        int minSize = 24;
        xpLevel = Math.min(Math.max(xpLevel, 0), 30) / 3;
        return Math.min(minSize + (xpLevel * 2), 44);
    }
    boolean isLeftClickDown() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    }

    @Override
    public void close() {
        if (exitTimer <= 0) {
            super.close();
            if (caughtFish) {
                MinecraftClient.getInstance().player.playSound(ModSounds.FISH_CATCH_PULL, 0.7F, 1.0F);
                ClientPlayNetworking.send(new CaughtFishC2SPayload(caughtChest));
                if (caughtChest) {
                    MinecraftClient.getInstance().player.playSound(ModSounds.TREASURE_OPEN, 1.0F, 1.0F);
                }
            } else {
                ClientPlayNetworking.send(new LostFishC2SPayload());
            }
            return;
        }
        if (playing) {
            playing = false;
            catchProgress = 0;
            MinecraftClient.getInstance().player.playSound(ModSounds.FISH_ESCAPE);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MinecraftClient.getInstance().player.playSound(ModSounds.ROD_BEND, 1f, 1f);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    public String getLanguageName() {
        //converts minecraft's current language name to stardew's possible translations
        String input = client.options.language;

        if (input.startsWith("es_")) {
            //spanish
            return "es_es";
        }
        if (input.startsWith("de_")) {
            //dutch
            return "de_de";
        }
        if (input.startsWith("fr_")) {
            //french
            return "fr_fr";
        }
        if (input.startsWith("ko_")) {
            //korean
            return "ko_kr";
        }
        if (input.startsWith("hu_")) {
            //hungarian
            return "hu_hu";
        }
        if (input.startsWith("it_")) {
            //italian
            return "it_it";
        }
        if (input.startsWith("ja_")) {
            //japanese
            return "ja_jp";
        }
        if (input.startsWith("tr_")) {
            //turkish
            return "tr_tr";
        }
        if (input.startsWith("pt_")) {
            //brazillian portuguese
            return "pt_br";
        }
        if (input.startsWith("ru_")) {
            //russian
            return "ru_ru";
        }
        if  (input.startsWith("zh_")) {
            //simplified chinese
            return "zh_ch";
        }
        //fallback to english
        return "en_us";
    }
}
