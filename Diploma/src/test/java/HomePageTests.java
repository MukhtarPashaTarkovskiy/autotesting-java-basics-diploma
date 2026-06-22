import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class HomePageTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private String urlHomePage = "http://intershop5.skillbox.ru/";

    /* pageBooksSectionLocator - локатор ищет блок КНИГИ на главной странице */
    private By pageBooksSectionLocator = By.xpath("//aside[contains(@class,'widget')]//a[contains(@href,'/catalog/books/')]");

    /* booksHeaderLocator - локатор ищет главный заголовок на странице КНИГИ, после перехода на нее с главной страницы */
    private By booksHeaderLocator = By.xpath("//h1[contains(@class,'entry-title') and text()='Книги']");

    /* pageTabletsSectionLocator - локатор ищет блок ПЛАНШЕТЫ на главной странице */
    private By pageTabletsSectionLocator = By.xpath("//aside[contains(@class,'widget')]//a[contains(@href,'/electronics/pad/')]");

    /* tabletsHeaderLocator - локатор ищет главный заголовок на странице ПЛАНШЕТЫ, после перехода на нее с главной страницы */
    private By tabletsHeaderLocator = By.xpath("//h1[contains(@class,'entry-title') and text()='Планшеты']");

    /* pageBooksSectionLocator - локатор ищет блок ФОТОАППАРАТЫ на главной странице */
    private By pageCamerasSectionLocator = By.xpath("//aside[contains(@class,'widget')]//a[contains(@href,'/electronics/photo_video/')]");

    /* camerasHeaderLocator - локатор ищет заголовок на странице Фото/видео, после перехода на нее с главной страницы */
    private By camerasHeaderLocator = By.xpath("//h1[contains(@class,'entry-title') and text()='Фото/видео']");

    /* buttonLadaAddCartLinkSaleSectionLocator - локатор ищет кнопку 'В корзину'
    в карточке товара Lada, после перехода в нее из раздела Распродажа главной страницы сайта */
    private By buttonLadaProductCardFromTheSectionLocator = By.xpath("//div[@id='primary']//*[@name='add-to-cart']");

    /* Локатор qtyInput ищет элемент, который меняет количество товаров в корзине, карточки товара Lada,
    после перехода в нее из раздела Распродажа главной страницы сайта*/
    private By qtyInput = By.xpath("//div[@class='quantity']//input[@name='quantity']");

    /* ladaLinkLocatorHomepage – локатор находит ссылку на товар "Lada"
    в ПЕРВОМ активном слайде секции «РАСПРОДАЖА» на главной странице */
    private By ladaLinkLocatorHomepage = By.xpath("(//aside[@id='accesspress_store_product-2']//li[contains(@class,'slick-active')]//a[@title='Lada'])[1]");

    /* buttonCartMenuMainPageLocator - локатор ищет кнопку корзину в главном меню навигации в хедере */
    private By buttonCartMenuHeadearLocator = By.xpath("//div[@id='menu']//a[contains(@href,'/cart/')]");


    /* ladaLinkLocator - локатор находит на страницы корзины строку товара Lada */
    private By ladaLinkLocator = By.xpath("//td[contains(@class,'product-name')]//a[normalize-space()='Lada']");

    /* ladaQtyLocator - локатор ищет на страницы Корзина поле с количеством товара, которое было добавлено в корзину в карточке товара Lada */
    private By ladaQtyLocator = By.xpath(
            "//tr[contains(@class,'cart_item')]" +
                    "[.//td[contains(@class,'product-name')]//a[normalize-space()='Lada']]" +
                    "//td[contains(@class,'product-quantity')]//input[@type='number']"
    );

    /* ipadPromoBannerTitleLocator - локатор находит блок промо 'Уже в продаже' */
    private By ipadPromoBannerLocator = By.xpath("//aside[@id='accesspress_store_full_promo-2']//a[contains(@href,'?product=ipad-2020-32gb-wi-fi')]");

    /* ipadPromoBannerTitleLocator - локатор находит заголовок карточки товара iPad, после перехода в нее с главной страницы раздела промо */
    private By ipadPromoBannerTitleLocator = By.xpath("//div[contains(@class,'summary')]//h1[contains(text(),'iPad 2020 32gb wi-fi')]");

    /* firstLadaProductCardInNewArrivalsSectionLocator - локатор находит первую карточку товара Lada в разделе НОВЫЕ ПОСТУПЛЕНИЯ. */
    private By firstProductCardLadaInNewArrivalsSectionLocator = By.xpath(
            "(//aside[@id='accesspress_store_product-3']" +
                    "//li[contains(@class,'slick-slide') and contains(@class,'slick-active') and @aria-hidden='false']" +
                    "//a[@title='Lada' and contains(@href,'/product/lada')])[1]"
    );

    /* actualTitleProductCardLadaInNewArrivalsSectionLocator - локатор ищет элемент с заголовком в карточке товара, после перехода в нее с раздела НОВЫЕ ПОСТУПЛЕНИЯ. */
    private By actualTitleProductCardLadaInNewArrivalsSectionLocator = By.xpath("//div[contains(@class,'summary')]//h1[contains(@class,'product_title') and text()='Lada']");

    /* loginAccountButtonLocator - локатор ищет кнопку входа в аккаунт Войти, в хедере главной страницы */
    private By loginAccountButtonLocator = By.cssSelector("header .top-header.clearfix a.account");

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

    /* Метод testHomePageBooksSection тестирует секцию главной страницы: "КНИГИ". */
    @Test
    public void testHomePageBooksSection() {

        // 1. Открываем главную
        driver.navigate().to(urlHomePage);

        // 2. Кликаем по блоку «Книги»
        WebElement booksLink = wait.until(
                ExpectedConditions.elementToBeClickable(pageBooksSectionLocator)
        );
        booksLink.click();

        // 3. Ждём, пока изменится URL
        wait.until(ExpectedConditions.urlContains("/product-category/catalog/books/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product-category/catalog/books/"),
                "Ожидали URL раздела Книги, но получили: " + currentUrl);

        // 4. Ждём появления заголовка КНИГИ и проверяем его текст
        WebElement booksHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(booksHeaderLocator)
        );

        assertEquals("КНИГИ", booksHeader.getText().trim(),
                "Заголовок раздела не совпадает с 'КНИГИ'");
    }

    /* Метод testHomePageBooksSection тестирует секцию главной страницы: "ПЛАНШЕТЫ". */
    @Test
    public void testHomePageTabletsSections() {
        driver.navigate().to(urlHomePage);
        WebElement tabletsLink = wait.until(ExpectedConditions.elementToBeClickable(pageTabletsSectionLocator));
        tabletsLink.click();

        wait.until(ExpectedConditions.urlContains("/product-category/catalog/electronics/pad/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product-category/catalog/electronics/pad/"),
                "Ожидали URL раздела Планшеты, но получили: " + currentUrl);

        WebElement tabletsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(tabletsHeaderLocator));
        assertEquals("ПЛАНШЕТЫ", tabletsHeader.getText().trim(),
                "Заголовок раздела не совпадает с 'Планшеты'");

    }

    /* Метод testHomePageBooksSection тестирует секцию главной страницы: "ФОТОАППАРАТЫ". */
    @Test
    public void testHomePageCamerasSections() {
        driver.navigate().to(urlHomePage);
        WebElement camerasLink = wait.until(ExpectedConditions.elementToBeClickable(pageCamerasSectionLocator));
        camerasLink.click();

        wait.until(ExpectedConditions.urlContains("/product-category/catalog/electronics/photo_video/"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product-category/catalog/electronics/photo_video/"),
                "Ожидали URL раздела Фотоаппараты, но получили: " + currentUrl);

        WebElement tabletsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(camerasHeaderLocator));
        assertEquals("ФОТО/ВИДЕО", tabletsHeader.getText().trim(),
                "Заголовок раздела не совпадает с 'ФОТО/ВИДЕО'");

    }


    /* Метод testHomePageSaleSections тестирует секцию главной страницы: "РАСПРОДАЖА". */
    @Disabled("Тест временно отключён: на учебном сайте изменилась карточка товара Lada")
    /* Метод testHomePageSaleSections тестирует секцию главной страницы: "РАСПРОДАЖА". */
    @Test
    public void testHomePageSaleSections() {

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

    /* Метод testPromoSection тестирует секцию главной страницы: "УЖЕ В ПРОДАЖЕ". */
    @Test
    public void testPromoSection() {

        driver.navigate().to(urlHomePage);
        // Находим элемент промораздела под разделом Распродажа и кликаем по нему
        WebElement promoSection = wait.until(ExpectedConditions.elementToBeClickable(ipadPromoBannerLocator));
        promoSection.click();

        // Проверяем переход на страницу с карточкой товара iPad 2020 32gb wi-fi
        wait.until(ExpectedConditions.urlContains("/?product=ipad-2020-32gb-wi-fi"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/?product=ipad-2020-32gb-wi-fi"),
                "Ожидали URL страницы с карточкой товара iPad 2020, но получили: " + currentUrl);

        // Проверяем текст заголовка в карточке товара iPad 2020 32gb wi-fi
        WebElement promoIpadTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(ipadPromoBannerTitleLocator));
        String actualResult = promoIpadTitle.getText();
        assertEquals("iPad 2020 32gb wi-fi", actualResult,
                "Текст заголовка карточки товара не соответствует ожидаемому тексту");

    }

    @Disabled("Тест временно отключён: товар Lada в слайдере учебного сайта отображается нестабильно")
    /* Метод testNewArrivalsSection - тестирует переход на страницу товара из раздела НОВЫЕ ПОСТУПЛЕНИЯ */
    @Test
    public void testNewArrivalsSection() {

        driver.navigate().to(urlHomePage);

        WebElement firstCard = wait.until(ExpectedConditions.elementToBeClickable(firstProductCardLadaInNewArrivalsSectionLocator));
        // Находим и сохраняем в переменную значение заголовка тестируемой карточки из раздела НОВЫЕ ПОСТУПЛЕНИЯ.
        String expectedTitle = firstCard.getAttribute("title");
        Actions actions = new Actions(driver);
        actions.moveToElement(firstCard).click().perform();
        WebElement atualTitleLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(actualTitleProductCardLadaInNewArrivalsSectionLocator));
        // Находим и сохраняем в переменную значение заголовка в карточке товара, после перехода на страницу товара кликом.
        String actualTitle = atualTitleLocator.getText();
        // Проверяем, что попали на страницу карточки Lada
        assertTrue(driver.getCurrentUrl().contains("/product/lada/"));
        assertEquals(expectedTitle, actualTitle,
                "Текст заголовка в карточке товара не соответствует ожидаемому значению.");

    }

/* Метод testButtonLoginAccount тестирует переход на страницу авторизации кликом по кнопке 'Войти'. */
    @Test
    public void testButtonLoginAccount() {

        driver.navigate().to(urlHomePage);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginAccountButtonLocator));
        loginButton.click();
        String currentUrl = driver.getCurrentUrl();
        // Проверяем соответствует ли адрес страницы, на которую произошел переход по адресу страницы авторизации
        assertTrue(currentUrl.contains("/my-account/"),"Адрес страницы не соответствует ожидаемому");
    }


}


