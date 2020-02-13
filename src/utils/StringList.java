package utils;

import java.util.Arrays;

public class StringList {
    /**
     * Связный список
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
         * Нулевой конструктор, все поля равны null
         */
        public StringItem() {
        }

        public boolean hasNext() {
            return next != null;
        }
    }

    /* ***Fields*** */

    /**
     * Голова списка
     */
    private StringItem _head;

    /**
     * Хвост списка
     */
    private StringItem _tail;

    /**
     * Длина строки
     */
    private int length;

    /* ***Constructors*** */

    /**
     * Нулевой констуктор
     */
    public StringList() {
    }

    /**
     * Конструктор с инициализацией структуры и добавлением строки
     *
     * @param string строка
     */
    public StringList(String string) {
        append(string);
    }

    /**
     * Конструктор с инициализацией структуры и добавлением строки
     *
     * @param string строка
     */
    public StringList(char[] string) {
        append(string);
    }

    /* ***Private methods*** */

    private StringItem addNodeAfter(StringItem node) {
        StringItem newNode = new StringItem();
        if (_head == null) {
            _head = _tail = newNode;
        } else {
            if (node == _tail) {
                node.next = newNode;
                _tail = newNode;
            }
            else {
                newNode.next = node.next;
                node.next = newNode;
            }
        }
        return newNode;
    }

    /**
     * Добавить в конец строки "null" если при добавлении строка не была инициализирована
     *
     * @return "null" как строка
     */
    private StringList appendNull() {
        char[] val = {'n', 'u', 'l', 'l'};
        return append(val);
    }

    private byte countCharLength(char[] chars) {
        int count = 0;
        for (char c : chars) {
            if (c != (char) 0)
                count++;
        }
        return (byte) count;
    }

    private StringItem getNodeFromIndex(int index) {
        checkIndex(index, this.length);
        StringItem node = _head;
        while (true) {
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else return node;
        }
    }

    /**
     * Пройтись по всему списку с указанного места и преобразовать в строку
     *
     * @param node элемент с которого начать преобразование
     * @return строка со всеми элементами
     */
    private String PrintList(StringItem node) {
        int offset = 0;
        char[] string = new char[this.length];
        while (node.hasNext()) {
            System.arraycopy(node.symbols, 0, string, offset, node.len);
            offset += node.len;
            if (node.next.symbols == null && node.next.hasNext())
                node.next = node.next.next;
            if ((node.len + node.next.len) <= 16) {
                System.arraycopy(node.next.symbols, 0, node.symbols, node.len, node.next.len);
                node.len = countCharLength(node.symbols);
                if (node.next != _tail)
                    node.next = node.next.next;
                else
                    node.next.symbols = null;
                node.next.len = 0;
            }
            node = node.next;
        }
        return new String(string);
    }

    /**
     * Проверить выход за границы массива
     *
     * @param index  индекс в массиве
     * @param length длина строки
     */
    private void checkIndex(int index, int length) {
        if (index < 0 || index > length)
            throw new IndexOutOfBoundsException(index);
    }

    /**
     * Добавить блок со строкой после указанного элемента
     *
     * @param string строка для добавления
     * @param node   элемент, после которого нужно добавить строку
     */
    private void appendStringAfter(StringItem node, char[] string) {
        StringItem currentNode = addNodeAfter(node);
        currentNode.symbols = new char[StringItem.SIZE];
        int stringLength = countCharLength(string);

        if (stringLength > StringItem.SIZE) {
            System.arraycopy(string, 0, currentNode.symbols, 0, StringItem.SIZE);
            currentNode.len = countCharLength(currentNode.symbols);
            this.length += currentNode.len;

            char[] tempString = new char[stringLength - StringItem.SIZE];
            System.arraycopy(string, StringItem.SIZE, tempString, 0, stringLength - StringItem.SIZE);
            string = tempString;
            appendStringAfter(currentNode, string);
        } else {
            //System.arraycopy(string, 0, currentPart, 0, stringLength);
            System.arraycopy(string, 0, currentNode.symbols, 0, stringLength);
            currentNode.len = countCharLength(currentNode.symbols);
            this.length += currentNode.len;
        }
    }

    /* ***Public methods*** */

    public StringList append(char symbol) {
        char[] sym = {symbol};
        return append(sym);
    }

    public StringList append(char[] string) {
        if (string == null)
            return appendNull();

        appendStringAfter(_tail, string);
        return this;
    }

    public StringList append(String string) {
        if (string == null)
            return appendNull();
        return append(string.toCharArray());
    }

    /**
     * Вернуть символ в указанном месте
     *
     * @param index индекс символа
     * @return символ
     */
    public char charAt(int index) {
        checkIndex(index, this.length);
        StringItem node = _head;
        while (true) {
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else return node.symbols[index];
        }
    }

    /**
     * Установить символ в указанном месте
     *
     * @param index индекс символа
     */
    public void setCharAt(int index, char symbol) {
        checkIndex(index, this.length);
        StringItem node = _head;
        while (true) {
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else {
                node.symbols[index] = symbol;
                return;
            }
        }
    }

    public StringList test(int index, String string) {
        insert(index, string);
        return this;
    }

    public void insert(int index, String string) {
        checkIndex(index, this.length);
        StringItem node = _head;
        while (true) {
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else {
                char[] firstPart = new char[StringItem.SIZE];
                char[] secondPart = new char[StringItem.SIZE];
                System.arraycopy(node.symbols, 0, firstPart, 0, index);
                System.arraycopy(node.symbols, index, secondPart, 0, StringItem.SIZE - index);
                StringItem firstNode = new StringItem();
                StringItem secondNode = new StringItem();
                firstNode.symbols = firstPart;
                secondNode.symbols = secondPart;
                firstNode.len = countCharLength(firstPart);
                secondNode.len = countCharLength(secondPart);

                secondNode.next = node.next;
                node = firstNode;
                appendStringAfter(node, string.toCharArray());
                node.next.next = secondNode;
                return;
            }
        }
    }

    /**
     * Длина всей строки
     *
     * @return длина строки
     */
    public int length() {
        return this.length;
    }

    @Override
    public String toString() {
        return PrintList(_head);
    }

    public StringList substring(int start, int end) {
        //TODO
        return null;
    }
}
