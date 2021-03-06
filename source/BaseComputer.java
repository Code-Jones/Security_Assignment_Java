import java.io.Serializable;
import java.lang.invoke.WrongMethodTypeException;
import java.util.Objects;
import java.util.UUID;

public final class BaseComputer implements Serializable {

    private final UUID identifier = UUID.randomUUID();
    private String cpu; // can't be final if you do encryption
    private final int ram;
    private final int diskSize;
    private LaptopComputer laptopComputer = null;
    private DesktopComputer desktopComputer = null;

    private BaseComputer(String cpu, int ram, int diskSize, int screenSize) {
        this.cpu = cpu;
        this.ram = ram;
        this.diskSize = diskSize;
        this.laptopComputer = LaptopComputer.getInstance(screenSize);
    }

    private BaseComputer(String cpu, int ram, int diskSize, String gpu) {
        this.cpu = cpu;
        this.ram = ram;
        this.diskSize = diskSize;
        this.desktopComputer = DesktopComputer.getInstance(gpu);
    }

    public static BaseComputer addLaptop(String cpu, int ram, int diskSize, int screenSize) {
        return new BaseComputer(cpu, ram, diskSize, screenSize);
    }

    public static BaseComputer addDesktop(String cpu, int ram, int diskSize, String gpu) {
        return new BaseComputer(cpu, ram, diskSize, gpu);
    }

    protected int getScreenSize() {
        try {
            return this.laptopComputer.getScreenSize();
        } catch (Exception e) {
            throw new WrongMethodTypeException("Wrong Object Type");
        }
    }

    protected String getGpu() {
        try {
            return this.desktopComputer.getGpu();
        } catch (Exception e) {
            throw new WrongMethodTypeException("Wrong Object Type");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseComputer that = (BaseComputer) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
        cpu = caesarCipher(cpu, -1);
    }


    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException, ClassNotFoundException {
        String temp = cpu;
        cpu = caesarCipher(cpu, 1);
        stream.defaultWriteObject();
        cpu = temp;
    }


    private static String caesarCipher(String str, int shift) {
        StringBuilder rotated = new StringBuilder();
        for (char c : str.toCharArray()) {
            rotated.append((char) (c + shift));
        }
        return rotated.toString();
    }

    @Override
    public String toString() {
        if (laptopComputer != null) {
            return "Type: LaptopComputer\n" +
                   "CPU: " + cpu + "\n" +
                   "RAM: " + ram + "\n" +
                   "DISK: " + diskSize + "\n" +
                   "Screen Size: " + getScreenSize() + "\n" +
                   "---------------";
        } else {
            return "Type: DesktopComputer\n" +
                   "CPU: " + cpu + "\n" +
                   "RAM: " + ram + "\n" +
                   "DISK: " + diskSize + "\n" +
                   "GPU: " + getGpu() + "\n" +
                   "---------------";
        }
    }


}
