package utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CyclicQueue {
    private int _head, _tail;
    private final ReentrantLock queueLock;
    private final Condition insertCondition;
    private final Condition removeCondition;
    private final int queueSize;

    /**
     * Стандартнй конструктор с 10 элементами
     */
    public CyclicQueue() {
        this(10);
    }

    /**
     * Конструктор с указанным размером
     *
     * @param queueSize размер очереди
     */
    public CyclicQueue(int queueSize) {
        queueLock = new ReentrantLock();
        insertCondition = queueLock.newCondition();
        removeCondition = queueLock.newCondition();
        this.queueSize = queueSize;
    }

    /**
     * Добавить новый элемент, если по истечении 5 секунд не удаётся его добавить,
     * то вызываем RuntimeException
     */
    public void insert() {
        queueLock.lock();
        try {
            while (isFull())
                if (!insertCondition.await(5, TimeUnit.SECONDS))
                    throw new RuntimeException("Cannot insert new Item");
            ++_tail;
            System.out.println("Item #" + _tail + " was added");
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
            ++_head;
            System.out.println("Item #" + _head + " was removed");
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
     * @return true, если очередь полная (хвост - голова, по умолчанию 10)
     */
    public boolean isFull() {
        return _tail - _head == queueSize;
    }

    /**
     * Проверяем пустоту очереди
     *
     * @return true, если очередь пуста (хвост является головой)
     */
    public boolean isEmpty() {
        return _tail == _head;
    }
}