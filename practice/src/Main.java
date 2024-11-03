import java.nio.CharBuffer;
import java.util.Arrays;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 构造从start到end的序列：
        final int start = 10;
        final int end = 20;
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        // 洗牌算法shuffle可以随机交换List中的元素位置:
       Collections.shuffle(list);
        // 随机删除List中的一个元素:
        int removed = list.remove((int) (Math.random() * list.size()));
        int found = findMissingNumber(start, end, list);
        System.out.println(list.toString());
        System.out.println("missing number: " + found);
        System.out.println(removed == found ? "测试成功" : "测试失败");
    }

    static int findMissingNumber(int start, int end, List<Integer> list) {
        Collections.sort(list);
        for (var integer:list){
            if (integer != start)
                return start;
            start++;
        }
        return 0;
    }
}

interface Processor{
    default String name(){
        return this.getClass().getSimpleName();
    }

    Object process(Object o);
}

class Applicator{
    static void apply(Processor pro, Object o){
        System.out.println("Using processor " + pro.name());
        System.out.println(pro.process(o));
    }
}

interface StringProcessor extends Processor{
    @Override
    String process(Object o);
    String s = "Hello world, my name is LeiJun";
    static void main(String[] args){
        Applicator.apply(new Upcase(), s);
        Applicator.apply(new Lowcase(), s);
        Applicator.apply(new Upcase(), new Object());
    }
}

class Upcase implements StringProcessor{
    @Override
    public String process(Object o) {
        try{
            String s = (String)o;
            return s.toUpperCase();
        }
        catch (ClassCastException e){
            System.out.println(e.toString());
            return "ERROR";
        }
    }
}

class Lowcase implements StringProcessor{
    @Override
    public String process(Object o) {
        try{
            String s = (String)o;
            return s.toLowerCase();
        }
        catch (ClassCastException e){
            System.out.println(e.toString());
            return "ERROR";
        }
    }
}