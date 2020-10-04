package ExtentReportScreenshot.ExtentReportScreenshot;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



public class FreeCRMTest {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;

	
	
	@BeforeTest
	public void setExtent(){
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html", true);
		//initializing the ExtentReports class
		//Where exactly you want to generate your report
		//user.dir-what is your project directory
		//true--replace existing folder
		
		//define parameters
		extent.addSystemInfo("Host Name", "Ajita Mac");
		extent.addSystemInfo("User Name", "Ajita Lamsal");
		extent.addSystemInfo("Environment", "QA");
		
	}
	
	@AfterTest
	public void endReport(){
		extent.flush();//once all the test cases are done,close the connection with extent report after it is created
		extent.close();
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException{
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		//SimpleDateFormat() is the class in Java
		//in format method you have to pass object of date class.Date class is available in Java,new Date will give current date
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
	
	
	
	
	@BeforeMethod
	public void setup(){
		System.setProperty("webdriver.chrome.driver", "/Users/ajitalamsal/Downloads/chromedriver");
        driver = new ChromeDriver(); 
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("https://www.freecrm.com/");
		
	}
	
	
	
	@Test
	public void freeCrmTitleTest(){
		extentTest = extent.startTest("freeCrmTitleTest");//if you have 10 test cases,everytime you have to add this line
		String title = driver.getTitle();
		System.out.println(title);
		Assert.assertEquals(title,"#1 Free CRM customer relationship management software cloud");
	}
	
	@Test
	public void freemCRMLogoTest(){
		extentTest = extent.startTest("freemCRMLogoTest");
		boolean b = driver.findElement(By.xpath("//a[@title='free crm home' and @class='brand-']")).isDisplayed();
		Assert.assertTrue(b);
	}
	
	
	
	
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException{
		
		if(result.getStatus()==ITestResult.FAILURE){//only for failure test cases
			//Failure will result 2,in IT test result and TestNG,failure test cases are defined by 2
			//ITestResult claass is coming from TestNG, if the test pass,fail or skip,it will store in this class object
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName()); //to add name in extent report
			
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable()); //to add error/exception in extent report
			
			String screenshotPath = FreeCRMTest.getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //to add screenshot in extent report
			//extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath)); //to add screencast/video in extent report
		}
		else if(result.getStatus()==ITestResult.SKIP){
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());//for skip number is 3
		}
		else if(result.getStatus()==ITestResult.SUCCESS){
			extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());//for pass number is 1

		}
		
	
		extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
		driver.quit();
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
	


