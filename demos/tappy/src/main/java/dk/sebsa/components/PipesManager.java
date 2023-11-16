package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.util.Random;

public class PipesManager implements Component {

    private float timerMax = 2.5f;
    private float timer = 0;
    private Entity e;

    @Override
    public void onEnable(Entity entity) {
        e = entity;
    }

    @Override
    public void update(FrameData frameData) {
        if (timer <= 0) {
            spawnPipe();
            timer = timerMax;
        }
        timer -= Time.getDeltaTime();
    }

    public void spawnPipe() {
        var pipe = new Entity(e);
        SpriteRenderer sr = new SpriteRenderer(new Identifier("tappy", "pipe.spr"));
        sr.scale = 0.2f;
        pipe.addComponent(sr);
        pipe.addComponent(new PipeComponent());
        pipe.transform.setPosition(500, Random.getInt(-125, 125), 0);
    }
}
