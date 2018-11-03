import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.reflect.FieldUtils;

import static org.testng.Assert.*;

public class TestPen {

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testConstructorPen_oneParam() throws IllegalAccessException {
        int inkContainerValue = 1;
        String field1 = "inkContainerValue";

        Pen pen = new Pen(inkContainerValue);

        assertEquals(inkContainerValue, FieldUtils.readField(pen, field1, true));        //Через рефлексию берём инициализированное поле
    }                                                                                              //и убеждаемся что оно изменилось при создании объекта класса Pen

    @Test
    public void testConstructorPen_twoParam() throws IllegalAccessException {
        int inkContainerValue = 1;
        double sizeLetter = 0.1;

        String field1 = "inkContainerValue";
        String field2 = "sizeLetter";

        Pen pen = new Pen(inkContainerValue, sizeLetter);

        assertEquals(inkContainerValue, FieldUtils.readField(pen, field1, true));        //Через рефлексию берём инициализированное поле
        assertEquals(sizeLetter, FieldUtils.readField(pen, field2, true));
    }                                                                                              //и убеждаемся что оно изменилось при создании объекта класса Pen

    @Test
    public void testConstructorPen_threeParam() throws IllegalAccessException {
        int inkContainerValue = 1;
        double sizeLetter = 0.1;
        String color = "BROWN";

        String field1 = "inkContainerValue";
        String field2 = "sizeLetter";
        String field3 = "color";

        Pen pen = new Pen(inkContainerValue, sizeLetter, color);

        assertEquals(inkContainerValue, FieldUtils.readField(pen, field1, true));        //Через рефлексию берём инициализированное поле
        assertEquals(sizeLetter, FieldUtils.readField(pen, field2, true));
        assertEquals(color, FieldUtils.readField(pen, field3, true));
    }                                                                                              //и убеждаемся что оно изменилось при создании объекта класса Pen
//****************Тестирование метода write()*****************
    @Test
    public void testWrite_isWorkFalse() {
        int inkContainerValue = 0;                  //ожидаем выполнения условия !isWork
        String word = "word";
        String response = "";                        //ожидаемый результат

        Pen pen = new Pen(inkContainerValue);
        assertEquals(pen.write(word), response);
    }

    @Test
    public void testWrite_sizeOfWordTrue_defaultSize() {
        String word = "word";
        int inkContainerValue = word.length() + 1;  //ожидаем пропуск условия !isWork и выполнения условия sizeOfWord<=inkContainerValue

        Pen pen = new Pen(inkContainerValue);       //создаем Pen со значением sizeOfWord по умолчанию
        assertEquals(pen.write(word), word);        //проверяем, что условие sizeOfWord<=inkContainerValue выполняется и метод возращает word
    }

    @Test
    public void testWrite_sizeOfWordFalse_defaultSize() {
        String word = "word";
        int inkContainerValue = 1;  //ожидаем пропуск условия !isWork и выполнения условия sizeOfWord<=inkContainerValue

        Pen pen = new Pen(inkContainerValue);       //создаем Pen со значением sizeOfWord по умолчанию
        assertEquals(pen.write(word), word.substring(0, inkContainerValue));        //проверяем, что условие sizeOfWord<=inkContainerValue НЕ выполняется и метод возращает первую букву word
    }

    @Test
    public void testWrite_inkContainerValue_isValid() throws IllegalAccessException {
        String word = "word";
        int inkContainerValue = 10;                             //ожидаем пропуск условия !isWork и выполнения условия sizeOfWord<=inkContainerValue
        double sizeLetter = 0.15;

        double sizeOfWord = word.length()*sizeLetter;           //считаем sizeOfWord
        double containerValue_mod = inkContainerValue - sizeOfWord; //containerValue_mod является аналогом inkContainerValue -= sizeOfWord с приведением в double

        Pen pen = new Pen(inkContainerValue, sizeLetter);
        pen.write(word);

        assertEquals(containerValue_mod, FieldUtils.readField(pen, "inkContainerValue", true));
        //Сравниваем посчитанный нами containerValue_mod и измененённое поле inkContainerValue
        //Фиксируем баг - т.к. отсутствует явное приведение типов к double и дробная часть пропадает
    }
//****************Тестирование метода write()*****************

    @Test
    public void testGetColor() {
        String color = "RED";

        Pen pen = new Pen(1, 1.0, color);
        assertEquals(color , pen.getColor());
    }
    //***********Тестирование метода isWork()***************
    @Test
    public void testIsWork_containerPositive() {
        int inkContainerValue = 1;

        Pen pen = new Pen(inkContainerValue);
        assertTrue(pen.isWork());
    }

    @Test
    public void testIsWork_containerEmpty() {
        int inkContainerValue = 0;

        Pen pen = new Pen(inkContainerValue);
        assertFalse(pen.isWork());
    }
    //***********Тестирование метода isWork()***************


    @Test
    public void testDoSomethingElse() throws IOException {
        int inkContainerValue = 1000;
        double sizeLetter = 1.0;
        String color = "RED";

        Pen pen = new Pen (inkContainerValue, sizeLetter, color);

        File file = new File("temp.txt"); // создаем файл
        PrintStream ps = new PrintStream(file); // создаем поток вывода в файл
        PrintStream standardOut = System.out; // сохраняем стандартный поток вывода
        System.setOut(ps); // присваиваем файловый поток в качестве основного
        pen.doSomethingElse(); // вызываем метод
        Assert.assertEquals(Files.readAllLines(Paths.get(file.toURI())).get(0), color); // проверяем что результат в файле равен ожидаемому
        System.setOut(standardOut); // возвращаем метод в исходное состояние
    }
}