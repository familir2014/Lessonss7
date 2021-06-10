

import java.lang.reflect.Method;
import java.util.ArrayList;

//
//        В качестве тестов выступают классы с наборами методов, снабженных аннотациями @Test. Класс, запускающий тесты, должен иметь статический метод start(Class testClass),
//        которому в качестве аргумента передается объект типа Class. Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite, если он присутствует.
//        Далее запускаются методы с аннотациями @Test, а по завершении всех тестов – метод с аннотацией @AfterSuite.
//        К каждому тесту необходимо добавить приоритеты (int-числа от 1 до 10), в соответствии с которыми будет выбираться порядок их выполнения. Если приоритет одинаковый,
//        то порядок не имеет значения. Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре.
//        Если это не так – необходимо бросить RuntimeException при запуске «тестирования».
public class Main {

    public static void main(String[] args) throws Exception {
        Class c = NewClass.class;
        Object testObj = c.newInstance();
        Method[] methods = c.getDeclaredMethods();
        ArrayList<Method> al = new ArrayList<>();
        Method beforeMethod = null;
        Method afterMethod = null;
        for (Method o : c.getDeclaredMethods()) {
            if (o.isAnnotationPresent(Test.class)) {
                al.add(o);
            }
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethod == null) beforeMethod = o;
                else throw new RuntimeException("Больше одного метода с аннотацией BeforeSuite");
            }
            if (o.isAnnotationPresent(AfterSuite.class)) {
                if (afterMethod == null) afterMethod = o;
                else throw new RuntimeException("Больше одного метода с аннотацией AfterSuite");
            }
        }
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < methods.length; j++) {
                if (methods[j].getAnnotation(Test.class) != null) {
                    if (methods[j].getAnnotation(Test.class).priority() == j) {
                        al.add(methods[j]);
                    }
                }
            }
        }


        if (beforeMethod != null) beforeMethod.invoke(testObj, null);
        for (Method o : al) o.invoke(testObj, null);
        if (afterMethod != null) afterMethod.invoke(testObj, null);
    }
}
