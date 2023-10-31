package dk.sebsa.spellbook.audio;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;

/**
 * Plays sound from the position of an entity
 *
 * @author sebs
 * @since 1.0.0
 */
public class SoundPlayer implements Component {
    /**
     * Weather the sound is played from the position of the soundsource
     */
    public boolean relative = true;
    /**
     * Weather the sound should loop, and replay when finished
     */
    public boolean loop = false;
    /**
     * The sound buffer to play
     */
    public AssetReference sound;

    private SoundSource source;

    @Override
    public void onEnable(Entity entity) {
        if (sound != null) {
            source = new SoundSource(loop, relative);
            source.setBuffer(sound.get());
        }
    }

    /**
     * Plays / Resumes the sound
     */
    public void start() {
        if (source == null) {
            if (sound == null) {
                Spellbook.instance.error("No sound was said when calling SoundPlayer.start()", false);
                return;
            }
            source = new SoundSource(loop, relative);
            source.setBuffer(sound.get());
        }
        source.play();
    }

    /**
     * Stops the audio if it is playing
     */
    public void stop() {
        source.stop();
    }

    /**
     * Pauses the audio if it is playing
     * Call this.play() will resume the sound
     */
    public void pause() {
        source.pause();
    }

    @Override
    public void onDisable() {
        source.cleanup();
        if (sound != null) sound.unReference();
        source = null;
    }

    /**
     * Weather the sound is currently playing
     *
     * @return true if the sound is playing
     */
    public boolean isPlaying() {
        return source.isPlaying();
    }
}
