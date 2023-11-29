package com.haiphamcoder.apisplitabtest.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ABTestService {
    public static double g1 = 0.2;
    public static double g2 = 0.3;
    public static double g3 = 0.4;

    public String assignVariant(String userId) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest((userId).getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16).substring(0, 8);
            double dicision = Long.parseLong(hashText, 16) * 1.0 / 0xffffffffL;
            if (dicision < g1) {
                return "A";
            } else if (dicision < g1 + g2) {
                return "B";
            } else if (dicision < g1 + g2 + g3) {
                return "C";
            } else {
                return "D";
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
