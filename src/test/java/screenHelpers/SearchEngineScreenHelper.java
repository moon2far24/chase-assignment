package screenHelpers;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import util.LocatorReader;
import util.PropertyReader;
import util.ReusableComponent;

import java.util.*;

public class SearchEngineScreenHelper extends ReusableComponent{

    private LocatorReader element;
    public SearchEngineScreenHelper(WebDriver driver, WebDriverWait wait, PropertyReader propertyReader, ExtentTest extentTest) {
        super(driver, wait, propertyReader, extentTest);

        element = new LocatorReader("web_locators.xml");
    }

    @Deprecated
    public void openSearchEngine(String url){

        try {
            // Open a new tab using JavaScript
            ((JavascriptExecutor) driver).executeScript("window.open('', '_blank');");

            // Switch to the new tab
            String originalTab = driver.getWindowHandle();
            for (String newTab : driver.getWindowHandles()) {
                if (!newTab.equals(originalTab)) {
                    driver.switchTo().window(newTab);
                    break;
                }
            }
            //open google
            driver.get(propertyReader.readApplicationFile(url));
        } catch (Exception e) {
            e.printStackTrace();
            attachScreenshotToReport(extentTest,"Exception Occurred: "+e.getMessage(),"negative");
        }


    }

    @Deprecated
    public void verifySearchEnginePageTitle(String str_pageTitle){

        try {
            if (driver.getTitle().equalsIgnoreCase(propertyReader.readApplicationFile(str_pageTitle))){

                attachScreenshotToReport(extentTest,"Successfully navigated to google","positive");

            }else {

                attachScreenshotToReport(extentTest,"Something went wrong... page title doesn't matched. EXPECTED: "+str_pageTitle+" BUT FOUND: "+driver.getTitle(),"positive");

            }

        } catch (Exception e) {
            attachScreenshotToReport(extentTest,"Exception occurred "+e.getMessage(),"negative" );
        }

    }

    @Deprecated
    public void searchNewsHeadlineInToSearchEngine(String newHeadline){

        try {
            sendKeys(element.getLocator("GoogleLocators.SearchInputBox"),newHeadline);
            driver.findElement(ByLocator(element.getLocator("GoogleLocators.SearchInputBox"))).sendKeys(Keys.ENTER);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    public void verifyNavigationToResultScreen(){

        waitForElementsToVisible(element.getLocator("GoogleLocators.NewsParentBlockList"),20);
        if (isElementPresent(element.getLocator("GoogleLocators.NewsParentBlockList"))){

            attachScreenshotToReport(extentTest,"Successfully landed on result screen","positive");

        }else {
            attachScreenshotToReport(extentTest,"Something went wrong.. result not present.","negative");
            Assert.fail("Something went wrong.. result not present.");
        }

    }

    public void validateNewsOnGoogleSearch(String str_news){

        try {
            List<WebElement> parent_search_list = driver.findElements(ByLocator(element.getLocator("GoogleLocators.NewsParentBlockList")));
            int maxSize = parent_search_list.size();
            System.out.println("total result found  -->> "+maxSize);
            List<String> news_list = new ArrayList<String>();
            int counter=0;
            for (int i=0; i<maxSize-3; i++){
               String avoidNewsSiteName =  driver.findElements(ByLocator(element.getLocator("GoogleLocators.NewsDomainNameList"))).get(i).getText().trim();
                String avoidNewsSiteURL =  driver.findElements(ByLocator(element.getLocator("GoogleLocators.NewsDomainURLList"))).get(i).getText().trim();
                System.out.println("News site name -->> "+avoidNewsSiteName+" and new site URL -->> "+avoidNewsSiteURL);
                if (!avoidNewsSiteName.contains("Guardian") || !avoidNewsSiteURL.contains("https://www.theguardian.com")) {
                    String newsHeadLineFromGoogle = driver.findElements(ByLocator(element.getLocator("GoogleLocators.NewsHeadLineList"))).get(i).getText().trim();
                    System.out.println("News headline from google -->> " + newsHeadLineFromGoogle);

                    //getting relevance score b/w theGuardian news and the news listed on google.com
                    double relevanceScore =  calculateRelevance(extractKeywords(str_news),extractKeywords(newsHeadLineFromGoogle));
                    System.out.println("Relevance score for search "+i+" is"+relevanceScore);
                    if (relevanceScore > 0.1){
                        news_list.add(newsHeadLineFromGoogle);
                            counter++;
                    }
                    if(counter == 2){
                        attachScreenshotToReport(extentTest,"Found two relevant news on google, which are news 1 from site :"+avoidNewsSiteName+" and site address --> "+avoidNewsSiteURL+" and container news headlines like -->> '"+news_list.get(0)+"' and '"+news_list.get(1),"positive");
                        break;
                    }
                }
            }
            System.out.println("Counter -->> "+counter);
            if (counter < 2){
                attachScreenshotToReport(extentTest,"No relevant news found on google.com","negative");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //</editor-fold>






}
