import engine.EngineCore;

public class Driver {

    public static void main(String[] args) {
        EngineCore engine = new EngineCore(15,
                "Untitled Boat Game", "./Assets"
        );

        engine.start();
    }
}
