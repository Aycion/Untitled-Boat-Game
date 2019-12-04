package engine;

public class Driver {

    public static void main(String[] args) {
        EngineCore engine = new EngineCore(20, 2, 15, "game", "./Assets");

        engine.start();
    }
}
