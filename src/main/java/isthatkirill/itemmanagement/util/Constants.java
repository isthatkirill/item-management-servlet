package isthatkirill.itemmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String CREATE_CATEGORY = "INSERT INTO categories (name, description) VALUES (?, ?)";
    public static final String FIND_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    public static final String EXISTS_CATEGORY_BY_ID = "SELECT 1 FROM categories WHERE id = ?";
    public static final String FIND_ALL_CATEGORIES = "SELECT * FROM categories ORDER BY id ASC";
    public static final String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id = ?";
    public static final String FIND_STOCK_BY_ID = "SELECT stock_units FROM items WHERE id = ?";
    public static final String FIND_ALL_ITEMS_SHORT = "SELECT id, name FROM items ORDER BY id ASC";
    public static final String UPDATE_ITEM_SUPPLY = "UPDATE items SET purchase_price = ?, stock_units = stock_units + ? WHERE id = ?";
    public static final String UPDATE_ITEM_SALE = "UPDATE items SET sale_price = ?, stock_units = stock_units - ? WHERE id = ?";
    public static final String DELETE_ITEM_BY_ID = "DELETE FROM items WHERE id = ?";
    public static final String FIND_SUPPLY_BY_ID = "SELECT * FROM supplies WHERE id = ?";
    public static final String EXISTS_ITEM_BY_ID = "SELECT 1 FROM items WHERE id = ?";
    public static final String CHECK_DATA = "Проверьте правильность вводимых данных.";

    public static final String CREATE_ITEM = """
            INSERT INTO items (name, description, purchase_price, sale_price, stock_units, brand, created_at, category_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String FIND_ITEM_BY_ID = """
            SELECT i.*, c.name as category_name FROM items i
            LEFT JOIN categories c ON i.category_id = c.id WHERE i.id = ?
            """;
    public static final String CREATE_SALE = "INSERT INTO sales (amount, price, item_id, created_at) VALUES (?, ?, ?, ?)";
    public static final String FIND_AVERAGE_SALE_PRICE = """
            SELECT CAST(SUM(amount * price) AS DECIMAL(10, 2)) / SUM(amount) AS average_price
            FROM sales WHERE item_id = ?
            """;
    public static final String FIND_ALL_SALES_EXTENDED = """
            SELECT s.*, i.name as item_name FROM sales s
            LEFT JOIN items i ON i.id = s.item_id ORDER BY s.id ASC
            """;
    public static final String FIND_SALE_BY_ID = "SELECT * FROM sales WHERE id = ?";
    public static final String CREATE_SUPPLY = """
            INSERT INTO supplies (company, amount, price, item_id, created_at)
            VALUES (?, ?, ?, ?, ?)
            """;
    public static final String FIND_AVERAGE_SUPPLY_PRICE = """
            SELECT CAST(SUM(amount * price) AS DECIMAL(10, 2)) / SUM(amount) AS average_price
            FROM supplies WHERE item_id = ?
            """;
    public static final String FIND_ALL_SUPPLIES_EXTENDED = """
            SELECT s.*, i.name as item_name FROM supplies s
            LEFT JOIN items i ON i.id = s.item_id ORDER BY s.id ASC
            """;

    public static final String GET_CATEGORY_STOCK_REPORT = """
            SELECT
                c.id,
                c.name,
                c.description,
                COUNT(i.id) as items_in_category,
                SUM(i.stock_units) as stock_units,
                CAST(SUM(i.stock_units * i.purchase_price) AS DECIMAL(13, 2)) as stock_price,
                COUNT(s.item_id) as supplies_count,
                MAX(s.last_supply_date) as last_supply_date,
                (SELECT
                    CONCAT(i_inner.name, ' (id = ', i_inner.id, ' stock = ', i_inner.stock_units, ')')
                    FROM items i_inner
                    WHERE i_inner.category_id = c.id
                    ORDER BY i_inner.stock_units DESC LIMIT 1) as most_units_item,\s
                (SELECT
                    CONCAT(i_inner.name, ' (id = ', i_inner.id, ' stock = ', i_inner.stock_units, ')')
                    FROM items i_inner
                    WHERE i_inner.category_id = c.id
                    ORDER BY i_inner.stock_units ASC LIMIT 1) as less_units_item,\s
                (SELECT
                    CONCAT(i_inner.name, ' (id = ', i_inner.id, ' price = ', CAST(i_inner.purchase_price AS DECIMAL(13, 2)), ')')
                    FROM items i_inner
                    WHERE i_inner.category_id = c.id
                    ORDER BY i_inner.purchase_price DESC LIMIT 1) as most_expensive_item,
                (SELECT
                    CONCAT(i_inner.name, ' (id = ', i_inner.id, ' price = ', CAST(i_inner.purchase_price AS DECIMAL(13, 2)), ')')
                    FROM items i_inner
                    WHERE i_inner.category_id = c.id
                    ORDER BY i_inner.purchase_price ASC LIMIT 1) as most_cheap_item
            FROM categories c
            LEFT JOIN items i ON c.id = i.category_id
            LEFT JOIN (
                SELECT
                    s.item_id,
                    COUNT(s.id) as supplies_count,
                    MAX(s.created_at) as last_supply_date
                FROM supplies s
                GROUP BY s.item_id
            ) s ON i.id = s.item_id
            GROUP BY c.id, c.name, c.description
            ORDER BY c.id;
            """;

    public static final String GET_CATEGORY_SALE_REPORT = """
            SELECT
                c.id,
                c.name,
                c.description,
                SUM(sup.supply_price) AS supply_price,
                SUM(sal.sale_price) AS sale_price,
                SUM(sal.sale_price - sup.supply_price) AS profit,
                CAST(SUM(sal.sale_price - sup.supply_price) / SUM(sup.supply_price) * 100 AS DECIMAL(13, 2)) AS profit_percentage,
                SUM(sal.sold) AS sold,
                MAX(sal.last_sale_date) AS last_sale_date,
                SUM(sal.sales_count) AS sales_count
            FROM categories c
            LEFT JOIN items i ON c.id = i.category_id
            LEFT JOIN (
                SELECT
                    item_id,
                    CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS supply_price
                FROM supplies
                GROUP BY item_id
            ) sup ON i.id = sup.item_id
            LEFT JOIN (
                SELECT
                    item_id,
                    CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS sale_price,
                    SUM(amount) AS sold,
                    MAX(created_at) AS last_sale_date,
                    COUNT(sales) AS sales_count
                FROM sales
                GROUP BY item_id
            ) sal ON i.id = sal.item_id
            GROUP BY c.id, c.name, c.description
            ORDER BY c.id;
                    """;

    public static final String GET_ITEM_SALE_REPORT = """
            SELECT
                i.id,
                i.name,
                i.description,
                i.brand,
                sup.supply_price,
                sal.sale_price,
                sal.sale_price - sup.supply_price AS profit,
                CAST((sal.sale_price - sup.supply_price) / sup.supply_price * 100 AS DECIMAL(13, 2)) as profit_percentage,
                sal.sold,
                sal.last_sale_date,
                sal.sales_count,
                sal.most_big_sale_ttl_price
            FROM items i
            LEFT JOIN ( SELECT item_id, CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS supply_price
                        FROM supplies GROUP BY item_id) sup ON i.id = sup.item_id
            LEFT JOIN ( SELECT item_id, CAST(SUM(amount * price) AS DECIMAL(13, 2)) AS sale_price,
                        SUM(amount) as sold, MAX(created_at) as last_sale_date, COUNT(sales) as sales_count,
                        CAST(MAX(amount * price) AS DECIMAL(13, 2)) as most_big_sale_ttl_price\s
                        FROM sales GROUP BY item_id) sal ON i.id = sal.item_id
            ORDER BY i.id
                    """;

    public static final String GET_ITEM_STOCK_REPORT = """
            SELECT
                 i.id,
                 i.name,
                 i.description,
                 i.brand,
                 i.stock_units,
                 CAST(i.purchase_price AS DECIMAL(10, 2)),\s
                 CAST((i.stock_units * i.purchase_price) AS DECIMAL(10, 2)) AS stock_price,
                 c.name as category_name,
                 s.last_supply_date,
                 s.supplies_count
             FROM items i
             LEFT JOIN categories c ON c.id = i.category_id
             LEFT JOIN ( SELECT item_id, MAX(created_at) as last_supply_date,
                        COUNT(id) as supplies_count FROM supplies GROUP BY
                         item_id) s ON i.id = s.item_id
             ORDER BY i.id
                    """;

    public static final List<String> POSSIBLE_STOCK_ITEM_FIELDS = new ArrayList<>() {{
        add("name");
        add("description");
        add("brand");
        add("stock_units");
        add("purchase_price");
        add("stock_price");
        add("category_name");
        add("supplies_count");
        add("last_supply_date");
    }};

    public static List<String> POSSIBLE_STOCK_CATEGORY_FIELDS = new ArrayList<>() {{
        add("name");
        add("description");
        add("items_in_category");
        add("stock_units");
        add("stock_price");
        add("supplies_count");
        add("last_supply_date");
        add("most_units_item");
        add("less_units_item");
        add("most_cheap_item");
        add("most_expensive_item");
    }};

    public static List<String> POSSIBLE_PROFIT_ITEM_FIELDS = new ArrayList<>() {{
        add("name");
        add("description");
        add("brand");
        add("supply_price");
        add("sale_price");
        add("profit");
        add("profit_percentage");
        add("sold");
        add("last_sale_date");
        add("sales_count");
        add("most_big_sale_ttl_price");
    }};

    public static List<String> POSSIBLE_PROFIT_CATEGORY_FIELDS = new ArrayList<>() {{
        add("name");
        add("description");
        add("supply_price");
        add("sale_price");
        add("profit");
        add("profit_percentage");
        add("sold");
        add("last_sale_date");
        add("sales_count");
    }};

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final DateTimeFormatter FORMATTER_FILE = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");

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
                <a href="/report">Отчеты</a>
                <div class="theme-toggle">
                        <label class="switch">
                            <input type="checkbox" onclick="toggleTheme()" id="themeToggle">
                            <span class="slider round"></span>
                        </label>
                    </div>
            </div>
            """;

    public static final String CREATE_TABLE_TEST = """
            create table if not exists categories (
            	id bigint generated by default as identity primary key,
            	name varchar(128) not null,
            	description varchar(1024) not null
            );
                        
            CREATE TABLE IF NOT EXISTS items (
            	id bigint generated by default as identity primary key,
            	name varchar(128) not null,
            	description varchar(1024) not null,
            	purchase_price decimal,
            	sale_price decimal,
            	stock_units int not null,
            	brand varchar(128),
            	created_at timestamp not null,
            	category_id bigint references categories (id) ON DELETE SET NULL
            );
                        
            CREATE TABLE IF NOT EXISTS supplies (
            	id bigint generated by default as identity primary key,
            	company VARCHAR(255),
            	amount bigint not null,
            	price decimal not null,
            	item_id bigint not null references items (id) ON DELETE CASCADE,
            	created_at TIMESTAMP NOT NULL
            );
                        
            CREATE TABLE IF NOT EXISTS sales (
            	id bigint generated by default as identity primary key,
            	amount bigint not null,
            	price decimal not null,
            	item_id bigint not null references items (id) ON DELETE CASCADE,
            	created_at TIMESTAMP NOT NULL
            );
            """;

    public static final String DROP_TABLE_TEST = "drop table if exists supplies, items, categories, sales CASCADE";
}
