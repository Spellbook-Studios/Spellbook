package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.util.FileUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * A GLSL shader program
 * @author sebs
 * @since 0.0.1
 */
public class GLSLShaderProgram implements Asset {
    private int programId;
    private int vertexShaderId, fragmentShaderId;
    private final Map<String, Integer> uniforms = new HashMap<>();
    private final ClassLogger logger;
    /**
     * Weather the shader has been prepared by GL2D
     */
    public boolean initFor2D = false;

    /**
     * GLSLShaderprogram with Spellbook logger
     */
    public GLSLShaderProgram() {
        logger = new ClassLogger(this, Spellbook.getLogger());
    }

    @Override
    public void load(AssetReference location) {
        logger.log("Creating shader program");
        programId = glCreateProgram();
        if(programId == 0) { Spellbook.instance.error("Could not create shader: " + glGetError(), true); return; }

        // Create Shader
        logger.log("Loading shader code");
        String[] shaderCode;
        try {
            shaderCode = FileUtils.readAllLines(FileUtils.loadFile(location.location)).split("// SPELLBOOK END VERTEX SHADER //");
        } catch (IOException e) { Spellbook.instance.error("Could not load shader: " + logger.stackTrace(e), true); return; }

        // Create shaders
        vertexShaderId = createShader(shaderCode[0], GL_VERTEX_SHADER);
        fragmentShaderId = createShader(shaderCode[1], GL_FRAGMENT_SHADER);

        logger.log("Linking shaders");
        link();
        logger.log("Shader done!");
    }

    /**
     * Creates a shader
     * @param shaderCode The code for the shader in a string with line endings as \n
     * @param shaderType The type of shader, i.e. vertex or fragment
     * @return The shader id
     */
    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) { Spellbook.instance.error("Error creating shader. Type: " + shaderType, true); return 0; }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            Spellbook.instance.error("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024), true); return 0; }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * Links the gl shaders to this program
     */
    private void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            Spellbook.instance.error("Program failed to link.\n" + glGetProgramInfoLog(programId, glGetProgrami(programId, GL_INFO_LOG_LENGTH)), true);
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            logger.warn("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    @Override
    public void destroy() {
        unbind(); // Make sure that no shader is bound
        if(programId != 0) {
            logger.log("Destroying shader");
            glDeleteProgram(programId);
        }
    }

    // Shade use code
    /**
     * Sets the current program used by OpenGL to be this shaderProgram
     */
    public void bind() { glUseProgram(programId); }

    /**
     * Unbinds shaders from the GL rendering pipeline
     */
    public void unbind() { glUseProgram(0); }

    /**
     * Creates a uniform
     * @param uniformName The uniform as written in the shader
     * @param logger Logger to log warnings to
     */
    public void createUniform(String uniformName, Logger logger) {
        logger.trace(" - Creating uniform named - " + uniformName);
        if(uniforms.containsKey(uniformName)) return;
        int uniformLocation = glGetUniformLocation(programId,
                uniformName);
        if (uniformLocation < 0) logger.warn("Failed to find uniform - " + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    /**
     * Sets shader uniform (vec4)
     * @param uniformName Name to set
     * @param x Value 1
     * @param y Value 2
     * @param z Value 3
     * @param w Value 4
     */
    public void setUniform(String uniformName, float x, float y, float z, float w) {
        glUniform4f(uniforms.get(uniformName), x, y, z, w);
    }

    /**
     * Sets shader uniform (vec2)
     * @param uniformName Name to set
     * @param x Vector x
     * @param y Vector y
     */
    public void setUniform(String uniformName, float x, float y) {
        glUniform2f(uniforms.get(uniformName), x, y);
    }

    /**
     * Sets shader uniform (vec4)
     * @param uniformName Name to set
     * @param value Value to set
     */
    public void setUniform(String uniformName, Color value) {
        glUniform4f(uniforms.get(uniformName), value.r, value.g, value.b, value.a);
    }

    /**
     * Sets shader uniform (int)
     * @param uniformName Name to set
     * @param value Value to set
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * Sets shader uniform (Matrix4x4)
     * @param uniformName Name to set
     * @param value Value to set
     */
    public void setUniform(String uniformName, Matrix4x4f value) {
        int location = uniforms.get(uniformName);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.getBuffer(buffer);

        if(location != -1) glUniformMatrix4fv(location, false, buffer);
        buffer.flip();
    }

    /**
     * Sets shader uniform (Matrix4x4)
     * @param uniformName Name to set
     * @param value Value to set
     */
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    /**
     * Sets shader uniform (vec2)
     * @param uniformName Name to set
     * @param v Value to set
     */
    public void setUniform(String uniformName, Vector2f v) {
        glUniform2f(uniforms.get(uniformName), v.x, v.y);
    }

    /**
     * Sets shader uniform (vec3)
     * @param uniformName Name to set
     * @param v Value to set
     */
    public void setUniform(String uniformName, Vector3f v) {
        glUniform3f(uniforms.get(uniformName), v.x, v.y, v.z);
    }
}
