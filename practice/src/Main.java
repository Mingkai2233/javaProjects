import java.util.Arrays;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {

    public static void main(String[] args) {

    }
    static void changeIt(int a){
        a++;
    }

}

class Base{
    int a = 10;
    final void doSomething(String s){
        System.out.println(this.getClass() + s);
    }
}

class Derived extends Base{
    //@Override
    void doSomething(int s) {
        System.out.println(Integer.valueOf(this.a).toString() + this.getClass() + s);
    }
}