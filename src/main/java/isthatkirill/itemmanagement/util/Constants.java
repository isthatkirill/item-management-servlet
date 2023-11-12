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
                        <a href="/item?action=create">Добавить товар</a>
                        <a href="/item?action=read">Просмотреть информацию о товаре</a>
                        <a href="/item?action=update">Изменить товар</a>
                        <a href="/item?action=delete">Удалить товар</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Категории
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/category?action=create">Добавить категорию</a>
                        <a href="/category?action=read">Просмотреть информацию о категории</a>
                        <a href="/category?action=update">Изменить категорию</a>
                        <a href="/category?action=delete">Удалить категорию</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Продажи
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item?action=create-sale">Добавить продажу</a>
                        <a href="/item?action=update-sale">Изменить продажу</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Поступления
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item?action=create-receipt">Добавить поступление</a>
                        <a href="/item?action=update-receipt">Изменить поступление</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Отчеты
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/item?action=report">Сгенерировать отчет о продажах</a>
                        <a href="/item?action=report">Сгенерировать отчет о наличии</a>
                    </div>
                </div>
            </div>
            """;

}
