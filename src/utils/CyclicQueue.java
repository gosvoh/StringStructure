package utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс, представляющий собой циклический список
 *
 * @author Aleksei Vokhmin
 */
public class CyclicQueue {
    private int _head, _tail;
    private final int[] queue;
    private final ReentrantLock queueLock;
    private final Condition insertCondition;
    private final Condition removeCondition;
    private final int SIZE;

    /**
     * Стандартнй конструктор с 10 элементами
     */
    public CyclicQueue() {
        this(10);
    }

    /**
     * Конструктор с указанным размером
     *
     * @param SIZE размер очереди
     */
    public CyclicQueue(int SIZE) {
        queue = new int[SIZE];
        queueLock = new ReentrantLock();
        insertCondition = queueLock.newCondition();
        removeCondition = queueLock.newCondition();
        this.SIZE = SIZE;
        _tail = -1;
        _head = -1;
    }

    /**
     * Добавить новый элемент, если по истечении 5 секунд не удаётся его добавить,
     * то вызываем RuntimeException
     */
    public void insert(int value) {
        queueLock.lock();
        try {
            while (isFull())
                if (!insertCondition.await(5, TimeUnit.SECONDS))
                    throw new RuntimeException("Cannot insert new Item");

            if (_head == -1) _head = 0;
            _tail = (_tail + 1) % SIZE;
            queue[_tail] = value;

            System.out.println("Item " + value + " was added to position " + _tail);
            System.out.println(this);
            removeCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Удалить элемент, если по истечении 5 секунд не удаётся его удалить,
     * то вызываем RuntimeException
     */
    public void remove() {
        queueLock.lock();
        try {
            while (isEmpty())
                if (!removeCondition.await(5, TimeUnit.SECONDS))
                    throw new RuntimeException("Cannot remove Item");

            int remVal = queue[_head];
            System.out.println("Item " + remVal + " was removed from position " + _head);
            if (_head == _tail) {
                _head = -1;
                _tail = -1;
            } else _head = (_head + 1) % SIZE;
            System.out.println(this);
            insertCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Проверяем полность очереди
     *
     * @return true, если очередь полная
     */
    public boolean isFull() {
        return ((_head == 0) && (_tail == SIZE - 1)) || (_head == _tail + 1);
    }

    /**
     * Проверяем пустоту очереди
     *
     * @return true, если очередь пуста
     */
    public boolean isEmpty() {
        return _head == -1;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Queue is empty!";
        StringBuilder stringBuilder = new StringBuilder();

        int i;
        for (i = _head; i != _tail; i = (i + 1) % SIZE)
            stringBuilder.append(queue[i]).append(" ");
        stringBuilder.append(queue[i]);

        return stringBuilder.toString();
    }
}