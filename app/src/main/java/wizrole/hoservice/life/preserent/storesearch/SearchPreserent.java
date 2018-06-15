package wizrole.hoservice.life.preserent.storesearch;


import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;
import wizrole.hoservice.life.model.storesearch.SearchBackInterface;
import wizrole.hoservice.life.model.storesearch.SearchHttp;

/**
 * Created by liushengping on 2018/1/4/004.
 * 何人执笔？
 */

public class SearchPreserent implements SearchBackInterface {

    public SearchInterface searchInterface;
    public SearchHttp searchHttp;
    public SearchPreserent(SearchInterface searchInterface){
        this.searchInterface=searchInterface;
        searchHttp=new SearchHttp(this);
    }

    public void searchGetInfor(String msg){
        searchHttp.SearchGetInfor(msg);
    }
    @Override
    public void Succ(Object o) {
        searchInterface.getSearchInforSucc((MyStoreInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        searchInterface.getSearchInforFail(msg);
    }
}
