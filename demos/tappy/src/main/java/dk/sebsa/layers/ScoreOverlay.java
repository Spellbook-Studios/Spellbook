package dk.sebsa.layers;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.marble.MarbleIMRenderer;
import dk.sebsa.spellbook.math.Rect;

public class ScoreOverlay extends Layer {
    private MarbleIMRenderer pointsRenderer;

    @Override
    protected void userEvent(UserEvent event) {

    }

    @Override
    protected void render(Marble marble, Rect r) {
        if (pointsRenderer == null) pointsRenderer = marble.getMarbleIM(
                marble.font(new Identifier("tappy", "SAOWelcomeTT-Bold.ttf"), 82),
                null);
        pointsRenderer.labelCenterH(String.valueOf(Tappy.instance.points), new Rect(0, 20, r.width, 124));
    }
}
