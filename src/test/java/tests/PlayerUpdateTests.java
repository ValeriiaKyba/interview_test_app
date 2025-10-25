package tests;

import dataProviders.PlayerUpdateDataProvider;
import dto.request.PlayerUpdateRequestDto;
import helpers.AllureHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import dto.response.PlayerCreateResponseDto;
import dto.response.PlayerUpdateResponseDto;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import tests.base.BaseApiTest;

import java.util.List;

public class PlayerUpdateTests extends BaseApiTest {

    private static final String CREATOR = "supervisor";

    // Positive cases
    @Test(dataProvider = "positiveUpdate", dataProviderClass = PlayerUpdateDataProvider.class)
    public void testPlayerUpdatePositive(String description,
                                         String editor,
                                         String roleToCreate,
                                         List<String> updateFields) {

        AllureHelper.addStep("Positive case: " + description);

        PlayerCreateResponseDto target;
        
        if ("user".equals(editor)) {
            target = TestDataHelper.createPlayer(CREATOR, roleToCreate);
            editor = target.getLogin();
        } else {
            target = TestDataHelper.createPlayer(CREATOR, roleToCreate);
        }

        TestDataHelper.registerForCleanup(target);

        PlayerUpdateRequestDto request = getPlayerUpdateRequestDto(updateFields, target);

        Response resp = client.updatePlayer(editor, target.getId(), request);

        Assert.assertEquals(resp.getStatusCode(), 200, "Expected 200 OK for allowed update");

        PlayerUpdateResponseDto updated = resp.as(PlayerUpdateResponseDto.class);

        AllureHelper.addStep("Validating full response object consistency");

        SoftAssert soft = new SoftAssert();

        if (updateFields.contains("age"))
            soft.assertEquals(updated.getAge(), request.getAge(), "Age should be updated");
        else
            soft.assertEquals(updated.getAge(), target.getAge(), "Age should remain unchanged");

        if (updateFields.contains("gender"))
            soft.assertEquals(updated.getGender(), request.getGender(), "Gender should be updated");
        else
            soft.assertEquals(updated.getGender(), target.getGender(), "Gender should remain unchanged");

        if (updateFields.contains("login"))
            soft.assertEquals(updated.getLogin(), request.getLogin(), "Login should be updated");
        else
            soft.assertEquals(updated.getLogin(), target.getLogin(), "Login should remain unchanged");

        if (updateFields.contains("screenName"))
            soft.assertEquals(updated.getScreenName(), request.getScreenName(), "ScreenName should be updated");
        else
            soft.assertEquals(updated.getScreenName(), target.getScreenName(), "ScreenName should remain unchanged");

        if (updateFields.contains("role"))
            soft.assertEquals(updated.getRole(), request.getRole(), "Role should be updated");
        else
            soft.assertEquals(updated.getRole(), target.getRole(), "Role should remain unchanged");

        soft.assertEquals(updated.getId(), target.getId(), "User ID should remain unchanged");

        soft.assertAll();

        AllureHelper.attachText("Response", resp.asPrettyString());
    }

    private static PlayerUpdateRequestDto getPlayerUpdateRequestDto(List<String> updateFields, PlayerCreateResponseDto target) {
        PlayerUpdateRequestDto.Builder builder = new PlayerUpdateRequestDto.Builder();

        if (updateFields.contains("age")) builder.age(40);
        if (updateFields.contains("gender")) builder.gender("female");
        if (updateFields.contains("login")) builder.login("upd_" + target.getLogin());
        if (updateFields.contains("screenName")) builder.screenName("upd_" + target.getScreenName());
        if (updateFields.contains("password")) builder.password("newPass123");
        if (updateFields.contains("role")) builder.role("user");

        return builder.build();
    }

