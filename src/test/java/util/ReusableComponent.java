package util;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class ReusableComponent{

    public static WebDriver driver;
    public WebDriverWait wait;
    public PropertyReader propertyReader;
    public ExtentTest extentTest;
    public ReusableComponent(WebDriver driver, WebDriverWait wait, PropertyReader propertyReader, ExtentTest extentTest){
        this.driver = driver;
        this.propertyReader = propertyReader;
        this.extentTest = extentTest;
    }

    /**
     * It will return the locator type during run time based on locator supply.
     *
     * @param locator
     * @return
     */
    public By ByLocator(String locator) {
        By result = null;

        if (locator.startsWith("//")) {
            result = By.xpath(locator);
        } else if (locator.startsWith("/")) {
            result = By.xpath(locator);
        } else if (locator.startsWith("#")) {
            result = By.name(locator.replace("#", ""));
        }else if (locator.startsWith("class=")) {
            result = By.className(locator.replace("class=", ""));
        } else if (locator.startsWith("name=")) {
            result = By.name(locator.replace("name=", ""));
        }else if (locator.startsWith("id=")) {
            result = By.id(locator.replace("id=", ""));
        } else if (locator.startsWith("(")) {
            result = By.xpath(locator);
        } else {

            result = By.id(locator);
        }

        return result;
    }
    public void captureScreenshot(String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);

            // This will attach the screenshot to the Extent Report and embed it
            extentTest.log(Status.FAIL, "Test Failed: " + testName,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(
                            Base64.getEncoder().encodeToString(screenshotBytes)).build()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait till given time to visible element on screen.
     *
     * @param locator
     * @param timeout
     */
    @Deprecated
    public void waitForElementToVisible(String locator, int timeout){

            new WebDriverWait(driver,timeout).until(ExpectedConditions.visibilityOf(driver.findElement(ByLocator(locator))));
    }

    @Deprecated
    public void waitForElementsToVisible(String locator, int timeout){

            new WebDriverWait(driver,timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByLocator(locator)));

    }

    /**
     * Check if element present or not. Return boolean value.
     *
     * @param locator
     * @return
     */
    public boolean isElementPresent(String locator) {
        Boolean result = false;
        try {
            driver.findElement(ByLocator(locator));
            result = true;
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * Wait for element present
     *
     * @param locator
     * @param timeout
     */
    public void WaitForElementPresent(String locator, int timeout) {
        for (int i = 0; i < timeout; i++) {
            if (isElementPresent(locator)) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Perform click operation on given locator.
     *
     * @param locator
     */
    public void clickOn(String locator){
        WaitForElementPresent(locator, 30);
        Assert.assertTrue(isElementPresent(locator), "Element Locator :"
                + locator + " Not found");
        WebElement el = driver.findElement(ByLocator(locator));
        el.click();
    }

    /**
     * Enter text into given text field locator.
     *
     * @param locator
     * @param text
     */
    public void sendKeys(String locator, String text) {
        WaitForElementPresent(locator, 30);
        Assert.assertTrue(isElementPresent(locator), "Element Locator :"
                + locator + " Not found");
        WebElement el = driver.findElement(ByLocator(locator));
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Store text from a locator
     *
     * @param locator
     * @return
     */
    public String getText(String locator) {
        String text="";
        try {
            WaitForElementPresent(locator, 8);
            if (isElementPresent(locator)) {
                text = driver.findElement(ByLocator(locator)).getText();
            }else{
                attachScreenshotToReport(extentTest,"Element Locator :" + locator + " Not found","negative" );
                //Assert.assertTrue(isElementPresent(locator), "Element Locator :" + locator + " Not found");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public void attachScreenshotToReport(ExtentTest extentTest, String stepLog, String commentType){
        try {
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
                if (commentType.toLowerCase().equalsIgnoreCase("negative")) {
                //    extentTest.fail(stepLog).addScreenCaptureFromBase64String(new String(screenshot+commentType+getCurrentDate().replaceAll(" ","_")));
                    extentTest.log(Status.FAIL, "Test Failed with reason -->> '" + stepLog+"'",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(
                                    Base64.getEncoder().encodeToString(screenshot)).build());
                } else if ((commentType.toLowerCase().equalsIgnoreCase("positive"))){

               //     extentTest.pass(stepLog).addScreenCaptureFromBase64String(new String(screenshot+commentType+getCurrentDate().replaceAll(" ","_")));
                    extentTest.log(Status.PASS, "'" + stepLog+"'",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(
                                    Base64.getEncoder().encodeToString(screenshot)).build());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentDate() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        // Format the current date
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    public void assertEqual(String locator,String str_value) {
        try {

            if (isElementPresent(locator)) {
                if (isTextPresent(locator, str_value)) {

                    extentTest.pass("'"+str_value+"' is present");

                } else {

                    attachScreenshotToReport(extentTest,"Expected :'" + str_value + "' BUT FOUND:'" + getText(locator) + "'","negative" );

                    Assert.assertEquals(getText(locator), str_value,
                            "Expected :'" + str_value + "' BUT FOUND:'" + getText(locator) + "'");
                }
            }else {

                attachScreenshotToReport(extentTest,locator+" is not present on the screen.","negative" );

                Assert.fail();
            }

        } catch (Exception e) {
            extentTest.fail("Exception occurred: "+e.getMessage());
            Assert.fail();
        }
    }

    public boolean isTextPresent(String locator, String str_text_to_compare) {

        boolean status = false;
         try {
                    WebElement ele_text = driver.findElement(ByLocator(locator));
                    String text = ele_text.getText().trim();

                    if (text.equalsIgnoreCase(str_text_to_compare)) {

                        status = true;
                    }

                } catch (Exception e) {
                    attachScreenshotToReport(extentTest,"Exception Occurred: "+e.getMessage(),"negative" );
                    Assert.fail();
                }

        return status;

    }

    // Function to extract keywords from a string
    public List<String> extractKeywords(String input) {
        // Split the input string into words
        String[] words = input.split("\\s+");

        // Define a set to store unique keywords
        Set<String> keywords = new HashSet<>();

        // Define a set of common stop words to be ignored (customize as needed)
        Set<String> stopWords = new HashSet<>(Arrays.asList("is", "a", "the", "and", "in", "of"));

        // Iterate through words and add non-stop words to the keywords set
        for (String word : words) {
            String cleanedWord = word.toLowerCase().replaceAll("[^a-zA-Z]", ""); // Remove non-alphabetic characters
            if (!stopWords.contains(cleanedWord)) {
                keywords.add(cleanedWord);
            }
        }

        // Convert the set of keywords back to a list
        return new ArrayList<>(keywords);
    }

    // Function to calculate relevance based on the number of common keywords
    public double calculateRelevance(List<String> keywords1, List<String> keywords2) {
        Set<String> commonKeywords = new HashSet<>(keywords1);
        commonKeywords.retainAll(keywords2); // Retain only common keywords

        // Calculate relevance as the ratio of common keywords to total keywords
        double relevance;
        if (keywords1.size() + keywords2.size() > 0) {
            relevance = (double) commonKeywords.size() / (keywords1.size() + keywords2.size());
        } else {
            relevance = 0.0; // Handle the case of empty input strings
        }

        return relevance;
    }

}
