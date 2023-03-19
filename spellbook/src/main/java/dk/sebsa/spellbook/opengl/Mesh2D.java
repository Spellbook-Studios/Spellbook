package dk.sebsa.spellbook.opengl;

import dk.sebsa.spellbook.asset.Asset;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

/**
 * A representation of a OpenGL mesh
 * @author sebs
 * @since 0.0.1
 */
public class Mesh2D {
    private int v_id;
    private int u_id;
    private int vao;

    private final float[] vertices, uvs;

    // Static Stuff
    private static final float[] square = new float[] {
            0, 1, 1, 1, 1, 0,
            1, 0, 0, 0, 0, 1
    };
    private static final float[] uv = new float[] {
            0, 0, 1, 0, 1, 1,
            1, 1, 0, 1, 0, 0
    };

    private static Mesh2D quad;
    public static Mesh2D getQuad() {
        if(quad != null) return quad;

        return new Mesh2D(square, square);
    }

    public Mesh2D(float[] vertices, float[] uvs) {
        this.vertices = vertices;
        this.uvs = uvs;

        load();
    }

    protected void load() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        v_id = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, createBuffer(vertices), GL30.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, vertices.length/3, GL30.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        u_id = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, createBuffer(uvs), GL30.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
    }

    public void unbind() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void destroy() {
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(v_id);
        GL15.glDeleteBuffers(u_id);
    }
}
