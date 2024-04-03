import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.TextReportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({TextReportExtension.class})
public class WebTests extends BaseTest {

    /**
     * Перейти на страницу Drag and Drop.
     * Перетащить элемент A на элемент B.
     * Задача на 10 баллов – сделать это, не прибегая к методу DragAndDrop().
     * Проверить, что элементы поменялись местами
     */
    @DisplayName("Drag and Drop 1")
    @Test
    public void test11DragAndDrop() {
        SelenideElement elementA = $(byId("column-a"));
        SelenideElement elementB = $(byId("column-b"));

        open(BASE_URL + "drag_and_drop");

        // Проверяем, что элементы на своих местах
        elementA.shouldHave(text("A"));
        elementB.shouldHave(text("B"));

        // Перетаскиваем элемент A на элемент B
        elementA.dragAndDrop(DragAndDropOptions.to(elementB));

        // Проверяем, что элементы поменялись местами
        elementA.shouldHave(text("B"));
        elementB.shouldHave(text("A"));
    }

    @DisplayName("Drag and Drop 2")
    @Test
    public void test12DragAndDrop() {
        SelenideElement elementA = $(byId("column-a"));
        SelenideElement elementB = $(byId("column-b"));

        open(BASE_URL + "drag_and_drop");

        // Проверяем, что элементы на своих местах
        elementA.shouldHave(text("A"));
        elementB.shouldHave(text("B"));

        // Перетаскиваем элемент A на элемент B
        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        actions.clickAndHold(elementA.shouldBe(visible)).moveToElement(elementB.shouldBe(visible)).release().build().perform();

        // Проверяем, что элементы поменялись местами
        elementA.shouldHave(text("B"));
        elementB.shouldHave(text("A"));
    }

    /**
     * Перейти на страницу Context menu.
     * Нажать правой кнопкой мыши на отмеченной области и проверить, что JS Alert имеет ожидаемый текст.
     */
    @DisplayName("Context menu")
    @Test
    public void test2ContextMenu() {

        SelenideElement contextMenu = $(byId("hot-spot"));

        open(BASE_URL + "context_menu");

        actions().contextClick(contextMenu).perform();

        Alert alert = switchTo().alert();
        assertEquals("You selected a context menu", alert.getText());
        alert.accept();
    }

    /**
     * Перейти на страницу Infinite Scroll.
     * Проскролить страницу до текста «Eius», проверить, что текст в поле зрения.
     */
    @DisplayName("Infinite Scroll")
    @Test
    public void test3InfiniteScroll() {

        SelenideElement textElement = $(byText("eius")).shouldBe(Condition.visible, Duration.ofSeconds(10));
        SelenideElement button = $(byId("page-footer"));

        open(BASE_URL + "infinite_scroll");

        while (!textElement.isDisplayed()) {
            //Actions actions = new Actions(WebDriverRunner.getWebDriver());
            //actions.scrollToElement(button);
            button.scrollTo();
        }

        // Проверить, что элемент виден
        textElement.shouldBe(Condition.visible);
    }

    /**
     * Перейти на страницу Key Presses.
     * Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
     * Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.
     */
    @DisplayName("Key Presses")
    @Test
    public void test4KeyPresses() {

        SelenideElement body = $(byId("target"));
        SelenideElement result = $(byId("result"));

        List<String> keysToPress1 = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j").collect(Collectors.toList());

        Map<String, CharSequence> keyMap = new HashMap<>();
        keyMap.put("ENTER", Keys.ENTER);
        keyMap.put("CONTROL", Keys.CONTROL);
        keyMap.put("ALT", Keys.ALT);
        keyMap.put("TAB", Keys.TAB);

        open(BASE_URL + "key_presses");

        body.shouldBe(visible).click();
        for (String key : keysToPress1) {
            body.shouldBe(visible).sendKeys(key);
            result.shouldHave(Condition.text("You entered: " + key));
        }

        body.shouldBe(visible).sendKeys(Keys.CONTROL);
        result.shouldHave(Condition.text("You entered: CONTROL"));

        body.shouldBe(visible).sendKeys(Keys.ALT);
        result.shouldHave(Condition.text("You entered: ALT"));

        body.shouldBe(visible).sendKeys(Keys.TAB);
        result.shouldHave(Condition.text("You entered: TAB"));

        body.shouldBe(visible).sendKeys(Keys.ENTER);
        result.shouldHave(Condition.text("You entered: ENTER"));
    }
}

