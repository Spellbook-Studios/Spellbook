package dk.sebsa.spellbook.ecs.tiles;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.util.IntGrid;

import java.util.function.Supplier;

/**
 * Renders a grid of tiles from a tileset
 *
 * @author sebs
 * @since 1.0.0
 */
public class TileGrid extends Component {
    public final Vector2f anchor = new Vector2f(0.5f, 0.5f);
    private final Vector2f totalSize = new Vector2f();
    private final IntGrid grid;
    private final Supplier<TileSet> t;
    public TileSet tileSet;

    /**
     * Creates a tilegrid with a size of 4x4, no default tile
     *
     * @param t Tileset to use
     */
    public TileGrid(Supplier<TileSet> t, int rows, int cols) {
        this.grid = new IntGrid(rows, cols, -1);
        this.t = t;
    }

    @Override
    public void onEnable() {
        this.tileSet = t.get();
        totalSize.set(grid.getRows() * tileSet.getTileSize().x, grid.getCols() * tileSet.getTileSize().y);
    }

    @Override
    public void onDisable() {
        this.tileSet.destroy();
        this.tileSet = null;
    }

    @Override
    public void render() {
        Spellbook.FRAME_DATA.drawTileGrid(new FrameData.DrawTileGrid(tileSet, grid, totalSize.mul(anchor.x - 1, anchor.y - 1).add(entity.transform.getGlobalPosition().x, entity.transform.getGlobalPosition().y)));
    }

    /**
     * Sets a cells value
     *
     * @param row The cells row
     * @param col The cells collumn
     * @param v   The value to set
     */
    public void setCell(int row, int col, int v) {
        grid.put(row, col, v);
    }
}
