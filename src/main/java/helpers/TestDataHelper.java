package helpers;

import clients.PlayerClient;
import dto.request.PlayerCreateRequestDto;
import dto.response.PlayerCreateResponseDto;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class TestDataHelper {

    private static final PlayerClient client = new PlayerClient();
    private static final ThreadLocal<List<PlayerCreateResponseDto>> threadPlayers =
            ThreadLocal.withInitial(ArrayList::new);


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
            registerForCleanup(created);
        }
        return created;
    }

    public static void registerForCleanup(PlayerCreateResponseDto player) {
        if (player != null) {
            threadPlayers.get().add(player);
        }
    }

    public static PlayerPair createPlayerPair(String creator, String editorRole, String targetRole) {
        PlayerCreateResponseDto editor = createPlayer(creator, editorRole);
        PlayerCreateResponseDto target = createPlayer(creator, targetRole);
        return new PlayerPair(editor, target);
    }

    public record PlayerPair(PlayerCreateResponseDto editor, PlayerCreateResponseDto target) {
    }

    public static void cleanupAll() {
        List<PlayerCreateResponseDto> players = threadPlayers.get();
        if (players.isEmpty()) {
            AllureHelper.attachText("Nothing to clean up — no players were created in this test: ", "0");
            return;
        }

        for (PlayerCreateResponseDto player : players) {
            try {
                CleanupHelper.deletePlayerIfExists(player.getId());
            } catch (Exception e) {
                AllureHelper.attachText("Cleanup failed for playerId " + player.getId(), e.getMessage());
            }
        }

        players.clear();
        threadPlayers.remove();
    }
}
