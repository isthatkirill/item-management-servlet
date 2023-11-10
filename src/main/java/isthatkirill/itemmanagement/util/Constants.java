package isthatkirill.itemmanagement.util;

/**
 * @author Kirill Emelyanov
 */

public class Constants {

    public static String COMMON_NAVBAR = """
            <div class="navbar">
                <a href="/item">Главная</a>
                <div class="dropdown">
                    <button class="dropbtn">Товары
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item/create-item">Добавить товар</a>
                        <a href="/item/get-item">Просмотреть информацию о товаре</a>
                        <a href="/item/update-item">Изменить товар</a>
                        <a href="/item/delete-item">Удалить товар</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Категории
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item/create-category">Добавить категорию</a>
                        <a href="/item/get-category">Просмотреть информацию о категории</a>
                        <a href="/item/update-category">Изменить категорию</a>
                        <a href="/item/delete-category">Удалить категорию</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Продажи
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item/create-sale">Добавить продажу</a>
                        <a href="/item/update-sale">Изменить продажу</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Поступления
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item/create-receipt">Добавить поступление</a>
                        <a href="/item/update-receipt">Изменить поступление</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Отчеты
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item/report/sale">Сгенерировать отчет о продажах</a>
                        <a href="/item/report/remainder">Сгенерировать отчет о наличии</a>
                    </div>
                </div>
            </div>
            """;

}
