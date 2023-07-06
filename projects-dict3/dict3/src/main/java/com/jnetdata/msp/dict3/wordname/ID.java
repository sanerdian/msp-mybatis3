package com.jnetdata.msp.dict3.wordname;
import com.google.common.math.LongMath;
import sun.rmi.runtime.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 获取long型唯一ID
 */
public class ID
{

    private static long tmpID = 0;

    private static boolean tmpIDlocked = false;

    public static long getId()  {

//        try {
//            Thread.sleep(1) ;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        long ltime = 0;
        while (true)
        {
            if(tmpIDlocked == false)
            {
                tmpIDlocked = true;
                //当前：（年、月、日、时、分、秒、毫秒）*10000
                ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date()).toString()) * 1000+ (new Random().nextInt(99)+1);
                if(tmpID < ltime)
                {
                    tmpID = ltime;
                }
                else
                {
                    tmpID = tmpID + 1;
                    ltime = tmpID;
                }
                tmpIDlocked = false;
                return ltime;
            }
        }
    }
}