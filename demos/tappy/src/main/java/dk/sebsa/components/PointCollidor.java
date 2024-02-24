package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;
import lombok.CustomLog;

@CustomLog
public class PointCollidor extends BoxCollider2D {
    private static final float SPEED = 150;
    public PointCollidor() {
        isTrigger = true;
    }

    @Override
    public void onCollide(Collider2D other) {
        logger.log("POINT!");
    }

    @Override
    public void update(FrameData frameData) {
        entity.transform.move(new Vector3f(-(SPEED * Time.getDeltaTime()), 0, 0));
        if (entity.transform.getLocalPosition().x < -1000) entity.delete();
    }
}
