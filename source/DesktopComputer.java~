import java.io.Serializable;

public final class DesktopComputer implements Serializable {

    private String gpu;

    private DesktopComputer(String gpu) {
        this.gpu = gpu;
    }

    protected static DesktopComputer getInstance(String gpu) {
        return new DesktopComputer(gpu);
    }

    protected String getGpu() {
        return gpu;
    }


    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
        gpu = caesarCipher(gpu, -1);
    }


    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException, ClassNotFoundException {
        String temp = this.gpu;
        gpu = caesarCipher(temp, 1);
        stream.defaultWriteObject();
        gpu = temp;
    }

    private static String caesarCipher(String str, int shift) {
        StringBuilder rotated = new StringBuilder();
        for (char c : str.toCharArray()) {
            rotated.append((char) (c + shift));
        }
        return rotated.toString();
    }


}
