import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationAndAuthorizationTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private String urlHomePage = "http://intershop5.skillbox.ru/";
    private String userName = "Borman";
    private String email = "skillbox@tes.com";
    private String password = "tester12";

    // headerLinkAccountPageLocator - ищет кнопку МОЙ АККАУНТ в меню хедера.
    private By headerLinkAccountPageLocator = By.cssSelector("ul#menu-primary-menu a[href*='/my-account/']");

    // footerLinkAccountPageLocator - ищет кнопку МОЙ АККАУНТ в меню футера.
    private By footerLinkAccountPageLocator = By.cssSelector("footer#colophon li.page_item a[href*='/my-account/']");

    // fieldNameLocator - ищет поле "Имя пользователя или почта *" на странице аккаунта.
    private By fieldNameLocator = By.id("username");

    // fieldPasswordLocator - ищет поле "Пароль *" на странице аккаунта.
    private By fieldPasswordLocator = By.id("password");

    // passwordRevealIconLocator - ищет иконку с глазком, открывающую пароль
    private By passwordRevealIconLocator = By.cssSelector(".password-input span.show-password-input");

    // loginButtonLocator - ищет кнопку "ВОЙТИ" на странице аккаунта.
    private By loginButtonLocator = By.xpath("//button[@type='submit' and @name='login']");

    // LinkToThePasswordRecoveryPageLocator - ищет кликабельную ссылку "Забыли пароль?" на странице аккаунта.
    private By LinkToThePasswordRecoveryPageLocator = By.cssSelector("a[href*='/my-account/lost-password/']");

    // buttonRegisterLocator - ищет кнопку "Зарегистрироваться" на странице аккаунта.
    private By buttonRegisterLocator = By.cssSelector("form.woocommerce-form-login button.custom-register-button");


    // fieldUserNameRegisterLocator - ищет поле для ввода имени на странице регистрации.
    private By fieldUserNameRegisterLocator = By.id("reg_username");

    // fieldEmailRegisterPageLocator - ищет поле email на странице регистрации.
    private By fieldEmailRegisterPageLocator = By.id("reg_email");

    // fieldPasswordRegisterPageLocator - ищет поле пароля на странице регистрации.
    private By fieldPasswordRegisterPageLocator = By.id("reg_password");

    //buttonRegisterOnRegisterPageLocator - ищет кнопку "ЗАРЕГИСТРИРОВАТЬСЯ" на странице регистрации.
    private By buttonRegisterOnRegisterPageLocator = By.xpath("//button[@name='register' and @type='submit']");

    // successRegistrationAlertLocator - ищет алерт с текстом об успешной регистрации.
    private By successRegistrationAlertLocator = By.xpath("//*[@id='primary']//div[@class='content-page']//div[contains(normalize-space(),'Регистрация завершена')]");

    // greetingMessageLocator ищет валидационное сообщение,
    // которое должно появляться при успешной авторизации на странице "МОЙ АККАУНТ".
    private By greetingMessageLocator = By.xpath(
            "//*[@id='primary']" +
                    "//div[contains(@class,'woocommerce-MyAccount-content')]" +
                    "//p[contains(normalize-space(),'Привет')]"
    );

    // logoutButtonLocator - ищет кнопку выхода из аккаунта "Выйти" на странице аккаунта.
    private By logoutButtonLocator = By.xpath("//*[@id='primary']" +
            "//div[contains(@class,'woocommerce-MyAccount-content')]" +
            "//p//a[contains(@href,'/customer-logout/')]");

    // logoutButtonInTheHeaderLocator - ищет кнопку входа/выхода в/из аккаунта в хедере.
    private By logoutButtonInTheHeaderLocator = By.xpath("//header[@id='mastheads']//div[contains(@class,'login-woocommerce')]//a[contains(@class,'account')]");

    //Метод generateShortUserName - генерирует случайное имя нового юзера.
    private String generateShortUserName() {
        return "tu" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    //Метод generateShortEmail генерирует случайную новую почту нового юзера.
    private String generateShortEmail(String userName) {
        return userName + "@t.t";
    }

    // invalidPasswordAlertLocator - ищет сообщение об ошибке при вводе неверного пароля в сценарии авторизации.
    private By invalidPasswordAlertLocator = By.xpath("//div[@id='primary']" +
            "//ul[contains(@class,'woocommerce-error') and @role='alert']" +
            "//li[contains(normalize-space(),'Веденный пароль для пользователя ')]");

    private By linkLostPasswordLocator = By.xpath("//div[@id='primary']//ul[contains(@class,'woocommerce-error') and @role='alert']" +
            "//li[contains(normalize-space(),'Веденный пароль для пользователя ')]" +
            "//a[contains(@href,'/lost-password/')]");

    // passwordResetFieldLocator - ищет поле для сброса пароля.
    private By passwordResetFieldLocator = By.xpath("//form[contains(@class,'lost_reset_password')]//input[@id='user_login']");

    // buttonPasswordResetLocator - ищет кнопку сброса пароля "СБРОСИТЬ ПАРОЛЬ".
    private By buttonPasswordResetLocator = By.xpath("//form[contains(@class,'lost_reset_password')]" +
            "//button[@type='submit' and contains(normalize-space(),'Сбросить пароль')]");

    // resetPasswordSuccessMessageLocator - ищет сообщение об отправке письма для сброса пароля.
    private By resetPasswordSuccessMessageLocator = By.xpath("//*[@id='primary']//div[contains(@class,'woocommerce')]" +
            "//*[contains(@class,'message') and contains(normalize-space(),'Password reset email has been sent.')]");


    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    //Метод testAccountRegistration тестирует успешную регистрацию, используются методы генерации случайных данных для
    // имени и почты, так как после каждой успешной регистрации тест будет давать ошибку "пользователь существует" при повторном прогоне.
    @Test
    public void testAccountRegistration() {

        driver.navigate().to(urlHomePage);

        // Генерируем корректные данные
        String userName = generateShortUserName();
        String email = generateShortEmail(userName);
        String password = "Qwerty123!";


        // Находим кнопку "МОЙ АККАУНТ" в хедере главной станицы и кликаем по ней.
        WebElement linkMyAccount = wait.until(ExpectedConditions.elementToBeClickable(headerLinkAccountPageLocator));
        linkMyAccount.click();
        // Проверяем, что адрес текущей страницы соответствует странице регистрации/авторизации.
        wait.until(ExpectedConditions.urlContains("/my-account/"));
        String urlMyAccount = driver.getCurrentUrl();
        assertTrue(urlMyAccount.contains("/my-account/"));

        // Ждем и жмем кнопку "ЗАРЕГИСТРИРОВАТЬСЯ".
        WebElement buttonRegisterElement = wait.until(ExpectedConditions.elementToBeClickable(buttonRegisterLocator));
        buttonRegisterElement.click();

        // Проверяем, что мы на странице Регистрации.
        wait.until(ExpectedConditions.urlContains("/register/"));
        String currentUrlRegisterPage = driver.getCurrentUrl();
        assertTrue(currentUrlRegisterPage.contains("/register/"),
                "Адрес страницы не соответствует адресу странице Регистрации, текущий адрес такой " + currentUrlRegisterPage);

        // Находим поля имени, почты и пароля и заполняем их данными на странице регистрации.
        WebElement fieldUserNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldUserNameRegisterLocator));
        fieldUserNameElement.sendKeys(userName);
        WebElement fieldEmailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldEmailRegisterPageLocator));
        fieldEmailElement.sendKeys(email);
        WebElement fieldPasswordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPasswordRegisterPageLocator));
        fieldPasswordElement.sendKeys(password);

        // Кликаем кнопку "ЗАРЕГИСТРИРОВАТЬСЯ" после заполнения всех полей на странице формы регистрации.
        WebElement buttonRegister = wait.until(ExpectedConditions.elementToBeClickable(buttonRegisterOnRegisterPageLocator));
        buttonRegister.click();

        // Проверяем находимся ли мы все еще на странице регистрации и появился ли alert с текстом об успешной регистрации.
        wait.until(ExpectedConditions.urlContains("/register/"));
        assertTrue(currentUrlRegisterPage.contains("/register/"),
                "Адрес страницы не соответствует адресу странице Регистрации, текущий адрес такой " + currentUrlRegisterPage);
        WebElement alertElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successRegistrationAlertLocator));
        String actualAlertMessage = alertElement.getText();
        String expectedAlertMessage = "Регистрация завершена";
        assertTrue(alertElement.isDisplayed(), "Alert с сообщением об успшной регистрации не появился.");
        assertEquals(expectedAlertMessage, actualAlertMessage,
                "Сообщение алерта: " + actualAlertMessage + ", что не соответствует ожидаемому тексту сообщения об успешной регистрации.");

    }

    // Метод testAuthorization тестирует авторизацию существующим пользователем.
    @Test
    public void testAuthorization() {
        driver.navigate().to(urlHomePage);
        // Находим кнопку "МОЙ АККАУНТ" в футере главной станицы и кликаем по ней.
        WebElement linkMyAccount = wait.until(ExpectedConditions.elementToBeClickable(footerLinkAccountPageLocator));
        linkMyAccount.click();

        // Проверяем, что адрес текущей страницы соответствует странице регистрации/авторизации.
        wait.until(ExpectedConditions.urlContains("/my-account/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/my-account/"));
        // Добавляем имя в поле для имени.
        WebElement nameFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldNameLocator));
        nameFieldElement.sendKeys(userName);
        // Добавляем пароль в поле для пароля.
        WebElement passwordFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPasswordLocator));
        passwordFieldElement.sendKeys(password);

        // Проверяем значение в поле имя.
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldNameLocator));
        assertEquals(userName, nameFieldElement.getAttribute("value"), "Имя в поле для имени не равно " + userName);

        // Проверяем значение в поле пароля.
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPasswordLocator));
        assertEquals(password, passwordFieldElement.getAttribute("value"), "Значение в поле пароля не равно " + password);

        // После заполнения полей кликаем по кнопке "ВОЙТИ".
        WebElement loginToAccountButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginToAccountButton.click();

        // Проверяем, что мы на станице аккаунта.
        wait.until(ExpectedConditions.urlContains("/my-account/"));
        assertTrue(currentUrl.contains("/my-account/"),
                "Адрес текущей страницы :" + currentUrl + " , что не соответствует ожидаемому значению адреса.");

        // Проверяем появилось ли сообщение приветствия пользователя на странице аккаунта, прошла
        // ли авторизация успешно.
        WebElement greeting = wait.until(ExpectedConditions.visibilityOfElementLocated(greetingMessageLocator));
        String actualGreeting = greeting.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();
        String expectedGreeting = String.format("Привет %s (Выйти)", userName);
        assertEquals(expectedGreeting, actualGreeting,
                "Текст приветствия некорректен");

        // После успешного входа в аккаунт протестируем выход их аккаунта кликом по кнопке "Выйти".
        WebElement LogoutButtonElement = wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator));
        LogoutButtonElement.click();

        // Проверяем адрес страницы.
        wait.until(ExpectedConditions.urlContains("/my-account/"));
        String newCurrentUrl = driver.getCurrentUrl();
        assertTrue(newCurrentUrl.contains("/my-account/"),
                "Текущий адрес страницы " + newCurrentUrl + " , что не соответствует адресу страницы регистрации/авторизации");
        // Проверяем изменился ли текст кнопки в хедере с "Выйти" на "Войти".
        WebElement logoutButtonHeaderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButtonInTheHeaderLocator));
        String actualTextLogoutButton = logoutButtonHeaderElement.getText();
        assertEquals("Войти", actualTextLogoutButton,
                "Текст кнопки выхода/входа из/в аккаунт не соответствует ожидаемому значению.");

    }

    // Метод testPasswordRecovery тестирует сценарий восстановления пароля, при вводе неверного пароля пользователем.
    @Test
    public void testPasswordRecovery() {
        String invalidPassword = password + "1";
        driver.navigate().to(urlHomePage);
        // Находим кнопку "МОЙ АККАУНТ" в футере главной станицы и кликаем по ней.
        WebElement linkMyAccount = wait.until(ExpectedConditions.elementToBeClickable(footerLinkAccountPageLocator));
        linkMyAccount.click();

        // Проверяем, что адрес текущей страницы соответствует странице регистрации/авторизации.
        wait.until(ExpectedConditions.urlContains("/my-account/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/my-account/"));
        // Добавляем имя в поле для имени.
        WebElement nameFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldNameLocator));
        nameFieldElement.sendKeys(userName);
        // Добавляем пароль в поле для пароля.
        WebElement passwordFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPasswordLocator));
        passwordFieldElement.sendKeys(invalidPassword);

        // Проверяем работу скрытия/открытия пароля кликом по иконке глазка.
        WebElement iconHiddenPasswordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordRevealIconLocator));
        String actualTypeValueFIeldPasswordBeforeCLickToIcon = passwordFieldElement.getAttribute("type");
        String expectedTypeValueFileldPasswordBeforeCLickToIcon = "password";
        assertEquals(expectedTypeValueFileldPasswordBeforeCLickToIcon, actualTypeValueFIeldPasswordBeforeCLickToIcon,
                "Значения атрибута type не равно password перед кликом по иконки глазка в поле пароля");
        iconHiddenPasswordElement.click();
        String actualTypeValueFIeldPasswordAfterCLickToIcon = passwordFieldElement.getAttribute("type");
        String expectedTypeValueFileldPasswordAfterCLickToIcon = "text";
        // Непосредственная проверка значения атрибута type после клика по иконке глазка в поле пароля.
        assertEquals(expectedTypeValueFileldPasswordAfterCLickToIcon, actualTypeValueFIeldPasswordAfterCLickToIcon,
                "Значения атрибута type не равно text перед кликом по иконки глазка в поле пароля");

        // После заполнения полей кликаем по кнопке "ВОЙТИ".
        WebElement loginToAccountButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginToAccountButton.click();

        // Проверяем появился ли алерт с сообщением о неверном пароле.
        WebElement alert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(invalidPasswordAlertLocator)
        );

        assertTrue(alert.isDisplayed(), "Алере с сообщением о неверном пароле не появился");

        String alertText = alert.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(alertText.contains(userName), "Текст алерта не содержит сообщение о неверном пароле для пользователя " + userName);

        // Находим кнопку "Забыли пароль?" и кликаем по ней.
        WebElement linkLostPasswordElement = wait.until(ExpectedConditions.elementToBeClickable(linkLostPasswordLocator));
        linkLostPasswordElement.click();

        // Проверяем, что мы попали на страницу восстановления пароля.
        String currentUrlPasswordRecoveryPage = driver.getCurrentUrl();
        assertTrue(currentUrlPasswordRecoveryPage.contains("/lost-password/"),
                "Текущая страница " + currentUrlPasswordRecoveryPage + " не является страницей восстановления пароля");

        // Находим поле для сброса пароля и вводим в него email пользователя, который хранится в поле email на уровне класса.
        WebElement resetFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordResetFieldLocator));
        resetFieldElement.sendKeys(email);

        // Находим кнопку "СБРОСИТЬ ПАРОЛЬ" для отправки письма на почту и кликаем по ней.
        WebElement buttonPasswordResetElement = wait.until(ExpectedConditions.elementToBeClickable(buttonPasswordResetLocator));
        buttonPasswordResetElement.click();

        // Проверяем появилось ли сообщение об отправке на почту письма для сброса пароля и проверяем его текст на соответствие
        // ожидаемому значению.
        WebElement resetEmailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordSuccessMessageLocator));
        assertTrue(resetEmailElement.isDisplayed(), "Сообщение об отправке письма для сброса пароля не появилось");
        String expectedTextResetEmail = "Password reset email has been sent.";
        String actualTextResetEmail = resetEmailElement.getText();
        assertEquals(expectedTextResetEmail, actualTextResetEmail,
                "Сообщение об отправке письма для сброса пароля " + actualTextResetEmail + " и не соответствует ожидаемому тексту");

    }

}
