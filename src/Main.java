import utils.StringList;

public class Main {
    public static void main(String[] args) {
        StringList list = new StringList();
        String string = "qwertyuiopaffffffffffffff";
        list.append(string).append('1');
        list.append(" what? ");
        System.out.println(list);
        System.out.println(list.length());
        System.out.println(list.charAt(15));
        //list.insert(10, "hello");
        list.append('2').append('3');
        System.out.println(list);

        char[] temp = new char[16];
        System.out.println(temp);
    }
}
