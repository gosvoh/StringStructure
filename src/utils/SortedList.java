package utils;

/**
 * Класс, представляющий собой список, принимающий объекты, реализующие
 * инрейфейс Comparable и наследующиеся до класса переданного параметра
 *
 * @author Aleksei Vokhmin
 */
public class SortedList<T extends Comparable<? super T>> {

    /**
     * Размер массива, инициализируется в конструкторе
     */
    private final int SIZE;
    /**
     * Основной массив данных для работы, инициализируется в конструкторе
     */
    private final T[] array;
    /**
     * Голова очереди
     */
    private int _tail;

    /**
     * Конструктор с передаваемым массивом указаного типа
     *
     * @param array массив
     */
    public SortedList(T[] array) {
        this.array = array;
        this.SIZE = array.length;
        this._tail = -1;
    }

    /**
     * Получить позицию конца списка +1
     *
     * @return конец списка +1
     */
    public int end() {
        return _tail + 1;
    }

    /**
     * Вставить элемент в указанную позицию
     *
     * @param value    элемент
     * @param position позиция
     * @throws ArrayIndexOutOfBoundsException если больше нет места в масиве
     */
    public void insert(T value, int position) {
        if (_tail + 1 == SIZE)
            throw new ArrayIndexOutOfBoundsException("There is no more space for new value!");
        if (checkPosition(position))
            return;
        //for (int i = _tail; i >= position; i--)  <- Это более старая версия того,
        //    array[i + 1] = array[i];                что написано ниже.
        System.arraycopy(array, position, array, position + 1, _tail + 1 - position);
        array[position] = value;
        ++_tail;
    }

    /**
     * Получить позицию указанного элемента
     *
     * @param value элемент в списке
     * @return позиция элемента
     * @throws IllegalArgumentException если элемент не найден в списке
     */
    public int locate(T value) {
        for (int i = 0; i < _tail + 1; i++) {
            if (array[i].equals(value))
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
    public T retrieve(int position) {
        if (checkPosition(position))
            throw new IllegalArgumentException("Wrong position!");
        if (array[position] == null)
            throw new IllegalArgumentException("Wrong position!");
        return array[position];
    }

    /**
     * Удалить элемент в указанной позиции
     *
     * @param position позиция
     * @throws IllegalArgumentException       если позиция неверна
     * @throws ArrayIndexOutOfBoundsException если список пуст
     */
    public void delete(int position) {
        if (checkPosition(position))
            return;
        array[position] = null;
        if (_tail == 0) {
            _tail = -1;
        } else {
            //for (int i = position; i <= _tail; i++)  <- Это более старая версия того,
            //    array[i] = array[i + 1];                что написано ниже.
            System.arraycopy(array, position + 1, array, position, _tail - position);
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
        if (checkPosition(position))
            throw new IllegalArgumentException("Wrong position!");
        if (position >= _tail + 1)
            throw new IllegalArgumentException("There's no elements after!");
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
        if (checkPosition(position))
            throw new IllegalArgumentException("Wrong position!");
        if (position == 0)
            throw new IllegalArgumentException("There's no elements before!");
        return position - 1;
    }

    /**
     * Обнулить список
     */
    public void makeNull() {
        _tail = -1;
    }

    /**
     * Получить позицию первого элемента
     *
     * @return позиция первого элемента
     */
    public int first() {
        return 0;
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
    private boolean checkPosition(int position) {
        return position > SIZE || position < 0;
    }

    /**
     * Поменять элементы массива местами.
     * Метод требуется для сортировки
     *
     * @param pos1 первый элемент
     * @param pos2 второй элемент
     */
    private void swap(int pos1, int pos2) {
        T val = array[pos1];
        array[pos1] = array[pos2];
        array[pos2] = val;
    }

    /**
     * Простая пузырьковая сортировка
     */
    public void sort() {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < _tail; i++) {
                if (array[i].compareTo(array[i + 1]) > 0) {
                    isSorted = false;
                    swap(i, i + 1);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (_tail == -1) return "List is empty!";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < _tail; i++)
            sb.append(array[i]).append(" ");
        sb.append(array[_tail]);
        return sb.toString();
    }
}
