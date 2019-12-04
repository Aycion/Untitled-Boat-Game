package engine;

import engine.assets.AssetCenter;
import game.Background;
import game.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.PriorityQueue;

public class EngineCore extends Canvas implements Runnable {

    // Game window dimensions
    public int gameWidth,
            gameHeight,
            scale;
    public String name;         // The name to appear in the window title bar
    public JFrame frame;        // The game window that holds the UI
    public Boolean running;     // Whether the game is currently running

    private int sleepTime = 15;  // How long to sleep in-between updates

    public PriorityQueue<GameObject> elements, tempElements;

//    private int[] pixels;

    public static int frameCount = 0;
    public static int logicCount = 0;


    public static int logicPerSecond = 60;
    public static AssetCenter assets;
    public static InputHandler inputs;
    public static GameClock clock;

    public EngineCore(int size, int ratio, int scale, String name, String path) {

        // Initialize the variables
        this.gameHeight = size;
        this.gameWidth = size * ratio;
        this.scale = scale;
        this.name = name;

        this.frame = new JFrame(name);

        // Starting the data collection/storage systems
        inputs = new InputHandler(this);
        assets = new AssetCenter(path);
        elements = new PriorityQueue<>();

        clock = new GameClock();

        // Add the sky and player
        try {
            this.addObject(new Background(this));
            this.addObject(new Player(this));
        } catch (ResourceNotFound e) {
            // this.pixels = ((DataBufferInt) backGround.getRaster().getDataBuffer()).getData();
            System.err.println("Image resource not found");
        }

        // Set up the game window and interface
        this.initWindow();
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
     * said frame. Then, add our {@link InputHandler} to the frame
     * as its {@link java.awt.event.KeyListener}. Finally, set the
     * frame to be focusable and resizable, and set the starting
     * position to null so that it shows up in the center of the screen.
     * Finally, display the frame by calling {@code frame.setVisible(true)}
     */
    private void initWindow() {
        // Setting up the canvas dimensions
        this.setMinimumSize(new Dimension(this.gameWidth * this.scale, this.gameHeight * this.scale));
        this.setMaximumSize(new Dimension(this.gameWidth * this.scale, this.gameHeight * this.scale));
        this.setPreferredSize(new Dimension(this.gameWidth * this.scale, this.gameHeight * this.scale));

        // Close operation; just exits the program when the window closes
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Give the frame a LayoutManager
        this.frame.setLayout(new BorderLayout());

        // Add the EngineCore to the game window
        this.frame.add(this, BorderLayout.CENTER);

        // Pack the elements into the frame, making it as small as possible
        this.frame.pack();

        // Add the event listeners
        this.frame.addKeyListener(inputs);

        // Make the frame focusable so it can capture input
        this.frame.setFocusable(true);
        this.frame.requestFocusInWindow();

        // Set the constants for the frame
        this.frame.setResizable(true);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

    }

    public EngineCore(int Size, int Ratio, int Scale, String Name, AssetCenter assetOverride) {
        this(Size, Ratio, Scale, Name, assetOverride.getPath());
        assets = assetOverride;
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

        inputs.reset();


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
        G.setBackground(Color.LIGHT_GRAY);
        G.clearRect(0, 0, this.gameWidth * this.scale, this.gameHeight * this.scale);

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

    public int getXBound() {
        return this.gameWidth;
    }

    public int getYbound() {
        return this.gameHeight;
    }

}
