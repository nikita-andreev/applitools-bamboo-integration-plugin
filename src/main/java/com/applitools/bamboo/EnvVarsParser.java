package com.applitools.bamboo;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EnvVarsParser {
    private final String rawEnvVars;
    private Map<String, String> pairs;
    private boolean isValid;

    class ParseErrorException extends Exception {
    }


    public EnvVarsParser(String value) {
        this.rawEnvVars = value;
    }

    public boolean isValid() {
        try {
            getMap();
        } catch (ParseErrorException e) {
            return false;
        }
        return true;
    }

    public Map<String, String> asMap() {
        try {
            return getMap();
        } catch (ParseErrorException e) {
            return new HashMap();
        }
    }

    private List<String> getPairs() {
        return Arrays.asList(rawEnvVars.split("\\s+"));
    }

    private Map<String, String> parse() throws ParseErrorException {
        List<String> pairs = getPairs();
        Map<String, String> resultMap = new HashMap<String, String>();
        for (ListIterator<String> pairsIterator = pairs.listIterator(); pairsIterator.hasNext(); ) {
            String aPair = pairsIterator.next();
            String[] split;
            String key, value;
            split = aPair.split("\\s*=\\s*");
            if (split.length != 2) {
                throw new ParseErrorException();
            }
            key = split[0];
            value = split[1];
            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                throw new ParseErrorException();
            }
            resultMap.put(key, value);
        }
        return resultMap;
    }

    private Map<String, String> getMap() throws ParseErrorException {
        if (null == pairs) {
            this.pairs = parse();
        }
        return pairs;
    }
}
