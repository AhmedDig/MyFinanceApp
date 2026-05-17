package com.yourname.finance.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\u000b"}, d2 = {"Lcom/yourname/finance/data/FinanceDatabase;", "Landroidx/room/RoomDatabase;", "()V", "bufferLedgerDao", "Lcom/yourname/finance/data/dao/BufferLedgerDao;", "categoryDao", "Lcom/yourname/finance/data/dao/CategoryDao;", "incomeStreamDao", "Lcom/yourname/finance/data/dao/IncomeStreamDao;", "transactionDao", "Lcom/yourname/finance/data/dao/TransactionDao;", "app_debug"})
@androidx.room.Database(entities = {com.yourname.finance.data.Transaction.class, com.yourname.finance.data.Category.class, com.yourname.finance.data.IncomeStream.class, com.yourname.finance.data.BufferLedger.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.yourname.finance.data.Converters.class})
public abstract class FinanceDatabase extends androidx.room.RoomDatabase {
    
    public FinanceDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.yourname.finance.data.dao.TransactionDao transactionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.yourname.finance.data.dao.CategoryDao categoryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.yourname.finance.data.dao.IncomeStreamDao incomeStreamDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.yourname.finance.data.dao.BufferLedgerDao bufferLedgerDao();
}