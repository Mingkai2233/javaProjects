package org.onjava8;

public class DotThis {
    int num;
    public DotThis(int num){
        this.num = num;
    }
    public void f(){
        System.out.println(num);
    }
    public class Inner{
        public DotThis outer(){
            return DotThis.this;
        }
    }


    public static void main(String[] args) {
        DotThis outer = new DotThis(0);
        DotThis.Inner inner = outer.new Inner();
        inner.outer().f();
    }
}
