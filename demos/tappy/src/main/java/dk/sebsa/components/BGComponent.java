package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;

public class BGComponent extends Component {
    public static final float SPEED = 150;

    @Override
    public void update(FrameData frameData) {
        entity.transform.move(new Vector3f(-(SPEED * Time.getDeltaTime()), 0, 0));
        if (entity.transform.getLocalPosition().x < -1000) entity.delete();
    }
}
