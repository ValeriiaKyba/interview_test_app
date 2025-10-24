package utils;

import helpers.CurlHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static utils.ConfigProvider.configuration;
import static utils.ConfigProvider.getBaseUrl;

public class ApiConfig {

    public static RequestSpecification requestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new CurlHelper());

        if (configuration().getProperty("logging.enabled").equals("true")) {
            builder.addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter());
        }

        return builder.build();
    }
}
