package engine;

import engine.assetmanager.AssetCenter;
import engine.graphics.Camera;
import engine.ecs.GameObject;
import game.assets.Driftwood;
import game.assets.Fortress;
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
    public String name;         // The name to appear in the window title bar
    public JFrame frame;        // The game window that holds the UI (this class)
    public Boolean running;     // Whether the game is currently running

    private int sleepTime = 15;  // How long to sleep in-between updates

    public PriorityQueue<GameObject> gameObjects, tempObjects;

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
     * @param name The name of the process (for the window)
     * @param assetPath The path to the assets folder
     */
    public EngineCore(String name, String assetPath) {
        EngineCore.instance = this;

        /*
        Initialize the static global elements
         */
        inputCaptor = new InputCaptor();        // Input handler
        assets = new AssetCenter(assetPath);    // Asset manager
        clock = new GameClock();    // Game clock
        audio = new GameAudio();    // Audio manager

        // Process name
        this.name = name;

        // Initialize the instance variables
        this.gameObjects = new PriorityQueue<>();

        // Set up the game window and interface
        this.initWindow();

        // Initialize and add the game world
        gameWorld = new GameWorld(this, new AffineTransform());
        this.addObject(gameWorld);

        // Initialize and add the game camera
        gameCamera = new Camera(this, new AffineTransform());
        this.addObject(gameCamera);

        /*
        Add objects that rely on assets (may throw exceptions)
         */
        try {
            // World map (and assoc. water texture)
            this.addObject(new WorldMap(this, new AffineTransform()));

            // Add the player object to the game
            player = new Player(this, AffineTransform.getTranslateInstance(650, 300));
            this.addObject(player);

            // TESTING: Add a fortress to the game
            this.addObject(new Fortress(this, AffineTransform.getTranslateInstance(900, 400)));

            // TESTING: Add a piece of driftwood to the game
             this.addObject(new Driftwood(this, AffineTransform.getTranslateInstance(200, 500)));
        } catch (ResourceNotFound e) {
            System.err.println("Image resource not found");
            e.printStackTrace();
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

        // Main game window
        this.frame = new JFrame(this.name);

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


        // Set the constants for the frame
        this.frame.setAlwaysOnTop(false);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

    }


    public synchronized void start() {
        this.running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        long LT = System.nanoTime();

        double logicDelta = 0;

        while (this.running) {
            clock.update();

//            this.frame.requestFocusInWindow();
            this.tempObjects = new PriorityQueue<>(this.gameObjects);
            boolean render = false;

            // Tracks when logic should update, informed by logicPerSecond
            logicDelta += clock.getDeltaTime() * logicPerSecond;

            //Logic set to perform only 60 times per second
            if (logicDelta >= 1) {
                logicCount++;
                this.logic();
                logicDelta -= 1;
                render = true;
            }

            //Sleep to limit the number of graphic updates (too much would slow the logic too)
            try {
                Thread.sleep(this.sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (render) {
                frameCount++;
                this.graphic();
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
        for (GameObject j : this.tempObjects) {
            j.logic();
        }

    }


    public void graphic() {

        BufferStrategy bs = super.getBufferStrategy();
        if (bs == null) {
            super.createBufferStrategy(3);
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
        for (GameObject j : this.tempObjects) {
            j.graphic(G);
        }

        G.dispose();
        bs.show();
    }


    public void addObject(GameObject newObject) {
        this.gameObjects.add(newObject);
    }

}
