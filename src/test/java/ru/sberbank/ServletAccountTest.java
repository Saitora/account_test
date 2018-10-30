package ru.sberbank;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import ru.sberbank.helpers.Generator;
import ru.sberbank.pages.AccountsPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServletAccountTest {

    private WebDriver webDriver;
    private Properties props;
    private WebDriverWait wait;
    private AccountsPage page;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ServletAccountTest.class);


    @Before
    public void beforeTest() throws IOException {
        ChromeDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();

        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(webDriver, 10);

        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        }
        webDriver.get(props.getProperty("url"));
        page = new AccountsPage(webDriver);
    }

    @After
    public void afterTest() {
        webDriver.quit();
    }

    // create Account Test
    @Test
    public void createAccountTest() throws InterruptedException {
        String accountNumber = createAccount();

        AccountsPage.AccountRow foundRow;
        foundRow = searchAccountByAccountNumber(accountNumber);

        assertThat(accountNumber, is(foundRow.getAccountNumber()));
    }

    // add Cash
    @Test
    public void addCashTest() throws InterruptedException {
        String accountNumber = createAccount();

        int cash = 100;

        addCash(accountNumber, cash);

        AccountsPage.AccountRow account = searchAccountByAccountNumber(accountNumber);

        assertThat(account.getValue(), is(Integer.toString(cash)));
    }

    // get Cash
    @Test
    public void getCashTest() throws InterruptedException {
        String accountNumber = createAccount();

        int cash = 100;
        int getCash = 50;

        addCash(accountNumber, cash);

        getCash(accountNumber, getCash);

        AccountsPage.AccountRow account = searchAccountByAccountNumber(accountNumber);

        assertThat(account.getValue(), is(Integer.toString(cash - getCash)));
    }

    // Transfer Cash
    @Test
    public void transferCashTest() throws InterruptedException {
        String fromAccountNumber = createAccount();
        String toAccountNumber = createAccount();

        int cash = 100;
        int transferCash = 50;

        addCash(fromAccountNumber, cash);

        transferCash(fromAccountNumber, toAccountNumber, transferCash);

        AccountsPage.AccountRow fromAccount = searchAccountByAccountNumber(fromAccountNumber);
        AccountsPage.AccountRow toAccount = searchAccountByAccountNumber(toAccountNumber);

        assertThat(
                (Integer.valueOf(fromAccount.getValue()) == (cash - transferCash)) &&
                (Integer.valueOf(toAccount.getValue()) == transferCash),
                is(true)
        );
    }

    //delete Account
    @Test
    public void deleteAccount() throws InterruptedException {
        String accountNumber = createAccount();

        AccountsPage.AccountRow account = searchAccountByAccountNumber(accountNumber);

        LOG.info("-> Удалить");
        page.clickDeleteBtn();

        try {
            account = searchAccountByAccountNumber(accountNumber);
        } catch (TimeoutException e) {
            //pass
        }
    }

    // get More Cash than Account has

    // transfer more Cash than Account has

    private void addCash(String accountNumber, int amount) throws InterruptedException {
        LOG.info("Пополнение Аккаунта: " + accountNumber + ", сумма: " + Integer.toString(amount));
        AccountsPage.AccountRow row = searchAccountByAccountNumber(accountNumber);
        assert(row.getAccountNumber().equals(accountNumber));
        LOG.info("-> Пополнить");
        page.clickAddCashBtn();
        LOG.info("-> Поле <Сумма>");
        page.fillAddCashField(amount);
        page.clickModalAddCashBtn();
    }

    private void getCash(String accountNumber, int amount) throws InterruptedException {
        LOG.info("Снятие с Аккаунта: " + accountNumber + ", сумма: " + Integer.toString(amount));
        AccountsPage.AccountRow row = searchAccountByAccountNumber(accountNumber);
        assert(row.getAccountNumber().equals(accountNumber));
        LOG.info("-> Снять");
        page.clickGetCashBtn();
        LOG.info("-> Поле <Сумма>");
        page.fillGetCashField(amount);
        page.clickModalGetCashBtn();
    }

    private void transferCash(String fromAccount, String toAccount, int amount) throws InterruptedException {
        LOG.info("Перевод с Аккаунта: " + fromAccount + ", на Аккаунт: " + toAccount + ", сумма: " + Integer.toString(amount));
        AccountsPage.AccountRow row = searchAccountByAccountNumber(fromAccount);
        assert(row.getAccountNumber().equals(fromAccount));
        LOG.info("-> Перевести");
        page.clickTransferCashBtn();
        LOG.info("-> Поле <Аккаунт> : " + toAccount);
        page.fillTransferAccountField(toAccount);
        LOG.info("-> Поле <Сумма> : " + amount);
        page.fillTransferCashField(amount);
        page.clickModalTransferCashBtn();
    }

    private AccountsPage.AccountRow searchAccountByAccountNumber(String accountNumber) throws InterruptedException {
        LOG.info("Поиск Аккаунта: " + accountNumber);
        page.searchForAccount(accountNumber);

        return page.getSelectedRow();
    }

    private String createAccount() throws InterruptedException {
        LOG.info("Создание Аккаунта");

        page.clickCreateAccountBtn();

        String accountNumber = Generator.generateAccountNumber();
        LOG.info("Номер счета = " + accountNumber);
        page.fillAccountNumber(accountNumber);

        page.clickModalCreateAccountBtn();

        return accountNumber;
    }

}