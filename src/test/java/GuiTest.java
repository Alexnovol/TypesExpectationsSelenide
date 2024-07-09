import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static utils.Locators.*;

public class GuiTest {

    private final String PATH_DYNAMIC_LOADING = "dynamic_loading";
    private String expEmergingText = "Hello World!";



    @BeforeAll
    public static void setUp() {

        WebDriverManager.chromedriver().setup();
        Configuration.baseUrl = "https://the-internet.herokuapp.com/";
    }


    @Test
    @DisplayName("Добавление и удаление кнопки")
    @Description("Проверка появления и исчезновения элемента")
    public void addRemoveElement() {

        open("add_remove_elements/");

        $x(XPATH_BUTTON_ADD_ELEMENT).click();

        SelenideElement buttonRemove = $x(XPATH_BUTTON_REMOVE_ELEMENT);
        buttonRemove.shouldBe(visible.because("Ожидалась кнопка удаления элемента, но ее нет"));
        buttonRemove.click();

        buttonRemove.should(disappear.because("Ожидалось исчезновение кнопки после клика"));
    }

    @Test
    @DisplayName("Авторизация в форме")
    @Description("Проверка определенного текста и определенного значения")
    public void signInForm() {

        String login = "tomsmith";
        String password = "SuperSecretPassword!";

        open("login");

        SelenideElement fieldUsername = $(ID_FIELD_USERNAME);
        fieldUsername.click();
        fieldUsername.setValue(login);
        fieldUsername.shouldHave(value(login));

        SelenideElement fieldPassword = $(ID_FIELD_PASSWORD);
        fieldPassword.click();
        fieldPassword.setValue(password);
        fieldPassword.shouldHave(value(password));

        $(CLASS_BUTTON_LOGIN).click();

        $("h4").shouldHave(text("Welcome to the Secure Area. When you are done click logout below."));

    }

    @Test
    @DisplayName("Проверка ошибки в событии onload")
    @Description("Проверка атрибута")
    public void checkErrorOnload() {

        open("javascript_error");

        $("body").shouldHave(attribute("onload", "loadError()"));
    }

    @Test
    @DisplayName("Загрузка скрытого текста")
    @Description("Проверка Таймаута")
    public void loadingHiddenText() {

        Configuration.timeout = 10000;

        open(PATH_DYNAMIC_LOADING);

        $x(XPATH_LINK_WITH_HIDDEN_ELEMENT).click();

        $x(XPATH_BUTTON_START).click();

        $x(XPATH_EMERGING_ELEMENT).shouldHave(text(expEmergingText));

    }

    @Test
    @DisplayName("Загрузка генерируемого текста")
    @Description("Проверка Слипа")
    public void loadingGeneratedText() {

        open(PATH_DYNAMIC_LOADING);

        $x(XPATH_LINK_WITH_GENERATED_ELEMENT).click();

        $x(XPATH_BUTTON_START).click();

        Selenide.sleep(10000);

        $x(XPATH_EMERGING_ELEMENT).shouldHave(text(expEmergingText));

    }

    @Test
    @DisplayName("Проверка Чек-бокса")
    @Description("Проверка атрибута")
    public void tickingCheckbox() {

        String attr = "checked";

        open("checkboxes");

        SelenideElement firstCheckbox = $x(XPATH_FIRST_CHECKBOX);
        firstCheckbox.click();
        firstCheckbox.shouldHave(attribute(attr));

        SelenideElement secondCheckbox = $x(XPATH_SECOND_CHECKBOX);
        secondCheckbox.click();
        secondCheckbox.click();
        secondCheckbox.shouldHave(attribute(attr));

    }

    @Test
    @DisplayName("Проверка Query-параметра Static в динамическом контенте")
    @Description("Проверка определенного текста")
    public void checkQueryParamDynamicContent() {

        open("dynamic_content");

        $(CSS_LINK_WITH_QUERY_PARAM).click();

        String firstText = $x(XPATH_FIRST_TEXT_DYNAMIC_CONTENT).text();
        String secondText = $x(XPATH_SECOND_TEXT_DYNAMIC_CONTENT).text();
        String thirdText = $x(XPATH_THIRD_TEXT_DYNAMIC_CONTENT).text();

        refresh();

        $x(XPATH_FIRST_TEXT_DYNAMIC_CONTENT).shouldHave(text(firstText));
        $x(XPATH_SECOND_TEXT_DYNAMIC_CONTENT).shouldHave(text(secondText));
        $x(XPATH_THIRD_TEXT_DYNAMIC_CONTENT).shouldNotHave(text(thirdText));

    }

    @Test
    @DisplayName("Смещение элемента меню")
    @Description("Проверка атрибута")
    public void shiftingElementMenu() {

        open("shifting_content/menu");

        String valLeftBefUpg = $(CSS_GALLERY_MENU_ELEMENT).getCssValue("left");

        refresh();

        $(CSS_GALLERY_MENU_ELEMENT).shouldNotHave(cssValue("left", valLeftBefUpg));

    }

    @Test
    @DisplayName("Нажатие клавиш")
    @Description("Проверка определенного текста")
    public void pressKey() {

        Keys space = Keys.SPACE;

        open("key_presses");

        $(ID_INPUT_FIELD_FOR_KEY_PRESSES).sendKeys(space);

        $(ID_RESULT_AFTER_KEY_PRESSES).shouldHave(text("You entered: " + space.name()));

    }

    @Test
    @DisplayName("Выбор элементов выпадающего списка")
    @Description("Проверка атрибута")
    public void checkDropDown() {

        String attr = "selected";

        open("dropdown");

        $x(XPATH_FIRST_ELEM_DROPDOWN).shouldHave(attribute(attr));

        SelenideElement secondElem = $x(XPATH_SECOND_ELEM_DROPDOWN);
        secondElem.click();
        secondElem.shouldHave(attribute(attr));

        SelenideElement thirdElem = $x(XPATH_THIRD_ELEM_DROPDOWN);
        thirdElem.click();
        thirdElem.shouldHave(attribute(attr));
    }

}
