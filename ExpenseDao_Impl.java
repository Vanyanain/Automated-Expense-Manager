package com.smartwallet.expense.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Expense> __insertionAdapterOfExpense;

  private final EntityDeletionOrUpdateAdapter<Expense> __deletionAdapterOfExpense;

  private final EntityDeletionOrUpdateAdapter<Expense> __updateAdapterOfExpense;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExpense = new EntityInsertionAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `expenses` (`id`,`merchantName`,`amount`,`dateInMillis`,`category`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        if (entity.merchantName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.merchantName);
        }
        statement.bindDouble(3, entity.amount);
        statement.bindLong(4, entity.dateInMillis);
        if (entity.category == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.category);
        }
      }
    };
    this.__deletionAdapterOfExpense = new EntityDeletionOrUpdateAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfExpense = new EntityDeletionOrUpdateAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `expenses` SET `id` = ?,`merchantName` = ?,`amount` = ?,`dateInMillis` = ?,`category` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        if (entity.merchantName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.merchantName);
        }
        statement.bindDouble(3, entity.amount);
        statement.bindLong(4, entity.dateInMillis);
        if (entity.category == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.category);
        }
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public long insert(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfExpense.insertAndReturnId(expense);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfExpense.handle(expense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfExpense.handle(expense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Expense>> getAllExpenses() {
    final String _sql = "SELECT * FROM expenses ORDER BY dateInMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, new Callable<List<Expense>>() {
      @Override
      @Nullable
      public List<Expense> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMerchantName = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantName");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDateInMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dateInMillis");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<Expense> _result = new ArrayList<Expense>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Expense _item;
            final String _tmpMerchantName;
            if (_cursor.isNull(_cursorIndexOfMerchantName)) {
              _tmpMerchantName = null;
            } else {
              _tmpMerchantName = _cursor.getString(_cursorIndexOfMerchantName);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpDateInMillis;
            _tmpDateInMillis = _cursor.getLong(_cursorIndexOfDateInMillis);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            _item = new Expense(_tmpMerchantName,_tmpAmount,_tmpDateInMillis,_tmpCategory);
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
