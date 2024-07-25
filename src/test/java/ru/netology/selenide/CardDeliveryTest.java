package ru.netology.selenide;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    // Объекты тестовых элементов с css-селекторами
    SelenideElement cityField = $("[data-test-id='city'] input");
    SelenideElement dateField = $("[data-test-id='date'] input");
    SelenideElement nameField = $("[data-test-id='name'] input");
    SelenideElement phoneField = $("[data-test-id='phone'] input");
    SelenideElement agreementCheckbox = $("[data-test-id='agreement']");
    SelenideElement submitButton = $(".button");
    SelenideElement loadingIndicator = $(".button__icon");
    SelenideElement successNotification = $(".notification__content");

    // Тестовые данные
    String validCity = "Санкт-Петербург";
    String invalidCity = "Гатчина";
    String validDate = generateDate(3, "dd.MM.yyyy");
    String invalidDate = generateDate(2, "dd.MM.yyyy");
    String validName = "а а";
    String invalidName = "f f";
    String validPhone = "+79200000000";
    String invalidPhone = "79200000000";

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitIfAllCorrect() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка состояния кнопки загрузки
        loadingIndicator.should(appear);

        // Проверка всплывающего окна об успешном завершении бронирования, содержашего дату
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
        successNotification.shouldHave(text(validDate));
    }

    @Test
    void shouldNotifyIfIncorrectCity() {

        // Заполнение поля "Город" некорректным значением
        cityField.setValue(invalidCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Город"
        $("[data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='city'] .input__sub").shouldBe(visible).shouldHave(text(("Доставка в выбранный город недоступна")));
    }

    @Test
    void shouldNotifyIfEmptyCity() {

        // Заполнение поля "Город" пустым значением
        cityField.setValue("");

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Город"
        $("[data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='city'] .input__sub").shouldBe(visible).shouldHave(text(("Поле обязательно для заполнения")));
    }

    @Test
    void shouldNotifyIfIncorrectDate() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата" некорректным значением
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(invalidDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Дата"
        dateField.parent().parent().$(".input_invalid .input__sub").shouldBe(visible);
        dateField.parent().parent().$(".input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotifyIfEmptyDate() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата" пустым значением
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue("");

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Дата"
        dateField.parent().parent().$(".input_invalid .input__sub").shouldBe(visible);
        dateField.parent().parent().$(".input_invalid .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotifyIfIncorrectName() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя" некорректным значением
        nameField.setValue(invalidName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Фамилия и имя"
        $("[data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='name'] .input__sub").shouldBe(visible).shouldHave(text(("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")));
    }

    @Test
    void shouldNotifyIfEmptyName() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя" пустым значением
        nameField.setValue("");

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Фамилия и имя"
        $("[data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='name'] .input__sub").shouldBe(visible).shouldHave(text(("Поле обязательно для заполнения")));
    }

    @Test
    void shouldNotifyIfIncorrectPhone() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона" некорректным значением
        phoneField.setValue(invalidPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Номер телефона"
        $("[data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='phone'] .input__sub").shouldBe(visible).shouldHave(text(("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")));
    }

    @Test
    void shouldNotifyIfEmptyPhone() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона" пустым значением
        phoneField.setValue("");

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка ошибки для поля "Номер телефона"
        $("[data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $("[data-test-id='phone'] .input__sub").shouldBe(visible).shouldHave(text(("Поле обязательно для заполнения")));
    }

    @Test
    void shouldNotifyIfNoAgreement() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка добавления нового класса в элемент с "Cоглашением"
        agreementCheckbox.shouldHave(cssClass("input_invalid")).shouldBe(visible);
    }

    @Test
    void shouldSubmitWithCityAutocomplete() {
        String cityStart = "Са"; // первые две буквы города
        String cityFull = "Санкт-Петербург"; // полный город для выбора
        SelenideElement cityDropdown = $(".menu-item__control"); // элемент выпадающего списка городов

        // Заполнение поля "Город" и выбор города из автодополнения
        cityField.setValue(cityStart);
        cityDropdown.shouldBe(visible);
        $$(".menu-item__control").findBy(text(cityFull)).click();

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка состояния кнопки загрузки
        loadingIndicator.should(appear);

        // Проверка всплывающего окна об успешном завершении бронирования, содержашего дату
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
        successNotification.shouldHave(text(validDate));
    }

    @Test
    void shouldSubmitWithCityAutocompleteFromSubstring() {
        String citySubstring = "ом"; // Буквы в середине названия города
        String cityFull = "Томск"; // полный город для выбора
        SelenideElement cityDropdown = $(".menu-item__control"); // элемент выпадающего списка городов

        // Заполнение поля "Город" и выбор города из автодополнения
        cityField.setValue(citySubstring);
        cityDropdown.shouldBe(visible);
        $$(".menu-item__control").findBy(text(cityFull)).click();

        // Очистка от значения по умолчанию и заполнение поля "Дата"
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateField.setValue(validDate);

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка состояния кнопки загрузки
        loadingIndicator.should(appear);

        // Проверка всплывающего окна об успешном завершении бронирования, содержашего дату
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
        successNotification.shouldHave(text(validDate));
    }

    @Test
    void shouldSubmitWithCalendarPopupWeekAhead() {

        // Заполнение поля "Город"
        cityField.setValue(validCity);

        // Открытие календаря и выбор даты на неделю вперед
        dateField.click(); // открывает высплывающий календарь
        LocalDate targetDate = LocalDate.now().plusWeeks(1); // +1 неделя от сегодняшней даты

        // Текущий и целевой месяцы
        int currentMonth = LocalDate.now().getMonthValue();
        int targetMonth = targetDate.getMonthValue();

        // Если целевой месяц отличается от текущего, переключаем календарь на следующий месяц
        if (currentMonth != targetMonth) {
            $(".calendar__arrow_direction_right[data-step='1']").click(); // переключение на следующий месяц
        }

        // Выбор дня в календаре
        String day = targetDate.format(DateTimeFormatter.ofPattern("d"));
        $$(".calendar__day").findBy(text(day)).click();

        // Заполнение поля "Фамилия и имя"
        nameField.setValue(validName);

        // Заполнение поля "Номер телефона"
        phoneField.setValue(validPhone);

        // Клик на чекбокс с "Согласием" и проверка поля
        agreementCheckbox.$(".checkbox__box").click();
        agreementCheckbox.$(".checkbox__control").shouldBe(selected);

        // Клик на кнопку "Забронировать"
        submitButton.click();

        // Проверка состояния кнопки загрузки
        loadingIndicator.should(appear);

        // Проверка всплывающего окна об успешном завершении бронирования, содержашего дату
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
        successNotification.shouldHave(text(targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}