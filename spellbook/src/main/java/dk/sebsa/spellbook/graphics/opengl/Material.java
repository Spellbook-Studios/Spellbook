package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

/**
 * The surface of model, something which can be rendered
 */
public class Material extends Asset {
    @Getter
    private Color color;
    @Getter
    private Texture texture;
    private boolean isTextured;

    /**
     * A material with the no texture and the color white
     */
    public Material() {
        color = Color.white;
        this.texture = null;
        isTextured = false;
    }

    /**
     * A material with no texture and the color supplied
     *
     * @param color The color of this material
     */
    public Material(Color color) {
        this.color = color;
        this.texture = null;
        isTextured = false;
    }

    /**
     * A material with a texture and the color white
     *
     * @param texture The texture of material
     */
    public Material(Texture texture) {
        this.texture = texture;
        this.color = Color.white;
        isTextured = true;
    }

    /**
     * A material with color and texture
     *
     * @param color   The color of this material
     * @param texture The texture of this material
     */
    public Material(Color color, Texture texture) {
        this.color = color;
        this.texture = texture;
        isTextured = true;
    }

    /**
     * Weather this material has a texture
     *
     * @return True if the material has a texture, false otherwise
     */
    public boolean isTextured() {
        return isTextured;
    }

    /**
     * Binds the material to a shader, and binds the texture if it is textured
     *
     * @param shader A shader with uniforms ["matColor"]
     */
    public void bind(GLSLShaderProgram shader) {
        shader.setUniform("matColor", color);
        if (isTextured) texture.bind();
    }


    /**
     * Unbinds the texture if textured
     */
    public void unbind() {
        if (isTextured) texture.unbind();
    }

    @Override
    public void load() {
        try {
            List<String> file = FileUtils.readAllLinesList(FileUtils.loadFile(location));
            for (String line : file) {
                if (line.startsWith("t")) {
                    isTextured = true;
                    texture = (Texture) AssetManager.getAssetS(new Identifier(line.split(":", 2)[1]));
                } else if (line.startsWith("c")) {
                    String[] e = line.split(":")[1].split(",");
                    color = Color.color(Float.parseFloat(e[0]), Float.parseFloat(e[1]), Float.parseFloat(e[2]), Float.parseFloat(e[3]));
                }
            }
        } catch (IOException e) {
            Spellbook.instance.error("Material: Failed to load file at location: " + location, false);
        }
    }

    @Override
    public void destroy() {
        if (texture != null) texture.unreference();
    }
}
