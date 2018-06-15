package wizrole.hoservice.life.preserent.orderlist;


import wizrole.hoservice.life.model.orderlist.GetOrderListBackInterface;
import wizrole.hoservice.life.model.orderlist.GetOrderListHttp;

/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class GetOrderListPreserent implements GetOrderListBackInterface {

    public GetOrderListBackInterface getOrderListBackInterface;
    public GetOrderListHttp getOrderListHttp;
    public GetOrderListPreserent(GetOrderListBackInterface getOrderListBackInterface){
        this.getOrderListBackInterface=getOrderListBackInterface;
        getOrderListHttp=new GetOrderListHttp(this);
    }

    public void getOrderListHttp(String id){
        getOrderListHttp.getOrderList(id);
    }
    @Override
    public void Succ(Object o) {
        getOrderListBackInterface.Succ(o);
    }

    @Override
    public void Fail(String msg) {
        getOrderListBackInterface.Fail(msg);
    }
}
