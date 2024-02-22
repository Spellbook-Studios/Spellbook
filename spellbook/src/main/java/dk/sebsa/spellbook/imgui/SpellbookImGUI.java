package dk.sebsa.spellbook.imgui;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineFrameEarly;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.io.GLFWWindow;
import imgui.ImGui;
import lombok.CustomLog;
import lombok.Getter;

/**
 * A module for spellbook enabling a ImGUI layer for debug purposes
 * Should not be used in production
 * Enabled via SpellbookCapabilities.debugIMGUI
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class SpellbookImGUI implements Module {
    @Getter
    private SpellbookImGuiImplGl3 implGl3;
    @Getter
    private SpellbookImGUIGLFWImpl implGLFW;
    @Getter
    private static SpellbookImGUI instance;
    private GLFWWindow window;

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
        instance = this;
    }

    @EventListener
    public void engineFrameEarly(EngineFrameEarly e) {
        SpellbookImGUI.getInstance().getImplGLFW().newFrame(window);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        logger.log("ImGUI Init");
        ImGui.createContext();

        // Create OpenGL and GLFW implementations
        implGLFW = new SpellbookImGUIGLFWImpl();
        implGLFW.init(e.moduleCore.getWindow().getId());
        window = e.moduleCore.getWindow();
        implGl3 = new SpellbookImGuiImplGl3();
        Spellbook.instance.getRenderer().queue(() -> implGl3.init("#version 150"));

        // Init ImGUI
        ImGui.init();
        ImGui.styleColorsDark();
    }

    @Override
    public void cleanup() {
        logger.log("ImGUI cleanup");
        Spellbook.instance.getRenderer().queue(() -> implGl3.dispose());
        ImGui.destroyContext();
    }

    @Override
    public String name() {
        return "Debug<ImGUI>";
    }
}
