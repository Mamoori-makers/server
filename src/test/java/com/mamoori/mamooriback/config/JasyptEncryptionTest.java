package com.mamoori.mamooriback.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptEncryptionTest {
    @Value("${jasypt.key}")
    private String key;

//    @Test
    void jasypt() {
        String url = "url";
        String username = "sa";
        String password = "password";

        printEncStr(url);
        printEncStr(username);
        printEncStr(password);
    }

    public void printEncStr(String str) {
        System.out.println(str + " -> ENC(" + jasyptEncoding(str) + ")");
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }

}
