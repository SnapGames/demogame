package samples;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 * project : DemoGame
 * <p>
 * SampleGameObject is a demonstration of a using GameObject to animate things.
 * 
 * @author Frédéric Delorme<frederic.delorme@gmail.com
 * @since 0.1
 */
public class SampleGameObject implements KeyListener {

    /**
     * The GameObject to animate, display and process all game entities.
     */
    public class GameObject {
        int x;
        int y;
        int dx;
        int dy;
        int maxD;
        int width;
        int height;
        Color color;

        /**
         * Default constructor initializing all main attribtues.
         */
        GameObject() {
            x = y = 0;
            dx = dy = 0;
            width = height = 0;
        }

        /**
         * Update the game object
         * 
         * @param ga
         * @param elpased
         */
        public void update(SampleGameObject ga, long elpased) {
            x += dx;
            y += dy;
        }

        /**
         * render the GameObject.
         * 
         * @param ga
         * @param g
         */
        public void draw(SampleGameObject ga, Graphics2D g) {
            g.setColor(this.color);
            g.fillRect(x, y, 16, 16);
        }
    }

    // Internal Renderinf buffer
    BufferedImage screenBuffer;
    // the Java Window to contains the game
    JFrame frame;
    // a flag to request to exit from this sample
    boolean exit = false;
    // how many frames per second on this screen ?
    int FPS = 30;
    // scaling factor
    int scale = 1;
    // debug display mode
    int debug = 0;
    // pause flag
    boolean pause = false;

    // internal rendering information.
    Color collidingColor;
    Color squareColor;

    // list of managed objects
    List<GameObject> objects = new ArrayList<>();

    /**
     * Let's create a SampleGameLoop process.
     * 
     * @param title  title for the window.
     * @param width  width for this window.
     * @param height height for this window.
     */
    public SampleGameObject(String title, int width, int height, int s) {
        scale = s;
        screenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        Insets ins = frame.getInsets();
        Dimension dim = new Dimension((width * scale) - (ins.left + ins.right),
                (height * scale) - (ins.top + ins.bottom));
        frame.setSize(dim);
        frame.setPreferredSize(dim);
        frame.pack();
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.requestFocus();
    }

    /*----- the KeyListener interface corresponding implementation -----*/
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameObject go = objects.get(0);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                go.dy = (go.dy < go.maxD ? go.dy + 1 : go.dy);
                break;
            case KeyEvent.VK_DOWN:
                go.dy = (go.dy >= -go.maxD ? go.dy - 1 : go.dy);
                break;
            case KeyEvent.VK_LEFT:
                go.dx = (go.dx >= -go.maxD ? go.dx - 1 : go.dx);
                break;
            case KeyEvent.VK_RIGHT:
                go.dx = (go.dx < go.maxD ? go.dx + 1 : go.dx);
                break;
            case KeyEvent.VK_ESCAPE:
                exit = true;
                break;
            case KeyEvent.VK_D:
                debug = (debug < 5 ? debug + 1 : 0);
                break;
            case KeyEvent.VK_P:
            case KeyEvent.VK_PAUSE:
                pause = !pause;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /*----- the sample gameloop processing -----*/

    public void run() {
        initialize();
        loop();
        frame.dispose();
    }

    /**
     * In a near futur, I am quite sure we will have some vaiables or objects to be
     * initialized.
     */
    public void initialize() {
        collidingColor = Color.WHITE;
        squareColor = Color.RED;
        for (int i = 0; i < 20; i++) {
            GameObject go = new GameObject();
            go.x = (int) Math.random() * (screenBuffer.getWidth() - 16);
            go.y = (int) Math.random() * (screenBuffer.getHeight() - 16);
            go.width = 16;
            go.height = 16;
            go.dx = (int) (Math.random() * 8);
            go.dy = (int) (Math.random() * 8);
            go.color = squareColor;

            objects.add(go);
        }
    }

    /**
     * The main loop or our Game Loop !
     */
    public void loop() {
        long nextTime = System.currentTimeMillis();
        long prevTime = nextTime;
        long elapsed = 0;
        while (!exit) {
            nextTime = System.currentTimeMillis();
            if (!pause) {
                update(elapsed);
            }
            render();
            elapsed = nextTime - prevTime;
            waitNext(elapsed);
            prevTime = nextTime;
        }
    }

    /**
     * Compute updates for the frame.
     * 
     * @param elapsed
     */
    public void update(long elapsed) {
        // loop objects
        for (GameObject go : objects) {
            go.color = squareColor;
            go.update(this, elapsed);
            constrainGameObject(go);
        }
    }

    private void constrainGameObject(GameObject go) {
        if (go.x > screenBuffer.getWidth() - go.width) {
            go.x = screenBuffer.getWidth() - go.width;
            go.dx = -go.dx;
            go.color = collidingColor;
        }
        if (go.y >= screenBuffer.getHeight() - go.height) {
            go.y = screenBuffer.getHeight() - go.height;
            go.dy = -go.dy;
            go.color = collidingColor;
        }
        if (go.x <= 0) {
            go.x = 0;
            go.dx = -go.dx;
            go.color = collidingColor;
        }
        if (go.y <= 0) {
            go.y = 0;
            go.dy = -go.dy;
            go.color = collidingColor;
        }
    }

    /**
     * Render the image according to already updated objects.
     */
    public void render() {
        Graphics2D g = (Graphics2D) screenBuffer.getGraphics();
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, screenBuffer.getWidth(), screenBuffer.getHeight());

        // loop objects
        for (GameObject go : objects) {
            go.draw(this, g);
        }

        // render to screen
        Graphics2D sg = (Graphics2D) frame.getContentPane().getGraphics();
        sg.drawImage(screenBuffer, 0, 0, screenBuffer.getWidth() * scale, screenBuffer.getHeight() * scale, 0, 0,
                screenBuffer.getWidth(), screenBuffer.getHeight(), null);

        // Add some debug information
        if (debug > 1) {
            sg.setColor(Color.ORANGE);
            sg.drawString(String.format("debug:%d | pause:%s", debug, (pause ? "on" : "off")), 10, 16);
            for (GameObject go : objects) {
                if (debug > 2) {
                    sg.drawString(String.format("pos:%03d,%03d", go.x, go.y), (go.x + go.width + 4) * scale,
                            go.y * scale);
                }
            }
        }
    }

    /**
     * Witing for the FPS requested.
     * 
     * @param elapsed
     */
    public void waitNext(long elapsed) {
        long waitTime = (1000 / FPS) - elapsed;
        try {
            Thread.sleep(waitTime > 0 ? waitTime : 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entry point for our SampleGameLoop demo.
     * 
     * @param argc
     */
    public static void main(String[] argc) {
        SampleGameObject sgl = new SampleGameObject("Sample Game Loop", 320, 240, 2);
        sgl.run();
    }

}