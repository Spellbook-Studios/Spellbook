package dk.sebsa.spellbook.ecs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An object within the game world.
 * @since 0.0.1
 * @author sebs
 */
public class Entity {
    @Getter private final List<Entity> children = new ArrayList<>();
    @Getter protected final List<Component> components = new ArrayList<>();
    @Getter private Entity parent;
    /**
     * The transform component of this entity
     */
    public final Transform transform;
    /**
     * Name for easy human identification
     */
    public String name = "New Entity";

    /**
     * For easy grouping of entities
     */
    public final String tag;

    @Getter private final String id = UUID.randomUUID().toString();

    /**
     * @param parent The parent of this entity
     */
    public Entity(Entity parent) {
        this.parent = parent;
        this.parent.children.add(this);
        transform = new Transform(this);
        tag = "Untagged";
    }

    /**
     * @param parent The parent of this entity
     * @param tag Tag for grouping with other entities
     */
    public Entity(Entity parent, String tag) {
        this.tag = tag;
        if(tag.equals("SPELLBOOK-SCENE")) { // Logic to run if this should be a root node
            transform = new LockedTransform(this);
            return;
        }

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

    /**
     * Removes an instance of a component from this entity
     * @param c Instance of component to remove
     */
    public void removeComponent(Component c) {
        components.remove(c);
        c.entity = null;
        c.onDisable();
    }

    /**
     * Adds a component to the entity
     * Also initializes the component
     * @param c The component to add
     * @return c
     */
    public Component addComponent(Component c) {
        components.add(c);
        c.init(this);
        return c;
    }
}
