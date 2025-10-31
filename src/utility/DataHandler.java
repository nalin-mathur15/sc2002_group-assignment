package utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Small persistence helper based on Java serialization.
 * - No DB/JSON/XML (fits your assignment rules).
 * - Works with any Serializable object (e.g., Map<String, Internship>).
 *
 * Usage:
 *   Map<String, Internship> data = DataHandler.load("data/internships.dat", HashMap::new);
 *   DataHandler.save("data/internships.dat", data);
 */
public final class DataHandler {

    private DataHandler() {}

    /**
     * Save any Serializable object to disk.
     */
    public static <T extends Serializable> boolean save(String filePath, T data) {
        try {
            ensureParentDir(filePath);
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)))) {
                oos.writeObject(data);
            }
            System.out.println("[DataHandler] Saved -> " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("[DataHandler] Failed saving " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Load a Serializable object from disk; if file is missing/corrupt, returns defaultValue.get().
     */
    public static <T extends Serializable> T load(String filePath, java.util.function.Supplier<T> defaultValue) {
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) {
            return defaultValue.get();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath)))) {
            @SuppressWarnings("unchecked")
            T obj = (T) ois.readObject();
            System.out.println("[DataHandler] Loaded <- " + filePath);
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DataHandler] Failed loading " + filePath + ": " + e.getMessage());
            return defaultValue.get();
        }
    }

    private static void ensureParentDir(String filePath) throws IOException {
        Path path = Paths.get(filePath).toAbsolutePath();
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}
