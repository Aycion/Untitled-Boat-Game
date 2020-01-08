import engine.EngineCore;

public class Driver {

    public static void main(String[] args) {
        EngineCore engine = new EngineCore(
                "Untitled Boat Game", "./Assets"
        );

        engine.start();
    }
}
