package dk.sebsa.components;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;

public class ObstacleComponent extends BoxCollider2D {
    @Override
    public void onCollide(Collider2D other) {
        Tappy.instance.death();
    }
}
