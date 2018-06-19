# WorkCollection
My work collection From Android

# 2018-3-26~（RuiXiuDe Company）
- A car - based company
- position: Developer of Android 

## 1.Solve the problem about greendao upgrade
Use about:
```java
public class GreenDaoOpenHelper extends DaoMaster.OpenHelper {

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    public GreenDaoOpenHelper(String path, String name) {
            super(new GreenDaoContext(path), name);
    }
    //the newVersion is 2,oldVersion is 1
    //CarBrushApplyInput Entity add one Boolean field
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
       if (oldVersion==1){
            MigrationHelper.getInstance().migrate(db, CarBrushApplyInputCacheDao.class);
        }
    }
}
```
