package utils;

public class StringList {
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

        public StringItem getNext() {
            return next;
        }

        public boolean hasNext() {
            return next != null;
        }
    }

    private StringItem _head, _tail;
    private int length, count;

    public StringList() {
    }

    public StringList(String string) {
        append(string);
    }

    public StringList(char[] chars) {
        append(chars);
    }

    @SuppressWarnings({"unused", "CopyConstructorMissesField"})
    public StringList(StringList stringList) {
        if (stringList._head != null)
            append(stringList.toString());
    }

    private byte countCharLength(char[] chars) {
        int count = 0;
        for (char c : chars) {
            if (c != (char) 0)
                count++;
        }
        return (byte) count;
    }

    private void sortNode(StringItem node) {
        if (node.hasNext())
            if ((node.len + node.next.len) <= 16) {
                System.arraycopy(node.next.symbols, 0, node.symbols, node.len, node.next.len);
                node.len = countCharLength(node.symbols);
                if (node.next.hasNext())
                    node.next = node.next.next;
                else {
                    node.next = null;
                    _tail = node;
                }
                count--;
                sortNode(node);
            }
    }

    private void addNode() {
        StringItem node = new StringItem();
        if (_head == null)
            _head = node;
        else
            _tail.next = node;
        _tail = node;
    }

    private StringItem addNode(StringItem node) {
        StringItem newNode = new StringItem();
        if (node == _head && _head == null) {
            _head = newNode;
            _tail = newNode;
        } else if (node == _tail) {
            _tail.next = newNode;
            _tail = newNode;
        } else {
            newNode.next = node.next;
            node.next = newNode;
        }
        count++;
        return newNode;
    }

    private StringList appendNull() {
        char[] val = {'n', 'u', 'l', 'l'};
        return append(val);
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

    private String PrintList(StringItem node) {
        if (node == _head) {
            if (node == null)
                return "null";
            if (!node.hasNext())
                return new String(_head.symbols);
        }
        int offset = 0;
        char[] string = new char[this.length];
        while (node != null) {
            sortNode(node);
            System.arraycopy(node.symbols, 0, string, offset, node.len);
            offset += node.len;
            node = node.next;
        }
        return new String(string);
    }

    private String PrintListWithoutSorting(StringItem node) {
        if (node == _head) {
            if (node == null)
                return "null";
            if (!node.hasNext())
                return new String(_head.symbols);
        }
        int offset = 0;
        char[] string = new char[this.length];
        while (node != null) {
            System.arraycopy(node.symbols, 0, string, offset, node.len);
            offset += node.len;
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
        if (index < 0 || index >= length)
            throw new IndexOutOfBoundsException("index " + index + ",length " + length);
    }

    static void checkBoundsBeginEnd(int begin, int end, int length) {
        if (begin < 0 || begin > end || end > length) {
            throw new IndexOutOfBoundsException("begin " + begin + ", end " + end + ", length " + length);
        }
    }

    private StringList appendAfter(StringItem node, char[] chars) {
        node = addNode(node);
        node.symbols = new char[StringItem.SIZE];
        if (chars.length >= 16) {
            System.arraycopy(chars, 0, node.symbols, 0, StringItem.SIZE);
            node.len = countCharLength(node.symbols);
            this.length += node.len;
            char[] temp = new char[chars.length - StringItem.SIZE];
            System.arraycopy(chars, StringItem.SIZE, temp, 0, chars.length - StringItem.SIZE);
            append(temp);
        } else {
            System.arraycopy(chars, 0, node.symbols, 0, countCharLength(chars));
            node.len = countCharLength(node.symbols);
            this.length += node.len;
        }
        return this;
    }

    /**
     * Добавить массив символов в конец строки
     *
     * @param chars массив символов
     * @return ссылку на этот объект
     */
    public StringList append(char[] chars) {
        if (chars == null)
            return appendNull();
        return appendAfter(_tail, chars);
    }

    /**
     * Добавить строку в конец строки
     *
     * @param string строка
     * @return ссылку на этот объект
     */
    public StringList append(String string) {
        if (string == null)
            return appendNull();
        return append(string.toCharArray());
    }

    /**
     * Добавить символ в конец строки
     *
     * @param symbol массив символов
     * @return ссылку на этот объект
     */
    public StringList append(char symbol) {
        return append(new char[]{symbol});
    }

    @Override
    public String toString() {
        return PrintList(_head);
        //return PrintListWithoutSorting(_head);
    }

    public int getCount() {
        return count;
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
            sortNode(node);
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
            sortNode(node);
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else {
                node.symbols[index] = symbol;
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

    public StringList insert(int index, String string) {
        checkIndex(index, this.length);
        StringItem node = _head;
        while (true) {
            sortNode(node);
            if (index > node.len) {
                index -= node.len;
                node = node.next;
            } else {
                char[] tmp = new char[StringItem.SIZE];
                System.arraycopy(node.symbols, 0, tmp, 0, StringItem.SIZE);
                node.symbols = new char[StringItem.SIZE];
                System.arraycopy(tmp, 0, node.symbols, 0, index);
                node.len = countCharLength(node.symbols);
                addNode(node);
                node.next.symbols = new char[StringItem.SIZE];
                System.arraycopy(tmp, index, node.next.symbols, 0, StringItem.SIZE - index);
                node.next.len = countCharLength(node.next.symbols);
                appendAfter(node, string.toCharArray());
                break;
            }
        }
        return this;
    }

    public StringList substring(int beginIndex, int endIndex) {
        int length = this.length();
        checkBoundsBeginEnd(beginIndex, endIndex, length);
        int subLen = endIndex - beginIndex;
        if (beginIndex == 0 && endIndex == length) {
            return this;
        } else {
            /*StringItem node = _head;
            while (true) {
                sortNode(node);
                if (beginIndex > node.len) {
                    beginIndex -= node.len;
                    node = node.next;
                } else {

                    char[] tmp = new char[subLen];
                    int offset = 0;
                    while (true) {
                        if (subLen <= StringItem.SIZE) {
                            System.arraycopy(node.symbols, 0, tmp, 0, subLen);
                            return new StringList(tmp);
                        } else {
                            System.arraycopy(node.symbols, 0, tmp, offset, StringItem.SIZE);
                            offset += StringItem.SIZE;
                            node = node.next;
                        }
                    }
                }
            }*/
            return new StringList(this.toString().substring(beginIndex, endIndex));
        }
    }
}
