package com.jnetdata.msp.core.model.util;


import com.jnetdata.msp.member.user.model.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Service
public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 1;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public static void doEncryptedPassword(User user) {
        String passwd = doEncryptedPassword(user.getPassWord());
        user.setMdPassWord(passwd);
    }
    /*
    * 重置密码
    * @param password 新密码
    * */
    public static String doEncryptedPassword(String password) {
        return new PasswordHelper().doEncryptedPassword(password, null);
    }
    public String doEncryptedPassword(String password, String credentialsSalt) {
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                Objects.isNull(credentialsSalt)?null:ByteSource.Util.bytes(credentialsSalt),
                hashIterations).toHex();
        return newPassword;
    }

    public static void main(String[] args) {
        String credentials = "123456";
        Object obj = new SimpleHash("MD5", credentials, null, 1);
        System.out.println(obj);

        System.out.println("helper = " + PasswordHelper.doEncryptedPassword("admin123"));

    }
}
