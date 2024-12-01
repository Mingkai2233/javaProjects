package org.onjava8;

import java.util.function.IntConsumer;

public class MM {
    interface IntCall{
        int call(int i);
    }
    static IntCall myfunc;
    public static void main(String[] args) {
        myfunc = (int i)->{
            if (i == 1) return 1;
            return myfunc.call(i-1)*i;
        };
        int res = myfunc.call(10);
        System.out.println(res);
    }
}
