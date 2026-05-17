package com.yourname.finance.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yourname.finance.data.IncomeStream;
import java.lang.Class;
import java.lang.Double;
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
public final class IncomeStreamDao_Impl implements IncomeStreamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<IncomeStream> __insertionAdapterOfIncomeStream;

  private final EntityDeletionOrUpdateAdapter<IncomeStream> __deletionAdapterOfIncomeStream;

  private final EntityDeletionOrUpdateAdapter<IncomeStream> __updateAdapterOfIncomeStream;

  public IncomeStreamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfIncomeStream = new EntityInsertionAdapter<IncomeStream>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `income_streams` (`id`,`source`,`type`,`stability`,`avgMonthly`,`paymentCycle`,`isActive`,`notes`,`lastModified`,`synced`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IncomeStream entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getSource() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSource());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getStability() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getStability());
        }
        if (entity.getAvgMonthly() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getAvgMonthly());
        }
        if (entity.getPaymentCycle() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPaymentCycle());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        statement.bindLong(9, entity.getLastModified());
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__deletionAdapterOfIncomeStream = new EntityDeletionOrUpdateAdapter<IncomeStream>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `income_streams` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IncomeStream entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfIncomeStream = new EntityDeletionOrUpdateAdapter<IncomeStream>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `income_streams` SET `id` = ?,`source` = ?,`type` = ?,`stability` = ?,`avgMonthly` = ?,`paymentCycle` = ?,`isActive` = ?,`notes` = ?,`lastModified` = ?,`synced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IncomeStream entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getSource() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSource());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getStability() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getStability());
        }
        if (entity.getAvgMonthly() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getAvgMonthly());
        }
        if (entity.getPaymentCycle() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPaymentCycle());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        statement.bindLong(9, entity.getLastModified());
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final IncomeStream stream, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfIncomeStream.insert(stream);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final IncomeStream stream, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfIncomeStream.handle(stream);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final IncomeStream stream, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfIncomeStream.handle(stream);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<IncomeStream>> getActiveStreams() {
    final String _sql = "SELECT * FROM income_streams WHERE isActive = 1 ORDER BY source";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"income_streams"}, new Callable<List<IncomeStream>>() {
      @Override
      @NonNull
      public List<IncomeStream> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStability = CursorUtil.getColumnIndexOrThrow(_cursor, "stability");
          final int _cursorIndexOfAvgMonthly = CursorUtil.getColumnIndexOrThrow(_cursor, "avgMonthly");
          final int _cursorIndexOfPaymentCycle = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentCycle");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<IncomeStream> _result = new ArrayList<IncomeStream>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IncomeStream _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpStability;
            if (_cursor.isNull(_cursorIndexOfStability)) {
              _tmpStability = null;
            } else {
              _tmpStability = _cursor.getString(_cursorIndexOfStability);
            }
            final Double _tmpAvgMonthly;
            if (_cursor.isNull(_cursorIndexOfAvgMonthly)) {
              _tmpAvgMonthly = null;
            } else {
              _tmpAvgMonthly = _cursor.getDouble(_cursorIndexOfAvgMonthly);
            }
            final String _tmpPaymentCycle;
            if (_cursor.isNull(_cursorIndexOfPaymentCycle)) {
              _tmpPaymentCycle = null;
            } else {
              _tmpPaymentCycle = _cursor.getString(_cursorIndexOfPaymentCycle);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpLastModified;
            _tmpLastModified = _cursor.getLong(_cursorIndexOfLastModified);
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _item = new IncomeStream(_tmpId,_tmpSource,_tmpType,_tmpStability,_tmpAvgMonthly,_tmpPaymentCycle,_tmpIsActive,_tmpNotes,_tmpLastModified,_tmpSynced);
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

  @Override
  public Flow<List<IncomeStream>> getAll() {
    final String _sql = "SELECT * FROM income_streams ORDER BY source";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"income_streams"}, new Callable<List<IncomeStream>>() {
      @Override
      @NonNull
      public List<IncomeStream> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStability = CursorUtil.getColumnIndexOrThrow(_cursor, "stability");
          final int _cursorIndexOfAvgMonthly = CursorUtil.getColumnIndexOrThrow(_cursor, "avgMonthly");
          final int _cursorIndexOfPaymentCycle = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentCycle");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<IncomeStream> _result = new ArrayList<IncomeStream>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IncomeStream _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpStability;
            if (_cursor.isNull(_cursorIndexOfStability)) {
              _tmpStability = null;
            } else {
              _tmpStability = _cursor.getString(_cursorIndexOfStability);
            }
            final Double _tmpAvgMonthly;
            if (_cursor.isNull(_cursorIndexOfAvgMonthly)) {
              _tmpAvgMonthly = null;
            } else {
              _tmpAvgMonthly = _cursor.getDouble(_cursorIndexOfAvgMonthly);
            }
            final String _tmpPaymentCycle;
            if (_cursor.isNull(_cursorIndexOfPaymentCycle)) {
              _tmpPaymentCycle = null;
            } else {
              _tmpPaymentCycle = _cursor.getString(_cursorIndexOfPaymentCycle);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpLastModified;
            _tmpLastModified = _cursor.getLong(_cursorIndexOfLastModified);
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _item = new IncomeStream(_tmpId,_tmpSource,_tmpType,_tmpStability,_tmpAvgMonthly,_tmpPaymentCycle,_tmpIsActive,_tmpNotes,_tmpLastModified,_tmpSynced);
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
