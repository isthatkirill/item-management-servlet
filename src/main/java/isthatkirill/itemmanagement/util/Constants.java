package isthatkirill.itemmanagement.util;

import java.time.format.DateTimeFormatter;

/**
 * @author Kirill Emelyanov
 */

public class Constants {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static final String COMMON_NAVBAR = """
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
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Поступления
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/supply?action=create">Добавить поступление</a>
                        <a href="/supply?action=update">Изменить поступление</a>
                    </div>
                </div>
                <div class="dropdown">
                    <button class="dropbtn">Продажи
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="/sale?action=create">Добавить продажу</a>
                        <a href="/sale?action=update">Изменить продажу</a>
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
                <div class="theme-toggle">
                        <label class="switch">
                            <input type="checkbox" onclick="toggleTheme()" id="themeToggle">
                            <span class="slider round"></span>
                        </label>
                    </div>
            </div>
            """;

}
