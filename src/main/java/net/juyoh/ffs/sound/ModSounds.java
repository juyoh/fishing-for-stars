package net.juyoh.ffs.sound;

import net.juyoh.ffs.FishingForStars;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent FISH_NOTIFY = registerSoundEvent("fish_notify");
    public static final SoundEvent FISH_ESCAPE = registerSoundEvent("fish_escape");
    public static final SoundEvent FISH_CATCH_PULL = registerSoundEvent("fish_catch_pull");
    public static final SoundEvent FISH_FINISH_DING = registerSoundEvent("fish_finish_ding");
    public static final SoundEvent FISH_BEHIND = registerSoundEvent("reel_behind");
    public static final SoundEvent FISH_OFF = registerSoundEvent("reel_off");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(FishingForStars.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        FishingForStars.LOGGER.info("Registering Mod Sounds for " + FishingForStars.MOD_ID);
    }
}
