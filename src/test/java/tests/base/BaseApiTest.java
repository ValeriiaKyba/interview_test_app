package tests.base;

import clients.PlayerClient;
import helpers.AllureHelper;
import helpers.TestDataHelper;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    protected PlayerClient client;

    @BeforeClass(alwaysRun = true)
    public void setUpBase() {
        AllureHelper.addStep("Initializing API clients and test context");
        client = new PlayerClient();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAfterTest(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        AllureHelper.addStep("Cleanup after test: " + testName);

        TestDataHelper.cleanupAll();
    }
}
