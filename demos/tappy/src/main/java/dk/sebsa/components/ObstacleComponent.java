package dk.sebsa.components;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.audio.SoundPlayer;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;

public class ObstacleComponent extends BoxCollider2D {
    @Override
    public void onCollide(Collider2D other) {
        if (other.getEntity().tag.equals("player")) {
            Tappy.instance.death();
            getEntity().getComponent(SoundPlayer.class).start();
        }
    }
}
