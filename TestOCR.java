package com.smartwallet.expense;

import com.smartwallet.expense.parser.ReceiptParser;
import com.smartwallet.expense.parser.CategoryClassifier;
import java.util.Arrays;

public class TestOCR {
    public static void main(String[] args) {
        String ocrText = "STARBUCKS STORE #1234\n" +
                         "12/15/2023 09:41 AM\n" +
                         "1 Venti Latte $5.45\n" +
                         "Total $5.45\n" +
                         "Visa XXXXXX";
        
        System.out.println("Merchant: " + ReceiptParser.extractMerchant(Arrays.asList(ocrText.split("\n"))));
        System.out.println("Date: " + ReceiptParser.extractDate(ocrText));
        System.out.println("Amount: " + ReceiptParser.extractTotalAmount(ocrText));
        System.out.println("Category: " + CategoryClassifier.categorize(ReceiptParser.extractMerchant(Arrays.asList(ocrText.split("\n")))));
    }
}
