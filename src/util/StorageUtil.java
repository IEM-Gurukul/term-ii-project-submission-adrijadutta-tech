package util;

import java.io.*;

public class StorageUtil {

    public static void saveObject(Object obj, String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error saving object to " + filepath + ": " + e.getMessage());
        }
    }

    public static Object loadObject(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading object from " + filepath + ": " + e.getMessage());
            return null;
        }
    }
}
