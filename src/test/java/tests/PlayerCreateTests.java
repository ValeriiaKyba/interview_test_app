package tests;

import clients.PlayerClient;
import dataProviders.PlayerCreateDataProvider;
import helpers.AllureHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import dto.request.PlayerCreateRequestDto;
import dto.response.PlayerCreateResponseDto;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;

public class PlayerCreateTests {

    private PlayerClient client;
    private static final String CREATOR = "supervisor";

    @BeforeClass
    public void setup() {
        client = new PlayerClient();
    }

    // Positive cases
    @Test(dataProvider = "positivePlayers", dataProviderClass = PlayerCreateDataProvider.class)
    public void testCreatePlayerPositive(String editor, PlayerCreateRequestDto request, String description) {
        AllureHelper.addStep("Positive case: " + description);

        Response response = client.createPlayer(editor, request);
        assertEquals(response.getStatusCode(), 200, "Expected status 200 for " + description);

        PlayerCreateResponseDto responseDto = response.as(PlayerCreateResponseDto.class);

        SoftAssert soft = new SoftAssert();

        soft.assertEquals(responseDto.getLogin(), request.getLogin(), "Login should match request");
        soft.assertNotNull(responseDto.getId(), "Expected generated player ID");
        soft.assertEquals(responseDto.getAge(), request.getAge(), "Age should match request");
        soft.assertEquals(responseDto.getGender(), request.getGender(), "Gender should match request");
        soft.assertEquals(responseDto.getRole(), request.getRole(), "Role should match request");
        soft.assertEquals(responseDto.getScreenName(), request.getScreenName(), "Screen name should match request");
        if (request.getPassword() != null) {
            soft.assertNotNull(responseDto.getPassword(),
                    "Password should be present in response when sent in request");
            soft.assertEquals(responseDto.getPassword(), request.getPassword(),
                    "Password in response should match the request");
        } else {
            soft.assertNull(responseDto.getPassword(),
                    "Password should NOT be present in response when not sent in request");
        }

        soft.assertAll();

        AllureHelper.attachText("Create Player Response", response.asPrettyString());
    }

    // Negative cases
    @Test(dataProvider = "negativePlayers", dataProviderClass = PlayerCreateDataProvider.class)
    public void testCreatePlayerNegative(String editor, PlayerCreateRequestDto request, String reason) {
        AllureHelper.addStep("Negative case: " + reason);

        Response response = client.createPlayer(editor, request);

        Assert.assertTrue(response.getStatusCode() >= 400, "Expected client-side validation error");
        AllureHelper.attachText("Negative Response", response.asPrettyString());
    }

    // Auth & Permission scenarios
    @Test(dataProvider = "authErrors", dataProviderClass = PlayerCreateDataProvider.class)
    public void testAuthAndPermissionErrors(String editor, PlayerCreateRequestDto request, int expectedCode, String reason) {
        AllureHelper.addStep("Authorization/Permission test: " + reason);

        Response response = client.createPlayer(editor, request);

        assertEquals(response.getStatusCode(), expectedCode,
                String.format("Expected HTTP %d for case: %s", expectedCode, reason));

        AllureHelper.attachText("Auth Response", response.asPrettyString());
    }

    // Duplicate scenarios
    @Test(dataProvider = "duplicatePlayers", dataProviderClass = PlayerCreateDataProvider.class)
    public void testDuplicatePlayers(String description,
                                     PlayerCreateRequestDto first,
                                     PlayerCreateRequestDto second,
                                     int expectedStatus) {
        AllureHelper.addStep("Duplicate case: " + description);

        Response firstResp = client.createPlayer(CREATOR, first);
        assertEquals(firstResp.getStatusCode(), 200);

        Response secondResp = client.createPlayer(CREATOR, second);
        assertEquals(secondResp.getStatusCode(), expectedStatus,
                "Expected " + expectedStatus + " for duplicate data");

        AllureHelper.attachText("Duplicate Response", secondResp.asPrettyString());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAfterTest(ITestResult result) {
        if (!TestDataHelper.isCleanupListEmpty()) {
            AllureHelper.addStep("Cleanup after test: " + result.getMethod().getMethodName());
            TestDataHelper.cleanupAll();
        }
    }
}
