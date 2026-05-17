package com.yourname.finance.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yourname.finance.data.BufferLedger;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BufferLedgerDao_Impl implements BufferLedgerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BufferLedger> __insertionAdapterOfBufferLedger;

  private final EntityDeletionOrUpdateAdapter<BufferLedger> __updateAdapterOfBufferLedger;

  public BufferLedgerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBufferLedger = new EntityInsertionAdapter<BufferLedger>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `buffer_ledger` (`month`,`startingBuffer`,`incomeSurplus`,`endingBuffer`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BufferLedger entity) {
        if (entity.getMonth() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getMonth());
        }
        statement.bindDouble(2, entity.getStartingBuffer());
        statement.bindDouble(3, entity.getIncomeSurplus());
        statement.bindDouble(4, entity.getEndingBuffer());
      }
    };
    this.__updateAdapterOfBufferLedger = new EntityDeletionOrUpdateAdapter<BufferLedger>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `buffer_ledger` SET `month` = ?,`startingBuffer` = ?,`incomeSurplus` = ?,`endingBuffer` = ? WHERE `month` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BufferLedger entity) {
        if (entity.getMonth() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getMonth());
        }
        statement.bindDouble(2, entity.getStartingBuffer());
        statement.bindDouble(3, entity.getIncomeSurplus());
        statement.bindDouble(4, entity.getEndingBuffer());
        if (entity.getMonth() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMonth());
        }
      }
    };
  }

  @Override
  public Object insert(final BufferLedger ledger, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBufferLedger.insert(ledger);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BufferLedger ledger, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBufferLedger.handle(ledger);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByMonth(final String month,
      final Continuation<? super BufferLedger> $completion) {
    final String _sql = "SELECT * FROM buffer_ledger WHERE month = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (month == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, month);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BufferLedger>() {
      @Override
      @Nullable
      public BufferLedger call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfStartingBuffer = CursorUtil.getColumnIndexOrThrow(_cursor, "startingBuffer");
          final int _cursorIndexOfIncomeSurplus = CursorUtil.getColumnIndexOrThrow(_cursor, "incomeSurplus");
          final int _cursorIndexOfEndingBuffer = CursorUtil.getColumnIndexOrThrow(_cursor, "endingBuffer");
          final BufferLedger _result;
          if (_cursor.moveToFirst()) {
            final String _tmpMonth;
            if (_cursor.isNull(_cursorIndexOfMonth)) {
              _tmpMonth = null;
            } else {
              _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
            }
            final double _tmpStartingBuffer;
            _tmpStartingBuffer = _cursor.getDouble(_cursorIndexOfStartingBuffer);
            final double _tmpIncomeSurplus;
            _tmpIncomeSurplus = _cursor.getDouble(_cursorIndexOfIncomeSurplus);
            final double _tmpEndingBuffer;
            _tmpEndingBuffer = _cursor.getDouble(_cursorIndexOfEndingBuffer);
            _result = new BufferLedger(_tmpMonth,_tmpStartingBuffer,_tmpIncomeSurplus,_tmpEndingBuffer);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BufferLedger>> getAll() {
    final String _sql = "SELECT * FROM buffer_ledger ORDER BY month DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"buffer_ledger"}, new Callable<List<BufferLedger>>() {
      @Override
      @NonNull
      public List<BufferLedger> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfStartingBuffer = CursorUtil.getColumnIndexOrThrow(_cursor, "startingBuffer");
          final int _cursorIndexOfIncomeSurplus = CursorUtil.getColumnIndexOrThrow(_cursor, "incomeSurplus");
          final int _cursorIndexOfEndingBuffer = CursorUtil.getColumnIndexOrThrow(_cursor, "endingBuffer");
          final List<BufferLedger> _result = new ArrayList<BufferLedger>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BufferLedger _item;
            final String _tmpMonth;
            if (_cursor.isNull(_cursorIndexOfMonth)) {
              _tmpMonth = null;
            } else {
              _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
            }
            final double _tmpStartingBuffer;
            _tmpStartingBuffer = _cursor.getDouble(_cursorIndexOfStartingBuffer);
            final double _tmpIncomeSurplus;
            _tmpIncomeSurplus = _cursor.getDouble(_cursorIndexOfIncomeSurplus);
            final double _tmpEndingBuffer;
            _tmpEndingBuffer = _cursor.getDouble(_cursorIndexOfEndingBuffer);
            _item = new BufferLedger(_tmpMonth,_tmpStartingBuffer,_tmpIncomeSurplus,_tmpEndingBuffer);
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
