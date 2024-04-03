import com.codeborne.selenide.*;
import com.codeborne.selenide.junit5.TextReportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
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
        Selenide.actions().clickAndHold(elementA.shouldBe(visible)).moveToElement(elementB.shouldBe(visible)).release().build().perform();

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

        Selenide.actions().contextClick(contextMenu).perform();

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

        SelenideElement texts = $(Selectors.withText("Eius"));
        SelenideElement button = $(byId("page-footer"));

        open(BASE_URL + "infinite_scroll");

        int currentSwipeCount = 0;

        while (!texts.isDisplayed()) {
            button.scrollTo();
            /*
            или
            Selenide.actions.scrollToElement(button);
            или
            Selenide.actions().scrollByAmount(0, 500).perform();
             */


            System.out.println("iter = " + currentSwipeCount);
            currentSwipeCount++;
        }

        // Проверить, что элемент виден
        texts.shouldBe(Condition.visible);
        System.out.println(texts.innerText());
    }

    /**
     * Перейти на страницу Key Presses.
     * Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
     * Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.
     */
    @DisplayName("Key Presses 1")
    @Test
    public void test51KeyPresses() {

        SelenideElement body = $(byId("target"));
        SelenideElement result = $(byId("result"));

        List<String> keysToPress1 = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
        List<Keys> keysToPress2 = List.of(Keys.ENTER, Keys.CONTROL, Keys.ALT, Keys.TAB);

        open(BASE_URL + "key_presses");

        body.shouldBe(visible);

        keysToPress1.forEach(key -> {
            Selenide.actions().sendKeys(key).perform();
            result.shouldHave(Condition.text("You entered: " + key));
        });

        keysToPress2.forEach(key -> {
            Selenide.actions().sendKeys(key).perform();
            result.shouldHave(Condition.text("You entered: " + key.name()));
        });
    }

    @DisplayName("Key Presses 2")
    @Test
    public void test52KeyPresses() {
        SelenideElement result = $(byId("result"));

        List<Object> keysToPress = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", Keys.ENTER, Keys.CONTROL, Keys.ALT, Keys.TAB);

        open(BASE_URL + "key_presses");

        keysToPress.forEach(key -> {
            if (key instanceof String letter) {
                Selenide.actions().sendKeys(letter).perform();
                result.shouldHave(Condition.text("You entered: " + letter));
            } else if (key instanceof Keys keys) {
                Selenide.actions().sendKeys(keys).perform();
                result.shouldHave(Condition.text("You entered: " + keys.name()));
            }
        });
    }
}

