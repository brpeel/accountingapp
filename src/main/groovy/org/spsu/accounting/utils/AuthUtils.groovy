package org.spsu.accounting.utils

import com.google.common.base.Charsets
import com.google.common.hash.Hashing

/**
 * Created by brettpeel on 2/22/15.
 */
class AuthUtils {

    public static String getHash(String text){
        return Hashing.sha256().hashString(text, Charsets.UTF_8).toString();
    }

    public static String generateRandomHash(String seed){
        return getHash(seed+System.currentTimeMillis())
    }
}
