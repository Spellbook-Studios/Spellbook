package dk.sebsa.spellbook.io;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.io.events.*;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.CustomLog;
import lombok.Getter;
import org.lwjgl.glfw.*;

/**
 * Gathers input form the user from a GLFWWindow
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class GLFWInput {
    /**
     * All 16 gamepads
     * They are all instantiated at program start, and are assigned to controller
     * when they connect and disconnect
     */
    public final GamePad[] gamePads = new GamePad[16];
    /**
     * -- GETTER --
     * The mouse position on screen
     */
    @Getter
    private final Vector2f mousePos = new Vector2f(0, 0);
    private final GLFWWindow glfwWindow;
    // Callbacks
    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWKeyCallback keyCallback;
    private final GLFWJoystickCallback joystickCallback;
    private final GLFWCursorPosCallback cursorCallback;
    private final GLFWScrollCallback scrollCallback;
    private final GLFWCharCallback charCallback; // For text input
    private final EventBus eventBus;
    // Input Data
    private byte[] keys;
    private byte[] keysPressed;
    private byte[] keysReleased;
    private byte[] buttons;
    private byte[] buttonsPressed;
    private byte[] buttonsReleased;
    /**
     * -- GETTER --
     * The horizontal scroll value, this is equal to all scroll offsets of entire
     * program lifetime
     */
    @Getter
    private double scrollX;
    /**
     * -- GETTER --
     * The vertical scroll value, this is equal to all scroll offsets of entire
     * program lifetime
     */
    @Getter
    private double scrollY;

    /**
     * @param e      The event used for loading loggers
     * @param window The window to bind to
     */
    public GLFWInput(EngineInitEvent e, GLFWWindow window) {
        this.glfwWindow = window;
        this.eventBus = Spellbook.instance.getEventBus();
        resetInputData(true);

        logger.log("Init input");

        // Create Callbacks
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (byte) (action != GLFW.GLFW_RELEASE ? 1 : 0);

                if (action == 1) {
                    buttonsPressed[button] = 1;

                    ButtonPressedEvent e = new ButtonPressedEvent(button);
                    eventBus.user(e);
                } else if (action == 0) {
                    buttonsReleased[button] = 1;

                    ButtonReleasedEvent e = new ButtonReleasedEvent(button);
                    eventBus.user(e);
                }
            }
        };

        joystickCallback = new GLFWJoystickCallback() {
            @Override
            public void invoke(int jid, int event) {
                if (event == GLFW.GLFW_CONNECTED) {
                    if (!GLFW.glfwJoystickIsGamepad(jid))
                        return;
                    connectGamePad(jid);
                } else if (event == GLFW.GLFW_DISCONNECTED) {
                    disconnectGamePad(jid);
                }
            }
        };

        keyCallback = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key != -1) {
                    keys[key] = (byte) (action != GLFW.GLFW_RELEASE ? 1 : 0);
                    if (action == 1) {
                        keysPressed[key] = 1;

                        KeyPressedEvent e = new KeyPressedEvent(key);
                        eventBus.user(e);
                    }
                    if (action == 0) {
                        keysReleased[key] = 1;
                        KeyReleasedEvent e = new KeyReleasedEvent(key);
                        eventBus.user(e);
                    }
                }
            }
        };

        charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                CharEvent e = new CharEvent(codepoint);
                eventBus.user(e);
            }
        };

        cursorCallback = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                // Save mouse position
                mousePos.set((float) xpos, (float) ypos);

                // Create Event
                MouseMoveEvent e = new MouseMoveEvent(mousePos.x, mousePos.y);
                eventBus.user(e);
            }
        };

        scrollCallback = new GLFWScrollCallback() {
            public void invoke(long window, double offsetx, double offsety) {
                MouseScrollEvent e = new MouseScrollEvent(offsetx, offsety);
                eventBus.user(e);

                scrollX += offsetx;
                scrollY += offsety;
            }
        };
    }

    private void connectGamePad(int jid) {
        if (!GLFW.glfwJoystickPresent(jid))
            return; // Returns if this is not a connected gamepad

        gamePads[jid].setController();
        logger.log("GamePad connected: " + gamePads[jid].toString());

        eventBus.engine(new GamePadConnectedEvent(gamePads[jid]));
    }

    private void disconnectGamePad(int jid) {
        if (!gamePads[jid].isConnected())
            return; // Returns if this pad was never connected

        logger.log("GamePad Disconnected: " + gamePads[jid].toString());
        gamePads[jid].setController();

        eventBus.engine(new GamePadDisConnectedEvent(gamePads[jid]));
    }

    /**
     * Sets callbacks to glfwWindow
     */
    public void addCallbacks() {
        logger.log("Setting input callbacks");
        // Set callbacks
        GLFW.glfwSetKeyCallback(glfwWindow.getId(), keyCallback);
        GLFW.glfwSetCursorPosCallback(glfwWindow.getId(), cursorCallback);
        GLFW.glfwSetScrollCallback(glfwWindow.getId(), scrollCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow.getId(), mouseButtonCallback);
        GLFW.glfwSetCharCallback(glfwWindow.getId(), charCallback);
        GLFW.glfwSetJoystickCallback(joystickCallback);
    }

    /**
     * Creates gamepad instances and scans for connected gamepads
     */
    public void scanGamePads() {
        // Scan for gamepads
        for (int jid = 0; jid < 16; jid++) {
            gamePads[jid] = new GamePad(jid);
            connectGamePad(jid);
        }
    }

    /**
     * Resets input data
     *
     * @param all Weather to reset data that should not be reset, only true at init
     */
    private void resetInputData(boolean all) {
        if (all)
            keys = new byte[GLFW.GLFW_KEY_LAST];
        keysPressed = new byte[GLFW.GLFW_KEY_LAST];
        keysReleased = new byte[GLFW.GLFW_KEY_LAST];

        if (all)
            buttons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
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
        joystickCallback.free();

        // Cleanup gamepads
        for (int jid = 0; jid < 16; jid++) {
            disconnectGamePad(jid);
            gamePads[jid] = null;
        }
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
     * Returns weather a key is currently pressed by a user
     *
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key is pressed down
     */
    public boolean isKeyDown(int key) {
        if (key < 0 || key > GLFW.GLFW_KEY_LAST)
            throw new IllegalArgumentException("Key not supported by GLFW");
        return keys[key] == 1;
    }

    /**
     * Returns weather a key was pressed this frame
     *
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key was just pressed
     */
    public boolean isKeyPressed(int key) {
        if (key < 0 || key > GLFW.GLFW_KEY_LAST)
            throw new IllegalArgumentException("Key not supported by GLFW");
        return keysPressed[key] == 1;
    }

    /**
     * Returns weather a key was released this frame
     *
     * @param key The key to check for use GLFW.GLFW_KEY_???
     * @return True if the key was just released
     */
    public boolean isKeyReleased(int key) {
        if (key < 0 || key > GLFW.GLFW_KEY_LAST)
            throw new IllegalArgumentException("Key not supported by GLFW");
        return keysReleased[key] == 1;
    }

    /**
     * Returns weather a button is currently pressed by a user
     *
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button is pressed down
     */
    public boolean isButtonDown(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST)
            throw new IllegalArgumentException("Button not supported by GLFW");
        return buttons[button] == 1;
    }

    /**
     * Returns weather a button was pressed this frame
     *
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button was just pressed
     */
    public boolean isButtonPressed(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST)
            throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsPressed[button] == 1;
    }

    /**
     * Returns weather a button was released this frame
     *
     * @param button The button to check for use GLFW.GLFW_MOUSE_BUTTON_?
     * @return True if the button was just released
     */
    public boolean isButtonReleased(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST)
            throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsReleased[button] == 1;
    }

    /**
     * Updates the state of gamepads
     */
    public void updateGamePad() {
        for (int jid = 0; jid < 16; jid++) {
            gamePads[jid].update();
        }
    }
}