    // Negative cases
    @Test(dataProvider = "negativeUpdate", dataProviderClass = PlayerUpdateDataProvider.class)
    public void testPlayerUpdateNegative(String description,
                                         String editor,
                                         String roleToCreate,
                                         PlayerUpdateRequestDto request,
                                         int expectedCode) {

        AllureHelper.addStep("Negative case: " + description);

        PlayerCreateResponseDto target;

        if ("user".equals(editor) && "user".equals(roleToCreate)) {
            PlayerCreateResponseDto userA = TestDataHelper.createPlayer(CREATOR, roleToCreate);
            PlayerCreateResponseDto userB = TestDataHelper.createPlayer(CREATOR, roleToCreate);
            editor = userA.getLogin();
            target = userB;
            TestDataHelper.registerForCleanup(userA);
            TestDataHelper.registerForCleanup(userB);
        } else {
            target = TestDataHelper.createPlayer(CREATOR, roleToCreate);
            TestDataHelper.registerForCleanup(target);
        }

        Response resp = client.updatePlayer(editor, target.getId(), request);

        Assert.assertEquals(resp.getStatusCode(), expectedCode,
                String.format("Expected %d for '%s'", expectedCode, description));

        AllureHelper.attachText("Response", resp.asPrettyString());
    }

    // Duplicate cases
    @Test(dataProvider = "duplicateUpdate", dataProviderClass = PlayerUpdateDataProvider.class)
    public void testPlayerUpdateDuplicate(String description,
                                          String editor,
                                          String roleToCreate,
                                          String duplicateField,
                                          int expectedCode,
                                          PlayerUpdateRequestDto request) {

        AllureHelper.addStep("Duplicate scenario: " + description);

        PlayerCreateResponseDto duplicate = TestDataHelper.createPlayer(CREATOR, roleToCreate);
        TestDataHelper.registerForCleanup(duplicate);

        PlayerCreateResponseDto target = TestDataHelper.createPlayer(CREATOR, roleToCreate);
        TestDataHelper.registerForCleanup(target);

        PlayerUpdateRequestDto.Builder builder = new PlayerUpdateRequestDto.Builder()
                .age(request.getAge())
                .gender(request.getGender())
                .role(request.getRole())
                .password(request.getPassword())
                .login(request.getLogin())
                .screenName(request.getScreenName());

        if ("login".equals(duplicateField)) {
            builder.login(duplicate.getLogin());
        } else if ("screenName".equals(duplicateField)) {
            builder.screenName(duplicate.getScreenName());
        }

        PlayerUpdateRequestDto updateRequest = builder.build();

        Response resp = client.updatePlayer(editor, target.getId(), updateRequest);

        Assert.assertEquals(resp.getStatusCode(), expectedCode,
                String.format("Expected %d for duplicate %s", expectedCode, duplicateField));

        AllureHelper.attachText("Response", resp.asPrettyString());

        AllureHelper.addStep("Validating that original player data remains unchanged after 409");

        Response getResponse = client.getPlayerById(
                new dto.request.PlayerGetByPlayerIdRequestDto.Builder()
                        .playerId(target.getId())
                        .build()
        );

        Assert.assertEquals(getResponse.getStatusCode(), 200, "GET player should return 200 after failed update");

        PlayerCreateResponseDto afterConflict = getResponse.as(PlayerCreateResponseDto.class);

        SoftAssert soft = new SoftAssert();

        soft.assertEquals(afterConflict.getLogin(), target.getLogin(), "Login should remain unchanged after 409");
        soft.assertEquals(afterConflict.getScreenName(), target.getScreenName(), "ScreenName should remain unchanged after 409");
        soft.assertEquals(afterConflict.getGender(), target.getGender(), "Gender should remain unchanged after 409");
        soft.assertEquals(afterConflict.getRole(), target.getRole(), "Role should remain unchanged after 409");
        soft.assertEquals(afterConflict.getAge(), target.getAge(), "Age should remain unchanged after 409");
        soft.assertEquals(afterConflict.getId(), target.getId(), "ID should remain unchanged");

        soft.assertAll();

        AllureHelper.attachText("Player state after 409", getResponse.asPrettyString());
    }
}
