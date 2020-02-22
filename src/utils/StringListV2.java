package utils;

import jdk.jshell.spi.ExecutionControl;

import java.util.Objects;

public class StringListV2 {
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

        public StringItem moveToNextNode() {
            if (node != null && node.hasNext()) {
                node = node.next;
                return node;
            }
            return null;
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
     * Количество элементов списка
     */
    private int _countOfNodes;
    //private Position _currentPosition;

    /**
     * Default constructor
     */
    public StringListV2() {
    }

    /**
     * Конструктор, в котором реализовано преобразование стринга в объект класса {@link StringListV2}
     *
     * @param string строка
     */
    public StringListV2(String string) throws ExecutionControl.NotImplementedException {
        this._length = string.length();
        if (this._length <= 16) {
            addNodeToTail(string.toCharArray());
            return;
        }



        throw new ExecutionControl.NotImplementedException("Not implemented yet");
    }

    /**
     * Конструктор, копирующий все элементы переданного списка для дальннейщей рбработки
     *
     * @param stringList список
     */
    public StringListV2(StringListV2 stringList) {
        if (stringList == null || stringList._head == null)
            appendEmpty();
        else {
            StringItem node = stringList._head;
            while (node.hasNext()) {
                addNodeToTail(node.symbols);
                node = node.next;
            }
            this._length = stringList._length;
            this._countOfNodes = stringList._countOfNodes;
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
            --_countOfNodes;
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
            node.symbols = new char[StringItem.SIZE];
            System.arraycopy(symbols, 0, node.symbols, 0, countCharLength(symbols));
            node.len = countCharLength(symbols);
        }
        if (_head == null)
            _head = node;
        else
            getLastNode().next = node;
        ++_countOfNodes;
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
        while (index > StringItem.SIZE && node.hasNext()) {
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

    private StringListV2 appendEmpty() {
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
    public StringListV2 append(String string) throws ExecutionControl.NotImplementedException {
        return append(new StringListV2(string));
    }

    /**
     * Добавить связный список {@link StringListV2} в конец строки
     *
     * @param stringList список для добавления
     * @return ссылку на этот объект
     */
    public StringListV2 append(StringListV2 stringList) throws ExecutionControl.NotImplementedException {
        addNodeToTail();
        throw new ExecutionControl.NotImplementedException("Not implemented yet");
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
    public StringListV2 setCharAt(int index, char symbol) {
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
    public StringListV2 insert(int index, String string) throws ExecutionControl.NotImplementedException {
        return this.insert(index, new StringListV2(string));
    }

    /**
     * Вставить связный список {@link StringListV2} после указанного символа
     *
     * @param index      индекс символа
     * @param stringList список для вставки
     * @return ссылка на текущий объект
     */
    public StringListV2 insert(int index, StringListV2 stringList) throws ExecutionControl.NotImplementedException {
        checkIndex(index, this._length);

        StringItem node = getPosition(index).node;

        throw new ExecutionControl.NotImplementedException("Not implemented yet");
    }

    /**
     * Получить подстроку в виде объекта {@link StringListV2}
     *
     * @param beginIndex начальный индекс подстроки
     * @param endIndex   конечный индекс подстроки
     * @return новый объект класса {@link StringListV2}
     */
    public StringListV2 substring(int beginIndex, int endIndex) throws ExecutionControl.NotImplementedException {
        checkBoundsBeginEnd(beginIndex, endIndex, this._length);
        if (endIndex - beginIndex == _length)
            return this;
        return new StringListV2(this.toString().substring(beginIndex, endIndex));
    }

    /**
     * Получить длину строки
     *
     * @return длина строки
     */
    public int length() {
        return this._length;
    }

    /**
     * Получмть количество элементов списка
     *
     * @return количество элементов списка
     */
    public int getCountOfNodes() {
        return _countOfNodes;
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
        while (node != null && node.hasNext()) {
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
        StringListV2 that = (StringListV2) o;
        return _length == that._length &&
                _countOfNodes == that._countOfNodes &&
                _head.equals(that._head);
    }

    /**
     * Переопределение метода.
     *
     * @return хэш объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(_head, _length, _countOfNodes);
    }
}
