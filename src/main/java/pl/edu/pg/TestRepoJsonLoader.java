package pl.edu.pg;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class TestRepoJsonLoader {

    private final String filePath;

    TestRepoJsonLoader(String filePath) {
        this.filePath = filePath;
    }

    public void saveJson(Set<Czlowiek> treeHeads) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(treeHeads, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public Set<Czlowiek> readJson() {
        try {
            Type type = new TypeToken<Set<Czlowiek>>() {
            }.getType();

            // custom deserializer for nested (HashSet/TreeSet)<Czlowiek>
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            type,
                            new JsonDeserializer<Set<Czlowiek>>() {
                                @Override
                                public Set<Czlowiek> deserialize(JsonElement json, Type type,
                                        JsonDeserializationContext context)
                                        throws JsonParseException {
                                    Set<Czlowiek> result = CzlowiekContainerFactory.chooseSet();
                                    JsonArray jsonArray = json.getAsJsonArray();
                                    for (JsonElement element : jsonArray) {
                                        result.add(context.deserialize(element, Czlowiek.class));
                                    }
                                    return result;
                                }
                            })
                    .create();

            return gson.fromJson(new FileReader(filePath), type);
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

}
