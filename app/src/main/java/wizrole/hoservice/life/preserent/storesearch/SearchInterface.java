package wizrole.hoservice.life.preserent.storesearch;


import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;

/**
 * Created by liushengping on 2018/1/4/004.
 * 何人执笔？
 */

public interface SearchInterface {
    void getSearchInforSucc(MyStoreInforBack myStoreInforBack);
    void getSearchInforFail(String msg);
}
