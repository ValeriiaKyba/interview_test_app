package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

public class AllureHelper {

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String content) {
        return content;
    }

    @Attachment(value = "{name}", type = "application/json")
    public static String attachJson(String name, String json) {
        return json;
    }

    @Attachment(value = "CURL", type = "text/plain")
    public static String attachCurl(String curlCommand) {
        return curlCommand;
    }

    public static void addStep(String stepDescription) {
        Allure.step(stepDescription);
    }
}
