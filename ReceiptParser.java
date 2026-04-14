package com.smartwallet.expense.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class ReceiptParser {

    public static String extractMerchant(List<String> lines) {
        if (lines != null && !lines.isEmpty()) {
            return lines.get(0); // Heuristic: first line is often the merchant
        }
        return "Unknown Merchant";
    }

    public static double extractTotalAmount(String fullText) {
        // Look for typical total formats, like Total: $12.34 or just 12.34
        Pattern p = Pattern.compile("(?i)(?:total|amount|due|balance).*?\\$?(\\d+\\.\\d{2})");
        Matcher m = p.matcher(fullText);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (Exception e) {}
        }
        
        // Fallback: finding the largest currency-like number
        Pattern p2 = Pattern.compile("\\d+\\.\\d{2}");
        Matcher m2 = p2.matcher(fullText);
        double maxAmount = 0.0;
        while (m2.find()) {
            try {
                double val = Double.parseDouble(m2.group());
                if (val > maxAmount) {
                    maxAmount = val;
                }
            } catch (Exception e) {}
        }
        return maxAmount;
    }

    public static String extractDate(String fullText) {
        // Basic MM/DD/YYYY or DD/MM/YYYY
        Pattern p = Pattern.compile("\\b\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}\\b");
        Matcher m = p.matcher(fullText);
        if (m.find()) {
            return m.group();
        }
        return "";
    }
}
