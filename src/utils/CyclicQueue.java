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
    private boolean contextSwitched = false, prevWasInsert = true;
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
        _tail = 9;
        _head = 0;
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

            if (!prevWasInsert) {
                contextSwitched = true;
                prevWasInsert = true;
                System.out.println("Context was switched");
            }

            _tail = next(_tail);
            queue[_tail] = value;
            System.out.println(Thread.currentThread().getName() + " added item " + value + " to position " + _tail);
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

            if (prevWasInsert) {
                contextSwitched = true;
                prevWasInsert = false;
                System.out.println("Context switched");
            }

            int remVal = queue[_head];
            System.out.println(Thread.currentThread().getName() + " removed item " + remVal + " from position " + _head);
            _head = next(_head);
            System.out.println(this);
            insertCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Проверяем полность очереди, если она полная, то
     * говорим что контекст изменился
     *
     * @return true, если очередь полная
     */
    public boolean isFull() {
        return next(next(_tail)) == _head;
    }

    /**
     * Проверяем пустоту очереди, если она пуста, то
     * говорим что контекст изменился
     *
     * @return true, если очередь пуста
     */
    public boolean isEmpty() {
        return next(_tail) == _head;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Queue is empty!";
        StringBuilder stringBuilder = new StringBuilder();

        int i;
        for (i = _head; i != _tail; i = next(i))
            stringBuilder.append(queue[i]).append(" ");
        stringBuilder.append(queue[i]);

        return stringBuilder.toString();
    }

    /**
     * Получить следующую позицию после указанной
     *
     * @param position позиция
     * @return следующая позиция
     */
    private int next(int position) {
        return (position + 1) % SIZE;
    }

    /**
     * Смотрим, изменился ли контекст
     *
     * @return true, если контекст был изменён
     */
    public boolean isContextSwitched() {
        return contextSwitched;
    }

    /**
     * Сбрасываем переменную изменения контекста
     */
    public void resetContextSwitched() {
        this.contextSwitched = false;
    }
}