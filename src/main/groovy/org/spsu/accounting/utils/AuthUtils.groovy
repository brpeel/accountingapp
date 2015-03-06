package org.spsu.accounting.utils

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.apache.commons.lang3.RandomStringUtils

/**
 * Created by brettpeel on 2/22/15.
 */
class AuthUtils {

    public static String getHash(final String text){
        return Hashing.sha256().hashString(text, Charsets.UTF_8).toString();
    }

    public static String generateString(){
        return generateString(9)
    }

    public static String generateString(int length){
        String charset = (('A'..'Z') + ('a'..'z') + ('0'..'9')).join()
        return generateString(length, charset)
    }

    public static String generateString(int length, String charset){
        return RandomStringUtils.random(length, charset.toCharArray())
    }
}
