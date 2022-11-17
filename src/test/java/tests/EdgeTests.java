package tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.Constants;



public class EdgeTests {

	public WebDriver driver;
	public JavascriptExecutor js;

	@BeforeTest
	public void startUp() throws MalformedURLException {
		EdgeOptions co = new EdgeOptions();
		co.addArguments("disable-infobars");
		co.addArguments("start-maximized");
		co.addArguments("--disable-notifications");
		co.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
		driver = new EdgeDriver(co);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		MutableCapabilities capabilities = new MutableCapabilities();
		capabilities.setCapability("browserName", "edge");
		capabilities.setCapability("browserVersion", "107.0");
		HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
		browserstackOptions.put("platformName", "Windows");
		browserstackOptions.put("osVersion", "11");
		browserstackOptions.put("projectName", "Learn BrowserStack");
		browserstackOptions.put("buildName", "learn_1.0.0");
		browserstackOptions.put("sessionName", "EPAM in Edge");
		browserstackOptions.put("buildTag", "reg");
		capabilities.setCapability("bstack:options", browserstackOptions);
		
		capabilities.setCapability(ChromeOptions.CAPABILITY, co);
		WebDriver driver = new RemoteWebDriver(new URL(Constants.URL), capabilities);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Setting name of the test
		js = (JavascriptExecutor) driver;
		js.executeScript(
				"browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"Test EPAM in Edge \" }}");
	}

	@Test
	public void searchEPAMInEdge() {
		driver.get("https://www.google.com/");
		System.out.println(driver.getTitle());
		try {
			Assert.assertTrue(driver.getTitle().equals("Google"));
		} catch (AssertionError e) {
			js.executeScript(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Title not matched!\"}}");
		}
		
		js.executeScript(
				"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Title matched!\"}}");
		driver.findElement(By.xpath("//input[@title='Search']")).sendKeys("EPAM", Keys.ENTER);
		String url = driver
				.findElement(By.xpath("//h3[contains(text(),'EPAM | Enterprise Software Development')]/parent::a"))
				.getAttribute("href");
		try {
			Assert.assertTrue(url.equals("https://www.epam.com/"));
		} catch (AssertionError e) {
			js.executeScript(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"EPAM url is incorrect!\"}}");
		}
		js.executeScript(
				"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"EPAM url is correct!\"}}");
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
