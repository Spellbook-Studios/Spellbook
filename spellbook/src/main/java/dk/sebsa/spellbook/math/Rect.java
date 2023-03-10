package dk.sebsa.spellbook.math;

/**
 * A representation of a 2 dimensional square
 */
public class Rect {
    /**
     * The location of the rects top-left corner within a 2d space
     */
    public float x, y;

    /**
     * The size of the rect
     */
    public float width, height;

    public Rect() {
        this(0,0,0,0);
    }

    public Rect(Vector2f pos, Vector2f size) {
        this(pos.x, pos.y, size.x, size.y);
    }

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect(Rect r) {
        this(r.x,r.y,r.width,r.height);
    }

    /**
     * Weather a 2d position is loaced within the rect
     * @param v The position
     * @return true if the vector is within the rect, false otherwise
     */
    public boolean inRect(Vector2f v) {
        return v.x > x && v.x < x+width && v.y > y && v.y < y+height;
    }

    /**
     * Weather a rect intersects / has any overlap with this rect
     * @param r The rect testing against
     * @return True of r intersects, false is otherwise
     */
    public boolean intersects(Rect r) {
        return !(x > r.x + r.width || x + width < r.x || y > r.y + r.height || y + height < r.y);
    }

    /**
     * Gets the intersection / overlap of two rects
     * it is wise to test if the rects intersect first by calling intersects(r)
     * @param r The rect testing up against
     * @param output This rect will be equal to the intersection of the two rects.
     */
    public void getIntersection(Rect r, Rect output) {
        float vx = Math.max(x, r.x);
        float vy = Math.max(y, r.y);
        output.set(vx, vy, Math.min(x + width, r.x + r.width) - vx, Math.min(y + height, r.y + r.height) - vy);
    }

    /**
     * Sets this rect to be equal the variables given
     * @param x The topleft corners x position
     * @param y The topleft corners y position
     * @param w The width of the rect
     * @param h The heifht of the rect
     * @return this
     */
    public Rect set(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    }

    /**
     * Sets the rect according to the variables given
     * @param pos The location of the top-left corner
     * @param size The size of the rect
     * @return this
     */
    public Rect set(Vector2f pos, Vector2f size) {
        x = pos.x;
        y = pos.y;
        width = size.x;
        height = size.y;
        return this;
    }

    /**
     * Sets this rects variables equal to another
     * @param r The rect to clone
     * @return this
     */
    public Rect set(Rect r) {
        return set(r.x, r.y, r.width, r.height);
    }

    /**
     * Adds the variables to this rects variables
     * @return this with added variables
     */
    public Rect add(float x, float y, float w, float h) {
        this.x += x;
        this.y += y;
        this.width += w;
        this.height += h;
        return this;
    }
    /**
     * Adds a rects variables to this rects variables
     * @return this with added variables
     */
    public Rect add(Rect r) {
        this.x += r.x;
        this.y += r.y;
        this.width += r.width;
        this.height += r.height;
        return this;
    }

    @Override
    public String toString() {
        return "Rect{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public boolean isZero() {
        return x == 0 && y == 0 && width == 0 && height == 0;
    }
}
