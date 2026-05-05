import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Paths;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductCatalogTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private String urlHomePage = "http://intershop5.skillbox.ru/";

    // productSearchBoxLocator - локатор ищет поле поиска товара
    private By productSearchBoxFieldLocator = By.cssSelector(".search-form input.search-field[name='s']");

    // productSearchBoxButtonLocator - локатор ищет кнопку лупы, которая запускает поиск товара из поля поиска товара.
    private By productSearchBoxButtonLocator = By.cssSelector(".search-form button.searchsubmit[type='submit']");

    // - локатор находит заголовок на странице Результата поиска товара
    private By titlePageResultSearchProductLocator = By.cssSelector("#title_bread_wrap h1.entry-title.ak-container");

    // - локатор находит кнопку Каталок в меню хедера
    private By headerMenuCatalogLocator = By.xpath("//ul[contains(@id,'menu-primary-menu')]//a[contains(text(),'Каталог')]");

    // blockProductCategoriesLocator - локатор ищет блок КАТЕГОРИИ ТОВАРОВ на странице КАТАЛОГ.
    private By blockProductCategoriesLocator = By.xpath("//div[@id='secondary']//span[contains(@class,'widget-title') and text()='Категории товаров']");

    // linkFooterAllProductsPageLocator - локатор ищет в футере ссылку на страницу ВСЕ ТОВАРЫ в футере.
    private By linkFooterAllProductsPageLocator = By.xpath("//footer[@id='colophon']//a[contains(@href,'/shop/')]");

    // titleAllProductsPageLocator - локатор ищет главный заголовок на странице ВСЕ ТОВАРЫ.
    private By titleAllProductsPageLocator = By.xpath(
            "//header[@id='title_bread_wrap']//h1[contains(@class,'entry-title') and text()='Все товары']"
    );

    // productCategoryWashLink - локатор ищет категорию товара 'Стиральные машинки'.
    private By productCategoryWashLink = By.xpath("//ul[contains(@class,'product-categories')]//a[contains(@href,'/wash/')]");

    // washingMachineLocator - Локатор ищет карточку стиральной машинки на странице СТИРАЛЬНЫЕ МАШИНЫ.
    private By washingMachineLocator = By.xpath("//div[contains(@class,'collection_desc')]//h3[contains(normalize-space(.), 'BEKO WRE64P1BWW')]");

    // buttonCartWashingMachine - локатор ищет кнопку В КОРЗИНУ в карточке товара стиральная машина, с id=95.
    private By buttonCartWashingMachineLocator = By.xpath("//a[@data-product_id='95' and contains(@class,'add_to_cart_button')]");

    // washingMachineMoreDetailsButtonLocator - локатор находит кнопку ПОДРОБНЕЕ в карточек товара СТИРАЛЬНАЯ МАШИНА с id=95.
    private By washingMachineMoreDetailsButtonLocator = By.xpath("//a[@data-product_id='95' and contains(@class,'add_to_cart_button')]/following-sibling::a[normalize-space()='Подробнее']");

    // washingMachineMoreDetailsButtonLocator - локатор ищет поле с названием товара в строке стиральная машина с id=95, которая была добавлена в корзину.
    private By washingMachineLinkInTheShoppingCartLocator = By.xpath("//tr[contains(@class,'cart_item')]\n" +
            "    [.//a[contains(normalize-space(.), 'BEKO WRE64P1BWW')]]\n" +
            "    //td[contains(@class,'product-name')]//a");

    // numberOfWashingMachinesInCartLocator - локатор ищет поле с количеством товара в строке стиральная машина с id=95, которая была добавлена в корзину.
    private By numberOfWashingMachinesInCartLocator = By.xpath("//tr[contains(@class,'cart_item')]\n" +
            "    [.//a[contains(normalize-space(.), 'BEKO WRE64P1BWW')]]\n" +
            "    //td[contains(@class,'product-quantity')]//input[contains(@class,'qty')]");

    @BeforeEach
    public void setUp() {
        String driverPath = Paths.get("drivers", "chromedriver.exe").toString();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

    }

    @AfterEach
    public void tearDown() {

        if (driver != null) driver.quit();

    }


    // Метод testProductSearchBox тестирует поиск товара через поле поиска в хедере страницы.
    @Test
    public void testProductSearchBox() {

        driver.navigate().to(urlHomePage);
        //Находим элемент поле поиска товара
        WebElement fieldSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(productSearchBoxFieldLocator));
        fieldSearch.sendKeys("Стиральная машина");

        // Находим и кликаем кнопку лупы, рядом с полем поиска товара.
        WebElement buttonSearch = wait.until(ExpectedConditions.elementToBeClickable(productSearchBoxButtonLocator));
        buttonSearch.click();

        // Проверяем что нас перекинуло на нужную страницу
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("post_type=product"),
                "В URL нет post_type=product");
        WebElement titleResult = wait.until(ExpectedConditions.visibilityOfElementLocated(titlePageResultSearchProductLocator));
        String expectedTitle = "РЕЗУЛЬТАТЫ ПОИСКА: “СТИРАЛЬНАЯ МАШИНА”";
        String actualTitle = titleResult.getText();
        assertEquals(expectedTitle, actualTitle,
                "Текст заголовка страницы не соответствует ожидаемому значению");

    }

    // Метод testButtonCatalogMenuHeader тестирует переход на страницу каталога кликом по кнопке Каталог в меню хедера.
    @Test
    public void testButtonCatalogMenuHeader() {

        driver.navigate().to(urlHomePage);
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement titleBlockCategoriesProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(blockProductCategoriesLocator));
        String expectedTitle = "КАТЕГОРИИ ТОВАРОВ";
        String actualTitle = titleBlockCategoriesProduct.getText();
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "/product-category/catalog/";

        // Проверяем url открывшейся страницы и есть ли на ней блок: КАТЕГОРИИ ТОВАРОВ.
        assertTrue(currentUrl.contains(expectedUrl),
                "Адрес страницы не соответствует ожиданию и возвращает " + currentUrl);
        assertEquals(expectedTitle, actualTitle,
                "Название блока не равно названию: КАТЕГОРИИ ТОВАРОВ ");

    }

    // Метод testTransitionFromTheFooterToTheAllProductsPage тестирует
    // переход на страницу каталога кликом по навигационной ссылке 'Все товары' в меню футера.
    @Test
    public void testTransitionFromTheFooterToTheAllProductsPage() {
        driver.navigate().to(urlHomePage);
        WebElement linkAllProducts = wait.until(ExpectedConditions.elementToBeClickable(linkFooterAllProductsPageLocator));
        linkAllProducts.click();
        String expectedUrl = "/shop/";
        String currentUrl = driver.getCurrentUrl();

        // Проверяем, что после клика в футере на ссылку Все товары, произошел переход на страницу Все товары.
        assertTrue(currentUrl.contains(expectedUrl),
                "Url не соответсвует ожидаемому и равен " + currentUrl);
        WebElement blockProductCatrgories = wait.until(ExpectedConditions.visibilityOfElementLocated(blockProductCategoriesLocator));
        String actualTitle = blockProductCatrgories.getText();
        String expectedTitle = "КАТЕГОРИИ ТОВАРОВ";
        assertEquals(expectedTitle, actualTitle,
                "На странице 'ВСЕ ТОВАРЫ' текст заголовка блока 'КАТЕГОРИИ ТОВАРОВ' отличается от ожидаемого значения");
    }

    //  Метод testAddingProductToCartFromTheProductCatalog тестирует добавление товара в корзину, при выборе товара через каталог товаров.
    @Test
    public void testAddingProductToCartFromTheProductCatalog() {

        driver.navigate().to(urlHomePage);

        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement titleBlockCategoriesProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(blockProductCategoriesLocator));
        String expectedTitle = "КАТЕГОРИИ ТОВАРОВ";
        String actualTitle = titleBlockCategoriesProduct.getText();
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "/product-category/catalog/";

        // Проверяем url открывшейся страницы и есть ли на ней блок: КАТЕГОРИИ ТОВАРОВ.
        assertTrue(currentUrl.contains(expectedUrl),
                "Адрес страницы не соответствует ожиданию и возвращает " + currentUrl);
        assertEquals(expectedTitle, actualTitle,
                "Название блока не равно названию: КАТЕГОРИИ ТОВАРОВ ");

        // В блоке КАТЕГОРИИ ТОВАРОВ кликаем по категории стиральные машины.
        WebElement linkWash = wait.until(ExpectedConditions.elementToBeClickable(productCategoryWashLink));
        linkWash.click();

        // Проверяем на соответствие адрес страницы 'Стиральных машин', после клика в блоке 'КАТЕГОРИИ ТОВАРОВ' по ссылке на эту страницу.
        String expectedUrlWashMachines = "/catalog/appliances/wash/";
        String actualCurrentUrlWashMachines = driver.getCurrentUrl();
        assertTrue(actualCurrentUrlWashMachines.contains(expectedUrlWashMachines),
                "Адрес странице не соответствует ожидаемому адресу страницы со стиральными машинам, текущий адрес такой: " + currentUrl);

        // Находим кнопку В КОРЗИНУ и кликаем по ней.
        WebElement buttonAddToCart = wait.until(ExpectedConditions.elementToBeClickable(buttonCartWashingMachineLocator));
        buttonAddToCart.click();

        // Кликаем по появившейся кнопке ПОДРОБНЕЕ.
        WebElement buttonMoreDetails = wait.until(ExpectedConditions.elementToBeClickable(washingMachineMoreDetailsButtonLocator));
        buttonMoreDetails.click();

        WebElement lineWashingMachineInTheCart = wait.until(ExpectedConditions.visibilityOfElementLocated(washingMachineLinkInTheShoppingCartLocator));
        String expectedTextLink = "Стиральная машина BEKO WRE64P1BWW";
        String actualTextLink = lineWashingMachineInTheCart.getText();

        // Проверяем, что в корзину добавилась стиральная машинка.
        assertTrue(actualTextLink.contains(expectedTextLink),
                "Текст в поле с названием товара в корзине, не содержит названия добавленной стиральной машинки");

        WebElement numberWashingMachines = wait.until(ExpectedConditions.visibilityOfElementLocated(numberOfWashingMachinesInCartLocator));
        int actualCountWashingMachinesInTheCart = Integer.parseInt(numberWashingMachines.getAttribute("value"));
        // Проверяем на соответствие количество в корзине стиральных машин с соответствующими параметрами.
        assertEquals(1, actualCountWashingMachinesInTheCart,
                "Количество товара Стиральная машина в козине не равно 1");
    }

    // Метод тестирует переход в карточку товара по пути: клик по кнопке КАТАЛОГ в хедере -> клик по ссылке Стиральные машины
