import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class MyTest {
    @Test
    public void testCopy(){

    }

    interface Myout{
        void out();
    }
    @Test
    public void testVarCapture(){
        int num = 0;
        for (int i = 0; i < 10; i++){
            int finalI = i;
            new Myout(){
                int nummber = finalI;
                public void out(){
                    System.out.println(nummber);
                }
            }.out();
        }
    }

    @Test
    public void testFinally(){
        for (int i = 0; i < 10; i++){
            try{
                if (i%2==0) continue;
                System.out.println(i);
            }
            finally {
                System.out.println(i);
            }
        }
    }

    @Test
    public void testFinal(){
        final int[] nums = {1,2,3};
        nums[0] = 90;
        int[] nums2 = {2,3,4};
        Arrays.stream(nums).forEach(System.out::println);
    }

    @Test
    public void testStream(){
        List<Integer> list = new ArrayList<>();
        System.out.println(list.stream().reduce(Integer::sum).orElse(0));
        System.out.println(Stream.<String>empty().findAny());
    }
}
