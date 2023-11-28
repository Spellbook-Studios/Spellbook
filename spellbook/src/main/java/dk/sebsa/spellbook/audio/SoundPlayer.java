package dk.sebsa.spellbook.audio;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Component;

/**
 * Plays sound from the position of an entity
 *
 * @author sebs
 * @since 1.0.0
 */
public class SoundPlayer extends Component {
    /**
     * Weather the sound is played from the position of the soundsource
     */
    public boolean relative = true;
    /**
     * Weather the sound should loop, and replay when finished
     */
    public boolean loop = false;
    /**
     * Identifier of the soundbuffer to play
     */
    public Identifier identifier;
    private Sound sound;

    private SoundSource source;

    /**
     * Soundplayer with no sound
     */
    public SoundPlayer() {
    }

    /**
     * @param identifier Identifier of a sound buffer
     */
    public SoundPlayer(Identifier identifier) {
        this.identifier = identifier;
    }

    public void onEnable() {
        sound = (Sound) AssetManager.getAssetS(identifier);
        if (sound != null) {
            source = new SoundSource(loop, relative);
            source.setBuffer(sound);
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
            source.setBuffer(sound);
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
        if (sound != null) sound.unreference();
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
