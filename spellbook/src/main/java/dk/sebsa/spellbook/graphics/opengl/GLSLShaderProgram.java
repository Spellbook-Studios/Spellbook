package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * A GLSL shader program
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class GLSLShaderProgram extends Asset {
    private final Map<String, Integer> uniforms = new HashMap<>();
    private int programId;
    private int vertexShaderId, fragmentShaderId;

    @Override
    public void load() {
        loadShader();
    }

    private void loadShader() {
        logger.log("Creating shader program");
        programId = glCreateProgram();
        if (programId == 0) {
            Spellbook.instance.error("Could not create shader: " + glGetError(), true);
            return;
        }

        // Create Shader
        logger.log("Loading shader code");
        String[] shaderCode;
        try {
            shaderCode = FileUtils.readAllLines(FileUtils.loadFile(location)).split("// SPELLBOOK END VERTEX SHADER //");
        } catch (IOException e) {
            Spellbook.instance.error("Could not load shader: " + logger.stackTrace(e), true);
            return;
        }

        // Create shaders
        vertexShaderId = createShader(shaderCode[0], GL_VERTEX_SHADER);
        fragmentShaderId = createShader(shaderCode[1], GL_FRAGMENT_SHADER);

        logger.log("Linking shaders");
        link();
        logger.log("Shader done!");

    }

    /**
     * Creates a shader
     *
     * @param shaderCode The code for the shader in a string with line endings as \n
     * @param shaderType The type of shader, i.e. vertex or fragment
     * @return The shader id
     */
    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            Spellbook.instance.error("Error creating shader. Type: " + shaderType, true);
            return 0;
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            Spellbook.instance.error("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024), true);
            return 0;
        }

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
    }

    /**
     * Destroys the shader
     */
    @Override
    public void destroy() {
        Spellbook.instance.getRenderer().queue(this::destroyShader);
    }

    private void destroyShader() {
        unbind(); // Make sure that no shader is bound
        if (programId != 0) {
            logger.log("Destroying shader");
            glDeleteProgram(programId);
        }
    }

    /**
     * Sets the current program used by OpenGL to be this shaderProgram
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     * Unbinds shaders from the GL rendering pipeline
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Creates a uniform
     *
     * @param uniformName The uniform as written in the shader
     */
    public void createUniform(String uniformName) {
        logger.trace(" - Creating uniform named - " + uniformName);
        if (uniforms.containsKey(uniformName)) return;
        int uniformLocation = glGetUniformLocation(programId,
                uniformName);
        if (uniformLocation < 0) logger.warn("Failed to find uniform - " + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    /**
     * Sets shader uniform (vec4)
     *
     * @param uniformName Name to set
     * @param x           Value 1
     * @param y           Value 2
     * @param z           Value 3
     * @param w           Value 4
     */
    public void setUniform(String uniformName, float x, float y, float z, float w) {
        glUniform4f(uniforms.get(uniformName), x, y, z, w);
    }

    /**
     * Sets shader uniform (vec2)
     *
     * @param uniformName Name to set
     * @param x           Vector x
     * @param y           Vector y
     */
    public void setUniform(String uniformName, float x, float y) {
        glUniform2f(uniforms.get(uniformName), x, y);
    }

    /**
     * Sets shader uniform (vec4)
     *
     * @param uniformName Name to set
     * @param value       Value to set
     */
    public void setUniform(String uniformName, Color value) {
        glUniform4f(uniforms.get(uniformName), value.r, value.g, value.b, value.a);
    }

    /**
     * Sets shader uniform (int)
     *
     * @param uniformName Name to set
     * @param value       Value to set
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * Sets shader uniform (Matrix4x4)
     *
     * @param uniformName Name to set
     * @param value       Value to set
     */
    public void setUniform(String uniformName, Matrix4x4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.getBuffer(stack.mallocFloat(16)));
        }
    }

    /**
     * Sets shader uniform (Matrix4x4)
     *
     * @param uniformName Name to set
     * @param value       Value to set
     */
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    /**
     * Sets shader uniform (vec2)
     *
     * @param uniformName Name to set
     * @param v           Value to set
     */
    public void setUniform(String uniformName, Vector2f v) {
        glUniform2f(uniforms.get(uniformName), v.x, v.y);
    }

    /**
     * Sets shader uniform (vec3)
     *
     * @param uniformName Name to set
     * @param v           Value to set
     */
    public void setUniform(String uniformName, Vector3f v) {
        glUniform3f(uniforms.get(uniformName), v.x, v.y, v.z);
    }

    /**
     * Gets the location of an attribute variable with specified name.
     *
     * @param name Attribute name
     * @return Location of the attribute
     */
    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(programId, name);
    }

    /**
     * Enables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    }

    /**
     * Disables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    }

    /**
     * Sets the vertex attribute pointer.
     *
     * @param location Location of the vertex attribute
     * @param size     Number of values per vertex
     * @param stride   Offset between consecutive generic vertex attributes in
     *                 bytes
     * @param offset   Offset of the first component of the first generic vertex
     *                 attribute in bytes
     */
    public void pointVertexAttribute(int location, int size, int stride, int offset) {
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }
}
