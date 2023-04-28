package dk.sebsa.spellbook.ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An entity made to be the root entity, and which administers component calls to it's children
 * @author sebsn
 * @since 1.0.0
 */
public class Scene extends Entity{
    /**
     * Creates a new root node entity
     */
    public Scene() {
        super(null, "SPELLBOOK-SCENE");
    }

    private final List<Component> components = Collections.synchronizedList(new ArrayList<>());

    /**
     * Gathers all the components recursively from all this entity's children
     * @return The list
     */
    public List<Component> getAllComponent() {
        components.clear();
        getAllComponents(this);
        return components;
    }

    private void getAllComponents(Entity e) {
        for(Entity e2 : e.getChildren()) {
            for(Component component : e2.getComponents()) {
                if(component.isEnabled())
                    components.add(component);
            }

            getAllComponents(e2);
        }
    }
}
