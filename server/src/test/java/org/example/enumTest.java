package org.example;

/**
 * @ClassName: enumTest
 * @Author: MaCongYi
 * @create: 2022-09-02 17:26
 * @Description:
 * @Version: 1.0
 */
public enum enumTest {
    redis_order("fdsa"),
    redis_order2("fdsa");

private final String s;

    enumTest(String s) {
        this.s=s;
    }
}
