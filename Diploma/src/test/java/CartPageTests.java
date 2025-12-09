import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartPageTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private String urlHomePage = "http://intershop5.skillbox.ru/";
    private String coupon = "sert500";

    // ladaLinkLocator - локатор находит на страницы корзины строку товара Lada
    private By ladaLinkLocator = By.xpath("//td[contains(@class,'product-name')]//a[normalize-space()='Lada']");

    // ladaQtyLocator - локатор ищет на страницы Корзина поле с количеством товара, которое было добавлено в корзину в карточке товара Lada
    private By ladaQtyLocator = By.xpath(
            "//tr[contains(@class,'cart_item')]" +
                    "[.//td[contains(@class,'product-name')]//a[normalize-space()='Lada']]" +
                    "//td[contains(@class,'product-quantity')]//input[@type='number']"
    );

    // ladaLinkLocatorHomepage – локатор находит ссылку на товар "Lada"
    // в ПЕРВОМ активном слайде секции «РАСПРОДАЖА» на главной странице
    private By ladaLinkLocatorHomepage = By.xpath("(//aside[@id='accesspress_store_product-2']//li[contains(@class,'slick-active')]//a[@title='Lada'])[1]");

    // buttonLadaAddCartLinkSaleSectionLocator - локатор ищет кнопку 'В корзину'
    // в карточке товара Lada, после перехода в нее из раздела Распродажа главной страницы сайта
    private By buttonLadaProductCardFromTheSectionLocator = By.xpath("//div[@id='primary']//*[@name='add-to-cart']");

    // Локатор qtyInput ищет элемент, который меняет количество товаров в корзине, карточки товара Lada,
    // после перехода в нее из раздела Распродажа главной страницы сайта
    private By qtyInput = By.xpath("//div[@class='quantity']//input[@name='quantity']");

    // buttonCartMenuMainPageLocator - локатор ищет кнопку корзину в главном меню навигации в хедере
    private By buttonCartMenuHeadearLocator = By.xpath("//div[@id='menu']//a[contains(@href,'/cart/')]");

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

    //buttonPlaceOrderLocator - локатор находит кнопку 'Оформить заказ', на странице корзины.
    private By buttonPlaceOrderLocator = By.xpath("//div[contains(@class,'wc-proceed-to-checkout')]/a[contains(normalize-space(),'ОФОРМИТЬ ЗАКАЗ')]");

    //pricePerBookLocator - локатор ищет цену книги Мартина Форда.
    private By pricePerBookLocator = By.cssSelector("li.post-5350 span.price ins .woocommerce-Price-amount bdi");

    // paymentItemOnTheCartPageLocator - локатор ищет элемент с суммой на странице корзины в заказе, в поле К ОПЛАТЕ.
    private By paymentItemOnTheCartPageLocator = By.cssSelector("tr.order-total span.woocommerce-Price-amount.amount bdi");

    // removeButton - локатор ищет кнопку для удаления определенного товара из корзины.
    private By removeButtonLocator = By.cssSelector("a.remove[data-product_id='5350']");

    // cartItemRemovedMessage - локатор ищет элемент с текстом об удалении товара из корзины на странице корзины.
    private By cartItemRemovedMessageLocator = By.cssSelector(".woocommerce-message[role='alert']");

    // returnToCartLocator - локатор находит кнопку "Вернут?" в корзине.
    private By returnToCartLocator = By.cssSelector(".woocommerce-message[role='alert'] a.restore-item");

    // linkToCartPageInFooterLocator - локатор находит в футере ссылку на страницу корзины.
    private By linkToCartPageInFooterLocator = By.cssSelector("footer#colophon li.page_item.page-item-20 a");

    // couponFieldOnTheCartPageLocator - локатор находит на странице корзины поле ввода купона.
    private By couponFieldOnTheCartPageLocator = By.cssSelector("input#coupon_code.input-text");

    // buttonCouponOnTheCartPageLocator - локатор находит кнопку "Применить купон" на странице корзины.
    private By buttonCouponOnTheCartPageLocator = By.cssSelector("button[name='apply_coupon']");

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

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    /* Метод testAddingABookToCart тестирует добавление товара в корзину. */
    @Test
    public void testAddingABookToCart() {

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

        // Проверяем, что попали на страницу оформления заказа.
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/checkout/"),
                "Адрес страницы не соответствует ожидаемому адресу и равен " + currentUrl);


    }

    // Метод testDeletingAndRestoringItemsFromTheCart тестирует удаление из корзины товара кликом по крестику и восстановление
    // в корзине товара кликом п ссылке "Вернуть?"
    @Test
    public void testDeletingAndRestoringItemsFromTheCart() {

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
        //Ждем появления кнопки ПОДРОБНЕЕ, чтобы в корзину успел добавиться товар.
        WebElement detailsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonDetailsLocator));

        // В хедере находим кнопку корзины в меню и кликаем по ней.
        WebElement cartItemMainMenu = wait.until(ExpectedConditions.elementToBeClickable(buttonCartMenuHeadearLocator));
        cartItemMainMenu.click();


        wait.until(ExpectedConditions.urlContains("/cart/"));
        WebElement fieldTextInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookTextInCardtLocator));
        String expectedTextFieldCart = "Мартин Форд. Роботы наступают";
        String actualTextFieldCart = fieldTextInCart.getText();

        // Проверяем, что текст в поле Корзины Товар соответствует названию книги, которая была добавлена в корзину.
        assertTrue(actualTextFieldCart.contains(expectedTextFieldCart),
                "Текст поля Товар в содержит ожидаемый текст из заголовка книги.");

        // Проверяем, что количество товара в поле Корзины КОЛИЧЕСТВО соответствует количеству книг, которое было добавлено в корзину.
        WebElement countBooksInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookCountInCardtLocator));
        int actualCountBookInCart = Integer.parseInt(countBooksInCart.getAttribute("value"));
        assertEquals(1, actualCountBookInCart,
                "Количество книг не равно 1");

        // Кликаем по полю с крестиком в корзине, в строке добавленного товара
        WebElement removeButtonInCart = wait.until(ExpectedConditions.elementToBeClickable(removeButtonLocator));
        removeButtonInCart.click();

        WebElement messageRemoveElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemRemovedMessageLocator));
        String actualTextMessageRemove = messageRemoveElement.getText();
        String expectedTextMessageRemove = "“Мартин Форд. Роботы наступают” удален. Вернуть?";
        // Проверяем появилось ли сообщение об удалении товара из корзины и далее проверяем текст сообщения об удалении товара из Корзины.
        assertTrue(messageRemoveElement.isDisplayed(), "Сообщение об удалении товара не появилось");
        assertEquals(expectedTextMessageRemove, actualTextMessageRemove,
                "Текст об удалении товара не соответствует ожидаемому тексту.");

        // Кликаем по кнопке "Вернуть?".
        WebElement buttonReturn = wait.until(ExpectedConditions.elementToBeClickable(returnToCartLocator));
        buttonReturn.click();

        // Ждем пока подгрузится страница корзины и на ней появится поле товар,
        // чтобы быть уверенными, что можно начать проверять строку с товаром в корзине.
        wait.until(ExpectedConditions.urlContains("/cart/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(bookTextInCardtLocator));

        // Проверяем, что текст в поле Корзины Товар соответствует названию книги, которая была добавлена в корзину.
        assertTrue(actualTextFieldCart.contains(expectedTextFieldCart),
                "Текст поля Товар в содержит ожидаемый текст из заголовка книги.");
        // Проверяем, что количество товара в поле КОЛИЧЕСТВО корзины соответствует количеству книг, которое было добавлено в корзину.
        countBooksInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(bookCountInCardtLocator));
        assertEquals(1, actualCountBookInCart,
                "Количество книг не равно 1");

    }

    // Метод testApplyingTheCouponOnTheCartPage тестирует применение купона на скидку, на странице корзины.
    @Test
    public void testApplyingTheCouponOnTheCartPage() {

        driver.navigate().to(urlHomePage);

        // Добавляем книгу в корзину через клик по кнопке Каталог в меню хедера -> клик по ссылке Книги на странице каталога
        // в блоке Категории товаров -> на странице с книгами клик по кнопке Корзина, в одной из карточек с книгой.
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement linkBooksPage = wait.until(ExpectedConditions.elementToBeClickable(linkBooksPageLocator));
        linkBooksPage.click();
        WebElement priceBook = wait.until(ExpectedConditions.visibilityOfElementLocated(pricePerBookLocator));
        String expectedPriceString = priceBook.getText();
        WebElement buttonBooksCart = wait.until(ExpectedConditions.elementToBeClickable(martinFordAddToCartLocator));
        buttonBooksCart.click();

        //Ждем появления кнопки ПОДРОБНЕЕ, чтобы в корзину успел добавиться товар.
        WebElement detailsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonDetailsLocator));

        // В блоке "Страницы сайта" футера находим кликабельную ссылку "Корзина"и кликаем по ней.
        WebElement cartItemMainMenu = wait.until(ExpectedConditions.elementToBeClickable(linkToCartPageInFooterLocator));
        cartItemMainMenu.click();


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

        // Находим поле для ввода купона.
        WebElement cuponField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(couponFieldOnTheCartPageLocator));
        WebElement buttonCuponField = wait.until(
                ExpectedConditions.elementToBeClickable(buttonCouponOnTheCartPageLocator));

