package dk.sebsa.spellbook.opengl;

import dk.sebsa.spellbook.math.Color;
import lombok.Getter;

public class Material {
    @Getter private final Color color;
    @Getter private final Texture texture;
    private final boolean isTextured;

    public Material() {
        color = Color.white;
        this.texture = null;
        isTextured = false;
    }

    public Material(Color color) {
        this.color = color;
        this.texture = null;
        isTextured = false;
    }

    public Material(Texture texture) {
        this.texture = texture;
        this.color = Color.white;
        isTextured = true;
    }

    public Material(Color color, Texture texture) {
        this.color = color;
        this.texture = texture;
        isTextured = true;
    }

    public boolean isTextured() {
        return isTextured;
    }
}
