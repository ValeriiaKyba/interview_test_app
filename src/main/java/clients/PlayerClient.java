package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PlayerCreateRequestDto;
import dto.request.PlayerDeleteRequestDto;
import dto.request.PlayerGetByPlayerIdRequestDto;
import dto.request.PlayerUpdateRequestDto;
import helpers.AllureHelper;
import io.qameta.allure.Step;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ApiConfig;

import static io.restassured.RestAssured.given;

public class PlayerClient {

    private static final String CREATE_PLAYER = "/player/create/{editor}";
    private static final String DELETE_PLAYER = "/player/delete/{editor}";
    private static final String GET_PLAYER_BY_ID = "/player/get";
    private static final String GET_ALL_PLAYERS = "/player/get/all";
    private static final String UPDATE_PLAYER = "/player/update/{editor}/{id}";

    @Step("Create player by editor: {editor}")
    public Response createPlayer(String editor, PlayerCreateRequestDto request) {
        RequestSpecification spec = given()
                .spec(ApiConfig.requestSpec())
                .pathParam("editor", editor);

        addIfNotNull(spec, "age", request.getAge());
        addIfNotNull(spec, "gender", request.getGender());
        addIfNotNull(spec, "login", request.getLogin());
        addIfNotNull(spec, "role", request.getRole());
        addIfNotNull(spec, "screenName", request.getScreenName());
        addIfNotNull(spec, "password", request.getPassword());

        AllureHelper.attachJson("Create Player Request", requestToJson(request));

        Response response = spec
                .when()
                .get(CREATE_PLAYER)
                .then()
                .log().ifValidationFails(LogDetail.ALL)
                .extract().response();

        AllureHelper.attachJson("Create Player Response", response.asPrettyString());
        return response;
    }

    @Step("Delete player by editor: {editor}")
    public Response deletePlayer(String editor, PlayerDeleteRequestDto request) {
        RequestSpecification spec = given()
                .spec(ApiConfig.requestSpec())
                .pathParam("editor", editor)
                .body(request);

        AllureHelper.attachJson("Delete Player Request", requestToJson(request));

        Response response = spec
                .when()
                .delete(DELETE_PLAYER)
                .then()
                .log().ifValidationFails(LogDetail.ALL)
                .extract().response();

        AllureHelper.attachJson("Delete Player Response", response.asPrettyString());
        return response;
    }

    @Step("Get player by ID")
    public Response getPlayerById(PlayerGetByPlayerIdRequestDto request) {
        RequestSpecification spec = given()
                .spec(ApiConfig.requestSpec())
                .body(request);

        AllureHelper.attachJson("Get Player By ID Request", requestToJson(request));

        Response response = spec
                .when()
                .post(GET_PLAYER_BY_ID)
                .then()
                .log().ifValidationFails(LogDetail.ALL)
                .extract().response();

        AllureHelper.attachJson("Get Player By ID Response", response.asPrettyString());
        return response;
    }

    @Step("Get all players")
    public Response getAllPlayers() {
        RequestSpecification spec = given().spec(ApiConfig.requestSpec());

        Response response = spec
                .when()
                .get(GET_ALL_PLAYERS)
                .then()
                .log().ifValidationFails(LogDetail.ALL)
                .extract().response();

        AllureHelper.attachJson("Get All Players Response", response.asPrettyString());
        return response;
    }

    @Step("Update player by editor: {editor}, id: {id}")
    public Response updatePlayer(String editor, Long id, PlayerUpdateRequestDto request) {
        RequestSpecification spec = given()
                .spec(ApiConfig.requestSpec())
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(request);

        AllureHelper.attachJson("Update Player Request", requestToJson(request));

        Response response = spec
                .when()
                .patch(UPDATE_PLAYER)
                .then()
                .log().ifValidationFails(LogDetail.ALL)
                .extract().response();

        AllureHelper.attachJson("Update Player Response", response.asPrettyString());
        return response;
    }

    private void addIfNotNull(RequestSpecification spec, String key, Object value) {
        if (value != null) {
            spec.queryParam(key, value);
        }
    }

    private String requestToJson(Object dto) {
        try {
            return new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dto);
        } catch (Exception e) {
            return "Unable to convert request to JSON: " + e.getMessage();
        }
    }
}
