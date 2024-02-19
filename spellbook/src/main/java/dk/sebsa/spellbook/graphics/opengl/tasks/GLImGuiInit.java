package dk.sebsa.spellbook.graphics.opengl.tasks;

import dk.sebsa.spellbook.graphics.GraphTask;
import imgui.gl3.ImGuiImplGl3;

public class GLImGuiInit extends GraphTask {
    private final ImGuiImplGl3 implGl3;

    public GLImGuiInit(ImGuiImplGl3 implGl3) {
        this.implGl3 = implGl3;
    }

    @Override
    public String name() {
        return "GL<ImGuiInit>";
    }

    @Override
    public void execute() throws InterruptedException {
        implGl3.init("#version 150");
    }
}
