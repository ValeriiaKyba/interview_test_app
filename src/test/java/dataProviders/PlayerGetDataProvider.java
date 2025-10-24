package dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PlayerGetByPlayerIdRequestDto;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerGetDataProvider {

    private static final String JSON_PATH = "src/test/resources/testdata/player_get.json";

    private static JsonNode readJson() throws IOException {
        return new ObjectMapper().readTree(new File(JSON_PATH));
    }

    @DataProvider(name = "positiveGet")
    public Object[][] positiveGet() throws IOException {
        JsonNode array = readJson().get("positiveGet");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            data.add(new Object[]{node.get("description").asText()});
        }
        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "negativeGet")
    public Object[][] negativeGet() throws IOException {
        JsonNode array = readJson().get("negativeGet");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String description = node.get("description").asText();
            Long playerId = node.has("playerId") ? node.get("playerId").asLong() : null;
            int expectedCode = node.get("expectedCode").asInt();

            PlayerGetByPlayerIdRequestDto request = new PlayerGetByPlayerIdRequestDto.Builder()
                    .playerId(playerId)
                    .build();

            data.add(new Object[]{description, request, expectedCode});
        }
        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "getAll")
    public Object[][] getAll() throws IOException {
        JsonNode array = readJson().get("getAll");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String description = node.get("description").asText();

            List<String> targetRoles = new ArrayList<>();
            if (node.has("targetRoles") && node.get("targetRoles").isArray()) {
                for (JsonNode field : node.get("targetRoles")) {
                    targetRoles.add(field.asText());
                }
            }

            data.add(new Object[]{description, targetRoles});
        }
        return data.toArray(new Object[0][0]);
    }
}
