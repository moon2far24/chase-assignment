package stepDefinitions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.bouncycastle.tsp.TSPUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import screenHelpers.NewsWebSiteScreenHelper;
import screenHelpers.SearchEngineScreenHelper;
import util.ExtentManager;
import util.PropertyReader;


public class newsValidationStepsDefinition {

    public WebDriver driver;
    public WebDriverWait wait;
    public ExtentReports extentReports;
    public ExtentTest extentTest;
    public PropertyReader propertyReader;
    public NewsWebSiteScreenHelper newsHelper;
    public SearchEngineScreenHelper searchHelper;

    @Deprecated
    @Before()
    public void startBrowser(Scenario scenario) {
        try {
            String browser = System.getProperty("browser", "chrome"); // Default to Chrome if no browser specified
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions option = new ChromeOptions();
                    option.addArguments("--remote-allow-origins=*");
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(option);
                    driver.manage().window().maximize();
                    wait = new WebDriverWait(driver,30);
                    propertyReader = new PropertyReader();
                    extentReports = ExtentManager.getInstance();
                    extentTest = ExtentManager.createTest(scenario.getName());
                    newsHelper = new NewsWebSiteScreenHelper(driver,wait,propertyReader,extentTest);
                    searchHelper = new SearchEngineScreenHelper(driver,wait,propertyReader,extentTest);

                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

        //   extentReports = ExtentManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After()
    public void teardownWebDriver(Scenario scenario) {
        if (driver != null) {
            if (scenario.isFailed()) {
            //    newsHelper.captureScreenshot(scenario.getName());
                extentTest.fail("'"+scenario.getName()+"' -->> "+"Failed");
            }
            extentReports.flush();
            driver.quit();
        }
    }

    @Deprecated
    @Given("I am on the {string} news page")
    public void i_am_on_the_news_page(String url) {
        try {

            newsHelper.openApplicationAndWaitForPageLoad(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Deprecated
    @Then("I should be on the {string} page")
    public void i_should_be_on_the_page(String title) {
        newsHelper.handleCookiePrompt();
        newsHelper.verifyNewsWebSitePageTitle(propertyReader.readApplicationFile(title));
    }
    @Deprecated
    @When("I click on the first news article")
    public void i_click_on_the_first_news_article() {

        newsHelper.clickOnFirstNewsArticleFromTheList();

    }
    @Deprecated
    @Then("I should see the article content")
    public void i_should_see_the_article_content() {

        newsHelper.getNewsHeadlineFromDetailPage();
    }

    @Deprecated
    @Given("I open search engine {string} and verify page title {string}")
    public void i_open_search_engine_and_verify_page_title(String url, String pageTitle) {

            searchHelper.openSearchEngine(url);
            searchHelper.verifySearchEnginePageTitle(pageTitle);

    }

    @Deprecated
    @When("I search for {string}")
    public void i_search_for(String string) {

        System.out.println("News hedlines -->> "+newsHelper.news_headlines);
        searchHelper.searchNewsHeadlineInToSearchEngine(newsHelper.news_headlines);

    }
    @Deprecated
    @Then("I should see search results")
    public void i_should_see_search_results() {

            searchHelper.verifyNavigationToResultScreen();

    }
    @Deprecated
    @Then("I should find at least two relevant results")
    public void i_should_find_at_least_two_relevant_results() {

        searchHelper.validateNewsOnGoogleSearch(newsHelper.news_headlines);

    }
}
