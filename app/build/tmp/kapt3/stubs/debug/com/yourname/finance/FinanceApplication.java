package com.yourname.finance;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/yourname/finance/FinanceApplication;", "Landroid/app/Application;", "()V", "db", "Lcom/yourname/finance/data/FinanceDatabase;", "getDb", "()Lcom/yourname/finance/data/FinanceDatabase;", "setDb", "(Lcom/yourname/finance/data/FinanceDatabase;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "onCreate", "", "seedDataIfNeeded", "Lkotlinx/coroutines/Job;", "app_debug"})
public final class FinanceApplication extends android.app.Application {
    public com.yourname.finance.data.FinanceDatabase db;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    
    public FinanceApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yourname.finance.data.FinanceDatabase getDb() {
        return null;
    }
    
    public final void setDb(@org.jetbrains.annotations.NotNull()
    com.yourname.finance.data.FinanceDatabase p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final kotlinx.coroutines.Job seedDataIfNeeded() {
        return null;
    }
}