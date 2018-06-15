package wizrole.hoservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a on 2017/8/28.
 */

public class SearchGoodsHelper extends SQLiteOpenHelper {

    public static int dbVersion=1;

    public SearchGoodsHelper(Context context) {
        super(context, "my_goods_his", null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table my_goods_his(id integer "+" primary key autoincrement ,content varchar(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
