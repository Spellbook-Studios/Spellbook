package dk.sebsa.spellbook.imgui;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.*;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Time;
import imgui.ImGui;
import imgui.ImGuiIO;

import java.text.DecimalFormat;

/**
 * A UI layer rendered using ImGUI
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class ImGUILayer extends Layer {
    /**
     * Empty ImGUI layer
     */
    public ImGUILayer() {
        super();
    }

    /**
     * Draw the UI using ImGUI.??
     */
    protected abstract void drawImGUI();

    /**
     * Weather to disable the default drawing windows
     *
     * @return true disables the window, otherwise they are drown
     */
    protected abstract boolean disableDefaultWindows();

    @Override
    protected void userEvent(UserEvent e) {
        if (!Spellbook.instance.getCapabilities().debugIMGUI) return;
        final ImGuiIO io = ImGui.getIO();
        if (e.eventType() == Event.EventType.ioMouseMove) {
            MouseMoveEvent e2 = (MouseMoveEvent) e;
            io.setMousePos(e2.mouseX, e2.mouseY);
        } else if (e.eventType() == Event.EventType.ioButtonPressed) {
            ButtonPressedEvent e2 = (ButtonPressedEvent) e;
            io.setMouseDown(e2.button, true);
        } else if (e.eventType() == Event.EventType.ioButtonReleased) {
            ButtonReleasedEvent e2 = (ButtonReleasedEvent) e;
            io.setMouseDown(e2.button, false);
        } else if (e.eventType() == Event.EventType.ioMouseScroll) {
            MouseScrollEvent e2 = (MouseScrollEvent) e;
            io.setMouseWheelH(io.getMouseWheelH() + (float) e2.offsetX);
            io.setMouseWheel(io.getMouseWheel() + (float) e2.offsetY);
        } else if (e.eventType() == Event.EventType.ioKeyPressed) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            io.setKeysDown(e2.key, true);
        } else if (e.eventType() == Event.EventType.ioKeyReleased) {
            KeyReleasedEvent e2 = (KeyReleasedEvent) e;
            io.setKeysDown(e2.key, false);
        } else if (e.eventType() == Event.EventType.ioChar) {
            CharEvent e2 = (CharEvent) e;
            io.addInputCharacter(e2.codePoint);
        }
    }

    private static final DecimalFormat df = new DecimalFormat("#.#####");

    @Override
    public void render(Marble marble, Rect r) {
        if (!Spellbook.instance.getCapabilities().debugIMGUI) return;
        // Starts the imgui frame
        ImGui.newFrame();

        // draw the frame
        if (!disableDefaultWindows()) {
            String aft = df.format(Time.getAFL());

            ImGui.begin("Engine Stats");
            ImGui.text("FPS: " + Time.getFPS() + "(" + aft + "ms)");
            ImGui.end();

            // Debug Info
            ImGui.begin("Debug Info");
            ImGui.text("Coal: " + Spellbook.SPELLBOOK_VERSION);
            ImGui.newLine();
            ImGui.text("JAVA: V" + System.getProperty("java.version") + " from " + System.getProperty("java.vendor"));
            ImGui.text("OS: " + System.getProperty("os.name") + " V" + System.getProperty("os.version"));
            ImGui.text("DIR: " + System.getProperty("user.dir"));
            ImGui.text("PRC: " + System.getProperty("os.arch") + " " + Runtime.getRuntime().availableProcessors() + " Cores");
            ImGui.text("GRPH: " + Spellbook.graphicsCard);
            ImGui.end();

            // Console
            ImGui.begin("Console");
            ImGui.separator();

            /// Logs
            ImGui.beginChild("Log");
            ImGui.text("Coming Soon. :D");

            // End Console
            ImGui.endChild();
            ImGui.end();
        }

        drawImGUI();

        // End the frame and render it to the screen
        ImGui.endFrame();
        ImGui.render();
        SpellbookImGUI.getInstance().getImplGl3().renderDrawData(ImGui.getDrawData());
    }
}
