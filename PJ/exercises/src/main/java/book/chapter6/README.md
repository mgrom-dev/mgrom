## Глава 6. Управление доступом (стр. 187)
### Задания:
1. (1) Определите класс в пакете. Создайте экземпляр класса за пределами пакета. (стр. 192)
2. (2) Преобразуйте фрагменты из этого раздела в программу. Убедитесь в том, что конфликты имен действительно возникают.
3. (2) Создайте два пакета debug и debugoff, содержащие одинаковые классы с методом debug(). Первая версия выводит на консоль свой аргумент типа String, вторая не делает ничего. Используйте директиву static import для импортирования класса в тестовую программу и продемонстрируйте эффект условной компиляции. (стр. 195)
4. (2) Покажите, что методы со спецификатором protected обладают доступом в пределах пакета, но не являются открытыми.
5. (2) Создайте класс с полями и методами, обладающими разными уровнями доступа: public, private, protected, доступом в пределах пакета. Создайте объект этого класса и посмотрите, какие сообщения выдает компилятор при попытке обращения к разным членам класса. Учтите, что классы, находящиеся в одном каталоге, входят в «пакет по умолчанию».
6. (1) Создайте класс с защищенными данными. Создайте в том же файле второй класс с методом, работающим с защищенными данными из первого класса. (стр. 200)
7. (1) Создайте библиотеку в соответствии с фрагментами кода, содержащими описания access и Widget. Создайте объект Widget в классе, не входящем в пакет access. (стр. 203)
8. (4) По образцу примера Lunch.java создайте класс с именем ConnectionManager, который управляет фиксированным массивом объектов Connection. Программист- клиент не должен напрямую создавать объекты Connection, а может получать их только с помощью статического метода в классе ConnectionManager. Когда запас объектов у класса ConnectionManager будет исчерпан, он должен вернуть ссылку null. Протестируйте классы в методе main().
9. (2) Поместите следующий файл в каталог access/local (предположительно заданный в переменной CLASSPATH) (стр. 204)

```java
// access/local/PackagedClass.java
class PackagedClass {
    public PackagedClass() {
        System.out.println("Coздаем класс в пакете");
    }
}
```
Затем сохраните в каталоге, отличном от access/local, такой файл:
```java
// access/foreign/Foreign.java
import book.chapter6.acess.*;

public class Foreign {
    public static void main(String[] args) {
        PackagedClass pc = new PackagedClass();
    }
}
```
Объясните, почему компилятор выдает сообщение об ошибке. Изменит ли что-нибудь помещение класса Foreign в пакет access.local?
1. Потому что класс PackagedClass доступен только в пределах его пакета
2. Да, тогда все будет работать, так как классы будут находиться в одном пакете

### Заключение:
По оценкам проекты на языке С начинают «рассыпаться» примерно тогда, когда код достигает объема от 50 до 100 Кбайт, так как С имеет единое «пространство имен»; в системе возникают конфликты имен, создающие массу неудобств. BJava ключевое слово package, схема именования пакетов и ключевое слово import обеспечивают полный контроль над именами, так что конфликта имен можно легко избежать.

Объявление полей и методов со спецификатором private только помогает пользователям класса, так как они сразу видят, какие члены класса для них важны, а какие можно игнорировать. Все это упрощает понимание и использование класса.

Вторая, более важная причина для ограничения доступа — возможность изменения внутренней реализации класса, не затрагивающего программистов-клиентов. Например, сначала вы реализуете класс одним способом, а затем выясняется, что реструктуризация кода позволит повысить скорость работы. Отделение интерфейса от реализации позволит сделать это без нарушения работоспособности существующего пользовательского кода, в котором этот класс используется.

Открытый интерфейс класса — это то, что фактически видит его пользователь, поэтому очень важно «довести до ума» именно эту, самую важную, часть класса в процессе анализа и разработки. И даже при этом у вас остается относительная свобода действий. Даже если идеальный интерфейс не удалось построить с первого раза, вы можете добавить в него новые методы — без удаления уже существующих методов, которые могут использоваться программистами-клиентами.