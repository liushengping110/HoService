package wizrole.hoservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.life.model.goodssearch.SearchGoodsHis;


/**
 * Created by a on 2017/8/28.
 * 商品搜索历史记录数据库
 */

public class SearchGoodsReader {

    /**
     * 查询数据
     * @param context
     * @return
     */
    public static List<SearchGoodsHis> searchInfors(Context context) {
        List<SearchGoodsHis> infors = new ArrayList<SearchGoodsHis>();
        SearchGoodsHis orderInfor = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            SearchGoodsHelper dbHelper = new SearchGoodsHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from my_goods_his";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    /** 历史记录 */
                    String content = cursor.getString(cursor
                            .getColumnIndex("content"));
                    orderInfor = new SearchGoodsHis(content);
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
    public static void addInfors(SearchGoodsHis inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchGoodsHelper dbHelper = new SearchGoodsHelper(context);
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("content",inforOrder.getContent());
            db.insert("my_goods_his", null, values);
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
    public static void deleteInfors(SearchGoodsHis inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchGoodsHelper dbHelper=new SearchGoodsHelper(context);
            db=dbHelper.getReadableDatabase();
            //删除条件
            String whereClause = "content=?";
            //删除条件参数
            String[] whereArgs = {String.valueOf(inforOrder.getContent())};
            //执行删除
            db.delete("my_goods_his",whereClause,whereArgs);
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
    public static void editInfors(SearchGoodsHis inforOrder,Context context) {
        SQLiteDatabase db = null;
        try {
            SearchGoodsHelper dbHelper=new SearchGoodsHelper(context);
            db=dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("content", inforOrder.getContent());
            String whereClause = "id=?";
            String[] whereArgs = new String[] { String.valueOf(inforOrder.getContent()) };
            db.update("my_goods_his", values, whereClause, whereArgs);
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
