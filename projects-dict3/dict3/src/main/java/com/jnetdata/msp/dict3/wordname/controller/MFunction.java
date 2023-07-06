package com.jnetdata.msp.dict3.wordname.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MFunction {
    protected static String extracted(int caladay) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();
        System.out.println(dateFormat.format(currentDate));
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, caladay); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        Date currentDateCalc = c.getTime();

//        System.out.println(dateFormat.format(currentDateCalc));

        return dateFormat.format(currentDateCalc);

    }
}
