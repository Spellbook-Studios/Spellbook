package dk.sebsa.spellbook.imgui;

import dk.sebsa.spellbook.math.Rect;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiPlatformIO;
import imgui.ImGuiViewport;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiNavInput;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Copied from default spair.imgui implementation
 * Just cut down to what we need
 * @author sebs
 * @since 1.0.0
 */
@SuppressWarnings("MissingJavadoc")
public class SpellbookImGUIGLFWImpl {
    private static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    protected static final boolean IS_WINDOWS = OS.contains("win");
    protected static final boolean IS_APPLE = OS.contains("mac") || OS.contains("darwin");

    // Pointer of the current GLFW window
    private long windowPtr;

    // Some features may be available only from a specific version
    private boolean glfwHasPerMonitorDpi;
    private boolean glfwHasMonitorWorkArea;

    // For application window properties
    private final int[] winWidth = new int[1];
    private final int[] winHeight = new int[1];
    private final int[] fbWidth = new int[1];
    private final int[] fbHeight = new int[1];


    // Empty array to fill ImGuiIO.NavInputs with zeroes
    private final float[] emptyNavInputs = new float[ImGuiNavInput.COUNT];

    // Monitor properties
    private final int[] monitorX = new int[1];
    private final int[] monitorY = new int[1];
    private final int[] monitorWorkAreaX = new int[1];
    private final int[] monitorWorkAreaY = new int[1];
    private final int[] monitorWorkAreaWidth = new int[1];
    private final int[] monitorWorkAreaHeight = new int[1];
    private final float[] monitorContentScaleX = new float[1];
    private final float[] monitorContentScaleY = new float[1];

    // Internal data
    private boolean wantUpdateMonitors = true;
    private double time = 0.0;

