package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.math.Vector2f;
import org.lwjgl.glfw.*;

public class GLFWInput {
    // Input Data
    private byte[] keys;
    private byte[] keysPressed;
    private byte[] keysReleased;
    private byte[] buttons;
    private byte[] buttonsPressed;
    private byte[] buttonsReleased;
    private double scrollX, scrollY = 0;
    private final Vector2f mousePos = new Vector2f(0, 0);

    private final GLFWWindow window;

    // Callbacks
    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWKeyCallback keyCallback;
    private final GLFWCursorPosCallback cursorCallback;
    private final GLFWScrollCallback scrollCallback;
    private final GLFWCharCallback charCallback; // For text input

    private final ClassLogger logger;

    public GLFWInput(EngineInitEvent e, GLFWWindow window) {
        this.logger = new ClassLogger(this, e.logger);
        this.window = window;
        resetInputData(true);

        logger.log("Init input");

        // Create Callbacks
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (byte) (action != GLFW.GLFW_RELEASE ? 1 : 0);

                if (action == 1) {
                    buttonsPressed[button] = 1;

                    //ButtonPressedEvent e = new ButtonPressedEvent();
                    //e.mouse = mousePos;
                    //e.button = button;
                    //app.stack.event(e);
                } else if (action == 0) {
                    buttonsReleased[button] = 1;

                    //ButtonReleasedEvent e = new ButtonReleasedEvent();
                    //e.mouse = mousePos;
                    //e.button = button;
                    //app.stack.event(e);
                }
            }
        };

        keyCallback = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key != -1) {
                    keys[key] = (byte) (action != GLFW.GLFW_RELEASE ? 1 : 0);
                    if (action == 1) {
                        keysPressed[key] = 1;
//
//                        KeyPressedEvent e = new KeyPressedEvent();
//                        e.key = key;
//                        app.stack.event(e);
                    }
                    if (action == 0) {
                        keysReleased[key] = 1;
//                        KeyReleasedEvent e = new KeyReleasedEvent();
//                        e.key = key;
//                        app.stack.event(e);
                    }
                }
            }
        };

        charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
//                CharEvent e = new CharEvent();
//                e.codePoint = codepoint;
//                app.stack.event(e);
            }
        };

        cursorCallback = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                // Create Event
//                MouseMoveEvent e = new MouseMoveEvent();
//                e.mousePosX[0] = (int) xpos;
//                e.mousePosY[0] = (int) ypos;
//                e.offsetMousePosX[0] = (int) (mouseX-xpos);
//                e.offsetMousePosY[0] = (int) (mouseY-ypos);
//                app.stack.event(e);

                // Save Mousepos
                mousePos.set((float) xpos, (float) ypos);
            }
        };

        scrollCallback = new GLFWScrollCallback() {
            public void invoke(long window, double offsetx, double offsety) {
//                MouseScrollEvent e = new MouseScrollEvent();
//                e.offsetX = offsetx;
//                e.offsetY = offsety;
//                app.stack.event(e);

                scrollX += offsetx;
                scrollY += offsety;
            }
        };
    }

    /**
     * Sets callbacks to glfwWindow
     */
    public void addCallbacks() {
        logger.log("Setting input callbacks");
        // Set callbacks
        GLFW.glfwSetKeyCallback(window.getId(), keyCallback);
        GLFW.glfwSetCursorPosCallback(window.getId(), cursorCallback);
        GLFW.glfwSetScrollCallback(window.getId(), scrollCallback);
        GLFW.glfwSetMouseButtonCallback(window.getId(), mouseButtonCallback);
        GLFW.glfwSetCharCallback(window.getId(), charCallback);
    }

    /**
     * Resets input data
     * @param all Weather to reset data that should not be reset, only true at init
     */
    private void resetInputData(boolean all) {
        if(all) keys = new byte[GLFW.GLFW_KEY_LAST];
        keysPressed = new byte[GLFW.GLFW_KEY_LAST];
        keysReleased = new byte[GLFW.GLFW_KEY_LAST];

        if(all) buttons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
        buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
        buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
    }

    /**
     * Frees callbacks
     */
    public void cleanup() {
        logger.log("Freeing GLFW Input callbacks");
        mouseButtonCallback.free();
        keyCallback.free();
        cursorCallback.free();
        scrollCallback.free();
    }

    /**
     * Reset Button Pressed and KeyPressed
     */
    public void endFrame() {
        resetInputData(false);
    }

    // GET INPUT DATA METHODS
    // BORING AF
    // DONT LOOK AT

    /**
     * Gets the mouse position on screen
     * @return The current mouse position
     */
    public Vector2f getMousePos() { return mousePos; }

    /**
     * Returns weather a key is currently pressed by a user
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key is pressed down
     */
    public boolean isKeyDown(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keys[key] == 1;
    }

    /**
     * Returns weather a key was pressed this frame
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key was just pressed
     */
    public boolean isKeyPressed(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keysPressed[key] == 1;
    }

    /**
     * Returns weather a key was released this frame
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key was just released
     */
    public boolean isKeyReleased(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keysReleased[key] == 1;
    }

    /**
     * Returns weather a button is currently pressed by a user
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button is pressed down
     */
    public boolean isButtonDown(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttons[button] == 1;
    }

    /**
     * Returns weather a button was pressed this frame
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button was just pressed
     */
    public boolean isButtonPressed(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsPressed[button] == 1;
    }

    /**
     * Returns weather a button was released this frame
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button was just released
     */
    public boolean isButtonReleased(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsReleased[button] == 1;
    }

    /**
     * Get horizontal scroll value, this is equal to all scroll offsets of entire program lifetime
     */
    public double getScrollX() {
        return scrollX;
    }

    /**
     * Get vertical scroll value, this is equal to all scroll offsets of entire program lifetime
     */
    public double getScrollY() {
        return scrollY;
    }
}
