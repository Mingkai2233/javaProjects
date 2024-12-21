package functional;// functional/TransformFunction.java

import java.util.function.*;

class I {
  @Override
  public String toString() { return "I"; }
}

class O {
  @Override
  public String toString() { return "O"; }
}

class C {
  @Override
  public String toString() { return "C"; }
}

public class TransformFunction {
  static Function<I,O> transform(Function<I,O> in) {
    return in.andThen(o -> {
      // 在保证输入输出类型一致的情况下，能增加要做的操作
      System.out.println(o);
      return o;
    });
  }
  static Function<I,C> transformC(Function<I,O> in) {
    return in.andThen(o->{
      System.out.println(o);
      C c =  new C();
      System.out.println(c);
      return c;
    });
  }
  public static void main(String[] args) {
    Function<I,O> f2 = transform(i -> {
      System.out.println(i);
      return new O();
    });

    O o = f2.apply(new I());
    Function<I,C> f3 = transformC(f2);
    f3.apply(new I());
  }
}