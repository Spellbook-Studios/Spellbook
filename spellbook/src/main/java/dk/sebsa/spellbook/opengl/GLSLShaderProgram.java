package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.util.FileUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class GLSLShaderProgram implements Asset {
    private int programId;
    private int vertexShaderId, fragmentShaderId;
    private final Map<String, Integer> uniforms = new HashMap<>();
    private final ClassLogger logger;

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
}
