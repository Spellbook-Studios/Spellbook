package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.math.Vector2f;
import lombok.Getter;
import lombok.ToString;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

/**
 * There exists 16 gamepads
 * If the gamepad is not connected the name is "Disconnected"
 * When a controller connects it is an assigned one of theese gamepad instances
 * The controller keeps the instance, and it's id until it is disconnected
 *
 * @author sebs
 * @since 1.0.0
 */
@ToString
public class GamePad {
    /**
     * The ID of the gamepad
     * When GLFW is initialized, detected gamepads are added to the beginning of the array. Once a gamepads is detected, it keeps its assigned ID until it is disconnected or the library is terminated, so as gamepads are connected and disconnected, there may appear gaps in the IDs.
     */
    public final int id;

    @Getter
    private boolean isConnected = false;
    @Getter
    private String name;
    @Getter
    private GLFWGamepadState rawState;

    /**
     * Right joystick position
     */
    public final Vector2f axisRight = new Vector2f();
    /**
     * Left joystick position
     */
    public final Vector2f axisLeft = new Vector2f();
    /**
     * Weather the A button is pressed
     */
    public boolean buttonA;
    /**
     * Weather the B button is pressed
     */
    public boolean buttonB;
    /**
     * Weather the X button is pressed
     */
    public boolean buttonX;
    /**
     * Weather the Y button is pressed
     */
    public boolean buttonY;

    /**
     * The deadzone for the right joystick
     */
    public float deadZoneRight = 0.1f;
    /**
     * The deadzone for the left joystick
     */
    public float deadZoneLeft = 0.1f;

    /**
     * @param id The GLFW assigned jid of the controller
     */
    public GamePad(int id) {
        this.id = id;
        rawState = GLFWGamepadState.create();
    }

    /**
     * Loads controller data
     * This is to be called when the controller connected changes
     */
    public void setController() {
        isConnected = GLFW.glfwJoystickIsGamepad(id);
        rawState = GLFWGamepadState.create();
        update();

        if (isConnected) {
            name = GLFW.glfwGetGamepadName(id);
        } else {
            name = "Disconnected";
        }
    }

    /**
     * Updates the state of the GamePad
     */
    public void update() {
        GLFW.glfwGetGamepadState(id, rawState);

        // Axis
        axisRight.set(rawState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X), rawState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y));
        axisLeft.set(rawState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X), rawState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y));
        if (axisRight.magnitude() < deadZoneRight) axisRight.zero();
        if (axisLeft.magnitude() < deadZoneLeft) axisLeft.zero();

        // Buttons
        buttonA = rawState.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == GLFW.GLFW_PRESS;
        buttonB = rawState.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B) == GLFW.GLFW_PRESS;
        buttonX = rawState.buttons(GLFW.GLFW_GAMEPAD_BUTTON_X) == GLFW.GLFW_PRESS;
        buttonY = rawState.buttons(GLFW.GLFW_GAMEPAD_BUTTON_Y) == GLFW.GLFW_PRESS;
    }
}
