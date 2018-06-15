package wizrole.hoservice.beam;

import java.util.List;

import wizrole.hoservice.life.model.getgoodtype.FoodDetail;

/**
 * Created by a on 2017/5/2.
 * 我的订单
 */

public class MyOrder {

    public String storeName;
    public String orderStatus;
    public String time;
    public String order_example;
    public List<FoodDetail> list;
    public double allPrice;

    public String getOrder_example() {
        return order_example;
    }

    public void setOrder_example(String order_example) {
        this.order_example = order_example;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setList(List<FoodDetail> list) {
        this.list = list;
    }

    public void setAllPrice(double allPrice) {
        this.allPrice = allPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getTime() {
        return time;
    }

    public List<FoodDetail> getList() {
        return list;
    }

    public double getAllPrice() {
        return allPrice;
    }
}
