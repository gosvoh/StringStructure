import utils.StringList;

public class Main {
    public static void main(String[] args) {
        StringList stringList = new StringList();
        stringList.append("test").append('1');
        stringList.append(" hello").append(" world");
        stringList.append('!');
        stringList.insert(4, "test");
        System.out.println(stringList);
        System.out.println(stringList.substring(0, 18));
    }
}
