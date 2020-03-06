package utils;

import java.util.Objects;
import java.util.concurrent.locks.Condition;

public class StringList {
    /**
     * Класс, представляющий элемент связного списка
     */
    private static class StringItem {
        /**
         * Символы в блоке
         */
        char[] symbols;

        /**
         * Размер блока
         */
        final static int SIZE = 16;

        /**
         * Количество символов в блоке
         */
        byte len;

        /**
         * Следующий элемент списка
         */
        StringItem next;

        /**
         * Конструктор, в котором для удобства инициализируется новый массив символов
         */
        public StringItem() {
            symbols = new char[StringItem.SIZE];
        }

        /**
         * Узнать, существует ли следующий элемент списка
         *
         * @return true, если елемент существует
         */
        public boolean hasNext() {
            return next != null;
        }
    }

    /**
     * Класс для доступа к элементу в списке и символу в этом элементе
     */
    private static class Position {
        /**
         * Элемент списка
         */
        private StringItem node;

        /**
         * Индекс символа в элементе списка
         */
        private int position;

        /**
         * Default constructor
         */
        public Position() {
        }

        /**
         * Конструктор, задающий элемент списка и позицию символа в нём
         *
         * @param index позиция символа
         * @param node  элемент списка
         */
        public Position(int index, StringItem node) {
            this.position = index;
            this.node = node;
        }
    }

    /**
     * Голова списка
     */
    private StringItem _head;
    /**
     * Длина строки
     */
    private int _length;

    /**
     * Default constructor
     */
    public StringList() {
        _head = null;
    }

    /**
     * Конструктор, в котором реализовано преобразование стринга в объект класса {@link StringList}
     *
     * @param string строка
     */
    public StringList(String string) {
        if (string.length() <= StringItem.SIZE)
            addNodeToTail(string.toCharArray());
        else {
            int offset = 0;
            for (int i = 0; i < string.length() / StringItem.SIZE; i++) {
                addNodeToTail(string.substring(offset, (i + 1) * StringItem.SIZE).toCharArray());
                offset += StringItem.SIZE;
            }
            addNodeToTail(string.substring(offset).toCharArray());
        }
    }

    /**
     * Конструктор, копирующий все элементы переданного списка для дальннейщей рбработки
     *
     * @param stringList список
     */
    public StringList(StringList stringList) {
        if (stringList == null || stringList._head == null)
            appendEmpty();
        else {
            StringItem node = stringList._head;
            while (node != null) {
                addNodeToTail(node.symbols);
                node = node.next;
            }
            this._length = stringList._length;
        }
    }

    //--------------------------Приватные методы--------------------------------

    /**
     * Компоновка элементов списка и удаление лишних элементов
     *
     * @param node элемент списка
     */
    private void sortNode(StringItem node) {
        if (node.hasNext() && (node.len + node.next.len) <= StringItem.SIZE) {
            System.arraycopy(node.next.symbols, 0, node.symbols, node.len, node.next.len);
            node.len = countCharLength(node.symbols);
            if (node.next.hasNext())
                node.next = node.next.next;
            else
                node.next = null;
            sortNode(node);
        }
    }

    /**
     * Подсчёт количества значащих символов в символьном массиве (не более 127 элементов)
     *
     * @param chars символьный массив
     * @return количество элементов с типом byte
     */
    private byte countCharLength(char[] chars) {
        int count = 0;
        for (char c : chars) {
            if (c != (char) 0)
                count++;
        }
        return (byte) count;
    }

    /**
     * Проверить заданный индекс на выход за границу строки
     *
     * @param index  индекс элемента
     * @param length длина строки
     */
    private void checkIndex(int index, int length) {
        if (index < 0 || index >= length)
            throw new IndexOutOfBoundsException("index " + index + ",length " + length);
    }

    /**
     * Проверить заданные границы на выход за границы строки
     *
     * @param begin  начальный индекс
     * @param end    конечный индекс
     * @param length длина строки
     */
    private void checkBoundsBeginEnd(int begin, int end, int length) {
        if (begin < 0 || begin > end || end > length) {
            throw new IndexOutOfBoundsException("begin " + begin + ", end " + end + ", length " + length);
        }
    }

    /**
     * Добавить нод в хвост списка
     *
     * @return элемент хвоста списка
     */
    private StringItem addNodeToTail() {
        return addNodeToTail(new char[0]);
    }

    /**
     * Добавить нод в хвост списка
     *
     * @param symbols элементы массива
     * @return элемент хвоста списка
     */
    private StringItem addNodeToTail(char[] symbols) {
        StringItem node = new StringItem();
        if (symbols.length != 0) {
            if (symbols.length > StringItem.SIZE)
                throw new IllegalArgumentException("Количество элементов массива больше, чем может быть!");
            System.arraycopy(symbols, 0, node.symbols, 0, countCharLength(symbols));
            node.len = countCharLength(symbols);
            this._length += node.len;
        }
        if (_head == null)
            _head = node;
        else
            getLastNode().next = node;
        return node;
    }

