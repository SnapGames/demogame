package core;

import core.audio.SoundSystem;
import core.collision.MapCollidingService;
import core.io.InputHandler;
import core.state.StateManager;
import core.system.SystemManager;

/**
 * An extra class to demonstrate some basics to create a simple java game.
 *
 * @author Frédéric Delorme
 * @since 2019
 */
public class Game {

    public static long goIndex = 0;
    public Config config;
    public boolean exitRequest = false;
    private String[] argc;

    public SystemManager sysMan;

    public InputHandler inputHandler;
    public Renderer renderer;
    public StateManager stateManager;
    private SoundSystem soundSystem;
    private MapCollidingService mapCollider;

    /**
     * Create the Game container.
     *
     * @param argc list of arguments.
     * @see Config#analyzeArgc(String[])
     */
    public Game(String[] argc) {
        super();
        config = Config.analyzeArgc(argc);
    }

    public void run() {
        System.out.println("Run game");
        initialize();
        loop();
        System.out.println("Game stopped");
        dispose();
        System.exit(0);
    }

    public void initialize() {
        ResourceManager.add(new String[]{"/res/game.json", "/res/bgf-icon.png"});

        sysMan = SystemManager.initialize(this);

        inputHandler = new InputHandler(this);
        sysMan.add(inputHandler);
        renderer = new Renderer(this);
        sysMan.add(renderer);
        soundSystem = new SoundSystem(this);
        sysMan.add(soundSystem);
        mapCollider = new MapCollidingService(this);
        sysMan.add(mapCollider);

        stateManager = new StateManager(this);

        sysMan.add(stateManager);

    }

    public void loop() {
        stateManager.initialize(this);

        long startTime = System.currentTimeMillis();
        long previousTime = startTime;

        while (!exitRequest) {
            startTime = System.currentTimeMillis();

            float elapsed = startTime - previousTime;

            stateManager.input(this);
            stateManager.update(this, elapsed);
            stateManager.render(this, renderer);

            float wait = ((config.fps * 0.001f));

            if (wait > 0) {
                try {
                    Thread.sleep((int) wait);
                } catch (InterruptedException e) {
                    System.out.println(String.format("Unable to wait %d wait ms", wait));
                }
            }
            previousTime = startTime;
        }
    }

    public void dispose(){
        sysMan.dispose();
    }

    /**
     * The famous java Execution entry point.
     *
     * @param argc
     */
    public static void main(String[] argc) {
        Game dg = new Game(argc);
        dg.run();
    }
}