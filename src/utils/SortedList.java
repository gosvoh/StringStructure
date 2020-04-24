package utils;

/**
 * Класс, представляющий собой список, принимающий объекты, реализующие
 * инрейфейс Comparable и наследующиеся до класса переданного параметра
 *
 * @author Aleksei Vokhmin
 */
public class SortedList<T extends Comparable<? super T>> {

    private final int SIZE;
    private final Object[] array;
    private int _head, _tail;

    /**
     * Конструктор с передаваемым массивом указаного типа и
     * стандартным размером списка, равным 10
     *
     * @param array массив
     * @throws ArrayIndexOutOfBoundsException если передаваемый массив больше максимального размера
     */
    public SortedList(T[] array) {
        this(10, array);
    }

    /**
     * Конструктор с передаваемым массивом указаного типа и максимальным размером списка
     *
     * @param size  размер списка
     * @param array массив
     * @throws ArrayIndexOutOfBoundsException если передаваемый массив больше максимального размера
     */
    public SortedList(int size, T[] array) {
        this.SIZE = size;
        if (array.length > SIZE)
            throw new ArrayIndexOutOfBoundsException("Size of argument array is more than expected! Max size is " + SIZE);
        this.array = new Object[SIZE];
        int realLen = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                this.array[i] = array[i];
                ++realLen;
            }
        }
        _head = realLen == 0 ? -1 : 0;
        _tail = realLen == 0 ? -1 : realLen;
    }

    /**
     * Получить позицию конца списка +1
     *
     * @return конец списка +1
     */
    public int end() {
        if (_head == -1)
            return 0;
        return _tail + 1;
    }

    /**
     * Вставить элемент в указанную позицию
     *
     * @param value    элемент
     * @param position позиция
     * @throws IllegalArgumentException       если позиция неверна
     * @throws ArrayIndexOutOfBoundsException если больше нет места в масиве
     */
    public void insert(T value, int position) {
        if (length() >= SIZE)
            throw new ArrayIndexOutOfBoundsException("There is no more space for new value!");
        checkPosition(position);
        for (int i = _tail; i >= position; i--)
            swap(i + 1, i);
        array[position] = value;
        if (_head == -1) ++_head;
        ++_tail;
    }

    /**
     * Получить позицию указанного элемента
     *
     * @param value элемент в списке
     * @return позиция элемента
     * @throws IllegalArgumentException если элемент не найден в списке
     */
    @SuppressWarnings("unchecked")
    public int locate(T value) {
        for (int i = 0; i < end(); i++) {
            T val = (T) array[i];
            if (val.equals(value))
                return i;
        }
        throw new IllegalArgumentException("Wrong value!");
    }

    /**
     * Получить элемент в указаной позиции
     *
     * @param position позиция элемента
     * @return элемент
     * @throws IllegalArgumentException если позиция неверна
     */
    @SuppressWarnings("unchecked")
    public T retrieve(int position) {
        checkPosition(position);
        if (array[position] == null)
            throw new IllegalArgumentException("Wrong position!");
        return (T) array[position];
    }

    /**
     * Удалить элемент в указанной позиции
     *
     * @param position позиция
     * @throws IllegalArgumentException       если позиция неверна
     * @throws ArrayIndexOutOfBoundsException если список пуст
     */
    public void delete(int position) {
        if (_head == -1)
            throw new ArrayIndexOutOfBoundsException("Nothing to delete!");
        checkPosition(position);
        if (position == end())
            throw new IllegalArgumentException("Wrong position! Position must be >= 0 and < " + end());
        array[position] = null;
        if (length() == 0) {
            _head = -1;
            _tail = -1;
        } else {
            for (int i = position; i <= _tail; i++)
                swap(i, i + 1);
            --_tail;
        }
    }

    /**
     * Получить позицию следующего элемента
     *
     * @param position позиция элемента
     * @return следующая позиция
     * @throws IllegalArgumentException если позиция неверна
     */
    public int next(int position) {
        checkPosition(position);
        if (position == end())
            throw new IllegalArgumentException("Wrong position!");
        return position + 1;
    }

    /**
     * Получить позицию предыдущего элемента
     *
     * @param position позиция элемента
     * @return предыдущая позиция
     * @throws IllegalArgumentException если позиция неверна
     */
    public int previous(int position) {
        checkPosition(position);
        if (position == first())
            throw new IllegalArgumentException("Wrong position!");
        return position - 1;
    }

    /**
     * Обнулить список
     */
    public void makeNull() {
        while (_tail != _head) {
            array[_tail] = null;
            --_tail;
        }
        if (_head != -1)
            array[_head] = null;
        _head = -1;
        _tail = -1;
    }

    /**
     * Получить позицию первого элемента
     *
     * @return позиция первого элемента
     */
    public int first() {
        if (_head == -1)
            return end();
        return _head;
    }

    /**
     * Показать список
     */
    public void printList() {
        System.out.println(this);
    }

    /**
     * Проверить позицию, если она не верна, то кинуть исключение
     *
     * @param position позиция для проверки
     * @throws IllegalArgumentException если позиция неверна
     */
    private void checkPosition(int position) {
        if (position > SIZE || position < 0 || _head != -1 && (position > end() || position < first()))
            throw new IllegalArgumentException("Wrong position! Position must be >= 0 and <= " + end());
    }

    /**
     * Получить реальный размер списка
     *
     * @return размер списка
     */
    public int length() {
        if (_head == -1)
            return 0;
        int len = 0;
        for (int i = 0; i <= _tail; i++)
            if (array[i] != null)
                ++len;
        return len;
    }

    /**
     * Поменять элементы массива местами
     *
     * @param pos1 первый элемент
     * @param pos2 второй элемент
     */
    private void swap(int pos1, int pos2) {
        Object val = array[pos1];
        array[pos1] = array[pos2];
        array[pos2] = val;
    }

    /**
     * Простая пузырьковая сортировка
     */
    @SuppressWarnings("unchecked")
    public void sort() {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < _tail; i++) {
                T val = (T) array[i];
                if (val.compareTo((T) array[i + 1]) > 0) {
                    isSorted = false;
                    swap(i, i + 1);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (_head == -1) return "List is empty!";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < _tail; i++)
            sb.append(array[i]).append(" ");
        sb.append(array[_tail]);
        return sb.toString();
    }
}
