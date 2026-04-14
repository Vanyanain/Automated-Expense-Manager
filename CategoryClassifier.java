package com.smartwallet.expense.parser;

import java.util.HashMap;
import java.util.Map;

public class CategoryClassifier {
    private static final Map<String, String> RULES = new HashMap<>();
    
    static {
        RULES.put("uber", "Travel");
        RULES.put("lyft", "Travel");
        RULES.put("delta", "Travel");
        RULES.put("american airlines", "Travel");
        
        RULES.put("starbucks", "Food");
        RULES.put("mcdonalds", "Food");
        RULES.put("burger king", "Food");
        RULES.put("dunkin", "Food");
        
        RULES.put("walmart", "Shopping");
        RULES.put("target", "Shopping");
        RULES.put("amazon", "Shopping");
        
        RULES.put("shell", "Gas");
        RULES.put("chevron", "Gas");
        RULES.put("exxon", "Gas");
    }

    public static String categorize(String merchantName) {
        if (merchantName == null) return "Other";
        String lower = merchantName.toLowerCase();
        
        for (Map.Entry<String, String> entry : RULES.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Other";
    }
}
