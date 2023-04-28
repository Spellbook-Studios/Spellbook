package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.io.GLFWInput;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class PlayerMovement extends Component {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void update() {
        GLFWInput input = ((Core) Spellbook.instance.getModules().get(0)).getInput();
        if(input.isKeyDown(GLFW.GLFW_KEY_W)) {
            entity.transform.setPosition(entity.transform.getLocalPosition().x, entity.transform.getLocalPosition().y+10, 0);
        } else if(input.isKeyDown(GLFW.GLFW_KEY_D)) {
            entity.transform.setPosition(entity.transform.getLocalPosition().x+10, entity.transform.getLocalPosition().y, 0);
        } else if(input.isKeyDown(GLFW.GLFW_KEY_S)) {
            entity.transform.setPosition(entity.transform.getLocalPosition().x, entity.transform.getLocalPosition().y-10, 0);
        } else if(input.isKeyDown(GLFW.GLFW_KEY_A)) {
            entity.transform.setPosition(entity.transform.getLocalPosition().x-10, entity.transform.getLocalPosition().y, 0);
        }
    }

    @Override
    protected void render() {

    }

    @Override
    protected void onDisable() {

    }
}
