package helpers;

import clients.PlayerClient;
import dto.request.PlayerCreateRequestDto;
import dto.response.PlayerCreateResponseDto;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class TestDataHelper {

    private static final PlayerClient client = new PlayerClient();
    private static final List<Long> createdPlayerIds = new ArrayList<>();


    public static PlayerCreateResponseDto createPlayer(String creator, String role) {
        PlayerCreateRequestDto request = new PlayerCreateRequestDto.Builder()
                .age(25)
                .gender("male")
                .login(role + "_auto_" + System.currentTimeMillis())
                .role(role)
                .screenName(role + "_screen_" + System.currentTimeMillis())
                .password("abc12345")
                .build();

        Response response = client.createPlayer(creator, request);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to create player: " + response.asPrettyString());
        }

        PlayerCreateResponseDto created = response.as(PlayerCreateResponseDto.class);
        if (created != null && created.getId() != null) {
            registerForCleanup(created.getId());
        }
        return created;
    }

    public static void registerForCleanup(Long playerId) {
        if (playerId != null && !createdPlayerIds.contains(playerId)) {
            createdPlayerIds.add(playerId);
        }
    }

    public static PlayerPair createPlayerPair(String creator, String editorRole, String targetRole) {
        PlayerCreateResponseDto editor = createPlayer(creator, editorRole);
        PlayerCreateResponseDto target = createPlayer(creator, targetRole);
        return new PlayerPair(editor, target);
    }

    public record PlayerPair(PlayerCreateResponseDto editor, PlayerCreateResponseDto target) {}

    public static void cleanupAll() {
        AllureHelper.addStep("Cleanup all created players...");
        for (Long id : createdPlayerIds) {
            try {
                CleanupHelper.deletePlayerIfExists(id);
            } catch (Exception e) {
                AllureHelper.attachText("Cleanup failed for playerId " + id, e.getMessage());
            }
        }
        createdPlayerIds.clear();
    }

    public static boolean isCleanupListEmpty() {
        return createdPlayerIds.isEmpty();
    }
}
