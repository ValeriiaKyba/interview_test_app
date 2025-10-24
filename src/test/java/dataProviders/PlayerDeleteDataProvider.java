package dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PlayerDeleteRequestDto;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDeleteDataProvider {

    private static final String JSON_PATH = "src/test/resources/testdata/player_delete.json";

    private static JsonNode readJson() throws IOException {
        return new ObjectMapper().readTree(new File(JSON_PATH));
    }

    @DataProvider(name = "positiveDeletes")
    public Object[][] positiveDeletes() throws IOException {
        JsonNode array = readJson().get("positiveDeletes");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String description = node.get("description").asText();
            String editor = node.get("editor").asText();
            String targetRole = node.get("targetRole").asText();
            data.add(new Object[]{description, editor, targetRole});
        }
        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "negativeDeletes")
    public Object[][] negativeDeletes() throws IOException {
        JsonNode array = readJson().get("negativeDeletes");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String description = node.get("description").asText();
            int expectedCode = node.get("expectedCode").asInt();

            Long playerId = node.has("playerId") ? node.get("playerId").asLong() : null;
            PlayerDeleteRequestDto request = new PlayerDeleteRequestDto.Builder()
                    .playerId(playerId)
                    .build();

            data.add(new Object[]{description, request, expectedCode});
        }
        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "authDeletes")
    public Object[][] authDeletes() throws IOException {
        JsonNode array = readJson().get("authDeletes");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String description = node.get("description").asText();
            String editor = node.get("editor").asText();
            int expectedCode = node.get("expectedCode").asInt();
            data.add(new Object[]{description, editor, expectedCode});
        }
        return data.toArray(new Object[0][0]);
    }
}
