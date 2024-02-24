package dk.sebsa.components;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;
import lombok.CustomLog;

@CustomLog
public class PointsCollider extends BoxCollider2D {
    private static final float SPEED = 150;
    private int collideStatus = -1;

    public PointsCollider() {
        isTrigger = true;
    }

    @Override
    public void onCollide(Collider2D other) {
        if (other.getEntity().tag.equals("player")) collideStatus = 2;
    }

    @Override
    public void update(FrameData frameData) {
        entity.transform.move(new Vector3f(-(SPEED * Time.getDeltaTime()), 0, 0));
        if (entity.transform.getLocalPosition().x < -1000) entity.delete();
        if (collideStatus-- == 0) Tappy.instance.points++;
    }
}