package dk.sebsa.spellbook.ecs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * An object within the game world.
 * @since 0.0.1
 * @author sebs
 */
public class Entity {
    @Getter private final List<Entity> children = new ArrayList<>();
    @Getter private Entity parent;
    /**
     * The transform component of this entity
     */
    public final Transform transform;
    /**
     * Name for easy human identification
     */
    public String name = "New Entity";

    @Getter private final String id = UUID.randomUUID().toString();

    /**
     * @param parent The parent of this entity
     */
    public Entity(Entity parent) {
        this.parent = parent;
        this.parent.children.add(this);
        transform = new Transform(this);
    }

    /**
     * Parents this entity to another entity
     * @param e This entity's new parent
     */
    public void parent(Entity e) {
        if(parent != null) {
            if(parent != e) parent.children.remove(this);
            else return;
        }

        parent = e;
        parent.children.add(this);
        transform.recalculateLocalTransformation();
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
