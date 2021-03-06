import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ManageComputers {

    static final ArrayList < BaseComputer > computerList = new ArrayList < > ();
    static final String basePath = "/root/assign1Data/";
    static final Path path = Paths.get(basePath);
    static Scanner in = new Scanner(System.in);
    static boolean running = true;

    static {
        System.out.println("Welcome to the most secure inventory system ever");
    }

    public static void main(String[] args) {
        // security manager
        if (System.getSecurityManager() == null) {
            System.out.println("***ERROR: program must run under a Security Manager!***");
            System.exit(0);
        }
        
        do {
            switch (printMenu()) {
                case 1: // Load
                    try {
                        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
                        if (directoryStream.iterator().hasNext()) {
                            FileFilter filter = dir -> dir.getName().endsWith(".txt"); // security manager prevents this
                            File[] items = new File(basePath).listFiles(filter); // add filter
                            int tryAdd = computerList.size();
                            for (File item : items) {
                                BaseComputer temp = readFile(item); // dirty but whatever.
                                if (!computerList.contains(temp))
                                    computerList.add(temp);
                            }

                            System.out.printf("%d Computers added to the current list%n", computerList.size() - tryAdd);
                        } else System.out.println("No files to load");
                    } catch (Exception e) {
                        System.out.println('f');
                    }
                    break;
                case 2: // Save
                    try {
                        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
                        if (directoryStream.iterator().hasNext()) {
                            System.out.println("Files already saved will be overwritten. Is that okay ? (y/n)");
                            if (!in.next().equals("y")) {
                                System.out.println("No files overwritten");
                                break;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Something is wrong with the file directory");
                    }
                    if (computerList.size() > 0) {
                        for (int i = 0; i < computerList.size(); i++) {
                            writeFile(computerList.get(i), i);
                        }
                        System.out.println("Files saved");
                    } else System.out.println("Nothing to save.");
                    break;
                case 3: // List
                    printList();
                    break;
                case 4: // Add
                    addNewComputer();
                    break;
                case 5: // Delete
                    deleteComputer();
                    break;
                case 6: // Edit
                    editComputer();
                    break;
                case 0: // Exit
                    running = false;
                    in.close();
                    break;
                default:
                    System.out.println("Whoops, Something went wrong. Please try again.");
                    break;
            }
        } while (running);
    }

    private static void printList() {
        if (computerList.size() != 0) {
            for (int i = 0; i < computerList.size(); i++) {
                System.out.printf(
                        "---------------%n" +
                        "COMPUTER #%d%n" +
                        "%s%n",
                        i + 1, computerList.get(i).toString());
            }
        } else {
            System.out.println("Current list is empty");
        }
    }

    private static void editComputer() {
        try {
            if (computerList.size() != 0) {
                printList();
                System.out.print("Enter number of computer to edit: ");
                int input = validateMenuInput(1, computerList.size() + 1);
                menuAddComputer(computerList.get(input - 1).toString().contains("LaptopComputer") ? 1 : 2);
                computerList.remove(input);
            } else {
                System.out.println("Current list is empty");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Whoops, Some how you put in the wrong input.");
        }
    }

    private static void deleteComputer() {
        try {
            if (computerList.size() != 0) {
                printList();
                System.out.print("Enter number of computer to delete: ");
                int input = validateMenuInput(1, computerList.size() + 1);
                computerList.remove(input);
            } else {
                System.out.println("Current list is empty");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Whoops, Some how you put in the wrong input.");
        }
    }

    private static void addNewComputer() {
        boolean stillAdding = true;
        do {
            int input = printAddMenu();
            // todo fix this
            switch (input) {
                case 1:
                case 2:
                    menuAddComputer(input);
                    stillAdding = false;
                    break;
                case 3 : // Back to Menu
                    stillAdding = false;
                    break;
                default :
                    System.out.println("Whoops, Something went wrong. Please try again.");
                    break;
            }
        } while (stillAdding);
    }

    private static void menuAddComputer(int input) {
        System.out.print("Enter CPU type (i5/i7) : ");
        String cpu = validateGeneralInput("i5", "i7");
        System.out.print("Enter RAM size (8/16) : ");
        int ram = Integer.parseInt(validateGeneralInput("8","16"));
        System.out.print("Enter disk size (250/500) : ");
        int diskSize = Integer.parseInt(validateGeneralInput("250", "500"));
        if (input == 1) {
            try {
                System.out.print("Enter screen size (13/15) : ");
                int screenSize = Integer.parseInt(validateGeneralInput("13", "15"));
                computerList.add(BaseComputer.addLaptop(cpu, ram, diskSize, screenSize));
                System.out.println("Laptop added");
            } catch (Exception e) {
                System.out.println("Couldn't make laptop");
            }
        } else {
            try {
                System.out.print("Enter GPU (intel/amd/nvidia) : ");
                String gpu = validateGeneralInput("intel", "amd", "nvidia");
                computerList.add(BaseComputer.addDesktop(cpu, ram, diskSize, gpu));
                System.out.println("Desktop added");
            } catch (Exception e) {
                System.out.println("Couldn't make desktop");
            }
        }
    }



    private static BaseComputer readFile(File fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            BaseComputer baseComputer = (BaseComputer) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return baseComputer;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not read file");
            return null;
        }
    }

    private static void writeFile(BaseComputer baseComputer, int index) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(basePath + index + ".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(baseComputer);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("Could not write to file");
        }
    }

    private static int printMenu() {
        System.out.print("====MENU====\n" +
                         "1. Load\n" +
                         "2. Save\n" +
                         "3. List\n" +
                         "4. Add\n" +
                         "5. Delete\n" +
                         "6. Edit\n" +
                         "0. Exit\n" +
                         "Enter Selection: ");
        return validateMenuInput(0, 6);
    }

    private static int printAddMenu() {
        System.out.print("ADD NEW COMPUTER\n" +
                         "1. Add new Laptop Computer\n" +
                         "2. Add new Desktop Computer\n" +
                         "3. Back to Menu\n" +
                         "Enter Selection: ");
        return validateMenuInput(1, 3);
    }

    private static String validateGeneralInput(String inputOne, String inputTwo) {
        String input;
        do
            try {
                input = in.next();
                if (input.equals(inputOne) || input.equals(inputTwo)) {
                    return input;
                } else {
                    System.out.println("Please put in one of the given options");
                }
            } catch (InputMismatchException e) {
                System.out.println("Bad input, only the given options available.");
            }
        while (true);
    }
    private static String validateGeneralInput(String inputOne, String inputTwo, String inputThree) {
        String input;
        do
            try {
                input = in.next();
                if (input.equals(inputOne) || input.equals(inputTwo) || input.equals(inputThree)) {
                    return input;
                } else {
                    System.out.println("Please put in one of the given options");
                }
            } catch (InputMismatchException e) {
                System.out.println("Bad input, only the given options available.");
            }
        while (true);
    }

    private static int validateMenuInput(int min, int max) {
        int input;
        do
            try {
                input = Integer.parseInt(in.next());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please put on of the listed numbers");
                }
            } catch (InputMismatchException e) {
                System.out.println("Bad input, please put in a number on the list.");
            } catch (NumberFormatException e) {
                System.out.println("Only Numbers");
            }
        while (true);
    }
}
