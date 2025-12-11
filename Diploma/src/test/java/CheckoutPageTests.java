import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CheckoutPageTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private String urlHomePage = "http://intershop5.skillbox.ru/";
    private String userName = "Borman";
    private String email = "skillbox@tes.com";
    private String password = "tester12";
    private String coupon = "sert500";


    // 1. Локаторы для работы с авторизацией.

    // footerLinkAccountPageLocator - ищет кнопку МОЙ АККАУНТ в меню футера.
    private By footerLinkAccountPageLocator = By.cssSelector("footer#colophon li.page_item a[href*='/my-account/']");

    // fieldNameLocator - ищет поле "Имя пользователя или почта *" на странице аккаунта.
    private By fieldNameLocator = By.id("username");

    // fieldPasswordLocator - ищет поле "Пароль *" на странице аккаунта.
    private By fieldPasswordLocator = By.id("password");

    // loginButtonLocator - ищет кнопку "ВОЙТИ" на странице аккаунта.
    private By loginButtonLocator = By.xpath("//button[@type='submit' and @name='login']");

    // greetingMessageLocator ищет валидационное сообщение,
    // которое должно появляться при успешной авторизации на странице "МОЙ АККАУНТ".
    private By greetingMessageLocator = By.xpath(
            "//*[@id='primary']" +
                    "//div[contains(@class,'woocommerce-MyAccount-content')]" +
                    "//p[contains(normalize-space(),'Привет')]"
    );

    // 2. Локаторы для работы с добавлением товара в Корзину.

    // - локатор находит кнопку Каталок в меню хедера
    private By headerMenuCatalogLocator = By.xpath("//ul[contains(@id,'menu-primary-menu')]//a[contains(text(),'Каталог')]");

    // linkBooksPageLocator - локатор ищет элемент ссылки на страницу с книгами в блоке КАТАЛОГ ТОВАРОВ.
    private By linkBooksPageLocator = By.xpath("//li[contains(@class,'cat-item-28')]//a[contains(@href,'/catalog/books/')]");


    //martinFordAddToCartLocator - локатор ищет кнопку "В корзину" для книги "Мартин Форд. Роботы наступают" на странице "Книги"
    private By martinFordAddToCartLocator = By.cssSelector(
            "li.post-5350 a.button.add_to_cart_button.ajax_add_to_cart"
    );

    // buttonDetailsLocator - локатор ищет кнопку ПОДРОБНЕЕ.
    private By buttonDetailsLocator = By.cssSelector(".added_to_cart[title='Подробнее']");

    // bookTextInCardtLocator - локатор проверяет поле товар на странице корзины для книги, которая была добавлена.
    private By bookTextInCardtLocator = By.xpath("//td[contains(@class,'product-name')]//a[normalize-space()='Мартин Форд. Роботы наступают']"); // bookTextInCardtLocator - локатор проверяет поле товар на странице корзины для книги, которая была добавлена.

    // bookCountInCardtLocator - локатор находит в корзине поле с количеством товара, который был добавлен(книга в данном случае).
    private By bookCountInCardtLocator = By.xpath("//td[contains(@data-title,'Количество')]//label[contains(text(),'Мартин Форд.')]/following-sibling::input[@type='number']");


    //pricePerBookLocator - локатор ищет цену книги Мартина Форда.
    private By pricePerBookLocator = By.cssSelector("li.post-5350 span.price ins .woocommerce-Price-amount bdi");

    // buttonCartMenuMainPageLocator - локатор ищет кнопку корзину в главном меню навигации в хедере
    private By buttonCartMenuHeadearLocator = By.xpath("//div[@id='menu']//a[contains(@href,'/cart/')]");


    // removeButton - локатор ищет кнопку для удаления определенного товара из корзины.
    private By removeButtonLocator = By.cssSelector("a.remove[data-product_id='5350']");

    // cartItemRemovedMessage - локатор ищет элемент с текстом об удалении товара из корзины на странице корзины.
    private By cartItemRemovedMessageLocator = By.cssSelector(".woocommerce-message[role='alert']");

    // returnToCartLocator - локатор находит кнопку "Вернут?" в корзине.
    private By returnToCartLocator = By.cssSelector(".woocommerce-message[role='alert'] a.restore-item");

    //buttonPlaceOrderLocator - локатор находит кнопку 'Оформить заказ', на странице корзины.
    private By buttonPlaceOrderLocator = By.xpath("//div[contains(@class,'wc-proceed-to-checkout')]/a[contains(normalize-space(),'ОФОРМИТЬ ЗАКАЗ')]");

    // paymentItemOnTheCartPageLocator - локатор ищет элемент с суммой на странице корзины в заказе, в поле К ОПЛАТЕ.
    private By paymentItemOnTheCartPageLocator = By.cssSelector("tr.order-total span.woocommerce-Price-amount.amount bdi");

    // 3. Локаторы полей страницы "ОФОРМЛЕНИЯ ЗАКАЗА".

    // fieldNameLocator - ищет поле "Имя" на странице оформления заказа.
    private By fieldNameOrderPageLocator = By.cssSelector("form.checkout input#billing_first_name");

    // surnameOrderPageLocator - ищет поле "Фамилия" на странице оформления заказа.
    private By fieldSurnameOrderPageLocator = By.cssSelector("form.checkout input#billing_last_name");

    // billingCountrySelectLocator - локатор поля select страны.
    private By billingCountrySelectLocator = By.id("billing_country");

    // fieldStreetLocator - локатор ищет поле для ввода улицы на странице оформления заказа.
    private By fieldStreetLocator = By.cssSelector("input#billing_address_1.input-text");

    // fieldCityLocator - locator ищет
    private By fieldCityLocator = By.cssSelector("#billing_city.input-text");

    // fieldRegionLocator - локатор находит поле "Область" на странице оформления заказа.
    private By fieldRegionLocator = By.cssSelector("#billing_state.input-text");

    // fieldPostalCode - локатор находит поле "Почтовый индекс" на странице оформления заказа.
    private By fieldPostalCode = By.cssSelector("#billing_postcode.input-text");

    // fieldPhone - локатор находит поле "Телефон" на странице оформления заказа.
    private By fieldPhone = By.cssSelector("#billing_phone.input-text");

    // fieldEmailOrderPage - локатор находит поле "Адрес почты" на странице оформления заказа.
    private By fieldEmailOrderPage = By.cssSelector("#billing_email.input-text");

    // fieldComments - ищет поле для комментариев на странице оформления заказа.
    private By fieldComments = By.cssSelector("#order_comments.input-text");

    // buttonPlaceAnOrderLocator - ищет кнопку "ОФОРМИТЬ ЗАКАЗ" на странице оформления заказа.
    private By buttonPlaceAnOrderLocator = By.xpath("//button[@id='place_order' and @type='submit']");

    private By titleOrderReceiptPageLocator = By.cssSelector("#main h2.post-title");

    //footerLinkOrderPageLocator - ищет в меню хедера ссылку на страницу оформления заказа.
    private By footerLinkOrderPageLocator = By.cssSelector("footer#colophon a[href*='/checkout/']");

    // couponFieldActivationButtonLocator - локатор ищет ссылку "Нажмите для ввода купона", после клика по которой открывается поле для ввода купона на скидку.
    private By couponFieldActivationButtonLocator = By.cssSelector("div.woocommerce-form-coupon-toggle a.showcoupon");

    // fieldCouponOrderPageLocator - ищет поле для ввода купона на странице оформления заказа.
    private By couponInputLocator = By.cssSelector("form.checkout_coupon input#coupon_code.input-text");

    // applyCouponButtonLocator - ищет кнопку "ПРИМЕНИТЬ КУПОН", которая активирует купон на странице оформления заказа.
    private By applyCouponButtonLocator = By.xpath("//form[contains(@class,checkout_coupon)]" +
            "//button[@type='submit' and @value='Применить купон']");

    // messageSuccessfulCouponLocator - находит элемент с сообщением об успешном применении купона.
    private By messageSuccessfulCouponLocator = By.xpath("//ul[contains(@class,'woocommerce-error') and @role='alert']/li");

    // actualTotalCartPriceLocator - находит элемент с финальной суммой заказа на странице оформления заказа.
    private By actualTotalCartPriceLocator = By.cssSelector("tfoot tr.order-total span.woocommerce-Price-amount.amount bdi");

    // subtotalCartPriceLocator - находит элемент с суммой заказа на странице корзины, без применения скидки
    private By subtotalCartPriceLocator = By.cssSelector("tfoot tr.cart-subtotal span.woocommerce-Price-amount.amount bdi");

    // radioButtonCashOnDeliveryLocator - находит радиокнопку "Оплата при доставке".
    private By radioButtonCashOnDeliveryLocator = By.cssSelector("#payment_method_cod.input-radio");

    // paymentFormNoticeLocator- находит блок с сообщением о выбранной форме оплаты.
    private By paymentFormNoticeLocator = By.xpath("//li[contains(@class,'payment_method_cod')]" +
            "//div[contains(@class,'payment_method_cod')]");

    // 4. Локаторы сообщений валидации при отправке с незаполненными полями формы оформления заказа на странице ОФОРМЛЕНИЕ ЗАКАЗА.

    // nameFieldValidationMessageLocator - находим элемент с сообщением об ошибке при отправке формы с пустым полем имя.
    private By nameFieldValidationMessageLocator = By.cssSelector(
            "div.woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout" +
                    " ul.woocommerce-error" +
                    " li[data-id='billing_first_name']"
    );

    // lastNameFieldValidationMessageLocator - находим элемент с сообщением об ошибке при отправке формы с пустым полем фамилия.

    private By lastNameFieldValidationMessageLocator = By.cssSelector(
            "div.woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout" +
                    " ul.woocommerce-error" +
                    " li[data-id='billing_last_name']"
    );

    // addressFieldValidationMessageLocator - находим элемент с сообщением об ошибке при отправке формы с пустым полем адреса.
    private By addressFieldValidationMessageLocator = By.cssSelector(
            "div.woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout" +
                    " ul.woocommerce-error" +
                    " li[data-id='billing_address_1']"
    );

    // cityFieldValidationMessageLocator - находим элемент с сообщением об ошибке при отправке формы с пустым полем города.
    private By cityFieldValidationMessageLocator = By.cssSelector(
            "div.woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout" +
                    " ul.woocommerce-error" +
                    " li[data-id='billing_city']"
    );

    // indexFieldValidationMessageLocator - находим элемент с сообщением об ошибке при отправке формы с пустым полем почтового индекса.
    private By indexFieldValidationMessageLocator = By.cssSelector(
            "div.woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout" +
                    " ul.woocommerce-error" +
                    " li[data-id='billing_postcode']"
    );

    // phoneErrorInvalidNumberLocator - находим элемент с сообщением об ошибке 'неверный номер телефона' при отправке формы с пустым полем города.
    private By phoneErrorInvalidNumberLocator = By.xpath(
            "//ul[contains(@class,'woocommerce-error') and @role='alert']" +
                    "//li[@data-id='billing_phone' and contains(normalize-space(),'неверный номер телефона')]"
    );

    // phoneErrorRequiredLocator - находим элемент с сообщением об ошибке 'Телефон для выставления счета' при отправке формы с пустым полем города.
    private By phoneErrorRequiredLocator = By.xpath(
            "//ul[contains(@class,'woocommerce-error') and @role='alert']" +
                    "//li[@data-id='billing_phone' and contains(normalize-space(),'Телефон для выставления счета')]"
    );


    // Метод parsePriceToRubles очищает и парсит строки приводя их к числовым значениям
    // для работы с числами в корзине при применении купона на скидку.
    private int parsePriceToRubles(String priceText) {
        // Оставляем только цифры и запятую
        String cleaned = priceText.replaceAll("[^0-9,]", "");

        // Берём только часть до запятой (рубли)
        int commaIndex = cleaned.indexOf(',');
        if (commaIndex != -1) {
            cleaned = cleaned.substring(0, commaIndex);
        }

        return Integer.parseInt(cleaned);
    }


    @BeforeEach
    public void setUp() {
        String driverPath = Paths.get("drivers", "chromedriver.exe").toString();
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        login();        // авторизация один раз перед каждым тестом
        clearCart();    // очистка корзины перед каждым тестом

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }


    private void login() {
        driver.navigate().to(urlHomePage);

        WebElement linkMyAccount = wait.until(
                ExpectedConditions.elementToBeClickable(footerLinkAccountPageLocator));
        linkMyAccount.click();

        wait.until(ExpectedConditions.urlContains("/my-account/"));

        WebElement nameFieldElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(fieldNameLocator));
        nameFieldElement.sendKeys(userName);

        WebElement passwordFieldElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(fieldPasswordLocator));
        passwordFieldElement.sendKeys(password);

        WebElement loginToAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginToAccountButton.click();
    }

    private void clearCart() {
        driver.navigate().to("http://intershop5.skillbox.ru/cart/");

        // удаляем купон в корзине, если есть
        List<WebElement> removeCouponLinks =
                driver.findElements(By.cssSelector("tr.cart-discount a.woocommerce-remove-coupon"));

        if (!removeCouponLinks.isEmpty()) {
            WebElement removeCoupon = wait.until(
                    ExpectedConditions.elementToBeClickable(removeCouponLinks.get(0)));
            removeCoupon.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("tr.cart-discount")));
        }

        // удаляем товары
        List<WebElement> removeButtons = driver.findElements(By.cssSelector("a.remove"));
        while (!removeButtons.isEmpty()) {
            WebElement remove = wait.until(
                    ExpectedConditions.elementToBeClickable(removeButtons.get(0)));
            remove.click();
            wait.until(ExpectedConditions.stalenessOf(remove));
            removeButtons = driver.findElements(By.cssSelector("a.remove"));
        }

    }



    //Метод testCheckoutPage тестирует сценарий оформления заказа.
    @Test
    public void testCheckoutPage() {

        driver.navigate().to(urlHomePage);

        // Добавляем книгу в корзину через клик по кнопке Каталог в меню хедера -> клик по ссылке Книги на странице каталога
        // в блоке Категории товаров -> на странице с книгами клик по кнопке Корзина, в одной из карточек с книгой.
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement linkBooksPage = wait.until(ExpectedConditions.elementToBeClickable(linkBooksPageLocator));
        linkBooksPage.click();
        WebElement priceBook = wait.until(ExpectedConditions.visibilityOfElementLocated(pricePerBookLocator));
        String expectedPrice = priceBook.getText();
        WebElement buttonBooksCart = wait.until(ExpectedConditions.elementToBeClickable(martinFordAddToCartLocator));
        buttonBooksCart.click();
        //Ждем появления кнопки ПОДРОБНЕЕ, чтобы в корзину успел добавиться товар и кликаем по ней для перехода в корзину.
        WebElement detailsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonDetailsLocator));
        detailsButton.click();
        wait.until(ExpectedConditions.urlContains("/cart/"));
        WebElement fieldTextInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookTextInCardtLocator));

        String expectedTextFieldCart = "Мартин Форд. Роботы наступают";
        String actualTextFieldCart = fieldTextInCart.getText();

        // Проверяем, что текст в поле Корзины Товар соответствует названию книги, которая была добавлена в корзину.
        assertTrue(actualTextFieldCart.contains(expectedTextFieldCart),
                "Текст поля Товар в корзине содержит ожидаемый текст из заголовка книги.");

        // Проверяем, что количество товара в поле Корзины КОЛИЧЕСТВО соответствует количеству книг, которое было добавлено в корзину.
        WebElement countBooksInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookCountInCardtLocator));
        int actualCountBookInCart = Integer.parseInt(countBooksInCart.getAttribute("value"));
        assertEquals(1, actualCountBookInCart,
                "Количество книг не равно 1");

        // Проверяем поле К ОПЛАТЕ на странице КОРЗИНА на соответствие ожидаемой сумме.
        WebElement orderAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentItemOnTheCartPageLocator));
        String actualOrderAmount = orderAmount.getText();
        assertEquals(expectedPrice, actualOrderAmount,
                "Сумма в поле К ОПЛАТЕ в ЗАКАЗЕ на странице КОРЗИНЫ неверная.");

        // Нажимаем кнопку ОФОРМИТЬ ЗАКАЗ.
        WebElement buttonAddOrder = wait.until(ExpectedConditions.elementToBeClickable(buttonPlaceOrderLocator));
        buttonAddOrder.click();
        wait.until(ExpectedConditions.urlContains("/checkout/"));

        // 3. Оформление заказа после добавления товара в корзину и клика по кнопке ОФОРМИТЬ ЗАКАЗ.

        // Проверяем, что попали на страницу оформления заказа.
        String currentUrlOrderPage = driver.getCurrentUrl();
        assertTrue(currentUrlOrderPage.contains("/checkout/"),
                "Адрес страницы не соответствует ожидаемому адресу и равен " + currentUrlOrderPage);

        // Находим поле имени и заполняем его.
        WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldNameOrderPageLocator));
        nameElement.clear();
        nameElement.sendKeys("Василий");
        String nameText = nameElement.getAttribute("value");
        // Проверяем поле имя.
        assertEquals("Василий", nameText, "Имя не соответствует ожидаемому значению.");

        // Находим поле фамилии и заполняем его, предварительно очистив.
        WebElement surnameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldSurnameOrderPageLocator));
        surnameElement.clear();
        surnameElement.sendKeys("Иванов");
        String surnameText = surnameElement.getAttribute("value");
        // Проверяем поле фамилии.
        assertEquals("Иванов", surnameText, "Фамилия не соответствует ожидаемому значению.");

        // Находим HTML-элемент <select id="billing_country">, используя его, через select выберем страну в соответствующем поле.
        WebElement countrySelect = wait.until(ExpectedConditions.presenceOfElementLocated(
                billingCountrySelectLocator
        ));

        Select select = new Select(countrySelect);
        select.selectByVisibleText("Russia");
        String selected = select.getFirstSelectedOption().getText();
        // Проверяем поле с комбобоксом для выбора страны.
        assertEquals("Russia", selected, "В поле страна неверное значение.");
        // Далее заполняем все поля данными и прописываем сразу проверки каждого поля
        // на соответствие выходных данных введенным значениям.
        WebElement streetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldStreetLocator));
        streetElement.clear();
        streetElement.sendKeys("ул. Седова, дом 5");
        String street = streetElement.getAttribute("value");
        assertEquals("ул. Седова, дом 5", street, "Данные в поля 'улицы' не соответствуют введенным данным.");

        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldCityLocator));
        cityElement.clear();
        cityElement.sendKeys("Москва");
        String city = cityElement.getAttribute("value");
        assertEquals("Москва", city, "Город в поле город не соответствует ожидаемому значению");

        WebElement regionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldRegionLocator));
        regionElement.clear();
        regionElement.sendKeys("Московская область");
        String region = regionElement.getAttribute("value");
        assertEquals("Московская область", region, "Область в поле региона не соответствует ожидаемому значению");

        WebElement postalCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPostalCode));
        postalCodeElement.clear();
        postalCodeElement.sendKeys("103274");
        String postalCode = postalCodeElement.getAttribute("value");
        assertEquals("103274", postalCode, "Индекс неверный.");

        WebElement phoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPhone));
        phoneElement.clear();
        phoneElement.sendKeys("+7(985) 765-89-45");
        String phone = phoneElement.getAttribute("value");
        assertEquals("+7(985) 765-89-45", phone, "Телефон неверный.");

        WebElement emailElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldEmailOrderPage));
        emailElement.clear();
        emailElement.sendKeys(email);
        String emailOrderPage = emailElement.getAttribute("value");
        assertEquals(email, emailOrderPage, "Почта неверная.");

        WebElement commentsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldComments));
        commentsElement.clear();
        commentsElement.sendKeys("Пришлите, пожалуйста мой заказ до 20.02.2027 г., в первой половине дня.");
        String comment = commentsElement.getAttribute("value");
        assertEquals("Пришлите, пожалуйста мой заказ до 20.02.2027 г., в первой половине дня.", comment,
                "Сообщение в поле для ввода комментариев не соответствует ожидаемому результату.");

        WebElement buttonAddOrderElement = wait.until(ExpectedConditions.elementToBeClickable(buttonPlaceAnOrderLocator));
        buttonAddOrderElement.click();
        String expectedUrl = "/order-received/";
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        WebElement titleAddOrderPage = wait.until(ExpectedConditions.visibilityOfElementLocated(titleOrderReceiptPageLocator));
        String actualTitle = titleAddOrderPage.getText();
        String expectedTitle = "Заказ получен";
        String currentAddOrderPageUrl = driver.getCurrentUrl();
        assertTrue(currentAddOrderPageUrl.contains(expectedUrl), "Адрес страницы :" + currentAddOrderPageUrl +
                ", что не соответствует ожидаемому адресу.");
        assertEquals(expectedTitle, actualTitle,
                "Заголовок на странице :" + actualTitle + " , что не соответствует ожидаемому заголовку.");

    }

    // Метод testplacingAnOrderWithCouponAndCashOnDelivery - метод тестирует оформление заказа на странице заказа
    // с применением купона на скидку и выбора способа оплаты "Оплата при доставке".
    @Test
    public void testplacingAnOrderWithCouponAndCashOnDelivery() {

        driver.navigate().to(urlHomePage);

        // Добавляем книгу в корзину через клик по кнопке Каталог в меню хедера -> клик по ссылке Книги на странице каталога
        // в блоке Категории товаров -> на странице с книгами клик по кнопке Корзина, в одной из карточек с книгой.
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement linkBooksPage = wait.until(ExpectedConditions.elementToBeClickable(linkBooksPageLocator));
        linkBooksPage.click();
        WebElement priceBook = wait.until(ExpectedConditions.visibilityOfElementLocated(pricePerBookLocator));
        String expectedPrice = priceBook.getText();
        WebElement buttonBooksCart = wait.until(ExpectedConditions.elementToBeClickable(martinFordAddToCartLocator));
        buttonBooksCart.click();
        //Ждем появления кнопки ПОДРОБНЕЕ, чтобы в корзину успел добавиться товар и кликаем по ней для перехода в корзину.
        WebElement detailsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonDetailsLocator));
        detailsButton.click();
        wait.until(ExpectedConditions.urlContains("/cart/"));
        WebElement fieldTextInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookTextInCardtLocator));

        String expectedTextFieldCart = "Мартин Форд. Роботы наступают";
        String actualTextFieldCart = fieldTextInCart.getText();

        // Проверяем, что текст в поле Корзины Товар соответствует названию книги, которая была добавлена в корзину.
        assertTrue(actualTextFieldCart.contains(expectedTextFieldCart),
                "Текст поля Товар в корзине содержит ожидаемый текст из заголовка книги.");

        // Проверяем, что количество товара в поле Корзины КОЛИЧЕСТВО соответствует количеству книг, которое было добавлено в корзину.
        WebElement countBooksInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookCountInCardtLocator));
        int actualCountBookInCart = Integer.parseInt(countBooksInCart.getAttribute("value"));
        assertEquals(1, actualCountBookInCart,
                "Количество книг не равно 1");

        // Переходим из ссылки "Оформление заказа" в футере на страницу оформления заказа.
        WebElement orderPageElement = wait.until(ExpectedConditions.elementToBeClickable(footerLinkOrderPageLocator));
        orderPageElement.click();

        // 3. Оформление заказа после добавления товара в корзину и перехода на страницу оформления заказа.

        // Проверяем, что попали на страницу оформления заказа.
        String currentUrlOrderPage = driver.getCurrentUrl();
        assertTrue(currentUrlOrderPage.contains("/checkout/"),
                "Адрес страницы не соответствует ожидаемому адресу и равен " + currentUrlOrderPage);

        // Добавляем купон.
        // ссылка "Нажмите для ввода купона"
        // ждём, когда ссылка станет кликабельной, и кликаем
        WebElement showCouponLinkLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(couponFieldActivationButtonLocator));
        showCouponLinkLocator.click();

        // убеждаемся, что форма стала видимой (display: block)
        WebElement couponInput =
                wait.until(ExpectedConditions.visibilityOfElementLocated(couponInputLocator));
        // Проверяем, что форма для ввода купона появилась.
        assertTrue(couponInput.isDisplayed(), "Форма купона не отображается");
        // Очищаем поле для ввода купона и вводим в него купон.
        couponInput.clear();
        couponInput.sendKeys(coupon);

        // --- до клика по кнопке купона ---
        WebElement totalBeforeElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(actualTotalCartPriceLocator));
        String totalBeforeText = totalBeforeElement.getText();

        // нажимаем "Применить купон"
        WebElement buttonActivateCouponElement = wait.until(
                ExpectedConditions.elementToBeClickable(applyCouponButtonLocator));
        buttonActivateCouponElement.click();

       // ЖДЁМ, ПОКА СУММА "К ОПЛАТЕ" ИЗМЕНИТСЯ
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBe(actualTotalCartPriceLocator, totalBeforeText)
        ));

       // теперь читаем суммы после применения купона
        WebElement priceBookElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(subtotalCartPriceLocator));
        String expectedTotalPriceString = priceBookElement.getText();
        int expectedTotalPriceInt = parsePriceToRubles(expectedTotalPriceString);

        WebElement totalPriceElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(actualTotalCartPriceLocator));
        String actualTotalPriceString = totalPriceElement.getText();
        int actualTotalPriceInt = parsePriceToRubles(actualTotalPriceString);

        // проверяем, что скидка ровно 500
        expectedTotalPriceInt = expectedTotalPriceInt - 500;
        assertEquals(expectedTotalPriceInt, actualTotalPriceInt, "Финальная сумма заказа неверная.");

        // Находим поле имени и заполняем его.
        WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldNameOrderPageLocator));
        nameElement.clear();
        nameElement.sendKeys("Василий");
        String nameText = nameElement.getAttribute("value");
        // Проверяем поле имя.
        assertEquals("Василий", nameText, "Имя не соответствует ожидаемому значению.");

        // Находим поле фамилии и заполняем его, предварительно очистив.
        WebElement surnameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldSurnameOrderPageLocator));
        surnameElement.clear();
        surnameElement.sendKeys("Иванов");
        String surnameText = surnameElement.getAttribute("value");
        // Проверяем поле фамилии.
        assertEquals("Иванов", surnameText, "Фамилия не соответствует ожидаемому значению.");

        // Находим HTML-элемент <select id="billing_country">, используя его, через select выберем страну в соответствующем поле.
        WebElement countrySelect = wait.until(ExpectedConditions.presenceOfElementLocated(
                billingCountrySelectLocator
        ));

        Select select = new Select(countrySelect);
        select.selectByVisibleText("Russia");
        String selected = select.getFirstSelectedOption().getText();
        // Проверяем поле с комбобоксом для выбора страны.
        assertEquals("Russia", selected, "В поле страна неверное значение.");
        // Далее заполняем все поля данными и прописываем сразу проверки каждого поля
        // на соответствие выходных данных введенным значениям.
        WebElement streetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldStreetLocator));
        streetElement.clear();
        streetElement.sendKeys("ул. Седова, дом 5");
        String street = streetElement.getAttribute("value");
        assertEquals("ул. Седова, дом 5", street, "Данные в поля 'улицы' не соответствуют введенным данным.");

        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldCityLocator));
        cityElement.clear();
        cityElement.sendKeys("Москва");
        String city = cityElement.getAttribute("value");
        assertEquals("Москва", city, "Город в поле город не соответствует ожидаемому значению");

        WebElement regionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldRegionLocator));
        regionElement.clear();
        regionElement.sendKeys("Московская область");
        String region = regionElement.getAttribute("value");
        assertEquals("Московская область", region, "Область в поле региона не соответствует ожидаемому значению");

        WebElement postalCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPostalCode));
        postalCodeElement.clear();
        postalCodeElement.sendKeys("103274");
        String postalCode = postalCodeElement.getAttribute("value");
        assertEquals("103274", postalCode, "Индекс неверный.");

        WebElement phoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPhone));
        phoneElement.clear();
        phoneElement.sendKeys("+7(985) 765-89-45");
        String phone = phoneElement.getAttribute("value");
        assertEquals("+7(985) 765-89-45", phone, "Телефон неверный.");

        WebElement emailElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldEmailOrderPage));
        emailElement.clear();
        emailElement.sendKeys(email);
        String emailOrderPage = emailElement.getAttribute("value");
        assertEquals(email, emailOrderPage, "Почта неверная.");

        WebElement commentsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldComments));
        commentsElement.clear();
        commentsElement.sendKeys("Пришлите, пожалуйста мой заказ до 20.02.2027 г., в первой половине дня.");
        String comment = commentsElement.getAttribute("value");
        assertEquals("Пришлите, пожалуйста мой заказ до 20.02.2027 г., в первой половине дня.", comment,
                "Сообщение в поле для ввода комментариев не соответствует ожидаемому результату.");

        // Найдем элемент радиокнопки "Оплата при доставке" и кликнем по нему.
        WebElement radioButtonElement = wait.until(ExpectedConditions.elementToBeClickable(radioButtonCashOnDeliveryLocator));
        radioButtonElement.click();

        // Найдем блок с сообщением о выбранной форме оплаты и проверим содержание ссобщения.
        WebElement messagePaymentFormNoticeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentFormNoticeLocator));
        String actualMessagePaymentForm = messagePaymentFormNoticeElement.getText();
        String expectedMessagePaymentForm = "Оплата наличными при доставке заказа.";

        // Проверим, применился ли выбор опллаты.
        assertTrue(messagePaymentFormNoticeElement.isDisplayed(), "Ожидаемый блок с сообщением не появился");
        assertEquals(expectedMessagePaymentForm, actualMessagePaymentForm,
                "Сообщение об оплате :" + actualMessagePaymentForm + " не соответствует ожиданию.");


        WebElement buttonAddOrderElement = wait.until(ExpectedConditions.elementToBeClickable(buttonPlaceAnOrderLocator));
        buttonAddOrderElement.click();
        String expectedUrl = "/order-received/";
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        WebElement titleAddOrderPage = wait.until(ExpectedConditions.visibilityOfElementLocated(titleOrderReceiptPageLocator));
        String actualTitle = titleAddOrderPage.getText();
        String expectedTitle = "Заказ получен";
        String currentAddOrderPageUrl = driver.getCurrentUrl();
        assertTrue(currentAddOrderPageUrl.contains(expectedUrl), "Адрес страницы :" + currentAddOrderPageUrl +
                ", что не соответствует ожидаемому адресу.");
        assertEquals(expectedTitle, actualTitle,
                "Заголовок на странице :" + actualTitle + " , что не соответствует ожидаемому заголовку.");
    }


    // Метод testCheckoutPageWithEmptyFields тестирует страницу "ОФОРМЛЕНИЕ ЗАКАЗА"
    // при попытке сделать заказ, не заполнив обязательные поля.
    @Test
    public void testCheckoutPageWithEmptyFields() {

        driver.navigate().to(urlHomePage);

        // Добавляем книгу в корзину через клик по кнопке Каталог в меню хедера -> клик по ссылке Книги на странице каталога
        // в блоке Категории товаров -> на странице с книгами клик по кнопке Корзина, в одной из карточек с книгой.
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement linkBooksPage = wait.until(ExpectedConditions.elementToBeClickable(linkBooksPageLocator));
        linkBooksPage.click();
        WebElement priceBook = wait.until(ExpectedConditions.visibilityOfElementLocated(pricePerBookLocator));
        String expectedPrice = priceBook.getText();
        WebElement buttonBooksCart = wait.until(ExpectedConditions.elementToBeClickable(martinFordAddToCartLocator));
        buttonBooksCart.click();
        //Ждем появления кнопки ПОДРОБНЕЕ, чтобы в корзину успел добавиться товар и кликаем по ней для перехода в корзину.
        WebElement detailsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonDetailsLocator));
        detailsButton.click();
        wait.until(ExpectedConditions.urlContains("/cart/"));
        WebElement fieldTextInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookTextInCardtLocator));

        String expectedTextFieldCart = "Мартин Форд. Роботы наступают";
        String actualTextFieldCart = fieldTextInCart.getText();

        // Проверяем, что текст в поле Корзины Товар соответствует названию книги, которая была добавлена в корзину.
        assertTrue(actualTextFieldCart.contains(expectedTextFieldCart),
                "Текст поля Товар в корзине содержит ожидаемый текст из заголовка книги.");

        // Проверяем, что количество товара в поле Корзины КОЛИЧЕСТВО соответствует количеству книг, которое было добавлено в корзину.
        WebElement countBooksInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookCountInCardtLocator));
        int actualCountBookInCart = Integer.parseInt(countBooksInCart.getAttribute("value"));
        assertEquals(1, actualCountBookInCart,
                "Количество книг не равно 1");

        // Проверяем поле К ОПЛАТЕ на странице КОРЗИНА на соответствие ожидаемой сумме.
        WebElement orderAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentItemOnTheCartPageLocator));
        String actualOrderAmount = orderAmount.getText();
        assertEquals(expectedPrice, actualOrderAmount,
                "Сумма в поле К ОПЛАТЕ в ЗАКАЗЕ на странице КОРЗИНЫ неверная.");

        // Нажимаем кнопку ОФОРМИТЬ ЗАКАЗ.
        WebElement buttonAddOrder = wait.until(ExpectedConditions.elementToBeClickable(buttonPlaceOrderLocator));
        buttonAddOrder.click();
        wait.until(ExpectedConditions.urlContains("/checkout/"));

        // 3. Оформление заказа после добавления товара в корзину и клика по кнопке ОФОРМИТЬ ЗАКАЗ.

        // Проверяем, что попали на страницу оформления заказа.
        String currentUrlOrderPage = driver.getCurrentUrl();
        assertTrue(currentUrlOrderPage.contains("/checkout/"),
                "Адрес страницы не соответствует ожидаемому адресу и равен " + currentUrlOrderPage);

        // Находим поле имени и заполняем его.


        // Находим поле имени и очищаем его.
        WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldNameOrderPageLocator));
        nameElement.clear();
        String nameText = nameElement.getAttribute("value");
        // Проверяем поле имя.
        assertTrue(nameText.isEmpty(), "Поле имя не пустое.");

        // Находим поле фамилия и очищаем его.
        WebElement surnameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldSurnameOrderPageLocator));
        surnameElement.clear();
        String surnameText = surnameElement.getAttribute("value");
        // Проверяем поле фамилии.
        assertTrue(surnameText.isEmpty(), "Поле фамилия не пустое.");

        // Далее находим поочередно все поля и очищаем их, на случай того, что в них могли
        // остаться старые данные и прописываем сразу проверки каждого поля.
        WebElement streetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldStreetLocator));
        streetElement.clear();

        String street = streetElement.getAttribute("value");
        assertTrue(street.isEmpty(), "Поле улицы не пустое.");

        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldCityLocator));
        cityElement.clear();
        String city = cityElement.getAttribute("value");
        assertTrue(city.isEmpty(), "Поле город не пустое");

        WebElement regionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldRegionLocator));
        regionElement.clear();
        String region = regionElement.getAttribute("value");
        assertTrue(region.isEmpty(), "Поле региона не пустое");

        WebElement postalCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPostalCode));
        postalCodeElement.clear();
        String postalCode = postalCodeElement.getAttribute("value");
        assertTrue(postalCode.isEmpty(), "Поле Индекс не пустое.");

        WebElement phoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldPhone));
        phoneElement.clear();
        String phone = phoneElement.getAttribute("value");
        assertTrue(phone.isEmpty(), "Поле телефон не пустое.");

        WebElement emailElement = wait.until(ExpectedConditions.presenceOfElementLocated(fieldEmailOrderPage));
        emailElement.clear();
        String emailOrderPage = emailElement.getAttribute("value");
        assertTrue(emailOrderPage.isEmpty(), "Поле Почта не пустое.");

        WebElement commentsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldComments));
        commentsElement.clear();
        String comment = commentsElement.getAttribute("value");
        assertTrue(comment.isEmpty(),
                "Поле для ввода комментариев не пустое.");

        WebElement buttonAddOrderElement = wait.until(ExpectedConditions.elementToBeClickable(buttonPlaceAnOrderLocator));
        buttonAddOrderElement.click();
        // Проверяем сообщение для поля имени.
        WebElement fieldNameMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nameFieldValidationMessageLocator));
        String actualFieldNameMessage = fieldNameMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(fieldNameMessageElement.isDisplayed(), "Сообщение не появилось для пустого поля имени.");
        assertEquals("Имя для выставления счета обязательное поле.", actualFieldNameMessage,
                "Текст " + actualFieldNameMessage + " не соответствует ожидаемому тексту для пустого поля имени.");

        // Проверяем сообщение для поля фамилии.
        WebElement fieldLastNameMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameFieldValidationMessageLocator));
        String actualFieldLastNameMessage = fieldLastNameMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();
        assertTrue(fieldLastNameMessageElement.isDisplayed(), "Сообщение не появилось для пустого поля фамилии.");
        assertEquals("Фамилия для выставления счета обязательное поле.", actualFieldLastNameMessage,
                "Текст " + actualFieldLastNameMessage + " не соответствует ожидаемому тексту для пустого поля фамилии.");

        // Проверяем сообщение для поля адреса.
        WebElement addressFieldMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addressFieldValidationMessageLocator));
        String actualAddressFiledMessage = addressFieldMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(addressFieldMessageElement.isDisplayed(), "Сообщение не появилось для пустого поля адреса.");
        assertEquals("Адрес для выставления счета обязательное поле.", actualAddressFiledMessage,
                "Текст " + actualAddressFiledMessage + " не соответствует ожидаемому тексту для пустого поля адреса.");

        // Проверяем сообщение для поля города.
        WebElement cityFieldMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cityFieldValidationMessageLocator));
        String actualCityFieldMessage = cityFieldMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(cityFieldMessageElement.isDisplayed(), "Сообщение не появилось для пустого поля города.");
        assertEquals("Город / Населенный пункт для выставления счета обязательное поле.", actualCityFieldMessage,
                "Текст " + actualCityFieldMessage + " не соответствует ожидаемому тексту для пустого поля города.");

        // Проверяем сообщение для поля почтового индекса.
        WebElement indexFieldMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(indexFieldValidationMessageLocator));
        String actualIndexFieldMessage = indexFieldMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(indexFieldMessageElement.isDisplayed(), "Сообщение не появилось для пустого поля почтового индекса.");
        assertEquals("Почтовый индекс для выставления счета обязательное поле.", actualIndexFieldMessage,
                "Текст " + actualIndexFieldMessage + " не соответствует ожидаемому тексту для пустого поля города.");

        // Проверяем сообщения неверного номера телефона.

        WebElement invalidPhoneFieldMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneErrorInvalidNumberLocator));
        String actualInvalidPhoneFieldMessage = invalidPhoneFieldMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(invalidPhoneFieldMessageElement.isDisplayed(), "Сообщение для неверного номера телефона не появилось для поля телефона.");
        assertEquals("неверный номер телефона.", actualInvalidPhoneFieldMessage,
                "Текст " + actualInvalidPhoneFieldMessage + " не соответствует ожидаемому тексту для пустого поля города.");


        // Проверяем сообщение для пустого поля номера телефона.
        WebElement emptyFieldPhoneMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneErrorRequiredLocator));
        String actualEmptyFieldPhoneMessage = emptyFieldPhoneMessageElement.getText()
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(emptyFieldPhoneMessageElement.isDisplayed(),"Сообщение для пустого номера телефона не появилось для поля телефона.");
        assertEquals("Телефон для выставления счета обязательное поле.",actualEmptyFieldPhoneMessage,
                "Текст " + actualEmptyFieldPhoneMessage + " не соответствует ожидаемому тексту для пустого поля города.");
    }


}


