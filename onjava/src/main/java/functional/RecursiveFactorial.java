package functional;

public class RecursiveFactorial {
  static IntCall fact;
  public static void main(String[] args) {
    // 静态变量实现递归函数，计算阶乘
    fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
    for(int i = 0; i <= 10; i++)
      System.out.println(fact.call(i));
  }
}