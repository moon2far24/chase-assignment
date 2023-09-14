package screenHelpers;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import util.LocatorReader;
import util.PropertyReader;
import util.ReusableComponent;

import java.util.List;

public class NewsWebSiteScreenHelper extends ReusableComponent{

    private LocatorReader element;
    public String news_headlines = "";

    public NewsWebSiteScreenHelper(WebDriver driver, WebDriverWait wait, PropertyReader propertyReader, ExtentTest extentTest) {
        super(driver, wait, propertyReader, extentTest);
        element = new LocatorReader("web_locators.xml");
    }

    @Deprecated
    public void openApplicationAndWaitForPageLoad(String str_url){

        try{

            System.out.println("App url >>>> " +propertyReader.readApplicationFile(str_url));
            driver.get(propertyReader.readApplicationFile(str_url));
            System.out.println("Locator from xml >>>>> "+element.getLocator("HandleCookiePopup.AcceptCookiesButton"));


        }catch(Exception e){

            e.printStackTrace();
            attachScreenshotToReport(extentTest,"Something went wrong... "+e.getStackTrace(),"negative" );

        }

    }

    @Deprecated
    public void handleCookiePrompt(){

        try {
            waitForElementsToVisible(element.getLocator("HandleCookiePopup.CookieFrame"),30);
            driver.switchTo().frame(driver.findElement(ByLocator(element.getLocator("HandleCookiePopup.CookieFrame"))));
            waitForElementsToVisible(element.getLocator("HandleCookiePopup.AcceptCookiesButton"),30);
            if (isElementPresent(element.getLocator("HandleCookiePopup.AcceptCookiesButton"))){
                clickOn(element.getLocator("HandleCookiePopup.AcceptCookiesButton"));
                extentTest.pass("Click on 'Yes, I'm happy' button to accept cookie");
            }else {
                attachScreenshotToReport(extentTest,"Button not found","negative" );
            }
        } catch (Exception e) {
            e.printStackTrace();
            attachScreenshotToReport(extentTest,"Something went wrong... "+e.getStackTrace(),"negative" );
        }

    }

    @Deprecated
    public void verifyNewsWebSitePageTitle(String str_title){

        try {
            if (driver.getTitle().equalsIgnoreCase(str_title)){

                attachScreenshotToReport(extentTest,"Successfully navigated to news website","positive");

            }else {

                attachScreenshotToReport(extentTest,"Something went wrong... page title doesn't matched. EXPECTED: "+str_title+" BUT FOUND: "+driver.getTitle(),"negative");

            }

        } catch (Exception e) {
            attachScreenshotToReport(extentTest,"Exception occurred "+e.getMessage(),"negative" );
        }

    }

    @Deprecated
    public void clickOnFirstNewsArticleFromTheList(){

        driver.switchTo().defaultContent();
        waitForElementsToVisible(element.getLocator("NewAritcles.FirstNewsArticle1")+getCurrentDate()+element.getLocator("NewAritcles.FirstNewsArticle2"),20);
     //   waitForElementsToVisible(element.getLocator("NewAritcles.FirstNewsArticle1")+"13 September 2023"+element.getLocator("NewAritcles.FirstNewsArticle2"),20);
        List<WebElement> parent_ele_list = driver.findElements(ByLocator(element.getLocator("NewAritcles.FirstNewsArticle1")+getCurrentDate()+element.getLocator("NewAritcles.FirstNewsArticle2")));
     //   List<WebElement> parent_ele_list = driver.findElements(ByLocator(element.getLocator("NewAritcles.FirstNewsArticle1")+"13 September 2023"+element.getLocator("NewAritcles.FirstNewsArticle2")));
        if (parent_ele_list.get(0).isDisplayed()){

            WebElement child_ele = parent_ele_list.get(0).findElement(ByLocator(element.getLocator("NewAritcles.NewsArticleText")));
            if (child_ele.isDisplayed()){
               WebElement click_ele = parent_ele_list.get(0)
                               .findElement(ByLocator(element.getLocator("NewAritcles.NewsArticleClickableLink")));
               new WebDriverWait(driver,10)
                       .until(ExpectedConditions.elementToBeClickable(ByLocator(element.getLocator("NewAritcles.NewsArticleClickableLink"))));
                ((JavascriptExecutor)driver).executeScript("arguments[0].click();", click_ele);
                attachScreenshotToReport(extentTest,"Successfully navigate to detail news page after click on first news article.","positive");
            }

        }else {
            attachScreenshotToReport(extentTest,"First article not visible...","negative");
        }

    }

    @Deprecated
    public void getNewsHeadlineFromDetailPage(){

        try {
            waitForElementToVisible(element.getLocator("NewAritcles.NewsArticleHeadlineTextInDetailPage"),20);
            if (isElementPresent(element.getLocator("NewAritcles.NewsArticleHeadlineTextInDetailPage"))){

                attachScreenshotToReport(extentTest,"News headline is present on detail page.","positive");
                news_headlines = getText(element.getLocator("NewAritcles.NewsArticleHeadlineTextInDetailPage"));

            }else{
                attachScreenshotToReport(extentTest,"News headline is not present on detail screen","negative");
                Assert.fail("News headline is not present on detail screen");
            }
        } catch (Exception e) {
            e.printStackTrace();
            attachScreenshotToReport(extentTest,"Exception Occurred: "+e.getMessage(),"negative");
        }
    }
}
