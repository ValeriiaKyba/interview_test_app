package tests;

import clients.PlayerClient;
import dataProviders.PlayerGetDataProvider;
import dto.request.PlayerGetByPlayerIdRequestDto;
import dto.response.PlayerItem;
import helpers.AllureHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import dto.response.PlayerCreateResponseDto;
import dto.response.PlayerGetAllResponseDto;
import dto.response.PlayerGetByPlayerIdResponseDto;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class PlayerGetTests {

    private PlayerClient client;
    private static final String CREATOR = "supervisor";

    @BeforeClass
    public void setup() {
        client = new PlayerClient();
    }

    // Positive cases
    @Test(dataProvider = "positiveGet", dataProviderClass = PlayerGetDataProvider.class)
    public void testGetPlayerPositive(String description, String targetRole) {
        AllureHelper.addStep("Positive case: " + description);

        PlayerCreateResponseDto created = TestDataHelper.createPlayer(CREATOR, targetRole);

        PlayerGetByPlayerIdRequestDto request = new PlayerGetByPlayerIdRequestDto.Builder()
                .playerId(created.getId())
                .build();

        Response resp = client.getPlayerById(request);

        Assert.assertEquals(resp.getStatusCode(), 200, "Expected 200 OK for 'get player by id'");

        PlayerGetByPlayerIdResponseDto player = resp.as(PlayerGetByPlayerIdResponseDto.class);

        AllureHelper.addStep("Validating full response object consistency");

        SoftAssert soft = new SoftAssert();

        soft.assertEquals(player.getId(), created.getId(), "Player ID mismatch");
        soft.assertEquals(player.getLogin(), created.getLogin(), "Player Login mismatch");
        soft.assertEquals(player.getAge(), created.getAge(), "Player Age mismatch");
        soft.assertEquals(player.getGender(), created.getGender(), "Player Gender mismatch");
        soft.assertEquals(player.getPassword(), created.getPassword(), "Player Password mismatch");
        soft.assertEquals(player.getRole(), created.getRole(), "Player Role mismatch");
        soft.assertEquals(player.getScreenName(), created.getScreenName(), "Player Screen Name mismatch");

        soft.assertAll();

        AllureHelper.attachText("Response", resp.asPrettyString());

        TestDataHelper.registerForCleanup(created.getId());
    }

    // Negative cases
    @Test(dataProvider = "negativeGet", dataProviderClass = PlayerGetDataProvider.class)
    public void testGetPlayerNegative(String description,
                                      PlayerGetByPlayerIdRequestDto request,
                                      int expectedCode) {
        AllureHelper.addStep("Negative case: " + description);

        Response resp = client.getPlayerById(request);

        Assert.assertEquals(resp.getStatusCode(), expectedCode,
                String.format("Expected %d for '%s'", expectedCode, description));

        AllureHelper.attachText("Response", resp.asPrettyString());
    }


    @Test(dataProvider = "getAll", dataProviderClass = PlayerGetDataProvider.class)
    public void testGetAllPlayers(String description, List<String> targetRoles) {
        AllureHelper.addStep("Scenario: " + description);

        PlayerCreateResponseDto player1 = TestDataHelper.createPlayer(CREATOR, targetRoles.get(0));
        PlayerCreateResponseDto player2 = TestDataHelper.createPlayer(CREATOR, targetRoles.get(1));
        PlayerCreateResponseDto player3 = TestDataHelper.createPlayer(CREATOR, targetRoles.get(2));

        Response resp = client.getAllPlayers();

        Assert.assertEquals(resp.getStatusCode(), 200, "Expected 200 OK for 'get all players'");

        PlayerGetAllResponseDto players = resp.as(PlayerGetAllResponseDto.class);

        Assert.assertTrue(players.getPlayers() != null && !players.getPlayers().isEmpty(),
                "Expected non-empty player list");

        boolean containsAll = players.getPlayers().stream()
                .map(PlayerItem::getId)
                .collect(java.util.stream.Collectors.toSet())
                .containsAll(java.util.List.of(player1.getId(), player2.getId(), player3.getId()));

        Assert.assertTrue(containsAll, "Response should contain all newly created players");

        AllureHelper.attachText("Response", resp.asPrettyString());

        TestDataHelper.registerForCleanup(player1.getId());
        TestDataHelper.registerForCleanup(player2.getId());
        TestDataHelper.registerForCleanup(player3.getId());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAfterTest(ITestResult result) {
        AllureHelper.addStep("Cleaning up after test: " + result.getMethod().getMethodName());
        TestDataHelper.cleanupAll();
    }
}

