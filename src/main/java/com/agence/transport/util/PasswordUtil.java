package com.agence.transport.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    public static String hasher(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }
    
    public static boolean verifier(String motDePasse, String hash) {
        return BCrypt.checkpw(motDePasse, hash);
    }
}