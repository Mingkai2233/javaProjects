package functional;// functional/FunctionWithWrapped.java

import java.util.function.*;

public class FunctionWithWrapped {
  public static void main(String[] args) {
    Function<Integer, Double> fid = i -> (double)i;
    IntToDoubleFunction fid2 = i -> i;

    fid.apply(1);
    fid2.applyAsDouble(1);
  }
}