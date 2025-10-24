package helpers;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CurlHelper implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        logStep("Sending " + requestSpec.getMethod() + " request to: " + requestSpec.getURI());

        String curl = buildCurl(requestSpec);
        attachText("CURL Command", curl);

        if (requestSpec.getBody() != null) {
            attachJson("Request Body", requestSpec.getBody().toString());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        logStep("Received response with status: " + response.getStatusCode());
        attachJson("Response Body", response.asPrettyString());

        return response;
    }

    private String buildCurl(FilterableRequestSpecification requestSpec) {
        StringBuilder curl = new StringBuilder("curl -X ")
                .append(requestSpec.getMethod())
                .append(" '")
                .append(requestSpec.getURI())
                .append("'");

        requestSpec.getHeaders().forEach(header ->
                curl.append(" -H '")
                        .append(header.getName())
                        .append(": ")
                        .append(header.getValue())
                        .append("'")
        );

        if (requestSpec.getBody() != null) {
            String body = requestSpec.getBody().toString().replace("'", "\\'");
            curl.append(" -d '").append(body).append("'");
        }

        return curl.toString();
    }

    @Attachment(value = "{name}", type = "text/plain")
    private String attachText(String name, String content) {
        return content;
    }

    @Attachment(value = "{name}", type = "application/json")
    private String attachJson(String name, String content) {
        return content;
    }

    @Step("{step}")
    private void logStep(String step) {
    }
}
