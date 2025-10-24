package tests;

import clients.PlayerClient;
import dataProviders.PlayerDeleteDataProvider;
import dto.request.PlayerDeleteRequestDto;
import dto.response.PlayerCreateResponseDto;
import helpers.AllureHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PlayerDeleteTests {

    private PlayerClient client;
    private static final String CREATOR = "supervisor";

    @BeforeClass
    public void setup() {
        client = new PlayerClient();
    }

    // Positive cases
    @Test(dataProvider = "positiveDeletes", dataProviderClass = PlayerDeleteDataProvider.class)
    public void testDeletePlayerPositive(String description, String editor, String targetRole) {
        AllureHelper.addStep("Positive case: " + description);

        PlayerCreateResponseDto created = TestDataHelper.createPlayer(CREATOR, targetRole);

        PlayerDeleteRequestDto request = new PlayerDeleteRequestDto.Builder()
                .playerId(created.getId())
                .build();

        Response resp = client.deletePlayer(editor, request);
        Assert.assertEquals(resp.getStatusCode(), 204, "Expected successful deletion");
        AllureHelper.attachText("Response", resp.asPrettyString());
    }

    // Negative cases
    @Test(dataProvider = "negativeDeletes", dataProviderClass = PlayerDeleteDataProvider.class)
    public void testDeletePlayerNegative(String description, PlayerDeleteRequestDto request, int expectedCode) {
        AllureHelper.addStep("Negative case: " + description);

        Response resp = client.deletePlayer(CREATOR, request);
        Assert.assertEquals(resp.getStatusCode(), expectedCode,
                "Expected error code for invalid deletion attempt");
        AllureHelper.attachText("Response", resp.asPrettyString());
    }

    // Auth & Permission scenarios
    @Test(dataProvider = "authDeletes", dataProviderClass = PlayerDeleteDataProvider.class)
    public void testDeletePlayerUnauthorized(String description, String editor, String targetRole, int expectedCode) {
        AllureHelper.addStep("Authorization/Permission test: " + description);

        String actingEditorLogin;
        Long targetPlayerId;

        switch (description) {
            case "User tries to delete another user", "Supervisor tries to delete another supervisor" -> {
                TestDataHelper.PlayerPair pair = TestDataHelper.createPlayerPair(CREATOR, editor, targetRole);
                actingEditorLogin = pair.editor().getLogin();
                targetPlayerId = pair.target().getId();
            }
            case "User tries to delete self" -> {
                PlayerCreateResponseDto user = TestDataHelper.createPlayer(CREATOR, editor);
                actingEditorLogin = user.getLogin();
                targetPlayerId = user.getId();
            }
            default -> throw new IllegalArgumentException("Unhandled auth scenario: " + description);
        }

        PlayerDeleteRequestDto request = new PlayerDeleteRequestDto.Builder()
                .playerId(targetPlayerId)
                .build();

        Response response = client.deletePlayer(actingEditorLogin, request);

        Assert.assertEquals(response.getStatusCode(), expectedCode,
                String.format("Expected %d for '%s'", expectedCode, description));

        AllureHelper.attachText("Response", response.asPrettyString());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAfterTest(ITestResult result) {
        if (!TestDataHelper.isCleanupListEmpty()) {
            AllureHelper.addStep("Cleanup after test: " + result.getMethod().getMethodName());
            TestDataHelper.cleanupAll();
        }
    }
}
