package com.PWS.EmployeeService.jwtconfig;

import java.util.HashSet;
import java.util.Set;

public class InvalidTokenStore {

    private static Set<String> invalidatedTokens = new HashSet<>();

    public static boolean isTokenInvalid(String jwtToken) {
        return invalidatedTokens.contains(jwtToken);
    }

    public static void invalidateToken(String jwtToken) {
        invalidatedTokens.add(jwtToken);
    }

}