    /**
     * Добавляет новый нод после указаного нода
     *
     * @param node нод, после которого добавляем элемент
     * @return ссылка на текущий объект
     */
    private StringItem addNodeAfter(StringItem node) {
        StringItem newNode = new StringItem();
        if (node == null)
            throw new NullPointerException("Trying add new node to null");
        if (node.hasNext())
            newNode.next = node.next;
        node.next = newNode;
        return newNode;
    }

    /**
     * Получить последний элемент списка
     *
     * @return последний элемент списка
     */
    private StringItem getLastNode() {
        StringItem node = _head;
        if (node == null)
            throw new NullPointerException("Trying to get last node when head is null");
        while (node.hasNext()) {
            sortNode(node);
            if (node.hasNext())
                node = node.next;
        }
        return node;
    }

    /**
     * Получить позицию блока с курсором
     *
     * @param index индекс символа в строке
     * @return объект класса {@link Position}
     */
    private Position getPosition(int index) {
        StringItem node = _head;
        Position position = null;
        while (index > StringItem.SIZE && node != null) {
            index -= node.len;
            node = node.next;
        }
        if (index <= StringItem.SIZE) {
            position = new Position(index, node);
        }
        if (position == null)
            throw new NullPointerException("Position is null, error in checkIndex method");
        return position;
    }

    private StringList appendEmpty() {
        addNodeToTail(new char[]{'n', 'u', 'l', 'l'});
        return this;
    }
    //--------------------------------------------------------------------------

    /**
     * Добавить строку в конец строки
     *
     * @param string строка
     * @return ссылку на этот объект
     */
    public StringList append(String string) {
        return append(new StringList(string));
    }

    /**
     * Добавить связный список {@link StringList} в конец строки
     *
     * @param stringList список для добавления
     * @return ссылку на этот объект
     */
    public StringList append(StringList stringList) {
        if (stringList == null || stringList._head == null)
            appendEmpty();
        else {
            StringItem node = stringList._head;
            while (node != null) {
                addNodeToTail(node.symbols);
                node = node.next;
            }
        }
        return this;
    }

    /**
     * Вернуть символ в указанном месте
     *
     * @param index индекс символа
     * @return символ
     */
    public char charAt(int index) {
        Position position = getPosition(index);
        return position.node.symbols[position.position];
    }

    /**
     * Заместить символ в указанном месте
     *
     * @param index  индекс символа
     * @param symbol символ для замещения
     * @return ссылка на текщий объект
     */
    public StringList setCharAt(int index, char symbol) {
        Position position = getPosition(index);
        position.node.symbols[position.position] = symbol;
        return this;
    }

    /**
     * Вставить строку после указанного символа
     *
     * @param index  индекс символа
     * @param string строка для вставки
     * @return ссылка на текущий объект
     */
    public StringList insert(int index, String string) {
        return this.insert(index, new StringList(string));
    }

    /**
     * Вставить связный список {@link StringList} после указанного символа
     *
     * @param index      индекс символа
     * @param stringList список для вставки
     * @return ссылка на текущий объект
     */
    public StringList insert(int index, StringList stringList) {
        checkIndex(index, this._length);
        Position position = getPosition(index);
        StringItem prevNode = new StringItem();
        StringItem nextNode = new StringItem();
        System.arraycopy(position.node.symbols, 0, prevNode.symbols, 0, index);
        System.arraycopy(position.node.symbols, index, nextNode.symbols, 0, StringItem.SIZE - index);
        prevNode.len = countCharLength(prevNode.symbols);
        nextNode.len = countCharLength(nextNode.symbols);
        nextNode.next = position.node.next;

        StringList workList = new StringList(stringList);
        prevNode.next = workList._head;
        workList.getLastNode().next = nextNode;
        this._length += workList._length;

        position.node.symbols = prevNode.symbols;
        position.node.len = prevNode.len;
        position.node.next = prevNode.next;

        return this;
    }

    /**
     * Получить подстроку в виде объекта {@link StringList}
     *
     * @param beginIndex начальный индекс подстроки
     * @param endIndex   конечный индекс подстроки
     * @return новый объект класса {@link StringList}
     */
    public StringList substring(int beginIndex, int endIndex) {
        checkBoundsBeginEnd(beginIndex, endIndex, this._length);
        if (endIndex - beginIndex == _length)
            return this;
        return new StringList(this.toString().substring(beginIndex, endIndex));
    }

    /**
     * Получить длину строки
     *
     * @return длина строки
     */
    public int length() {
        return this._length;
    }

    //--------------------------Object method overrides-------------------------

    /**
     * Переопределение метода.
     *
     * @return данные списка в виде объекта String
     */
    @Override
    public String toString() {
        char[] ret = new char[_length];
        StringItem node = _head;
        int offset = 0;
        if (node == null)
            return "null";
        while (node != null) {
            sortNode(node);
            System.arraycopy(node.symbols, 0, ret, offset, node.len);
            offset += node.len;
            node = node.next;
        }
        return new String(ret);
    }

    /**
     * Переопределение метода.
     *
     * @return true, если это один и тот же объект
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringList that = (StringList) o;
        return _length == that._length &&
                _head.equals(that._head);
    }

    /**
     * Переопределение метода.
     *
     * @return хэш объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(_head, _length);
    }
}
