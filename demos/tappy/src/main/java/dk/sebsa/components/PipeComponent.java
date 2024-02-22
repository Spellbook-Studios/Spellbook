package dk.sebsa.components;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.phys.components.Collider2D;
import dk.sebsa.spellbook.phys.components.SpriteCollider2D;

public class PipeComponent extends SpriteCollider2D {
    private static final float SPEED = 150;

    /**
     * @param renderer The renderer to scale to
     */
    public PipeComponent(SpriteRenderer renderer) {
        super(renderer);
        isTrigger = true;
    }

    @Override
    public void onCollide(Collider2D other) {
        if (other.getEntity().tag.equals("player")) {
            Tappy.instance.death();
        }
    }

    @Override
    public void update(FrameData frameData) {
        entity.transform.move(new Vector3f(-(SPEED * Time.getDeltaTime()), 0, 0));
        if (entity.transform.getLocalPosition().x < -1000) entity.delete();
    }
}
