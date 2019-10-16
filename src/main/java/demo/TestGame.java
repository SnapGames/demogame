package demo;

import demo.map.MapLevel;
import demo.map.MapReader;
import demo.object.Camera;
import demo.object.GameObject;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * An extra class to demonstrate some basics to create a simple java game.
 *
 * @author Frédéric Delorme
 * @since 2019
 */
public class TestGame extends DemoGame {

    public MapLevel mapLevel;

    public int score = 0;
    public int lifes = 4;

    /**
     * Create the Game container.
     *
     * @param argc list of arguments.
     * @see Config#analyzeArgc(String[])
     */
    public TestGame(String[] argc) {
        super(argc);
        config = Config.analyzeArgc(argc);
    }

    /**
     * The famous java Execution entry point.
     *
     * @param argc
     */
    public static void main(String[] argc) {
        TestGame dg = new TestGame(argc);
        dg.run();
    }

    public void loadState() {
        mapLevel = MapReader.readFromFile("/res/maps/map_1.json");
        mapLevel.priority = 1;
        mapLevel.layer = 3;
        addObject(mapLevel);
        addObject(mapLevel.player);
        addAllObject(mapLevel.enemies);

        // Create camera
        Camera cam = new Camera("camera", mapLevel.player, 0.017f, new Dimension((int) mapLevel.width, (int) mapLevel.height));
        addObject(cam);
    }

    /**
     * Update all the object according to elapsed time.
     *
     * @param elapsed
     */
    public void update(float elapsed) {

        // update all objects
        for (GameObject go : objects.values()) {
            if (!(go instanceof Camera) && !(go instanceof MapLevel)) {
                go.update(this, elapsed);
                constrainToMapLevel(mapLevel, go);
            }
        }
        // active demo.object.Camera update
        if (this.camera != null) {
            camera.update(this, elapsed);
        }
    }

    public void input() {
        if (keys[KeyEvent.VK_ESCAPE]) {
            exitRequest = true;
        }

        mapLevel.player.setSpeed(0.0f, 0.0f);

        if (keys[KeyEvent.VK_UP]) {
            mapLevel.player.dy = -0.2f;
        }
        if (keys[KeyEvent.VK_DOWN]) {
            mapLevel.player.dy = 0.2f;
        }
        if (keys[KeyEvent.VK_LEFT]) {
            mapLevel.player.dx = -0.2f;
            mapLevel.player.direction = -1;
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            mapLevel.player.dx = 0.2f;
            mapLevel.player.direction = 1;
        }
        if (keys[KeyEvent.VK_SPACE]) {
            // Todo implement Jump
        }
    }

    public void drawHUD(Renderer r, Graphics2D g) {
        int offsetX = 4, offsetY = 30;
        Font f = g.getFont();
        g.setFont(f.deriveFont(8));
        r.drawOutLinedText(g, String.format("%05d", score), offsetX, offsetY, Color.WHITE, Color.BLACK);
        // draw Lifes
        String lifeStr = ":)";
        r.drawOutLinedText(g, String.format("%s",
                String.format("%0" + lifes + "d", 0).replace("0", lifeStr)),
                config.screenWidth - (60 + offsetX),
                offsetY,
                Color.GREEN,
                Color.BLACK);
        g.setFont(f);
    }
}