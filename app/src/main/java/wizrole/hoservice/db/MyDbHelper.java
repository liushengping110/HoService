package wizrole.hoservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

	public static int dbVersion=1;

	public MyDbHelper(Context context) {
		super(context, "perOrderInfor", null, dbVersion);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table perOrderInfor(id integer "+" primary key autoincrement ,name varchar(20) ,sex varchar(10),tel varchar(20),address varchar(50),isSelect integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
