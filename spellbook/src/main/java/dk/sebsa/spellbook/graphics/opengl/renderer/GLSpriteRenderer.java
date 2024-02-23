package dk.sebsa.spellbook.graphics.opengl.renderer;

import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.math.Rect;
import org.joml.Matrix4f;

/**
 * Renders 2D objects in batches
 * (Same as GL2DRender just with model matricies)
 *
 * @since 1.0.0
 * @author sebs
 */
public class GLSpriteRenderer extends GL2DRenderer {
    public GLSpriteRenderer(Identifier shaderI) {
        super(shaderI);
        shader.createUniform("mView");
    }

    public void begin(Matrix4f mProj) {
        super.begin(mProj);
        shader.setUniform("mView", Camera.activeCamera.getViewMatrix());
    }

    @Override
    public void drawQuad(Rect rect, Rect uv) {
        if (vertices.remaining() < Float.BYTES * 4) flush();
        float x1 = rect.x;
        float x2 = rect.x + rect.width;
        float y1 = rect.y;
        float y2 = rect.y + rect.height;

        float s1 = uv.x;
        float s2 = uv.x + uv.width;
        float t2 = uv.y;
        float t1 = uv.y + uv.height;

        vertices.put(x1).put(y2).put(s1).put(t2);
        vertices.put(x2).put(y2).put(s2).put(t2);
        vertices.put(x2).put(y1).put(s2).put(t1);

        vertices.put(x2).put(y1).put(s2).put(t1);
        vertices.put(x1).put(y1).put(s1).put(t1);
        vertices.put(x1).put(y2).put(s1).put(t2);

        numVertices += 6;
    }
}
