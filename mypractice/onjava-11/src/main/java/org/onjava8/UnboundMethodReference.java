package org.onjava8;// functional/UnboundMethodReference.java

// 没有方法引用的对象

class X {
  String f() { return "X::f()"; }
  static class InnerX{
    public static String f() { return "InnerX::f()"; };
  }
}

interface MakeString {
  String make();
}

interface TransformX {
  String transform(X x);
}

public class UnboundMethodReference {
  public static void main(String[] args) {
    MakeString ms = X.InnerX::f;
    TransformX sp = X::f;
    X x = new X();
    System.out.println(ms.make());
    System.out.println(sp.transform(x)); // [2]
    System.out.println(x.f()); // 同等效果
  }
}