package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class Material implements Asset {
    @Getter private Color color;
    @Getter private Texture texture;
    private AssetReference textureR; // Might exist texture reference
    private boolean isTextured;

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

    /**
     * Binds the material to a shader, and binds the texture if it is textured
     * @param shader A shader with uniforms ["matColor"]
     */
    public void bind(GLSLShaderProgram shader) {
        shader.setUniform("matColor", color);
        if(isTextured) texture.bind();
    }


    /**
     * Unbinds the texture if textured
     */
    public void unbind() {
        if(isTextured) texture.unbind();
    }

    @Override
    public void load(AssetReference location) {
        try {
            List<String> file = FileUtils.readAllLinesList(FileUtils.loadFile(location.location));
            for(String line : file) {
                if(line.startsWith("t")) {
                    isTextured = true;
                    textureR = AssetManager.getAssetS(line.split(":")[1]);
                    texture = textureR.get();
                }  else if(line.startsWith("c")) {
                    String[] e = line.split(":")[1].split(",");
                    color = Color.color(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
                }
            }
        } catch (IOException e) {
            Spellbook.instance.error("Material: Failed to load file at location: " + location, false);
        }
    }

    @Override
    public void destroy() {
        texture = null;
        if(textureR != null) textureR.unRefrence();
    }
}
