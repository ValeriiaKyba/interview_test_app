package helpers;

import clients.PlayerClient;
import dto.request.PlayerDeleteRequestDto;
import io.restassured.response.Response;

public class CleanupHelper {

    private static final PlayerClient client = new PlayerClient();

    public static void deletePlayerIfExists(Long playerId) {
        if (playerId == null) {
            AllureHelper.addStep("Skipped cleanup — playerId is null");
            return;
        }

        try {
            AllureHelper.addStep("Attempting to delete player ID: " + playerId + " as supervisor");

            PlayerDeleteRequestDto request = new PlayerDeleteRequestDto.Builder()
                    .playerId(playerId)
                    .build();

            Response deleteResponse = client.deletePlayer("supervisor", request);
            int status = deleteResponse.getStatusCode();

            if (status == 200 || status == 204) {
                AllureHelper.addStep("Player deleted successfully (ID: " + playerId + ")");
            } else if (status == 404) {
                AllureHelper.addStep("Player already deleted or not found (ID: " + playerId + ")");
            } else {
                AllureHelper.addStep("Unexpected status during cleanup: " + status);
                AllureHelper.attachText("Delete response", deleteResponse.asPrettyString());
            }
        } catch (Exception e) {
            AllureHelper.attachText("Cleanup exception", e.getMessage());
        }
    }
}
