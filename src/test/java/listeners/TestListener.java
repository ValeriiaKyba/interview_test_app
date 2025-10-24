package listeners;

import helpers.AllureHelper;
import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        AllureHelper.addStep("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        AllureHelper.addStep("Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        AllureHelper.addStep("Test failed: " + result.getMethod().getMethodName());

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            attachText("Failure reason", throwable.getMessage());
        }

        Object responseBody = result.getAttribute("responseBody");
        if (responseBody != null) {
            attachJson("Failed Response Body", responseBody.toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        AllureHelper.addStep("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        AllureHelper.addStep("Starting test suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        AllureHelper.addStep("Finished suite: " + context.getName());
    }

    @Attachment(value = "{name}", type = "text/plain")
    private String attachText(String name, String content) {
        return content;
    }

    @Attachment(value = "{name}", type = "application/json")
    private String attachJson(String name, String content) {
        return content;
    }
}
