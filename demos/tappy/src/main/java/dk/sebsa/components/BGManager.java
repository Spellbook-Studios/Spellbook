package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;

public class BGManager extends Component {
    public static final float BGSCALE = 0.5f;
    public static final float BGSIZE = 1920 * BGSCALE - 2;
    private float buffer = -BGSIZE;

    @Override
    public void onEnable() {
        //spawnBG(-(BGSIZE / 2));
        //buffer = BGSIZE / 2;
    }

    @Override
    public void update(FrameData frameData) {
        if (buffer < 1000) {
            spawnBG(buffer);
            buffer += BGSIZE;
        }
        buffer -= Time.getDeltaTime() * BGComponent.SPEED;
    }

    public void spawnBG(float x) {
        var bg = new Entity(entity);
        var spr = new SpriteRenderer(new Identifier("tappy", "background.spr"));
        bg.addComponent(spr);
        bg.addComponent(new BGComponent());
        bg.transform.setPosition(x, 130, 0);
        spr.anchor.set(0.5f, 0.5f);
        spr.layer = 0;
        spr.scale = 0.5f;
    }
}
