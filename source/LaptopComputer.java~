import java.io.Serializable;

public final class LaptopComputer implements Serializable {

    private final int screenSize;

    private LaptopComputer(int screenSize) {
        this.screenSize = screenSize;
    }

    protected static LaptopComputer getInstance(int screenSize) {
        return new LaptopComputer(screenSize);
    }

    protected int getScreenSize() {
        return this.screenSize;
    }


}
