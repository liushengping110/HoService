package wizrole.hoservice.life.preserent.getallstore;


import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public interface GetAllStoreInterface {
    void getAllStoreSucc(MyStoreInforBack getAllStoreBack);
    void getAllStoreFail(String msg);
}
