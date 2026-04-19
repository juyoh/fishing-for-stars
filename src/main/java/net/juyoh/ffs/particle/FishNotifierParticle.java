package net.juyoh.ffs.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class FishNotifierParticle extends BillboardParticle {
    float minU;
    float maxU;
    float minV;
    float maxV;
    public FishNotifierParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.getSprite(0, 20));
        this.maxAge = 20;

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        Sprite sprite = spriteProvider.getSprite(0, 20);
        this.minU = sprite.getMinU();
        this.maxU = sprite.getMaxU();
        this.minV = sprite.getMinV();
        this.maxV = sprite.getMaxV();

        this.velocityX = 0;
        this.velocityY = 0.02;
        this.velocityZ = 0;
        this.scale = 0.75f;
    }

    @Override
    protected float getMinU() {
        return minU;
    }

    @Override
    protected float getMaxU() {
        return maxU;
    }

    @Override
    protected float getMinV() {
        return minV;
    }

    @Override
    protected float getMaxV() {
        return maxV;
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @org.jspecify.annotations.Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new FishNotifierParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
