package dk.sebsa.spellbook;

import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import lombok.Getter;

import java.util.*;

/**
 * For data used on a pr frame basis
 * @author sebsn
 * @since 1.0.0
 */
public class FrameData {
    /**
     * Spriterenderers that has requested rendering
     * Sorted in sprites to make it easier on the renderer
     */
    @Getter private final Map<Sprite, Collection<SpriteRenderer>>[] renderSprite;

    /**
     * @param renderSpriteMaxLayers The maximum amount of layers to preapre for sprite rendering
     */
    public FrameData(int renderSpriteMaxLayers) {
        renderSprite = new HashMap[renderSpriteMaxLayers];

        for(int i = 0; i < renderSpriteMaxLayers; i++) {
            renderSprite[i] = new HashMap<>();
        }
    }

    /**
     * Sets a sprite for rendering
     * @param s SpriteRenderer to render
     */
    public void addRenderSprite(SpriteRenderer s) {
        renderSprite[s.layer].computeIfAbsent(s.sprite, sprite -> new HashSet<>()).add(s);
    }

    /**
     * Components under ECS.ROOT
     */
    public List<Component> components;
}
