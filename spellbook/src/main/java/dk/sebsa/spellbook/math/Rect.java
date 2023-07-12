package dk.sebsa.spellbook.math;

/**
 * A representation of a 2 dimensional square
 * @author sebs
 * @since 1.0.0
 */
public class Rect {
    /**
     * A vertically flipped UV Rect
     * (0,0,1,-1)
     */
    public static final Rect verticalFlippedUV = new Rect(0,0,1,-1);
    /**
     * A vertically simple UV Rect
     * (0,0,1,1)
     */
    public static final Rect UV = new Rect(0,0,1,1);

    /**
     * The location of the rects top-left corner within a 2d space
     */
    public float x, y;

    /**
     * The size of the rect
     */
    public float width, height;

    /**
     * A rect equal to (0,0,0,0)
     */
    public Rect() {
        this(0,0,0,0);
    }

    /**
     * Creates a rect with the info (pos.x, pos.y, size.x, size.y)
     * @param pos The x and y values of the rect
     * @param size The width and height values of the rect
     */
    public Rect(Vector2f pos, Vector2f size) {
        this(pos.x, pos.y, size.x, size.y);
    }

    /**
     * @param x The x position of the rect
     * @param y The y position of the rect
     * @param width The width of the rect
     * @param height The height of the rect
     */
    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * A new rect with the same data as another
     * (r.x,r.y,r.width,r.height)
     * @param r The rect to copy from
     */
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
     * Weather a rect intersects / has any overlap with this rect
     * // Assuming (x, y) is top left corner and (x+w, y-h) is bottom right
     * @param r The rect testing against
     * @return True of r intersects, false if otherwise
     */
    public boolean overlap(Rect r) {
        if (y < r.y-r.height || y-height > r.y) return false;
        if (x+width < r.x || x > r.x + r.width) return false;
        return true;
    }

    /**
     * Tests weather to rects overlap
     * If they do gets the overlap of the rects
     * @param r The rect testing up against
     * @param output This rect will be equal to the overkap of the two rects.
     * @return true if the overlap
     */
    public boolean getOverlap(Rect r, Rect output) {
        if(!overlap(r)) return false;

        output.x = Math.max(x, r.x);
        output.y = Math.min(y, r.y);
        output.width = Math.min(x + width, r.x + r.width) - output.x;
        output.height = Math.min(y, r.y) - Math.max(y - height, r.y - r.height);

        return true;
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
     * @param x X value to add
     * @param y Y value to add
     * @param w W value to add
     * @param h H value to add
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
     * @param r The rect to add to this recr
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

    /**
     * Returns a vector with the values equal to the pos of this rect
     * @return a new vector equal to v2f(this.x, this.y)
     */
    public Vector2f getPos() {return new Vector2f(x, y);}

    /**
     * Returns a vector with the values equal to the size of this rect
     * @return a new vector equal to v2f(this.width, this.height)
     */
    public Vector2f getSize() {return new Vector2f(width, height);}

    /**
     * Weather the rect is equal to (0,0,0,0)
     * @return True if equal to zero, false otherwise
     */
    public boolean isZero() {
        return x == 0 && y == 0 && width == 0 && height == 0;
    }
}
