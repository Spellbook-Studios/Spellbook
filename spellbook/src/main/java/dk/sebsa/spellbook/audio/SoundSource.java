package dk.sebsa.spellbook.audio;

import dk.sebsa.spellbook.math.Vector3f;

import static org.lwjgl.openal.AL11.*;

/**
 * Plays sounds
 * @author sebs
 * @since 1.0.0
 */
public class SoundSource {
    private final int sourceId;

    /**
     * @param loop Weather the sound should loop, and replay when finished
     * @param relative Weather the sound is played from the position of the soundsource (doppler effect), or not
     */
    public SoundSource(boolean loop, boolean relative) {
        sourceId = alGenSources();

        if (loop) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }

        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }
    }

    /**
     * Stops the sound if it is playing, and sets the target sound asset
     * @param sound The sound to play
     */
    public void setBuffer(Sound sound) {
        stop();
        alSourcei(sourceId, AL_BUFFER, sound.getSoundBufferId());
    }

    /**
     * Sets the position in global space of this sound source
     * @param position The position in global space
     */
    public void setPosition(Vector3f position) {
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    /**
     * Sets the speed of the sound source
     * @param speed The speed in each cardinal direction
     */
    public void setSpeed(Vector3f speed) {
        alSource3f(sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    /**
     * Sets the gain
     * @param gain Target gain
     */
    public void setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
    }

    /**
     * Sets a property of this sound source
     * @param param Parameter e.g. AL_LOOPING
     * @param value Value e.g. AL_TRUE
     */
    public void setProperty(int param, float value) {
        alSourcef(sourceId, param, value);
    }

    /**
     * Plays / Resumes the sound
     */
    public void play() {
        alSourcePlay(sourceId);
    }

    /**
     * Weather the sound is currently playing
     * @return true if the sound is playing
     */
    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    /**
     * Pauses the audio if it is playing
     * Call this.play() will resume the sound
     */
    public void pause() {
        alSourcePause(sourceId);
    }

    /**
     * Stops the audio if it is playing
     */
    public void stop() {
        alSourceStop(sourceId);
    }

    /**
     * Destroys the soundsource
     */
    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
    }
}
