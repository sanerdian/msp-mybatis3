package com.jnetdata.msp.util;

//import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 */
//@Slf4j
public class TryDothingUtils {

    /**
     *
     * @param times 最大重试次数
     * @param runnable
     * @param intervalMs 重试间隔时间（毫秒)
     */
    public static void tryDothing(int times, Runnable runnable, int intervalMs) {

        for (int i = 0; i < times; i++) {
            try {
                runnable.run();
                return;

            } catch (Exception e) {
//                log.error(e.getMessage());
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

}
