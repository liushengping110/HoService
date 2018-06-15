package wizrole.hoservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.beam.EditHostry;
import wizrole.hoservice.beam.PerInforOrder;

/**
 * Created by a on 2017/8/28.
 */

public class SearchHosReader {

    /**
     * 查询数据
     * @param context
     * @return
     */
    public static List<EditHostry> searchInfors(Context context) {
        List<EditHostry> infors = new ArrayList<EditHostry>();
        EditHostry orderInfor = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            SearchHosHelper dbHelper = new SearchHosHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from my_edithos";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    /** 历史记录 */
                    String content = cursor.getString(cursor
                            .getColumnIndex("content"));
                    orderInfor = new EditHostry(content);
                    infors.add(orderInfor);
                    orderInfor = null;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return infors;
    }

    /**
     * 添加
     */
    public static void addInfors(EditHostry inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchHosHelper dbHelper = new SearchHosHelper(context);
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("content",inforOrder.getContent());
            db.insert("my_edithos", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }

    /**
     * 删除
     */
    public static void deleteInfors(EditHostry inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchHosHelper dbHelper=new SearchHosHelper(context);
            db=dbHelper.getReadableDatabase();
            //删除条件
            String whereClause = "content=?";
            //删除条件参数
            String[] whereArgs = {String.valueOf(inforOrder.getContent())};
            //执行删除
            db.delete("my_edithos",whereClause,whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }


    /**
     * 编辑（修改）提醒
     */
    public static void editInfors(EditHostry inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchHosHelper dbHelper=new SearchHosHelper(context);
            db=dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("content", inforOrder.getContent());
            String whereClause = "id=?";
            String[] whereArgs = new String[] { String.valueOf(inforOrder.getContent()) };
            db.update("my_edithos", values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }
}
