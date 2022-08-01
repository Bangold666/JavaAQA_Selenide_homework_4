import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class CardOrderTest {

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }


    public String main(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldTestOrderFutureDate() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Шабалин Сергей");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestOrderToday() {
        SelenideElement form = $(".form");
        String planningDate = main(0);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Шабалин Сергей");
        form.$("[data-test-id=phone] input").setValue("+790123456781");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestOrderInUnDeliveryTown() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Нижнекамск");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Шабалин Сергей");
        form.$("[data-test-id=phone] input").setValue("+790123456781");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestOrderTownForNumbers() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("123455675");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Шабалин Сергей");
        form.$("[data-test-id=phone] input").setValue("+790123456781");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestOrderInDeliveryTownButOnLatin() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Moscow");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Шабалин Сергей");
        form.$("[data-test-id=phone] input").setValue("+790123456781");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void ShouldGoWithDoubleName() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Эмилия-Анна Васечкина");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + "22.08.2022"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void ShouldNotGoWithNameAndSymbols() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий+$@ Васечкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные" +
                " неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotGoWithNameAndNumbers() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий 1 Васечкин 2");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные" +
                " неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotGoWithNameOnLatin() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Vasya Vasechkin");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные" +
                " неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotGoWithEmptyName() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Поле обязательно для заполнения"
        ));
    }

    @Test
    void ShouldNotGoWithPhoneUnderLimit() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий Васечкин");
        form.$("[data-test-id=phone] input").setValue("+7927000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан" +
                " неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void ShouldNotGoWithPhoneOverLimit() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий Васечкин");
        form.$("[data-test-id=phone] input").setValue("+792700000001");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан" +
                " неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void ShouldNotGoWithEmptyNumber() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Поле обязательно для заполнения"
        ));
    }

    @Test
    void ShouldNotGoWithoutAgreementClick() {
        SelenideElement form = $(".form");
        String planningDate = main(3);

        form.$("[ data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Василий");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$$("button.button").last().click();

        $("[data-test-id='agreement'] [class='input__sub']").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }


}

