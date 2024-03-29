package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.util.Random;

public class PipesManager extends Component {
    private static final float TIMER_MAX = 2.5f;
    private float timer = 0;

    @Override
    public void update(FrameData frameData) {
        if (timer <= 0) {
            int o = Random.getInt(0, 275);
            spawnPipe(o - 370, new Identifier("tappy", "pipe.spr"));
            spawnPipe(o + 120, new Identifier("tappy", "pipe_flipped.spr"));
            spawnPoint(o - 125);
            timer = TIMER_MAX;
        }
        timer -= Time.getDeltaTime();
    }

    public void spawnPipe(int offset, Identifier sprite) {
        var pipe = new Entity(entity);
        pipe.name = "Pipe";
        SpriteRenderer sr = new SpriteRenderer(sprite);
        sr.scale = 0.6f;
        pipe.addComponent(sr);
        PipeComponent component = new PipeComponent(sr);
        pipe.addComponent(component);
        pipe.transform.setPosition(500, offset, 0);
    }

    public void spawnPoint(int offset) {
        var point = new Entity(entity);
        point.name = "Point";
        point.tag = "point"; // So that tappy doesn't stop its velocity
        PointsCollider pc = new PointsCollider();
        pc.size.set(75, 120);
        point.addComponent(pc);
        point.transform.setPosition(500, offset, 0);
    }
}