// Запоминаем старую сумму "К ОПЛАТЕ" до применения купона
        WebElement orderAmountBefore = wait.until(
                ExpectedConditions.visibilityOfElementLocated(paymentItemOnTheCartPageLocator));
        String oldOrderAmountText = orderAmountBefore.getText();

// Вводим купон и жмём кнопку
        cuponField.sendKeys(coupon);
        buttonCuponField.click();

// Ждём, пока сумма изменится
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBe(paymentItemOnTheCartPageLocator, oldOrderAmountText)
        ));

// Теперь читаем НОВУЮ сумму
        WebElement orderAmount = wait.until(
                ExpectedConditions.visibilityOfElementLocated(paymentItemOnTheCartPageLocator));
        String actualOrderAmountString = orderAmount.getText();

// expectedPriceString — это цена книги, которую сохранили раньше
        int priceRub = parsePriceToRubles(expectedPriceString);
        int actualRub = parsePriceToRubles(actualOrderAmountString);

// скидка 500 руб
        int expectedRubWithDiscount = priceRub - 500;

// Проверяем
        assertEquals(expectedRubWithDiscount, actualRub,
                "Сумма в поле К ОПЛАТЕ в ЗАКАЗЕ на странице КОРЗИНЫ неверная.");
    }

    /* Метод testAdding5ItemsToCart тестирует добавление товара в Корзину в количестве 5. */
    @Test
    public void testAdding5ItemsToCart() {
        driver.navigate().to(urlHomePage);

        // Находим карточку товара Lada (li, не клон) и кликаем по ней через Actions
        WebElement ladaCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(ladaLinkLocatorHomepage)
        );

        Actions actions = new Actions(driver);
        actions.moveToElement(ladaCard).click().perform();

        // После перехода на страницу карточки, проверяем адрес страницы.
        wait.until(ExpectedConditions.urlContains("/product/lada/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/lada/"),
                "Ожидали URL страницы с карточкой товара Lada, но получили: " + currentUrl);

        // Добавление товара в корзину, в количестве 5 шт.
        WebElement quantity = wait.until(ExpectedConditions.elementToBeClickable(qtyInput));
        quantity.clear();
        quantity.sendKeys("5");

        // Находим кнопку 'В корзину' и кликаем по ней.
        WebElement elementAddCard = wait.until(ExpectedConditions.elementToBeClickable(buttonLadaProductCardFromTheSectionLocator));
        elementAddCard.click();

        // В хедере находим кнопку корзины в меню и кликаем по ней.
        WebElement cartItemMainMenu = wait.until(ExpectedConditions.elementToBeClickable(buttonCartMenuHeadearLocator));
        cartItemMainMenu.click();


        // Проверяем, что мы попали на страницу корзины.
        wait.until(ExpectedConditions.urlContains("/cart/"));
        String currentCartUrl = driver.getCurrentUrl();
        assertTrue(currentCartUrl.contains("/cart/"),
                "Ожидали URL страницы Корзины, но получили: " + currentCartUrl);

        // Проверяем, что на странице Корзины появилась строка с товаром Lada.
        WebElement ladaLink = wait.until(ExpectedConditions.visibilityOfElementLocated(ladaLinkLocator));
        assertTrue(ladaLink.isDisplayed(),
                "Ожидали видеть товар Lada в корзине, но элемент не отображается");

        //Проверяем наличие товара наименованием Lada в корзине.
        WebElement ladaQtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(ladaQtyLocator));
        int actualQty = Integer.parseInt(ladaQtyInput.getAttribute("value"));
        assertEquals(5, actualQty,
                "Количество товара Lada в корзине неверное");
    }


}



