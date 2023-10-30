package dk.sebsa.spellbook;

import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.phys.components.Collider2D;
import lombok.Getter;

import java.util.*;

/**
 * For data used on a pr frame basis
 *
 * @author sebs
 * @since 1.0.0
 */
public class FrameData {
    /**
     * Spriterenderers that has requested rendering
     * Sorted in sprites to make it easier on the renderer
     */
    @Getter
    private final Map<Sprite, Collection<SpriteRenderer>>[] renderSprite;
    /**
     * Reference to the GLFWInput from the current window
     */
    public final GLFWInput input;
    /**
     * The marble UI module instance
     */
    public final Marble marble;

    /**
     * @param input                 The input. Used from Component.update
     * @param renderSpriteMaxLayers The maximum amount of layers to preapre for sprite rendering
     * @param marble                The marble UI moduke
     */
    public FrameData(GLFWInput input, int renderSpriteMaxLayers, Marble marble) {
        this.input = input;
        this.marble = marble;

        //noinspection unchecked
        renderSprite = new HashMap[renderSpriteMaxLayers];

        for (int i = 0; i < renderSpriteMaxLayers; i++) {
            renderSprite[i] = new HashMap<>();
        }
    }

    /**
     * Sets a sprite for rendering
     *
     * @param s SpriteRenderer to render
     */
    public void addRenderSprite(SpriteRenderer s) {
        renderSprite[s.layer].computeIfAbsent(s.sprite, sprite -> new HashSet<>()).add(s);
    }

    /**
     * Components under ECS.ROOT
     */
    public List<Component> components;

    /**
     * List of colliders in the Newton2D system that has not moved this frame
     */
    public final HashSet<Collider2D> newton2DSolids = new HashSet<>();

    /**
     * List of colliders in the Newton2D system that has moved this frame
     */
    public final HashSet<Collider2D> newton2DMovers = new HashSet<>();
}
