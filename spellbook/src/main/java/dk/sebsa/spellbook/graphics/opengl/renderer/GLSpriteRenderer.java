package dk.sebsa.spellbook.graphics.opengl.renderer;

import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Camera;
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
}
