package wizrole.hoservice.life.preserent.getsecondstore;


import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;
import wizrole.hoservice.life.model.getsecondstore.GetSecondStoreBackInterface;
import wizrole.hoservice.life.model.getsecondstore.GetSecondStoreHttp;

/**
 * Created by liushengping on 2018/1/31.
 * 何人执笔？
 */

public class GetSecondStorePreserent implements GetSecondStoreBackInterface {

    public GetSecondStoreInterface getSecondStoreInterface;
    public GetSecondStoreHttp getSecondStoreHttp;
    public GetSecondStorePreserent(GetSecondStoreInterface getSecondStoreInterface){
        this.getSecondStoreInterface=getSecondStoreInterface;
        getSecondStoreHttp=new GetSecondStoreHttp(this);
    }

    public void getSecondStore(String id,int page){
        getSecondStoreHttp.getAllStore(id,page);
    }

    @Override
    public void Succ(Object o) {
        getSecondStoreInterface.GetSecondStoreSucc((MyStoreInforBack)o);
    }

    @Override
    public void Fail(String msg) {
        getSecondStoreInterface.GetSecondStoreFail(msg);
    }
}
