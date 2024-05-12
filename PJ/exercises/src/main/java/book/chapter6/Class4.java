package book.chapter6;

import book.chapter6.coockie.Cookie1;

/**
 * Покажите, что методы со спецификатором protected обладают доступом в
 * пределах пакета, но не являются открытыми.
 * 
 * Создайте класс с полями и методами, обладающими разными уровнями
 * доступа: public, private, protected, доступом в пределах пакета. Создайте
 * объект этого класса и посмотрите, какие сообщения выдает компилятор при
 * попытке обращения к разным членам класса. Учтите, что классы, находящиеся в
 * одном каталоге, входят в «пакет по умолчанию».
 * 
 * Создайте класс с защищенными данными. Создайте в том же файле второй
 * класс с методом, работающим с защищенными данными из первого класса.
 */
public class Class4 {
    public static void main(String[] args) {
        // приватный метод вне пределах класса не доступен
        // Cookie2.bitePrivate();
        Cookie2.biteDefault();
        Cookie2.biteProtected();
        Cookie2.bitePublic();

        Cookie1.bitePublic();
        // приватный метод также не доступен, 
        // protected не доступен, по причине того что разные пакеты
        // с доступом по умолчанию не доступен, потому что разные директории
        // Cookie1.bitePrivate();
        // Cookie1.biteProtected();
        // Cookie1.biteDefault();

        Cookie3.bite();
        Cookie4.bite();
    }
}