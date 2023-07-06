package com.jnetdata.msp.dict3.wordname;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Entry {
    public static void main(String[] args) {
        long lval = 1480992336000L ;
        java.util.Date time=new java.util.Date((long)lval*1000);


//        System.out.println(time);




//        System.out.println(""+new java.text.SimpleDateFormat("yyyy MM-dd HH:mm:ss").format(new java.util.Date (1480992336000L)));



        SimpleDateFormat df = new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss");
        System.out.println(df.format(time));
    }
}
