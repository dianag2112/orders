package magelan.orders.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import magelan.orders.product.model.Product;
import magelan.orders.product.model.ProductCategory;
import magelan.orders.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        seedMenu();
    }

    private void seedMenu() {
        log.info("Seeding Magelan menu...");

        // 1. NEW
        saveIfMissing(
                "Телешка глава / Beef head meat",
                "150 г / 150 g",
                "6.20",
                ProductCategory.NEW,
                1,
                "Ново / New",
                null,
                null,
                1
        );
        saveIfMissing(
                "Пържени кюфтенца / Fried meatballs",
                "200 г / 200 g",
                "3.60",
                ProductCategory.NEW,
                1,
                "Ново / New",
                null,
                null,
                2
        );
        saveIfMissing(
                "Горски гъби микс / Wild mushroom mix",
                "300 г / 300 g",
                "4.80",
                ProductCategory.NEW,
                1,
                "Ново / New",
                null,
                null,
                3
        );

        // 2. SOUPS
        saveIfMissing(
                "Пилешка супа / Chicken soup",
                "450 мл / 450 ml",
                "2.60",
                ProductCategory.SOUP,
                2,
                "Супи / Soups",
                null,
                null,
                1
        );
        saveIfMissing(
                "Шкембе / Tripe soup",
                "450 мл / 450 ml",
                "2.60",
                ProductCategory.SOUP,
                2,
                "Супи / Soups",
                null,
                null,
                2
        );
        saveIfMissing(
                "Таратор / Tarator <i>(cold cucumber yogurt soup)</i>",
                "500 мл / 500 ml",
                "2.30",
                ProductCategory.SOUP,
                2,
                "Супи / Soups",
                null,
                null,
                3
        );

        // 3. COLD APPETIZERS
        saveIfMissing(
                "Сирене натюр / White cheese plain",
                "100 г / 100 g",
                "3.10",
                ProductCategory.COLD_APPETIZER,
                3,
                "Студени предястия / Cold appetizers",
                null,
                null,
                1
        );
        saveIfMissing(
                "Кашкавал натюр / Yellow cheese plain",
                "100 г / 100 g",
                "4.20",
                ProductCategory.COLD_APPETIZER,
                3,
                "Студени предястия / Cold appetizers",
                null,
                null,
                2
        );
        saveIfMissing(
                "Суджук/луканка / Sudzhuk <i>(spiced dry sausage)</i>",
                "100 г / 100 g",
                "5.20",
                ProductCategory.COLD_APPETIZER,
                3,
                "Студени предястия / Cold appetizers",
                null,
                null,
                3
        );
        saveIfMissing(
                "Пастърма/филе „Елена“ / Pastirma <i>(dry-cured beef)</i> / \"File Elena\" <i>(dry-cured pork)</i>",
                "100 г / 100 g",
                "6.20",
                ProductCategory.COLD_APPETIZER,
                3,
                "Студени предястия / Cold appetizers",
                null,
                null,
                4
        );

        // 4. SALADS
        saveIfMissing(
                "Краставици / Cucumbers",
                "300 г / 300 g",
                "3.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                1
        );
        saveIfMissing(
                "Домати / Tomatoes",
                "300 г / 300 g",
                "3.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                2
        );
        saveIfMissing(
                "Домати със сирене / Tomatoes and white cheese",
                "350 г / 350 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                3
        );
        saveIfMissing(
                "Мешана / Meshana <i>(cucumbers and tomatoes)</i>",
                "300 г / 300 g",
                "3.40",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                4
        );
        saveIfMissing(
                "Шопска / Shopska <i>(tomatoes, cucumbers, peppers, onions and white cheese)</i>",
                "350 г / 350 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                5
        );
        saveIfMissing(
                "Овчарска / Ovcharska <i>(tomatoes, cucumbers, peppers, onions, ham, mushrooms and white cheese)</i>",
                "450 г / 450 g",
                "4.80",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                6
        );
        saveIfMissing(
                "Зеле и моркови / Cabbage and carrots",
                "300 г / 300 g",
                "3.10",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                7
        );
        saveIfMissing(
                "Маслини с лук / Olives and onions",
                "150 г / 150 g",
                "3.40",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                8
        );
        saveIfMissing(
                "Бобена салата / Bean salad",
                "250 г / 250 g",
                "3.10",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                9
        );
        saveIfMissing(
                "Печени чушки / Roasted peeled peppers",
                "200 г / 200 g",
                "3.40",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                10
        );
        saveIfMissing(
                "Кисели краставици / Pickles",
                "150 г / 150 g",
                "3.10",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                11
        );
        saveIfMissing(
                "Зелена салата / Green salad",
                "300 г / 300 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                12
        );
        saveIfMissing(
                "Салата моркови / Carrot salad",
                "300 г / 300 g",
                "3.40",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                13
        );
        saveIfMissing(
                "Снежанка / Snezhanka <i>(yogurt and cucumber dip)</i>",
                "250 г / 250 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                14
        );
        saveIfMissing(
                "Мексиканска салата <i>(боб, чушки, царевица, лук, чили сос)</i> / Mexican salad <i>(beans, peppers, corn, onions, chilli)</i>",
                "300 г / 300 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                15
        );
        saveIfMissing(
                "Салата \"Ружа\" <i>(домати, краставици, зеле, лук, чушки, моркови)</i> / \"Ruzha\" <i>(tomatoes, cucumbers, cabbage, onions, peppers, carrots)</i>",
                "300 г / 300 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                16
        );
        saveIfMissing(
                "Туршия / Pickled vegetables",
                "300 г / 300 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                17
        );
        saveIfMissing(
                "Салатата на шефа <i>(луканка, филе, кашкавал, гъби, лук, домати, краставици, зеле)</i> / Chef's salad <i>(sudzhuk, ham, yellow cheese, mushrooms, onions, tomatoes, cucumbers, cabbage)</i>",
                "550 г / 550 g",
                "6.90",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                18
        );
        saveIfMissing(
                "Гръцка салата <i>(домати, краставици, марули, лук, сирене, „Перла“)</i> / Greek salad <i>(tomatoes, cucumbers, lettuce, onions, white cheese, marinated small fish)</i>",
                "350 г / 350 g",
                "5.10",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                19
        );
        saveIfMissing(
                "Салата „Риба тон“ <i>(марули, домати, царевица, лук, грах, риба тон, лимон)</i> / Tuna salad <i>(lettuce, tomatoes, corn, onions, peas, tuna, lemon)</i>",
                "500 г / 500 g",
                "6.50",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                20
        );
        saveIfMissing(
                "Салата „Магелан“ <i>(марули, лук, грах, царевица, миди, рулца от раци, „Перла“, лимон)</i> / \"Magelan\" salad <i>(lettuce, onions, peas, corn, mussels, surimi sticks, small marinated fish, lemon)</i>",
                "450 г / 450 g",
                "6.90",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                21
        );
        saveIfMissing(
                "Перла / Small marinated fish",
                "100 г / 100 g",
                "3.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                22
        );
        saveIfMissing(
                "Миди / Mussels",
                "150 г / 150 g",
                "4.20",
                ProductCategory.SALAD,
                4,
                "Салати / Salads",
                null,
                null,
                23
        );

        // 5. HOT APPETIZERS
        saveIfMissing(
                "Моцарелени пръчици / Mozzarella sticks",
                "200 г / 200 g",
                "4.10",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                1
        );
        saveIfMissing(
                "Кашкавал пане / Breaded yellow cheese",
                "200 г / 200 g",
                "4.20",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                2
        );
        saveIfMissing(
                "Топено сирене пане / Breaded processed cheese",
                "200 г / 200 g",
                "4.20",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                3
        );
        saveIfMissing(
                "Пържени картофи / French fries",
                "250 г / 250 g",
                "2.40",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                4
        );
        saveIfMissing(
                "Пържени картофи със сирене / French fries with cheese",
                "300 г / 300 g",
                "2.90",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                5
        );
        saveIfMissing(
                "Пилешки дроб / Chicken liver",
                "200 г / 200 g",
                "3.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                6
        );
        saveIfMissing(
                "Пилешки дроб по ловджийски / Chicken liver with peppers and onions",
                "250 г / 250 g",
                "4.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                7
        );
        saveIfMissing(
                "Пилешки сърца / Chicken hearts",
                "150 г / 150 g",
                "4.10",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                8
        );
        saveIfMissing(
                "Воденички / Chicken gizzards",
                "150 г / 150 g",
                "3.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                9
        );
        saveIfMissing(
                "Свински дроб с лук / Pork liver with onions",
                "300 г / 300 g",
                "4.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                10
        );
        saveIfMissing(
                "Пилешки крилца / Chicken wings",
                "300 г / 300 g",
                "4.10",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                11
        );
        saveIfMissing(
                "Панирани пилешки хапки / Breaded chicken bites",
                "200 г / 200 g",
                "4.40",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                12
        );
        saveIfMissing(
                "Панирани пилешки филенца със сусам / Sesame-crusted breaded chicken fillets",
                "200 г / 200 g",
                "4.40",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                13
        );
        saveIfMissing(
                "Панирани пилешки филенца с корнфлейкс / Cornflakes-crusted chicken fillets",
                "200 г / 200 g",
                "4.40",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                14
        );
        saveIfMissing(
                "Телешки език / Beef tongue",
                "150 г / 150 g",
                "6.40",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                15
        );
        saveIfMissing(
                "Шкембе в масло / Tripe in butter",
                "150 г / 150 g",
                "3.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                16
        );
        saveIfMissing(
                "Рулца от раци в масло / Surimi sticks in butter",
                "200 г / 200 g",
                "3.90",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                17
        );
        saveIfMissing(
                "Задушени зеленчуци / Sautéed vegetables",
                "300 г / 300 g",
                "4.20",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                18
        );
        saveIfMissing(
                "Царевица в масло / Butter-sautéed corn",
                "200 г / 200 g",
                "3.20",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                19
        );
        saveIfMissing(
                "Телешка глава / Beef head meat (Hot appetizers)",
                "150 г / 150 g",
                "6.20",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                20
        );
        saveIfMissing(
                "Пържени кюфтенца / Fried meatballs (Hot appetizers)",
                "200 г / 200 g",
                "3.60",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                21
        );
        saveIfMissing(
                "Горски гъби микс / Wild mushroom mix (Hot appetizers)",
                "300 г / 300 g",
                "4.80",
                ProductCategory.HOT_APPETIZER,
                5,
                "Топли предястия / Hot appetizers",
                null,
                null,
                22
        );

        // 6. MEZE
        saveIfMissing(
                "Катино мезе <i>(свинско месо, лук, гъби, подправки)</i> / Katino meze <i>(pork, onions, mushrooms, spices)</i>",
                "300 г / 300 g",
                "5.60",
                ProductCategory.MEZE,
                6,
                "Мезета / Meze",
                null,
                null,
                1
        );
        saveIfMissing(
                "Мезелък за двама <i>(наденица, пилешко филе, бекон, лук)</i> / Meze for two <i>(sausage, chicken fillet, bacon, onions)</i>",
                "450 г / 450 g",
                "7.00",
                ProductCategory.MEZE,
                6,
                "Мезета / Meze",
                null,
                null,
                2
        );
        saveIfMissing(
                "Капитанско мезе <i>(пилешко бонфиле, свинско бонфиле, подправки)</i> / Captain's meze <i>(chicken tenderloin, pork tenderloin, spices)</i>",
                "300 г / 300 g",
                "7.50",
                ProductCategory.MEZE,
                6,
                "Мезета / Meze",
                null,
                null,
                3
        );

        // 7. GRILL
        saveIfMissing(
                "Пилешко шишче / Chicken skewer",
                "100 г / 100 g",
                "2.10",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                1
        );
        saveIfMissing(
                "Пилешко филе с бекон / Chicken fillet with bacon",
                "100 г / 100 g",
                "2.10",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                2
        );
        saveIfMissing(
                "Пилешко филе / Chicken fillet",
                "150 г / 150 g",
                "3.00",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                3
        );
        saveIfMissing(
                "Сирене в бекон / White cheese in bacon",
                "250 г / 250 g",
                "4.80",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                4
        );
        saveIfMissing(
                "Бекон на скара / Grilled bacon",
                "200 г / 200 g",
                "4.10",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                5
        );
        saveIfMissing(
                "Свинско кюфте / Pork meatball",
                "70 г / 70 g",
                "1.20",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                6
        );
        saveIfMissing(
                "Свинско кебапче / Pork kebapche <i>(grilled minced meat sausage)</i>",
                "80 г / 80 g",
                "1.20",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                7
        );
        saveIfMissing(
                "Свинско кърначе / Pork кarnache <i>(grilled sausage)</i>",
                "150 г / 150 g",
                "3.00",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                8
        );
        saveIfMissing(
                "Свинско шишче / Pork skewer",
                "100 г / 100 g",
                "2.10",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                9
        );
        saveIfMissing(
                "Свинска пържола / Pork steak",
                "200 г / 200 g",
                "4.10",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                10
        );
        saveIfMissing(
                "Свински ребра / Pork ribs",
                "300 г / 300 g",
                "5.20",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                11
        );
        saveIfMissing(
                "Вурстчета / Würstchen <i>(small sausages)</i>",
                "200 г / 200 g",
                "3.90",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                12
        );
        saveIfMissing(
                "Магелански кюфтак <i>(кайма, шунка, кисели краставички, чушки, кашкавал)</i> / Magelan meatball <i>(minced meat, ham, pickles, peppers and yellow cheese)</i>",
                "250 г / 250 g",
                "4.50",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                13
        );
        saveIfMissing(
                "Телешко кюфте (от младо теле) / Veal meatball (made from young beef)",
                "150 г / 150 g",
                "4.60",
                ProductCategory.GRILL,
                7,
                "Скара / Grill",
                null,
                null,
                14
        );

        // 8. MAIN COURSES
        saveIfMissing(
                "Свинско по „Сашо-ански“ <i>(свинско със зеленчуци)</i> / \"Sasho-anski\" pork <i>(sautéed pork with vegetables)</i>",
                "400 г / 400 g",
                "6.20",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                1,
                "Свински ястия / Pork main courses",
                1
        );
        saveIfMissing(
                "Свинска кавърма / Pork kavarma <i>(pork stew)</i>",
                "450 г / 450 g",
                "4.90",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                1,
                "Свински ястия / Pork main courses",
                2
        );
        saveIfMissing(
                "Свинско бонфиле с гъби и сметана / Pork tenderloin with mushrooms and cream",
                "300 г / 300 g",
                "7.50",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                1,
                "Свински ястия / Pork main courses",
                3
        );

        saveIfMissing(
                "Пилешка кавърма / Chicken kavarma <i>(chicken stew)</i>",
                "450 г / 450 g",
                "4.90",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                2,
                "Пилешки ястия / Chicken main courses",
                1
        );
        saveIfMissing(
                "Пиле по „Сашо-ански“ <i>(пилешко със зеленчуци)</i> / \"Sasho-anski\" chicken <i>(sautéed chicken with vegetables)</i>",
                "400 г / 400 g",
                "6.20",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                2,
                "Пилешки ястия / Chicken main courses",
                2
        );
        saveIfMissing(
                "Пилешко филе с мед и лимон / Honey and lemon chicken fillet",
                "350 г / 350 g",
                "7.50",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                2,
                "Пилешки ястия / Chicken main courses",
                3
        );
        saveIfMissing(
                "Пиле „Жулиен“ / Chicken Julienne",
                "450 г / 450 g",
                "7.50",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                2,
                "Пилешки ястия / Chicken main courses",
                4
        );
        saveIfMissing(
                "Пиле „Чили“ / Chicken \"Chilli\"",
                "400 г / 400 g",
                "5.50",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                2,
                "Пилешки ястия / Chicken main courses",
                5
        );

        saveIfMissing(
                "Сирене по шопски / Shopski Baked Cheese",
                "400 г / 400 g",
                "3.50",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                3,
                "Гювечета / Individual clay pot dishes",
                1
        );
        saveIfMissing(
                "Гювече „Магелан“ <i>(домати, лук, чушки, гъби, шунка, сирене, яйце)</i> / Magelan clay pot <i>(tomatoes, onions, peppers, mushrooms, ham, white cheese, egg)</i>",
                "450 г / 450 g",
                "4.00",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                3,
                "Гювечета / Individual clay pot dishes",
                2
        );
        saveIfMissing(
                "Гювече „Микс“ <i>(домати, лук, чушки, гъби, шунка, луканка, сирене, кашкавал, яйце)</i> / Mixed clay pot <i>(tomatoes, onions, peppers, mushrooms, ham, lukanka, white cheese, yellow cheese, egg)</i>",
                "500 г / 500 g",
                "4.60",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                3,
                "Гювечета / Individual clay pot dishes",
                3
        );

        saveIfMissing(
                "Скумрия на скара / Grilled Mackerel",
                "100 г / 100 g",
                "2.00",
                ProductCategory.MAIN_COURSE,
                8,
                "Основни ястия / Main courses",
                4,
                "Риба / Fish",
                1
        );

        // 9. DRINKS
        saveIfMissing(
                "Еспресо / Espresso",
                null,
                "1.20",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                1,
                "Топли напитки / Hot beverages",
                1
        );
        saveIfMissing(
                "Чай (черен/зелен/билков) / Tea (black/green/herbal)",
                null,
                "1.20",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                1,
                "Топли напитки / Hot beverages",
                2
        );
        saveIfMissing(
                "Течна сметана / Coffee creamer",
                null,
                "0.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                1,
                "Топли напитки / Hot beverages",
                3
        );

        saveIfMissing(
                "Кока-кола / Coca-Cola",
                "330 мл / 330 ml",
                "1.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                1
        );
        saveIfMissing(
                "Швепс / Schweppes",
                "330 мл / 330 ml",
                "1.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                2
        );
        saveIfMissing(
                "Тоник / Schweppes Tonic",
                "330 мл / 330 ml",
                "1.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                3
        );
        saveIfMissing(
                "Сода / Sparkling water",
                "250 мл / 250 ml",
                "1.50",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                4
        );
        saveIfMissing(
                "Натурален сок / Fruit Juice",
                "200 мл / 200 ml",
                "1.50",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                5
        );
        saveIfMissing(
                "Минерална вода / Mineral water",
                "500 мл / 500 ml",
                "1.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                6
        );
        saveIfMissing(
                "Айрян / Ayran <i>(salted yogurt drink)</i>",
                "200 мл / 200 ml",
                "1.20",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                2,
                "Безалкохолни напитки / Soft drinks",
                7
        );

        saveIfMissing(
                "„Ариана“ / \"Ariana\"",
                "500 мл / 500 ml",
                "2.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                1
        );
        saveIfMissing(
                "„Пиринско“ / \"Pirinsko\"",
                "500 мл / 500 ml",
                "2.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                2
        );
        saveIfMissing(
                "„Бургаско“ / \"Burgasko\"",
                "500 мл / 500 ml",
                "2.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                3
        );
        saveIfMissing(
                "„Загорка“ / \"Zagorka\"",
                "500 мл / 500 ml",
                "2.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                4
        );
        saveIfMissing(
                "„Каменица“ 0.00 % / \"Kamenitza\" 0.00 %",
                "500 мл / 500 ml",
                "2.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                5
        );
        saveIfMissing(
                "„Нишко пиво“ / \"Nishko pivo\"",
                "500 мл / 500 ml",
                "2.40",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                6
        );
        saveIfMissing(
                "„Будвайзер“ / \"Budweiser\"",
                "500 мл / 500 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                7
        );
        saveIfMissing(
                "„Свияни“ / \"Svijany\"",
                "500 мл / 500 ml",
                "2.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                8
        );
        saveIfMissing(
                "„Козел“ / \"Kozel\"",
                "500 мл / 500 ml",
                "3.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                9
        );
        saveIfMissing(
                "„Козел тъмно“ (кен) / \"Kozel dark\" (can)",
                "500 мл / 500 ml",
                "3.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                10
        );
        saveIfMissing(
                "„Шоферхофер“ / \"Schöfferhofer\"",
                "500 мл / 500 ml",
                "3.50",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                11
        );
        saveIfMissing(
                "„Цар“ / \"Tsar\"",
                "250 мл / 250 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                3,
                "Бира / Beer",
                12
        );

        saveIfMissing(
                "Чаша / Glass",
                "200 мл / 200 ml",
                "3.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                4,
                "Вино <i>(червено, бяло)</i> / Wine <i>(red, white)</i>",
                1
        );
        saveIfMissing(
                "Гарафа / Carafe",
                "500 мл / 500 ml",
                "6.20",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                4,
                "Вино <i>(червено, бяло)</i> / Wine <i>(red, white)</i>",
                2
        );
        saveIfMissing(
                "Гарафа / Carafe (1000 ml)",
                "1000 мл / 1000 ml",
                "12.40",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                4,
                "Вино <i>(червено, бяло)</i> / Wine <i>(red, white)</i>",
                3
        );

        saveIfMissing(
                "„Савой“ / \"Savoy\" (Vodka)",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                1
        );
        saveIfMissing(
                "„Флирт“ / \"Flirt\"",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                2
        );
        saveIfMissing(
                "„Житня“ / \"Zytnia\"",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                3
        );
        saveIfMissing(
                "„Руски стандарт“ / \"Russian standard\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                4
        );
        saveIfMissing(
                "„Зелена марка“ / \"Zelena marka\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                5
        );
        saveIfMissing(
                "„Смирноф“ / \"Smirnoff\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                6
        );
        saveIfMissing(
                "„Абсолют“ / \"Absolut\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                7
        );
        saveIfMissing(
                "„Финландия“ / \"Finlandia\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                5,
                "Водка / Vodka",
                8
        );

        saveIfMissing(
                "„Савой“ / \"Savoy\" (Gin)",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                6,
                "Джин / Gin",
                1
        );
        saveIfMissing(
                "„Бийфитър“ / \"Beefeater\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                6,
                "Джин / Gin",
                2
        );

        saveIfMissing(
                "„Джей енд Би“ / \"J&B\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                1
        );
        saveIfMissing(
                "„Джим Бийм“ / \"Jim Beam\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                2
        );
        saveIfMissing(
                "„Тюламор Дю“ / \"Tullamore Dew\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                3
        );
        saveIfMissing(
                "„Джак Даниелс“ / \"Jack Daniel's\"",
                "50 мл / 50 ml",
                "3.70",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                4
        );
        saveIfMissing(
                "„Джеймисън“ / \"Jameson\"",
                "50 мл / 50 ml",
                "3.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                5
        );
        saveIfMissing(
                "„Джони Уокър“ / \"Johnny Walker\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                6
        );
        saveIfMissing(
                "„Четири рози“ / \"Four Roses\"",
                "50 мл / 50 ml",
                "3.10",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                7
        );
        saveIfMissing(
                "„Пади“ / \"Paddy\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                8
        );
        saveIfMissing(
                "„Бушмилс“ / \"Bushmills\"",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                7,
                "Уиски / Whiskey",
                9
        );

        saveIfMissing(
                "„Пещерска“ / \"Peshterska\"",
                "50 мл / 50 ml",
                "1.50",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                1
        );
        saveIfMissing(
                "„Троянска сливова“ / \"Troyanska slivova\" (plum)",
                "50 мл / 50 ml",
                "1.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                2
        );
        saveIfMissing(
                "„Сунгурларска“ / \"Sungurlarska\"",
                "50 мл / 50 ml",
                "1.70",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                3
        );
        saveIfMissing(
                "„Поморийска“ / \"Pomoriyska\"",
                "50 мл / 50 ml",
                "1.70",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                4
        );
        saveIfMissing(
                "„Поморийска“ (мускатова) / \"Pomoriyska\" (muscat)",
                "50 мл / 50 ml",
                "1.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                5
        );
        saveIfMissing(
                "„Бургаска мускатова“ / \"Burgaska muskatova\" (muscat)",
                "50 мл / 50 ml",
                "1.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                6
        );
        saveIfMissing(
                "„Стралджанска мускатова“ / \"Straldzhanska muskatova\" (muscat)",
                "50 мл / 50 ml",
                "2.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                7
        );
        saveIfMissing(
                "„Сливенска перла“ / \"Slivenska perla\"",
                "50 мл / 50 ml",
                "2.90",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                8,
                "Ракия / Rakia",
                8
        );

        saveIfMissing(
                "Текила „Сауца Голд“ / \"Sauza Gold\" Tequila",
                "25 мл / 25 ml",
                "1.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                1
        );
        saveIfMissing(
                "„Йегермайстер“ / \"Jagermeister\"",
                "25 мл / 25 ml",
                "1.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                2
        );
        saveIfMissing(
                "Узо „Пломари“ / Ouzo \"Plomari\"",
                "50 мл / 50 ml",
                "2.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                3
        );
        saveIfMissing(
                "Мастика „Пещера“ / Mastika \"Peshtera\"",
                "50 мл / 50 ml",
                "1.50",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                4
        );
        saveIfMissing(
                "Мента „Пещера“ / Menta \"Peshtera\" <i>(mint liqueur)</i>",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                5
        );
        saveIfMissing(
                "Ром „Атлантик“ / \"Atlantic\" rum",
                "50 мл / 50 ml",
                "1.30",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                6
        );
        saveIfMissing(
                "Ром „Капитан Морган“ / \"Captain Morgan\" rum",
                "50 мл / 50 ml",
                "2.60",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                7
        );
        saveIfMissing(
                "Мартини / Martini",
                "50 мл / 50 ml",
                "2.00",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                8
        );
        saveIfMissing(
                "Бренди „Плиска“ / \"Pliska\" brandy",
                "50 мл / 50 ml",
                "2.00",
                ProductCategory.DRINK,
                9,
                "Напитки / Drinks",
                9,
                "Други / Others",
                9
        );

        // 10. CRUNCHY
        saveIfMissing(
                "Печени фъстъци / Roasted peanuts",
                "100 г / 100 g",
                "1.60",
                ProductCategory.CRUNCHY,
                10,
                "Хрупки / Crunchy snacks",
                null,
                null,
                1
        );
        saveIfMissing(
                "Чипс / Potato chips",
                "100 г / 100 g",
                "2.60",
                ProductCategory.CRUNCHY,
                10,
                "Хрупки / Crunchy snacks",
                null,
                null,
                2
        );
        saveIfMissing(
                "Топли пуканки / Hot popcorn",
                "100 г / 100 g",
                "2.10",
                ProductCategory.CRUNCHY,
                10,
                "Хрупки / Crunchy snacks",
                null,
                null,
                3
        );
        saveIfMissing(
                "Шоколад „Милка“ / \"Milka\" chocolate",
                "100 г / 100 g",
                "2.60",
                ProductCategory.CRUNCHY,
                10,
                "Хрупки / Crunchy snacks",
                null,
                null,
                4
        );

        log.info("Magelan menu seeding finished. Total products: {}", productRepository.count());
    }

    private void saveIfMissing(
            String name,
            String description,
            String price,
            ProductCategory category,
            int sectionOrder,
            String sectionTitle,
            Integer subcategoryOrder,
            String subcategoryTitle,
            int itemOrder
    ) {
        productRepository.findByName(name).ifPresentOrElse(
                existing -> log.debug("Product already exists: {}", name),
                () -> {
                    LocalDateTime now = LocalDateTime.now();

                    Product product = Product.builder()
                            .name(name)
                            .description(description)
                            .price(new BigDecimal(price))
                            .category(category)
                            .sectionOrder(sectionOrder)
                            .sectionTitle(sectionTitle)
                            .subcategoryOrder(subcategoryOrder)
                            .subcategoryTitle(subcategoryTitle)
                            .itemOrder(itemOrder)
                            .createdOn(now)
                            .updatedOn(now)
                            .build();

                    productRepository.save(product);
                    log.info("Seeded product: {}", name);
                }
        );
    }
}