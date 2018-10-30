package ru.sberbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AccountsPage {

    enum SELECTORS {
        root("#content_col"),
        create_account_btn("#create_account_btn"),
        modal_account_number_field("#modal_create_account_number"),
        modal_create_account_btn("#modal_create_account_btn"),
        first_row("#left-tabs-example-pane-first > div > div.react-bootstrap-table > table > tbody > tr:nth-child(1)"),
        search_input("#left-tabs-example-pane-first > div > div.react-bootstrap-table > table > thead > tr > th > input"),
        columns("td"),
        add_cash_btn("#add_cash_btn"),
        modal_add_cash_field("#modal_add_cash"),
        modal_add_cash_btn("#modal_add_cash_btn"),
        get_cash_btn("#get_cash_btn"),
        modal_get_cash_field("#modal_get_cash"),
        modal_get_cash_btn("#modal_get_cash_btn"),
        transfer_cash_btn("#transfer_cash_btn"),
        modal_transfer_cash_account_field("#modal_transfer_account"),
        modal_transfer_cash_field("#modal_transfer_cash"),
        modal_transfer_cash_btn("#modal_transfer_cash_btn"),
        delete_btn("#delete_account_btn");

        private final String selector;

        SELECTORS(final String selector) {
            this.selector = selector;
        }

        public String getSelector() { return selector; }
    }

    private final WebElement root;
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AccountsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);

        // get root
        By path = By.cssSelector(SELECTORS.root.getSelector());
        this.root = wait.until(ExpectedConditions.presenceOfElementLocated(path));
    }

    //create account

    public void clickCreateAccountBtn() {
        By path = By.cssSelector(SELECTORS.create_account_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
    }

    public void fillAccountNumber(String accountNumber) {
        By path = By.cssSelector(SELECTORS.modal_account_number_field.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.clear();
        element.sendKeys(accountNumber);
    }

    public void clickModalCreateAccountBtn() throws InterruptedException {
        By path = By.cssSelector(SELECTORS.modal_create_account_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
        Thread.sleep(2000);
    }

    // add cash

    public void clickAddCashBtn() {
        By path = By.cssSelector(SELECTORS.add_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
    }

    public void fillAddCashField(int amount) {
        By path = By.cssSelector(SELECTORS.modal_add_cash_field.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.sendKeys(Integer.toString(amount));
    }

    public void clickModalAddCashBtn() throws InterruptedException {
        By path = By.cssSelector(SELECTORS.modal_add_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
        Thread.sleep(2000);
    }

    // get cash

    public void clickGetCashBtn() {
        By path = By.cssSelector(SELECTORS.get_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
    }

    public void fillGetCashField(int amount) {
        By path = By.cssSelector(SELECTORS.modal_get_cash_field.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.sendKeys(Integer.toString(amount));
    }

    public void clickModalGetCashBtn() throws InterruptedException {
        By path = By.cssSelector(SELECTORS.modal_get_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
        Thread.sleep(2000);
    }

    // transfer cash

    public void clickTransferCashBtn() {
        By path = By.cssSelector(SELECTORS.transfer_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
    }

    public void fillTransferAccountField(String toAccount) {
        By path = By.cssSelector(SELECTORS.modal_transfer_cash_account_field.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.sendKeys(toAccount);
    }

    public void fillTransferCashField(int amount) {
        By path = By.cssSelector(SELECTORS.modal_transfer_cash_field.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.sendKeys(Integer.toString(amount));
    }

    public void clickModalTransferCashBtn() throws InterruptedException {
        By path = By.cssSelector(SELECTORS.modal_transfer_cash_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
        Thread.sleep(2000);
    }

    // delete

    public void clickDeleteBtn() throws InterruptedException {
        By path = By.cssSelector(SELECTORS.delete_btn.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.click();
        Thread.sleep(2000);
    }

    // help methods

    public void searchForAccount(String accountNumber) throws InterruptedException {
        By path = By.cssSelector(SELECTORS.search_input.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        element.clear();
        Thread.sleep(2000);
        wait.until(new ExpectedCondition<Boolean>() {
            private WebElement el;
            private String attr;
            private String initialValue;

            private ExpectedCondition<Boolean> init( WebElement el, String attr, String initialValue ) {
                this.el = el;
                this.attr = attr;
                this.initialValue = initialValue;
                return this;
            }

            public Boolean apply(WebDriver driver) {
                String val = el.getAttribute(this.attr);
                return val.equals(this.initialValue);
            }
        }.init(element, "value", ""));
        element.sendKeys(accountNumber);
        wait.until(ExpectedConditions.textToBePresentInElementValue(element, accountNumber));
        Thread.sleep(2000);
    }

    public AccountRow getSelectedRow() {
        By path = By.cssSelector(SELECTORS.first_row.getSelector());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(path));
        path = By.cssSelector(SELECTORS.columns.getSelector());
        List<WebElement> elements = element.findElements(path);
        AccountRow row = new AccountRow();
        row.setAccountNumber(elements.get(0).getText());
        row.setCreated(elements.get(1).getText());
        row.setValue(elements.get(2).getText());
        return row;
    }

    public static class AccountRow {
        private String accountNumber;
        private String created;
        private String value;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



}
