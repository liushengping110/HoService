package wizrole.hoservice.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017/9/6.
 * 删除本机 照片和截图等图片文件
 */

public class DelImage {

    public Context context;
    public  void startDel(Context context){
        this.context=context;
        new Del().start();
    }


    public static   List<File> fileList = new ArrayList<File>();
    public static String[] img = new String[]{".jpg", ".png", ".gif", ".bmp"};
    /**
     * 遍历sdcard 找到某找类型的file放到list中。
     * 比较耗时 建议放在线程中做
     * @param file
     */
    public  static  void checkFile(File file) {// 遍历文件，在这里是遍历sdcard
        if (file.isDirectory()) {// 判断是否是文件夹
            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
            if (files != null) {// 如果文件夹不为空
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    checkFile(f);// 递归调用
                }
            }
        } else if (file.isFile()) {// 判断是否是文件
            int dot = file.getName().lastIndexOf(".");
            if (dot > -1 && dot < file.getName().length()) {
                String extriName = file.getName().substring(dot,
                        file.getName().length());// 得到文件的扩展名
                if (extriName.equals(img[0]) || extriName.equals(img[1])
                        || extriName.equals(img[2]) || extriName.equals(img[3])) {// 判断是否是图片文件 www.it165.net
                    fileList.add(file);
                }
            }
        }
    }

    public class  Del extends Thread{
        @Override
        public void run() {
            super.run();
            File file=new File("/sdcard/");
            checkFile(file);
            for (int i=0;i<fileList.size();i++){
                String path=fileList.get(i).getPath();
                if (!TextUtils.isEmpty(path)) {
                    File file_path = new File(path);
                    if (file_path.exists()){
                        file_path.delete();
                        refreshImg(file_path);
                    }
                }
            }
        }
    }

    private void DeleteImage(String imgPath) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=?",
                new String[] { imgPath }, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }

        if(result){//删除完成  通知更新图库

        }
    }

    public void refreshImg(File  file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }
}
