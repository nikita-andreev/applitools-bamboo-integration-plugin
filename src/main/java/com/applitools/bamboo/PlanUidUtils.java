package com.applitools.bamboo;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanUidUtils {
    final static private Pattern REGEXP = Pattern.compile("^([a-zA-Z0-9\\-]+)-JOB\\d+$");

    public static String planKeyWithoutJob(String value) {
        Matcher matcher = REGEXP.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return value;
        }
    }

    public static String getBatchId(String key, int id) {
        byte[] bytes = (planKeyWithoutJob(key) + String.valueOf(id)).getBytes();
        return UUID.nameUUIDFromBytes(bytes).toString();
    }
}
