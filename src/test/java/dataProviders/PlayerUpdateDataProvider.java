package dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PlayerUpdateRequestDto;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerUpdateDataProvider {

    private static final String JSON_PATH = "src/test/resources/testdata/player_update.json";

    private static JsonNode readJson() throws IOException {
        return new ObjectMapper().readTree(new File(JSON_PATH));
    }

    @DataProvider(name = "positiveUpdate")
    public Object[][] positiveUpdate() throws IOException {
        JsonNode array = readJson().get("positiveUpdate");
        List<Object[]> data = new ArrayList<>();

        for (JsonNode node : array) {
            String description = node.get("description").asText();
            String editor = node.get("editor").asText();
            String roleToCreate = node.get("roleToCreate").asText();

            List<String> updateFields = new ArrayList<>();
            if (node.has("updateFields") && node.get("updateFields").isArray()) {
                for (JsonNode field : node.get("updateFields")) {
                    updateFields.add(field.asText());
                }
            }

            if (updateFields.isEmpty()) {
                updateFields = List.of();
            }

            data.add(new Object[]{description, editor, roleToCreate, updateFields});
        }

        return data.toArray(new Object[0][0]);
    }


    @DataProvider(name = "negativeUpdate")
    public Object[][] negativeUpdate() throws IOException {
        JsonNode array = readJson().get("negativeUpdate");
        List<Object[]> data = new ArrayList<>();

        for (JsonNode node : array) {
            String description = node.get("description").asText();
            String editor = node.get("editor").asText();
            String roleToCreate = node.get("roleToCreate").asText();
            int expectedCode = node.get("expectedCode").asInt();

            PlayerUpdateRequestDto.Builder builder = new PlayerUpdateRequestDto.Builder()
                    .age(30)
                    .gender("male")
                    .login("upd_" + System.currentTimeMillis())
                    .role("user")
                    .screenName("screen_" + System.currentTimeMillis())
                    .password("abc12345");

            if (node.has("updateAge")) {
                builder.age(node.get("updateAge").asInt());
            }
            if (node.has("updateGender")) {
                builder.gender(node.get("updateGender").asText());
            }
            if (node.has("updateRole")) {
                builder.role(node.get("updateRole").asText());
            }

            PlayerUpdateRequestDto request = builder.build();

            data.add(new Object[]{description, editor, roleToCreate, request, expectedCode});
        }

        return data.toArray(new Object[0][0]);
    }

    @DataProvider(name = "duplicateUpdate")
    public Object[][] duplicateUpdate() throws IOException {
        JsonNode array = readJson().get("duplicateUpdate");
        List<Object[]> data = new ArrayList<>();

        for (JsonNode node : array) {
            String description = node.get("description").asText();
            String editor = node.get("editor").asText();
            String roleToCreate = node.get("roleToCreate").asText();
            String duplicateField = node.get("duplicateField").asText();
            int expectedCode = node.get("expectedCode").asInt();

            PlayerUpdateRequestDto request = new PlayerUpdateRequestDto.Builder()
                    .age(25)
                    .gender("male")
                    .login("some_login_" + System.currentTimeMillis())
                    .screenName("some_screen_" + System.currentTimeMillis())
                    .role("user")
                    .password("abc12345")
                    .build();

            data.add(new Object[]{description, editor, roleToCreate, duplicateField, expectedCode, request});
        }

        return data.toArray(new Object[0][0]);
    }
}