// в блоке КАТЕГОРИИ ТОВАРОВ -> клик по карточке товара (Стиральной машине) на странице со стиральными машинами.
    @Test
    public void testGoToProductCard() {
        driver.navigate().to(urlHomePage);
        WebElement buttonCatalog = wait.until(ExpectedConditions.elementToBeClickable(headerMenuCatalogLocator));
        buttonCatalog.click();
        WebElement titleBlockCategoriesProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(blockProductCategoriesLocator));
        String expectedTitle = "КАТЕГОРИИ ТОВАРОВ";
        String actualTitle = titleBlockCategoriesProduct.getText();
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "/product-category/catalog/";
        // Проверяем url открывшейся страницы и есть ли на ней блок: КАТЕГОРИИ ТОВАРОВ.
        assertTrue(currentUrl.contains(expectedUrl),
                "Адрес страницы не соответствует ожиданию и возвращает " + currentUrl);
        assertEquals(expectedTitle, actualTitle,
                "Название блока не равно названию: КАТЕГОРИИ ТОВАРОВ ");

        // В блоке КАТЕГОРИИ ТОВАРОВ кликаем по категории стиральные машины.
        WebElement linkWash = wait.until(ExpectedConditions.elementToBeClickable(productCategoryWashLink));
        linkWash.click();

        String expectedUrlWashMachines = "/catalog/appliances/wash/";
        String actualCurrentUrlWashMachines = driver.getCurrentUrl();
        // Проверяем на соответствие адрес страницы 'Стиральных машин', после клика в блоке 'КАТЕГОРИИ ТОВАРОВ' по ссылке на эту страницу.
        assertTrue(actualCurrentUrlWashMachines.contains(expectedUrlWashMachines),
                "Адрес странице не соответствует ожидаемому адресу страницы со стиральными машинам, текущий адрес такой: " + currentUrl);

        // Кликаем по карточке товара со стиральной машиной на странице со стиральными машинами.
        WebElement washingMachine = wait.until(ExpectedConditions.visibilityOfElementLocated(washingMachineLocator));
        washingMachine.click();
        String currentWashMachineUrl = driver.getCurrentUrl();
        //Проверяем корректно ли отрабатывает клик по карточке товара в разделе СТИРАЛЬНЫЕ МАШИНЫ, происходит ли переход на страницу карточки.
        assertTrue(currentWashMachineUrl.contains("wre64p1bww"),
                " Адрес страницы неверный");
    }

}
