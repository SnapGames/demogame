package samples.input;

import samples.Sample;
import samples.object.GameObject;
import java.awt.Graphics2D;

public class MouseCursor extends GameObject {

    public MouseCursor(String name) {
        super(name);
        type = GameObjectType.IMAGE;
    }

    @Override
    public void draw(Sample ga, Graphics2D g) {
        super.draw(ga, g);
        if (type != GameObjectType.IMAGE) {
            g.setColor(color);
            g.drawLine((int) (x - (width / 2)), (int) (y), (int) (x + (width / 2)), (int) (y + height));
            g.drawLine((int) (x), (int) (y - (height / 2)), (int) (x + width), (int) (y + (height / 2)));
        }
    }

    @Override
    public void update(Sample ga, double elapsed) {
        offsetX = ((SampleInputHandler) ga).getActiveCamera().x;
        offsetY = ((SampleInputHandler) ga).getActiveCamera().y;
    }
}