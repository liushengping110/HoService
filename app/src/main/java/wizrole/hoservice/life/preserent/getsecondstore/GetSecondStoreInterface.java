package wizrole.hoservice.life.preserent.getsecondstore;


import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;

/**
 * Created by liushengping on 2018/1/31.
 * 何人执笔？
 */

public interface GetSecondStoreInterface {

    void GetSecondStoreSucc(MyStoreInforBack myStoreInforBack);
    void GetSecondStoreFail(String msg);
}
