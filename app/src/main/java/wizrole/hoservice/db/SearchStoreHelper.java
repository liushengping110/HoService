package wizrole.hoservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a on 2017/8/28.
 */

public class SearchStoreHelper extends SQLiteOpenHelper {

    public static int dbVersion=1;

    public SearchStoreHelper(Context context) {
        super(context, "my_editdis", null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table my_editdis(id integer "+" primary key autoincrement ,content varchar(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
