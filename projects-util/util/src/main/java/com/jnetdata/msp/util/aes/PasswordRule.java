package com.jnetdata.msp.util.aes;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public abstract class PasswordRule {

    /**
     * 密码强度
     * @param username 账号
     * @param passwd 密码
     * @return 小写字母、大写字母、数字和特殊字符, 满足4类中的三类
     * 说明：按照要求，返回必须>=3才是合格的密码
     */
    public static int goodRule(String username, String passwd) {

        int strength = 0;
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(passwd)) {
            return strength;
        }
        username = space.matcher(username).replaceAll("");
        passwd = space.matcher(passwd).replaceAll("");
        if ((passwd.length() < 8 || passwd.indexOf(username) != -1)) {
            return strength;
        }

        if  ( number.matcher(passwd).find() ) {
            ++strength;
        }

        if ( special.matcher(passwd).find() ) {
            ++strength;
        }

        if ( alpha1.matcher(passwd).find() ) {
            ++strength;
        }

        if (alpha2.matcher(passwd).find()) {
            ++strength;
        }

        return strength;
    }
    private static Pattern space = Pattern.compile("([' ']+)");
    /**
     * 数字或者特殊字符
     */
    private static Pattern number = Pattern.compile("([\\d]+)");
    private static Pattern special = Pattern.compile("([\\W|_]+)");
    private static Pattern alpha1 = Pattern.compile("([a-z]+)");
    private static Pattern alpha2 = Pattern.compile("([A-Z]+)");


    /**
     * 校验密码规则，如不符合安全规则，将抛出异常
     * @param username
     * @param passwd
     */
    public static void validatePasswordRule(String username, String passwd){
        if(goodRule(username,passwd) < 3){
            throw new RuntimeException("您输入的密码不符合密码安全规则，" +
                    "密码安全规则为：" +
                    "1.密码由至少8位字母及数字字符组成；" +
                    "2.不能包含全部或部分的用户账号名；" +
                    "3.至少包含大写字母、小写字母、数字和特殊符号中的三种；");
        }
    }

    public static void main(String[] args) {
        AssertTrue(goodRule("18611805123", null) == 0);
        AssertTrue( goodRule("18611805123", "") == 0);
        AssertTrue(goodRule("18611805123", "186118") == 0);
        AssertTrue( goodRule("18611805123", "18611805123")  == 0);
        AssertTrue(goodRule("18611805123", "134567") == 0);

        AssertTrue(goodRule("18611805123", "11805123") == 1);
        AssertTrue(goodRule("18611805123", "abcdefgh") == 1);
        AssertTrue(goodRule("18611805123", "  abcd  efgh  ") == 1);
        AssertTrue(goodRule("18611805123", "abcdefgh@#$%") == 2);
        AssertTrue(goodRule("18611805123", "123_A#$)")==3);
        AssertTrue(goodRule("18611805123", "abcdefgh@#ABC")==3);
        AssertTrue(goodRule("18611805123", "abcdefgh_132")==3);

    }
    private static void AssertTrue(boolean t) {
        if (!t) {
            throw new RuntimeException("出错了!");
        }
    }
}
