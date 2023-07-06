package com.jnetdata.msp.config;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.model.util.PasswordHelper;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ConfigPropertiesValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return AppConfigProperties.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
//        String cpuSerial = getCPUSerial();
//        String md5 = PasswordHelper.doEncryptedPassword(cpuSerial + "cpuSerial");
//        AppConfigProperties config = (AppConfigProperties) o;
//        if (StringUtils.isEmpty(config.getKey())) {
//            errors.rejectValue("key", "app.key.empty", "[app.key] 属性必须要在配置文件配置");
//        } else if (!config.getKey().equals(md5)) {
//            errors.rejectValue("key", "app.key.error", "[app.key] 错误");
//        }
    }

    /**获取本机CPU信息
     */
    private static String getCPUSerial() {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) { //windows
            return getWindowsCPUSerial();
        }else{
            return getLinuxCPUSerial();
        }
    }

    private static String getWindowsCPUSerial() {
        String serial = "";
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            serial = sc.next();
            process.destroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return serial;
    }

    private static String getLinuxCPUSerial(){
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
            InputStream in;
            BufferedReader br;
            in = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            while (in.read() != -1) {
                result = br.readLine();
            }
            br.close();
            in.close();
            process.destroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}