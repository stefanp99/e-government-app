package com.stefan.egovernmentapp.utils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TwoFactorAuthUtil {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public static GoogleAuthenticatorKey generateSecretKey() {
        return gAuth.createCredentials();
    }

    public static boolean verifyCode(String secretKey, String code) {
        return gAuth.authorize(secretKey, Integer.parseInt(code));
    }
}