package com.example;

import com.google.common.hash.Hashing;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class HashTest {
    @Test
    public void testHashing() {
        String password = "maslo123";
        String hashed= DigestUtils.sha256Hex(password);
        String expected = "baca21721c6f2cce47ad8070fc9a1d777db736c2ae44d7aedc9eacf7aa2658b6";
        assertEquals(expected, hashed );

        String s = "ling lang guli guli";
        String sha256hex = Hashing.sha256().hashString(s, StandardCharsets.UTF_8).toString();
        System.out.println(sha256hex);
        String sha256hex2 = DigestUtils.sha256Hex(s);
        System.out.println(sha256hex2);
        assertEquals("5fc64be8d1d64e538ff562c441614fd231c428fa92033f5d3bcd826a6f598273", sha256hex);
        assertEquals("5fc64be8d1d64e538ff562c441614fd231c428fa92033f5d3bcd826a6f598273", sha256hex2);
    }
}