package dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PlayerCreateRequestDto;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCreateDataProvider {

    private static final String JSON_PATH = "src/test/resources/testdata/player_create.json";

    private static JsonNode readJson() throws IOException {
        return new ObjectMapper().readTree(new File(JSON_PATH));
    }

    @DataProvider(name = "positivePlayers")
    public Object[][] positivePlayers() throws IOException {
        return readSection("positivePlayers", false);
    }

    @DataProvider(name = "negativePlayers")
    public Object[][] negativePlayers() throws IOException {
        return readSection("negativePlayers", true);
    }

    @DataProvider(name = "authErrors")
    public Object[][] authErrors() throws IOException {
        JsonNode array = readJson().get("authErrors");
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String editor = node.get("editor").asText();
            int expectedCode = node.get("expectedCode").asInt();
            String reason = node.get("reason").asText();
            PlayerCreateRequestDto dto = buildDto(node);
            data.add(new Object[]{editor, dto, expectedCode, reason});
        }
        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "duplicatePlayers")
    public Object[][] duplicatePlayers() throws IOException {
        JsonNode array = readJson().get("duplicatePlayers");
        List<Object[]> data = new ArrayList<>();

        for (JsonNode node : array) {
            String description = node.get("description").asText();
            int expectedStatus = node.has("expectedStatus") ? node.get("expectedStatus").asInt() : 409;

            JsonNode firstNode = node.get("first");
            JsonNode secondNode = node.get("second");

            PlayerCreateRequestDto first = buildDto(firstNode);
            PlayerCreateRequestDto second = buildDto(secondNode);

            data.add(new Object[]{description, first, second, expectedStatus});
        }

        return data.toArray(new Object[0][0]);
    }

    private Object[][] readSection(String name, boolean withReason) throws IOException {
        JsonNode array = readJson().get(name);
        List<Object[]> data = new ArrayList<>();
        for (JsonNode node : array) {
            String editor = node.get("editor").asText();
            PlayerCreateRequestDto dto = buildDto(node);
            if (withReason) {
                data.add(new Object[]{editor, dto, node.get("reason").asText()});
            } else {
                data.add(new Object[]{editor, dto, node.get("description").asText()});
            }
        }
        return data.toArray(new Object[0][0]);
    }

    private PlayerCreateRequestDto buildDto(JsonNode node) {
        return new PlayerCreateRequestDto.Builder()
                .age(node.has("age") ? node.get("age").asInt() : null)
                .gender(node.has("gender") ? node.get("gender").asText() : null)
                .login(node.has("login") ? node.get("login").asText() : null)
                .role(node.has("role") ? node.get("role").asText() : null)
                .screenName(node.has("screenName") ? node.get("screenName").asText() : null)
                .password(node.has("password") ? node.get("password").asText() : null)
                .build();
    }
}
