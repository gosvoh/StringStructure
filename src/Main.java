import utils.StringList;

public class Main {
    public static void main(String[] args) {
        /*CyclicQueue<Integer> cyclicQueue = new CyclicQueue<>();
        Producer producer = new Producer(cyclicQueue);
        Consumer consumer1 = new Consumer(cyclicQueue);
        Consumer consumer2 = new Consumer(cyclicQueue);
        Consumer consumer3 = new Consumer(cyclicQueue);
        new Thread(producer).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();*/

        StringList stringList = new StringList("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        StringList list = new StringList(stringList);
        stringList.insert(25, list);
        list.append("qweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqwe");
        System.out.println(list);
    }
}

