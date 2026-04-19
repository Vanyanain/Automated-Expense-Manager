# 👛 Smart Wallet: Automated Expense Manager

![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=for-the-badge&logo=android) 
![Java](https://img.shields.io/badge/Language-Java-orange.svg?style=for-the-badge&logo=openjdk) 
![MLKit](https://img.shields.io/badge/ML%20Kit-Text%20Recognition-blue.svg?style=for-the-badge&logo=google)
![SQLite](https://img.shields.io/badge/Storage-Room%20/%20SQLite-lightblue.svg?style=for-the-badge&logo=sqlite)

**Smart Wallet** is an intelligent Android application built in Java that simplifies personal finance tracking. By leveraging **Google ML Kit OCR**, the app allows users to scan physical receipts, automatically extract transaction details, and visualize spending habits without manual data entry.

---

## ✨ Key Features

- **📸 Instant Receipt Scanning:** Capture receipt images using **CameraX** integration or select from the gallery.
- **🔍 AI Data Extraction:** Automatically parses the following using Google ML Kit:
  - **Merchant Name:** Heuristically identified from the receipt header.
  - **Total Amount:** Smart detection of "Total" keywords and currency patterns.
  - **Transaction Date:** Regex-based matching for multiple date formats.
- **📂 Smart Categorization:** Rule-based logic that automatically sorts expenses into categories (Food, Travel, Groceries, etc.).
- **📊 Spending Visualizations:** Interactive Pie and Bar charts powered by **MPAndroidChart** for instant financial insights.
- **💾 Local-First Privacy:** Built with **Room (SQLite)** for fast, offline-capable storage. (Note: This version avoids Firebase for enhanced user privacy and zero-config setup).

---

## 🛠 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 8+ |
| **OCR Engine** | Google ML Kit (Text Recognition) |
| **Database** | Room Persistence Library (SQLite) |
| **Camera** | Android CameraX API |
| **Visuals** | MPAndroidChart |
| **Architecture** | MVVM Pattern with LiveData |

---

## 🏗 Implementation Details

### 1. Data Layer (Room)
The app utilizes a local-first approach using the **Room** database to ensure speed and privacy.
- **Entity:** `Expense.java` (stores id, merchantName, amount, date, and category).
- **DAO:** `ExpenseDao.java` handles CRUD operations and aggregate queries for the dashboard charts.

### 2. Processing Logic (ML Kit)
- **ReceiptScanner:** High-level wrapper for the ML Kit `TextRecognizer`.
- **ReceiptParser:** Uses specialized Regex to extract data:
  - **Date:** Scans for patterns like `MM/DD/YYYY`, `DD-MM-YYYY`, or `Month DD, YYYY`.
  - **Total:** Searches for keywords (Total, Amount, Paid) followed by numeric values.
  - **Merchant:** Analyzes the top-most text blocks to identify the store name.
- **CategoryClassifier:** Assigns categories based on keyword mapping (e.g., "Shell" -> "Travel", "Walmart" -> "Groceries").

---

## 📁 Project Structure

```text
app/src/main/java/com/smartwallet/
├── data/
│   ├── AppDatabase.java       # Room database setup
│   ├── Expense.java           # Data Entity
│   └── ExpenseDao.java        # Database access methods
├── ml/
│   ├── ReceiptScanner.java    # ML Kit Text Recognition bridge
│   ├── ReceiptParser.java     # Logic for Date/Amount/Merchant extraction
│   └── CategoryClassifier.java # Logic for auto-categorization
├── ui/
│   ├── MainActivity.java      # Dashboard with Charts & List
│   ├── CameraActivity.java    # CameraX capture interface
│   └── ExpenseDetailActivity.java # Data review & manual correction screen
└── utils/
    └── DateUtils.java         # Formatting helpers
