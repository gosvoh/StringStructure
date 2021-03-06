package utils;

import java.util.Objects;

/**
 * Класс, представляющий собой список, принимающий объекты класса
 * String и объекты этого же класса
 *
 * @author Aleksei Vokhmin
 */

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
            len = 0;
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
     * Является ли этот список производным от String
     */
    private boolean isFromString = false;

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
        isFromString = true;
        StringItem node = new StringItem();
        _head = node;
        if (string.length() <= StringItem.SIZE) {
            System.arraycopy(string.toCharArray(), 0, _head.symbols, 0, string.length());
            node.len = countCharLength(node.symbols);
            _length += _head.len;
        } else {
            int offset = 0;
            int tempLen = string.length() / StringItem.SIZE;
            StringItem tail = _head;
            char[] substring;
            for (int i = 0; i < tempLen; i++) {
                node = addNodeAfter(tail);
                substring = string.substring(offset, (i + 1) * StringItem.SIZE).toCharArray();
                System.arraycopy(substring, 0, node.symbols, 0, substring.length);
                node.len = countCharLength(node.symbols);
                tail.next = node;
                tail = node;
                _length += node.len;
                offset += StringItem.SIZE;
            }
            node = addNodeAfter(tail);
            substring = string.substring(offset).toCharArray();
            System.arraycopy(substring, 0, node.symbols, 0, substring.length);
            node.len = countCharLength(node.symbols);
            tail.next = node;
            _length += node.len;
        }
    }

    /**
     * Конструктор, копирующий все элементы переданного списка для дальннейщей рбработки
     *
     * @param stringList список
     */
    public StringList(StringList stringList) {
        if (stringList != null && stringList._head != null) {
            StringItem workingNode = stringList._head;
            StringItem node = new StringItem();
            _head = node;
            while (workingNode != null) {
                node = addNodeAfter(node);
                node.symbols = workingNode.symbols;
                node.len = workingNode.len;
                workingNode = workingNode.next;
            }
            this._length = stringList._length;
            this.isFromString = false;
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
            if (node.next.hasNext()) node.next = node.next.next;
            else node.next = null;
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
        checkIndex(index, _length);
        StringItem node = _head;
        while (index > StringItem.SIZE && node != null) {
            index -= node.len;
            node = node.next;
        }
        return new Position(index, node);
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
        if (stringList != null && stringList._head != null) {
            StringList workList;
            if (stringList.isFromString) workList = stringList;
            else workList = new StringList(stringList);
            if (_head == null) _head = workList._head;
            else getLastNode().next = workList._head;
            _length += workList._length;
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

        System.arraycopy(position.node.symbols, 0, prevNode.symbols, 0, position.position);
        System.arraycopy(position.node.symbols, position.position, nextNode.symbols, 0, StringItem.SIZE - position.position);

        prevNode.len = countCharLength(prevNode.symbols);
        nextNode.len = countCharLength(nextNode.symbols);

        // Проверяем, является ли переданный список производным от String
        StringList workList;
        if (stringList.isFromString)    // Если да, то не создаём лишнюю копию списка
            workList = stringList;
        else                            // А если нет, то копируем через конструктор
            workList = new StringList(stringList);

        prevNode.next = workList._head;
        nextNode.next = position.node.next;
        workList.getLastNode().next = nextNode;
        this._length += workList._length;

        position.node.symbols = prevNode.symbols;
        position.node.len = prevNode.len;
        position.node.next = prevNode.next;

        sortNode(_head);

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

        Position firstPosition = getPosition(beginIndex);
        Position secondPosition = getPosition(endIndex);

        StringList retList = new StringList();
        StringItem node = new StringItem();
        retList._head = node;

        if (firstPosition.node == secondPosition.node) {
            System.arraycopy(firstPosition.node.symbols, firstPosition.position, node.symbols, 0, secondPosition.position - firstPosition.position);
            node.len = countCharLength(node.symbols);
            retList._length += node.len;
            return retList;
        }

        // Добавляем часть символов из первого блока, начиная с указанной позиции
        System.arraycopy(firstPosition.node.symbols, firstPosition.position, node.symbols, 0, StringItem.SIZE - firstPosition.position);
        node.len = countCharLength(node.symbols);
        retList._length += node.len;
        firstPosition.node = firstPosition.node.next;

        // Цикл для добавления символов из блоков, находящихся между первым и последним блоками
        while (firstPosition.node != secondPosition.node) {
            node = retList.addNodeAfter(node);
            node.symbols = firstPosition.node.symbols;
            node.len = firstPosition.node.len;
            retList._length += node.len;
            firstPosition.node = firstPosition.node.next;
        }
        // Добавляем часть символов из последнего блока, находящихся до указанной позиции
        node = retList.addNodeAfter(node);
        System.arraycopy(secondPosition.node.symbols, 0, node.symbols, 0, secondPosition.position);
        node.len = countCharLength(node.symbols);
        retList._length += node.len;
        return retList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringList that = (StringList) o;
        return _length == that._length &&
               isFromString == that.isFromString &&
               Objects.equals(_head, that._head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_head, _length, isFromString);
    }
}
