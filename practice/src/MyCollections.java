import java.util.List;

public class MyCollections {
    public static void main(String[] args) {
        List<Integer> list = List.of(1,2,3);
        var it = list.iterator();
        it.next();
        it.remove();
        System.out.println(list);
    }
}
