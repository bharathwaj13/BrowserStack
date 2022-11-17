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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.Constants;

public class ChromeTests2 {

	public WebDriver driver;
	public JavascriptExecutor js;

	@BeforeTest
	public void startUp() throws MalformedURLException {
		/*
		 * ChromeOptions co = new ChromeOptions(); co.addArguments("disable-infobars");
		 * co.addArguments("start-maximized");
		 * co.addArguments("--disable-notifications");
		 * co.setExperimentalOption("excludeSwitches",
		 * Arrays.asList("disable-popup-blocking"));
		 */
		MutableCapabilities capabilities = new MutableCapabilities();
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("browserVersion", "102.0");
		HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
		browserstackOptions.put("platformName", "Windows");
		browserstackOptions.put("osVersion", "11");
		browserstackOptions.put("projectName", "Learn BrowserStack");
		browserstackOptions.put("buildName", "learn_1.0.0");
		browserstackOptions.put("sessionName", "EPAM Website in Chrome");
		browserstackOptions.put("buildTag", "reg");
		capabilities.setCapability("bstack:options", browserstackOptions);
		// capabilities.setCapability(ChromeOptions.CAPABILITY, co);
		driver = new RemoteWebDriver(new URL(Constants.URL), capabilities);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Setting name of the test
		js = (JavascriptExecutor) driver;
		js.executeScript(
				"browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"Test EPAM in Chrome \" }}");

	}

	// Negative Test - So changing Title contains() check to equals()

	@Test
	public void launchEPAM() {
		driver.get("https://www.epam.com/");
		System.out.println(driver.getTitle());
		try {
			Assert.assertTrue(driver.getTitle().equals("EPAM"));
		} catch (AssertionError e) {
			js.executeScript(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Title not matched!\"}}");
			throw new RuntimeException(e);
		}
		js.executeScript(
				"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Title matched!\"}}");

	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
