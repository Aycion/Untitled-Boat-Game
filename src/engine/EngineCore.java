package engine;

import engine.assets.AssetCenter;
import engine.graphics.Camera;
import game.Driftwood;
import game.Fortress;
import game.Player;
import game.environment.GameWorld;
import game.environment.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferStrategy;
import java.util.PriorityQueue;

public class EngineCore extends Canvas implements Runnable {

    // Game window dimensions
    public int scale;
    public String name;         // The name to appear in the window title bar
    public JFrame frame;        // The game window that holds the UI (this class)
    public Boolean running;     // Whether the game is currently running

    private int sleepTime = 15;  // How long to sleep in-between updates

    public PriorityQueue<GameObject> elements, tempElements;

    public static int frameCount = 0;
    public static int logicCount = 0;

    public static int logicPerSecond = 60;
    public static AssetCenter assets;
    public static InputCaptor inputCaptor;
    public static GameClock clock;
    public static GameAudio audio;

    public static GameWorld gameWorld;
    public static Camera gameCamera;
    public static Player player;

    public static EngineCore instance;

    /**
     * Main constructor for creating an instance of the game
     * @param scale
     * @param name
     * @param path
     */
    public EngineCore(int scale, String name, String path) {
        EngineCore.instance = this;

        // Initialize the variables
        this.scale = scale;
        this.name = name;

        this.frame = new JFrame(name);

        inputCaptor = new InputCaptor();

        // Starting the data collection/storage systems
        assets = new AssetCenter(path);
        elements = new PriorityQueue<>();

        clock = new GameClock();
        audio = new GameAudio();

        // Set up the game window and interface
        this.initWindow();

        // Initialize and add the game world
        gameWorld = new GameWorld(this, new AffineTransform());
        this.addObject(gameWorld);

        // Initialize and add the game camera
        gameCamera = new Camera(this, new AffineTransform());
        this.addObject(gameCamera);

        try {
            this.addObject(new WorldMap(this, new AffineTransform()));
            // Add the player object to the game
            player = new Player(this, AffineTransform.getTranslateInstance(650, 300));
            this.addObject(player);

            // TESTING: Add a single "fortress" to the game
            this.addObject(new Fortress(this, AffineTransform.getTranslateInstance(900, 400)));

            // TESTING: Add a single piece of driftwood to the game
             this.addObject(new Driftwood(this, AffineTransform.getTranslateInstance(200, 500)));
        } catch (ResourceNotFound e) {
            System.err.println("Image resource not found");
        }

    }

    /**
     * Initialize the window with the necessary constants.
     *
     * Start by setting max, min, and preferred size to the game
     * dimensions multiplied with the game's set scale factor. Then
     * take the {@link JFrame} that represents the window contents
     * and set its close op and layout. Add the current
     * {@link EngineCore} instance to the frame and pack it, making
     * the instance of the {@link EngineCore} the primary contents of
     * said frame. Then, add our {@link InputCaptor} to the frame
     * as its {@link java.awt.event.KeyAdapter}. Finally, set the
     * frame to be focusable and resizable, and set the starting
     * position to null so that it shows up in the center of the screen.
     * Finally, display the frame by calling {@code frame.setVisible(true)}
     */
    private void initWindow() {

        // Set the screen to be maximized in both dimensions on the screen
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Remove the title bar for a fullscreen effect
        this.frame.setUndecorated(true);

        // Close operation; just exits the program when the window closes
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Give the frame a LayoutManager
        this.frame.setLayout(new BorderLayout());

        // Add the InputCaptor, an invisible frame element
        //  that captures KeyEvents with its KeyAdapter
        this.frame.add(inputCaptor);

        // Add the EngineCore to the game window
        this.frame.add(this, BorderLayout.CENTER);

        // Pack the elements into the frame, making it as small as possible
        this.frame.pack();

        // Make the frame focusable so it can capture input
        this.frame.setFocusable(true);
        this.frame.requestFocusInWindow();

        this.frame.setAlwaysOnTop(false);

        // Set the constants for the frame
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

    }

    public synchronized void start() {
        this.running = true;
        new Thread(this).start();
    }

    public synchronized void stop() {
        this.running = false;
        new Thread(this).start();
    }

    @Override
    public void run() {
        long LT = System.nanoTime();

        double logicDelta = 0;

        while (this.running) {
            clock.update();

            frame.requestFocusInWindow();
            tempElements = new PriorityQueue<>(elements);
            boolean render = true;

            // Tracks when logic should update, informed by logicPerSecond
            logicDelta += clock.getDeltaTime() * logicPerSecond;

            //Logic set to perform only 60 times per second
            if (logicDelta >= 1) {
                logicCount++;
                logic();
                logicDelta -= 1;
                render = true;
            }

            //Sleep to limit the number of graphic updates (too much would slow the logic too)
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (render) {
                frameCount++;
                graphic();
            }

            //Graphics update free to use all available resources.
            if (System.nanoTime() - LT >= 1000000000) {

                LT += 1000000000;
                System.out.println("FPS: " + frameCount + " LPS: " + logicCount);
                logicCount = 0;
                frameCount = 0;
            }

        }

        inputCaptor.reset();


    }

    public void logic() {
        for (GameObject j : tempElements) {
            j.logic();
        }

    }


    public void graphic() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D G = (Graphics2D) bs.getDrawGraphics();

        // Transform the graphics context according to the
        //  current camera transform.
        try {
            // Use the camera's inverse transform so camera
            //  movement can be tracked in terms of world-space
            //  (i.e. if the camera moves down, everything else
            //  should move up)
            G.setTransform(gameCamera.getTransform().createInverse());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        //calling the graphic methods of every element
        for (GameObject j : tempElements) {
            j.graphic(G);
        }

        G.dispose();
        bs.show();
    }


    public void addObject(GameObject newObject) {
        elements.add(newObject);
    }

}
