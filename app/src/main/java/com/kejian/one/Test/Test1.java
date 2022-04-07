package com.kejian.one.Test;

/**
 * @author Zhenxi on 2021/1/9
 */
public class Test1 {



    private static int MyHp =0;
    private static double addition =0.1;



    public int add(){
        int a=1;
        int b=2;
        return a+b;
    }

    public static int getHp(){
        if (MyHp == 0) {
            MyHp = 100;
        }
        int i = MyHp;
        int i2 = i + ((int) (((double) i) * addition));
        MyHp = i2;

        return i2;
    }

}
