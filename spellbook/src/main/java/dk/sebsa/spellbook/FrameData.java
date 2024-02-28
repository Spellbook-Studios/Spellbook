package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.threading.ITaskManager;
import dk.sebsa.spellbook.core.threading.Task;
import dk.sebsa.spellbook.core.threading.TaskGroup;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.tiles.TileSet;
import dk.sebsa.spellbook.graphics.opengl.Sprite;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.phys.components.Collider2D;
import dk.sebsa.spellbook.util.IntGrid;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

/**
 * For data used on a pr frame basis
 *
 * @author sebs
 * @since 1.0.0
 */
public class FrameData {
    /**
     * Reference to the GLFWInput from the current window
     */
    public final GLFWInput input;
    /**
     * The marble UI module instance
     */
    public final Marble marble;
    /**
     * List of colliders in the Newton2D system that has not moved this frame
     */
    public final HashSet<Collider2D> newton2DSolids = new HashSet<>();
    /**
     * List of colliders in the Newton2D system that has moved this frame
     */
    public final HashSet<Collider2D> newton2DMovers = new HashSet<>();
    /**
     * LIst of tilegrids to render
     */
    public final HashSet<DrawTileGrid> drawTileGrids = new HashSet<>();
    /**
     * Spriterenderers that has requested rendering
     * Sorted in sprites to make it easier on the renderer
     */
    @Getter
    private final Map<Sprite, Collection<SpriteRenderer>>[] renderSprite;
    private final ITaskManager taskManager;
    /**
     * Components under ECS.ROOT
     */
    public List<Component> components;

    /**
     * @param input                 The input. Used from Component.update
     * @param renderSpriteMaxLayers The maximum amount of layers to preapre for sprite rendering
     * @param marble                The marble UI moduke
     * @param taskManager           The spellbook task manager
     */
    public FrameData(GLFWInput input, int renderSpriteMaxLayers, Marble marble, ITaskManager taskManager) {
        this.input = input;
        this.marble = marble;
        this.taskManager = taskManager;

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
        renderSprite[s.layer].computeIfAbsent(s.getSprite(), sprite -> new HashSet<>()).add(s);
    }

    // Task Utility Functions
    // TODO: The first functions are for the dynamic task group
    // Last three are just wrappers around the taskmanager

    /**
     * Runs a task
     *
     * @param task The task to run
     * @return The task now running
     */
    public Task run(Task task) {
        return taskManager.run(task);
    }

    /**
     * Schedules an entire TaskGroup
     *
     * @param tasks Taskgroup to run
     * @return The TaskGroup now running
     */
    public TaskGroup run(TaskGroup tasks) {
        return taskManager.run(tasks);
    }

    /**
     * Runs a task and notifies a consumer when finished
     *
     * @param task     Task to run
     * @param consumer Consumer of the task
     * @return The task now running
     */
    public Task runNotifyOnFinish(Task task, Consumer<Task> consumer) {
        return taskManager.runNotifyOnFinish(task, consumer);
    }

    /**
     * Sets a tilegrid for rendering
     *
     * @param d Tilegrid to render
     */
    public void drawTileGrid(DrawTileGrid d) {
        drawTileGrids.add(d);
    }

    /**
     * A task to render a grid of tiles (see ecs/tiles/TileGrid)
     *
     * @param tiles The tiles to render
     * @param grid  Each row and column pair must return an int equal to the texture index, or -1, which should indicate not rendering
     * @param pos   Position to draw from
     */
    public record DrawTileGrid(TileSet tiles, IntGrid grid, dk.sebsa.spellbook.math.Vector2f pos) {
    }
}