    public boolean init(final long windowId) {
        this.windowPtr = windowId;

        detectGlfwVersionAndEnabledFeatures();

        final ImGuiIO io = ImGui.getIO();

        io.addBackendFlags(ImGuiBackendFlags.HasMouseCursors | ImGuiBackendFlags.HasSetMousePos | ImGuiBackendFlags.PlatformHasViewports);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;

        io.setKeyMap(keyMap);

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(windowId);
                return clipboardString != null ? clipboardString : "";
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String str) {
                glfwSetClipboardString(windowId, str);
            }
        });

        // Update monitors the first time (note: monitor callback are broken in GLFW 3.2 and earlier, see github.com/glfw/glfw/issues/784)
        updateMonitors();

        // Our mouse update function expect PlatformHandle to be filled for the main viewport
        final ImGuiViewport mainViewport = ImGui.getMainViewport();
        mainViewport.setPlatformHandle(windowPtr);

        if (IS_WINDOWS) {
            mainViewport.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(windowId));
        }

        return true;
    }

    /**
     * Updates {@link ImGuiIO} and {@link org.lwjgl.glfw.GLFW} state.
     */
    public void newFrame() {
        final ImGuiIO io = ImGui.getIO();

        glfwGetWindowSize(windowPtr, winWidth, winHeight);
        glfwGetFramebufferSize(windowPtr, fbWidth, fbHeight);
        io.setDisplaySize((float) winWidth[0], (float) winHeight[0]);

        if (winWidth[0] > 0 && winHeight[0] > 0) {
            final float scaleX = (float) fbWidth[0] / winWidth[0];
            final float scaleY = (float) fbHeight[0] / winHeight[0];
            io.setDisplayFramebufferScale(scaleX, scaleY);
        }
        if (wantUpdateMonitors) {
            updateMonitors();
        }

        final double currentTime = glfwGetTime();
        io.setDeltaTime(time > 0.0 ? (float) (currentTime - time) : 1.0f / 60.0f);
        time = currentTime;

        updateGamepads();
    }

    public void dispose() {

    }

    private void detectGlfwVersionAndEnabledFeatures() {
        final int[] major = new int[1];
        final int[] minor = new int[1];
        final int[] rev = new int[1];
        glfwGetVersion(major, minor, rev);

        final int version = major[0] * 1000 + minor[0] * 100 + rev[0] * 10;

        glfwHasPerMonitorDpi = version >= 3300;
        glfwHasMonitorWorkArea = version >= 3300;
    }

    private void updateGamepads() {
        final ImGuiIO io = ImGui.getIO();

        if (!io.hasConfigFlags(ImGuiConfigFlags.NavEnableGamepad)) {
            return;
        }

        io.setNavInputs(emptyNavInputs);

        final ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
        final int buttonsCount = buttons.limit();

        final FloatBuffer axis = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        final int axisCount = axis.limit();

        mapButton(ImGuiNavInput.Activate, 0, buttons, buttonsCount, io);   // Cross / A
        mapButton(ImGuiNavInput.Cancel, 1, buttons, buttonsCount, io);     // Circle / B
        mapButton(ImGuiNavInput.Menu, 2, buttons, buttonsCount, io);       // Square / X
        mapButton(ImGuiNavInput.Input, 3, buttons, buttonsCount, io);      // Triangle / Y
        mapButton(ImGuiNavInput.DpadLeft, 13, buttons, buttonsCount, io);  // D-Pad Left
        mapButton(ImGuiNavInput.DpadRight, 11, buttons, buttonsCount, io); // D-Pad Right
        mapButton(ImGuiNavInput.DpadUp, 10, buttons, buttonsCount, io);    // D-Pad Up
        mapButton(ImGuiNavInput.DpadDown, 12, buttons, buttonsCount, io);  // D-Pad Down
        mapButton(ImGuiNavInput.FocusPrev, 4, buttons, buttonsCount, io);  // L1 / LB
        mapButton(ImGuiNavInput.FocusNext, 5, buttons, buttonsCount, io);  // R1 / RB
        mapButton(ImGuiNavInput.TweakSlow, 4, buttons, buttonsCount, io);  // L1 / LB
        mapButton(ImGuiNavInput.TweakFast, 5, buttons, buttonsCount, io);  // R1 / RB
        mapAnalog(ImGuiNavInput.LStickLeft, 0, -0.3f, -0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickRight, 0, +0.3f, +0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickUp, 1, +0.3f, +0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickDown, 1, -0.3f, -0.9f, axis, axisCount, io);

        if (axisCount > 0 && buttonsCount > 0) {
            io.addBackendFlags(ImGuiBackendFlags.HasGamepad);
        } else {
            io.removeBackendFlags(ImGuiBackendFlags.HasGamepad);
        }
    }

    private void mapButton(final int navNo, final int buttonNo, final ByteBuffer buttons, final int buttonsCount, final ImGuiIO io) {
        if (buttonsCount > buttonNo && buttons.get(buttonNo) == GLFW_PRESS) {
            io.setNavInputs(navNo, 1.0f);
        }
    }

    private void mapAnalog(
            final int navNo,
            final int axisNo,
            final float v0,
            final float v1,
            final FloatBuffer axis,
            final int axisCount,
            final ImGuiIO io
    ) {
        float v = axisCount > axisNo ? axis.get(axisNo) : v0;
        v = (v - v0) / (v1 - v0);
        if (v > 1.0f) {
            v = 1.0f;
        }
        if (io.getNavInputs(navNo) < v) {
            io.setNavInputs(navNo, v);
        }
    }

    private void updateMonitors() {
        final ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        final PointerBuffer monitors = glfwGetMonitors();

        platformIO.resizeMonitors(0);

        for (int n = 0; n < monitors.limit(); n++) {
            final long monitor = monitors.get(n);

            glfwGetMonitorPos(monitor, monitorX, monitorY);
            final GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            final float mainPosX = monitorX[0];
            final float mainPosY = monitorY[0];
            final float mainSizeX = vidMode.width();
            final float mainSizeY = vidMode.height();

            if (glfwHasMonitorWorkArea) {
                glfwGetMonitorWorkarea(monitor, monitorWorkAreaX, monitorWorkAreaY, monitorWorkAreaWidth, monitorWorkAreaHeight);
            }

            float workPosX = 0;
            float workPosY = 0;
            float workSizeX = 0;
            float workSizeY = 0;

            // Workaround a small GLFW issue reporting zero on monitor changes: https://github.com/glfw/glfw/pull/1761
            if (glfwHasMonitorWorkArea && monitorWorkAreaWidth[0] > 0 && monitorWorkAreaHeight[0] > 0) {
                workPosX = monitorWorkAreaX[0];
                workPosY = monitorWorkAreaY[0];
                workSizeX = monitorWorkAreaWidth[0];
                workSizeY = monitorWorkAreaHeight[0];
            }

            // Warning: the validity of monitor DPI information on Windows depends on the application DPI awareness settings,
            // which generally needs to be set in the manifest or at runtime.
            if (glfwHasPerMonitorDpi) {
                glfwGetMonitorContentScale(monitor, monitorContentScaleX, monitorContentScaleY);
            }
            final float dpiScale = monitorContentScaleX[0];

            platformIO.pushMonitors(mainPosX, mainPosY, mainSizeX, mainSizeY, workPosX, workPosY, workSizeX, workSizeY, dpiScale);
        }

        wantUpdateMonitors = false;
    }
}