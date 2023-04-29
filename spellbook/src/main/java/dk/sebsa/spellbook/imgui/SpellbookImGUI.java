package dk.sebsa.spellbook.imgui;

import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineFrameEarly;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import lombok.Getter;

/**
 * A module for spellbook enabling a ImGUI layer for debug purposes
 * Should not be used in production
 * Enabled via SpellbookCapabilities.debugIMGUI
 *
 * @author sebs
 * @since 1.0.0
 */
public class SpellbookImGUI implements Module {
    private ClassLogger logger;
    @Getter private ImGuiImplGl3 implGl3;
    @Getter private SpellbookImGUIGLFWImpl implGLFW;
    @Getter private static SpellbookImGUI instance;

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
        instance = this;
    }

    @EventListener
    public void engineFrameEarly(EngineFrameEarly e) {
        SpellbookImGUI.getInstance().getImplGLFW().newFrame();
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        this.logger = new ClassLogger(this, e.logger);

        logger.log("ImGUI Init");
        ImGui.createContext();

        // Create OpenGL and GLFW implementations
        implGLFW = new SpellbookImGUIGLFWImpl();
        implGLFW.init(e.moduleCore.getWindow().getId());
        implGl3 = new ImGuiImplGl3();
        implGl3.init("#version 150");

        // Init ImGUI
        ImGui.init();
        ImGui.styleColorsDark();
    }

    @Override
    public void cleanup() {
        logger.log("ImGUI cleanup");
        implGl3.dispose();
        implGLFW.dispose();
        ImGui.destroyContext();
    }

    @Override
    public String name() {
        return "Debug<ImGUI>";
    }
}
