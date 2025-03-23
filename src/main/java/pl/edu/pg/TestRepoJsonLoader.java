package pl.edu.pg;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TestRepoJsonLoader {

    private final double version;
    private final String dirPath;
    private final String filePath;

    TestRepoJsonLoader(double version, String dirPath, String filePath) {
        this.version = version;
        this.dirPath = dirPath;
        this.filePath = filePath;
        try {
            Files.createDirectories(Paths.get(dirPath));
            Files.createDirectories(Paths.get(dirPath + "separated"));
        } catch (Exception e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    public void saveJson(Set<Czlowiek> treeHeads) {
        try (Writer writer = new FileWriter(dirPath + filePath)) {
            Gson gson = new GsonBuilder()
                    .setVersion(version)
                    .setPrettyPrinting()
                    .create();

            gson.toJson(treeHeads, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveJson(Czlowiek head, String filePath) {
        try (Writer writer = new FileWriter(dirPath + filePath)) {
            Gson gson = new GsonBuilder()
                    .setVersion(version)
                    .setPrettyPrinting()
                    .create();
            Set<Czlowiek> tmp = new HashSet<>();
            tmp.add(head);
            gson.toJson(tmp, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveJson(Stream<Czlowiek> people) {
        try {
            Files.walk(Paths.get(dirPath + "separated"))
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (IOException e) {
            System.err.println("Error deleting files: " + e.getMessage());
        }
        people.forEach(person -> saveJson(person, "separated/" + person.hashCode() + ".json"));
    }

    public Set<Czlowiek> readJson() {
        try {
            Type type = new TypeToken<Set<Czlowiek>>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .setVersion(version)
                    .registerTypeAdapter(type, customDeserializer)
                    .create();

            return gson.fromJson(new FileReader(dirPath + filePath), type);
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

    public Czlowiek readJson(String filePath) {
        try {
            Type type = new TypeToken<Set<Czlowiek>>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .setVersion(version)
                    .registerTypeAdapter(type, customDeserializer)
                    .create();

            Set<Czlowiek> tmp = gson.fromJson(new FileReader(dirPath + filePath), type);
            return tmp.iterator().next();
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

    // custom deserializer for nested (HashSet/TreeSet)<Czlowiek>
    private final JsonDeserializer<Set<Czlowiek>> customDeserializer = new JsonDeserializer<Set<Czlowiek>>() {
        @Override
        public Set<Czlowiek> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            Set<Czlowiek> result = CzlowiekContainerFactory.chooseSet();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                result.add(context.deserialize(element, Czlowiek.class));
            }
            return result;
        }
    };
}
