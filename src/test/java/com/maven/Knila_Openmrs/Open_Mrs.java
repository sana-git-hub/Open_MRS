package com.maven.Knila_Openmrs;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Open_Mrs {
	public static WebDriver driver;
	org.apache.logging.log4j.Logger log = LogManager.getLogger(Open_Mrs.class);

	@Test(priority = 0)
	public void browser_Launch() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\GOODLUCK\\eclipse-workspace\\Knila_Openmrs\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		log.info("Browser Launch");
	}

	@Test(enabled = false)
	public static void screenshot(String name) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File(
				"C:\\Users\\GOODLUCK\\eclipse-workspace\\Knila_Openmrs\\ScreenshotsOpenmrs\\" + name + ".png");
		FileUtils.copyFile(source, dest);
	}

	@Test(priority = 1)
	public void login() throws IOException {

		// Redirecting to openmrs url and logging in with the credentials

		driver.get("https://qa-refapp.openmrs.org/openmrs/login.htm");
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("Admin");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("Admin123");
		screenshot("Testcase 01");
		driver.findElement(By.id("Outpatient Clinic")).click();
		driver.findElement(By.id("loginButton")).click();
		screenshot("Testcase 02");
		log.info("Login successful");
		// Verifying dashboard page redirection using Assertion

		String expTitle = "Home";
		String actTitle = driver.getTitle();
		Assert.assertEquals(actTitle, expTitle);
		System.out.println("");
	}

	@Test(priority = 2)
	public void patient_Register() throws InterruptedException, IOException {
		driver.findElement(By.xpath("//a[contains(@id,'referenceapplication-registrationapp')]")).click();
		screenshot("Testcase 05");

		// Entering the patient details

		String firstName = "Sarah";
		driver.findElement(By.name("givenName")).sendKeys(firstName);
		String familyName = "Lawrence";
		driver.findElement(By.name("familyName")).sendKeys(familyName);
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		WebElement gender = driver.findElement(By.name("gender"));
		Select s = new Select(gender);
		s.selectByValue("F");
		WebElement gender1 = driver.findElement(By.xpath("//select/option[@value=\"F\"]"));
		String fem = gender1.getText();
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		String birthDay = "13";
		driver.findElement(By.name("birthdateDay")).sendKeys(birthDay);
		WebElement birthMon = driver.findElement(By.name("birthdateMonth"));
		Select s1 = new Select(birthMon);
		s1.selectByValue("3");
		WebElement month = driver.findElement(By.xpath("//select[@id=\"birthdateMonth-field\"]/option[@value=\"3\"]"));
		String birthMonth = month.getText();
		String birthYear = "2019";
		driver.findElement(By.name("birthdateYear")).sendKeys(birthYear);
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		String address1 = "10Ram Nagar";
		driver.findElement(By.id("address1")).sendKeys(address1);
		String address2 = "Chennai";
		driver.findElement(By.id("cityVillage")).sendKeys(address2);
		String state = "TN";
		driver.findElement(By.id("stateProvince")).sendKeys(state);
		String country = "India";
		driver.findElement(By.id("country")).sendKeys(country);
		String postal = "638004";
		driver.findElement(By.id("postalCode")).sendKeys(postal);
		screenshot("Testcase 06");
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		String phoneNum = "8776940031";
		driver.findElement(By.name("phoneNumber")).sendKeys(phoneNum);
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		WebElement nameAssert = driver.findElement(By.xpath("(//div[@id=\"dataCanvas\"]//child::p)[1]"));
		WebElement genderAssert = driver.findElement(By.xpath("(//div[@id=\"dataCanvas\"]//child::p)[2]"));
		WebElement birthAssert = driver.findElement(By.xpath("(//div[@id=\"dataCanvas\"]//child::p)[3]"));
		WebElement addressAssert = driver.findElement(By.xpath("(//div[@id=\"dataCanvas\"]//child::p)[4]"));
		WebElement phoneNumAssert = driver.findElement(By.xpath("(//div[@id=\"dataCanvas\"]//child::p)[5]"));
		screenshot("Testcase 07");
		log.info("Patient details entered");

		// Verifying patient details using Assertion

		Boolean result = false;
		if (nameAssert.getText().contains(firstName) && nameAssert.getText().contains(familyName)
				&& genderAssert.getText().contains(fem) && birthAssert.getText().contains(birthDay)
				&& birthAssert.getText().contains(birthMonth) && birthAssert.getText().contains(birthYear)
				&& addressAssert.getText().contains(address1) && addressAssert.getText().contains(address2)
				&& addressAssert.getText().contains(state) && addressAssert.getText().contains(country)
				&& addressAssert.getText().contains(postal) && phoneNumAssert.getText().contains(phoneNum)) {
			result = true;
		}
		Assert.assertTrue(result);
		driver.findElement(By.id("submit")).click();

		// Verifying the patient details page redirection using Assertion

		screenshot("Testcase 08");
		String patientPageExp = "OpenMRS Electronic Medical Record";
		String patientPageAct = driver.getTitle();
		Assert.assertEquals(patientPageAct, patientPageExp);

		// Verifying the age with given date of birth using Assertion

		int currentYear = 2023;
		int intYear = Integer.parseInt(birthYear);
		int ageActual = currentYear - intYear;
		WebElement age1 = driver.findElement(By.xpath("(//div[@class=\"gender-age col-auto\"]//span)[2]"));
		String ageString = age1.getText().substring(0, 2).replaceAll("[^0-9]", "");
		int ageExpected = Integer.parseInt(ageString);
		Assert.assertEquals(ageActual, ageExpected);
		log.info("Patient age calculated");
	}

	@Test(priority = 3)
	public void visit() throws AWTException, InterruptedException, IOException {
		driver.findElement(By.xpath(
				"//div[@id=\"edit-patient-identifier-dialog\"]//following-sibling::div[@class=\"dashboard clear row\"]//descendant::div[contains(text(),\"Start Visit\")]"))
				.click();
		driver.findElement(By.xpath("//button[@id=\"start-visit-with-visittype-confirm\"]")).click();
		screenshot("Testcase 09");
		driver.findElement(By.xpath("//div[@class=\"visit-actions active-visit\"]//a[contains(@id,'attachments')]"))
				.click();
		WebElement fileDrop = driver.findElement(By.xpath(
				"//div[@class=\"att_upload-container\"]//descendant::form[@id=\"visit-documents-dropzone\"]//descendant::div[@class=\"dz-default dz-message ng-binding\"]"));
		WebDriverWait wb = new WebDriverWait(driver, 10);
		wb.until(ExpectedConditions.visibilityOf(fileDrop));
		Actions ac = new Actions(driver);
		ac.click(fileDrop).build().perform();
		Thread.sleep(5000);
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_D);
		r.keyRelease(KeyEvent.VK_D);
		Thread.sleep(5000);
		r.keyPress(KeyEvent.VK_DOWN);
		r.keyRelease(KeyEvent.VK_DOWN);
		Thread.sleep(4000);
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(4000);
		r.keyPress(KeyEvent.VK_A);
		r.keyRelease(KeyEvent.VK_A);
		Thread.sleep(4000);
		r.keyPress(KeyEvent.VK_DOWN);
		r.keyRelease(KeyEvent.VK_DOWN);
		Thread.sleep(4000);
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(4000);
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(4000);
		screenshot("Testcase 10");
		driver.findElement(By.xpath("//textarea[@placeholder=\"Enter a caption\"]")).sendKeys("Patient test");
		driver.findElement(By.xpath("//button[@class=\"confirm ng-binding\"]")).click();
		screenshot("Testcase 11");
		log.info("Attachement uploaded");

		// Verifying the toaster message of file attachment

		WebElement toast1 = driver.findElement(By.xpath("//div[@class=\"toast-container toast-position-top-right\"]"));
		wb.until(ExpectedConditions.visibilityOf(toast1));
		if (toast1.isDisplayed()) {
			System.out.println("Testcase 11 : File attachment toaster message appeared");
		}
		driver.findElement(By.xpath("//a[contains(text(),'Sarah Lawrence')]")).click();
		screenshot("Testcase 12");

		// Verifying if attachment section has attachment

		WebElement thumbnail = driver.findElement(By.xpath("//div[@class=\"att_thumbnails-container\"]"));
		if (thumbnail.isDisplayed()) {
			System.out.println("Testcase 13 : Attachment section has attachment.");
		}

		// Verifying if recent Visit has current date with Attachment Upload tag

		DateFormat df = new SimpleDateFormat("dd.MMMM.yyyy");
		Date d = new Date();
		String currentDate = df.format(d);
		WebElement attachmentUpload = driver
				.findElement(By.xpath("//a[contains(@href,'/openmrs/coreapps/patientdashboard/patientDashboard')]"));
		WebElement date = driver
				.findElement(By.xpath("//div[contains(text(),'Attachment Upload')]//preceding-sibling::a"));
		if (attachmentUpload.isDisplayed() == true && date.getText().equals(currentDate)) {
			System.out.println("Testcase 14 : Date and attachment upload is present.");
		}
	}

	@Test(priority = 4)
	public void capture_Vitals() throws IOException {

		// Visit again and enter height weight in vitals page

		driver.findElement(
				By.xpath("//div[@class=\"col-12 col-lg-3 p-0\"]//descendant::div[contains(text(),'End Visit')]"))
				.click();
		screenshot("Testcase 15");
		driver.findElement(By.xpath(
				"//input[@id=\"visitId\"]//parent::div[@class=\"dialog-content\"]//descendant::button[text()='Yes']"))
				.click();
		driver.findElement(By.xpath(
				"//div[@id=\"edit-patient-identifier-dialog\"]//following-sibling::div[@class=\"dashboard clear row\"]//descendant::div[contains(text(),\"Start Visit\")]"))
				.click();
		driver.findElement(By.xpath("//button[@id=\"start-visit-with-visittype-confirm\"]")).click();
		driver.findElement(By.xpath("//a[contains(@id,'realTime.vitals')]")).click();
		screenshot("Testcase 16");
		driver.findElement(By.id("w8")).sendKeys("163");
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		driver.findElement(By.id("w10")).sendKeys("57");
		driver.findElement(By.xpath("//button[@id=\"next-button\"]")).click();
		screenshot("Testcase 17");
		log.info("Patient vital details given");

		// Testcase 17 - BMI calculation is not available in the vitals page

		driver.findElement(By.xpath("//a[@id=\"save-form\"]")).click();
		driver.findElement(By.xpath("//button[contains(text(),'Save')]")).click();
		driver.findElement(
				By.xpath("(//div[@id=\"visit-details\"]//descendant::a[@class=\"button task activelist\"])[1]"))
				.click();
		driver.findElement(
				By.xpath("//div[@id=\"simplemodal-container\"]//descendant::button[@class=\"confirm right\"]")).click();
		screenshot("Testcase 18");
		WebElement patientDetails = driver.findElement(By.xpath(
				"//div[@id=\"delete-patient-creation-dialog\"]//following-sibling::div[@class=\"patient-header row \"]//descendant::span[@class=\"PersonName-givenName\"]"));
		WebDriverWait wb = new WebDriverWait(driver, 10);
		wb.until(ExpectedConditions.visibilityOf(patientDetails));
		Actions ac = new Actions(driver);
		ac.moveToElement(patientDetails).click().build().perform();
		screenshot("Testcase 19");

		// Verifying if recent Visit has one more new entry for current date with Vitals
		// tag

		DateFormat df = new SimpleDateFormat("dd.MMMM.yyyy");
		Date d = new Date();
		String currentDate = df.format(d);
		WebElement attachmentUpload = driver.findElement(
				By.xpath("(//a[contains(@href,'/openmrs/coreapps/patientdashboard/patientDashboard')])[1]"));
		WebElement date = driver.findElement(By.xpath("//div[contains(text(),'Vitals')]//preceding-sibling::a"));
		if (attachmentUpload.isDisplayed() == true && date.getText().equals(currentDate)) {
			System.out.println("Testcase 21 : Vitals entry Date and attachment upload is present.");
		}
		driver.findElement(By.xpath("//a[contains(@id,'mergeVisits')]")).click();
		WebElement data1 = driver.findElement(
				By.xpath("//td[contains(text(),'Vitals')]//preceding-sibling::td/input[@type=\"checkbox\"]"));
		data1.click();
		WebElement data2 = driver.findElement(
				By.xpath("//td[contains(text(),'Attachment')]//preceding-sibling::td/input[@type=\"checkbox\"]"));
		data2.click();
		screenshot("Testcase 22");
		log.info("visit merged");

		driver.findElement(By.id("mergeVisitsBtn")).click();
		driver.findElement(By.xpath("//input[@class=\"cancel\"]")).click();
		List<WebElement> entries = driver.findElements(By.xpath(
				"//h3[text()='Recent Visits']//parent::div[@class=\"info-header\"]//following-sibling::div//child::ul/li"));
		if (entries.size() == 1) {
			System.out.println("Testcase 24 : Resent visit has only 1 entry");
		}
	}

	@Test(priority = 5)
	public void addPastAndDeleteVisit() throws IOException {
		driver.findElement(By.xpath("//div[contains(text(),'Add Past Visit')]")).click();

		// Verifying is the future date is not clickable in the date picker

		boolean dayBoolean = false;
		List<WebElement> days = driver.findElements(
				By.xpath("(//div[@class=\"datetimepicker-days\"])[1]//descendant::td[@class=\"day disabled\"]"));
		int daysSize = days.size();
		int count = 0;
		for (WebElement day : days) {
			if (day.getAttribute("class").equals("day disabled")) {
				count++;
			}
			if (daysSize == count) {
				dayBoolean = true;
			}
		}
		Assert.assertTrue(dayBoolean);
		screenshot("Testcase 25");
		log.info("Future dates are disabled");

		// Deleting patient details

		driver.findElement(
				By.xpath("//div[@id=\"retrospective-visit-creation-dialog\"]//descendant::button[@class=\"cancel\"]"))
				.click();
		driver.findElement(By.xpath("//div[contains(text(),'Delete Patient')]")).click();
		driver.findElement(By.xpath("//input[@id=\"delete-reason\"]")).sendKeys("Testing the application");
		screenshot("Testcase 27");
		log.info("user deleted");

		driver.findElement(
				By.xpath("//input[@id=\"delete-reason\"]//parent::div//child::button[@class=\"confirm right\"]"))
				.click();

		// Verifying the delete patient toaster message

		WebElement toast1 = driver.findElement(By.xpath("//div[@class=\"toast-container toast-position-top-right\"]"));
		if (toast1.isDisplayed()) {
			System.out.println("Testcase 28 : Patient detail is deleted toaster message appeared");
		}

		// Verifying if the patient record is deleted or not

		driver.findElement(By.xpath("//input[@id=\"patient-search\"]")).sendKeys("Sarah");
		String deleteRecord = driver.findElement(By.xpath("//td[@class=\"dataTables_empty\"]")).getText();
		if (deleteRecord.equalsIgnoreCase("No matching records found")) {
			System.out.println("Testcase 29 : Patient record is deleted");
		}
		screenshot("Testcase 29");
		driver.close();
	}

}
