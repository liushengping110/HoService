package wizrole.hoservice.interface_base;

/**
 * Created by ${liushengping} on 2017/9/29.
 * 何人执笔？
 * 文件下载
 */

public interface DownLoadListener {

    void onSchedule(int progress);

    void onSuccess();

    void onError();

    void onExists();//文件已存在
}
