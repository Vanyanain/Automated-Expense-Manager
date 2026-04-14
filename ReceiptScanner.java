package com.smartwallet.expense.parser;

import android.graphics.Bitmap;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;

public class ReceiptScanner {

    public interface ScanCallback {
        void onSuccess(String merchant, double amount, String date, String category);
        void onFailure(Exception e);
    }

    public static void processImage(Bitmap bitmap, ScanCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    String fullText = visionText.getText();
                    List<String> lines = new ArrayList<>();
                    
                    for (com.google.mlkit.vision.text.Text.TextBlock block : visionText.getTextBlocks()) {
                        for (com.google.mlkit.vision.text.Text.Line line : block.getLines()) {
                            lines.add(line.getText());
                        }
                    }
                    
                    String merchant = ReceiptParser.extractMerchant(lines);
                    double amount = ReceiptParser.extractTotalAmount(fullText);
                    String date = ReceiptParser.extractDate(fullText);
                    String category = CategoryClassifier.categorize(merchant);
                    
                    callback.onSuccess(merchant, amount, date, category);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
