package CustomListeners;

import org.testng.*;

public class TestListeners implements ITestListener  {

    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName()+" TC : passed");
    }

    public void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName()+" TC : failed");
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName()+" TC : skipped");
    }

}
