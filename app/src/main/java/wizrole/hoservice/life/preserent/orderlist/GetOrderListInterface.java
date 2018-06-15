package wizrole.hoservice.life.preserent.orderlist;


import wizrole.hoservice.life.model.orderlist.OrderListBack;

/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public interface GetOrderListInterface {

    void getOrderListSucc(OrderListBack orderListBack);
    void getOrderListFail(String msg);
}
