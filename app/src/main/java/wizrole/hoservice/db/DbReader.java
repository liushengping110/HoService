package wizrole.hoservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.beam.PerInforOrder;


/***
 * 订餐收获地址信息数据库
 */
public class DbReader {
	/***获取文件名称*/
	public static String getFileName(String path) {
		return path.substring(path.indexOf("/") + 1);
	}

	/**
	 * 查询数据
	 * @param context
	 * @return
     */
	public static List<PerInforOrder> searchInfors(Context context) {
		List<PerInforOrder> infors = new ArrayList<PerInforOrder>();
		PerInforOrder orderInfor = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			MyDbHelper dbHelper = new MyDbHelper(context);
			db = dbHelper.getReadableDatabase();
			String sql = "select * from perOrderInfor";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				do {
					/** 姓名 */
					String name = cursor.getString(cursor
							.getColumnIndex("name"));
					/**性别*/
					String sex = cursor.getString(cursor
							.getColumnIndex("sex"));
					/**手机号码*/
					String tel=cursor.getString(cursor
							.getColumnIndex("tel"));
					/**地址*/
					String address=cursor.getString(cursor
							.getColumnIndex("address"));
					//id
					int id=cursor.getInt(cursor
							.getColumnIndex("id"));
					//isSelect
					int isSelect=cursor.getInt(cursor
							.getColumnIndex("isSelect"));
					orderInfor = new PerInforOrder(id,name,sex,tel,address,isSelect);
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
	public static void addInfors(PerInforOrder inforOrder,Context context) {
		SQLiteDatabase db = null;
		try {
			MyDbHelper dbHelper = new MyDbHelper(context);
			db = dbHelper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("name",inforOrder.getName());
			values.put("sex",inforOrder.getSex());
			values.put("tel",inforOrder.getTel());
			values.put("address",inforOrder.getAddress());
			values.put("isSelect",inforOrder.isSelect());
			db.insert("perOrderInfor", null, values);
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
	public static void deleteInfors(PerInforOrder inforOrder,Context context) {
		SQLiteDatabase db = null;
		try {
			MyDbHelper dbHelper=new MyDbHelper(context);
			db=dbHelper.getReadableDatabase();
			//删除条件
			String whereClause = "id=?";
			//删除条件参数
			String[] whereArgs = {String.valueOf(inforOrder.getId())};
			//执行删除
			db.delete("perOrderInfor",whereClause,whereArgs);
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
	public static void editInfors(PerInforOrder inforOrder,Context context) {
		SQLiteDatabase db = null;
		try {
			MyDbHelper dbHelper=new MyDbHelper(context);
			db=dbHelper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("id", inforOrder.getId());
			values.put("name", inforOrder.getName());
			values.put("sex", inforOrder.getSex());
			values.put("tel", inforOrder.getTel());
			values.put("address", inforOrder.getAddress());
			values.put("isSelect", inforOrder.isSelect());
			String whereClause = "id=?";
			String[] whereArgs = new String[] { String.valueOf(inforOrder.getId()) };
			db.update("perOrderInfor", values, whereClause, whereArgs);
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

