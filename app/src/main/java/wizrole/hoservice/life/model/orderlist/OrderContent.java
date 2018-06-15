package wizrole.hoservice.life.model.orderlist;

/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class OrderContent {
    public String OrderGoodsLogo;//图片
    public String OrderGoodsName	;//	订单商品名
    public double OrderGoodPrice	;//	订单商品价格
    public int OrderGoodsNumber;//	订单商品数量

    public void setOrderGoodsLogo(String orderGoodsLogo) {
        OrderGoodsLogo = orderGoodsLogo;
    }

    public void setOrderGoodsName(String orderGoodsName) {
        OrderGoodsName = orderGoodsName;
    }

    public double getOrderGoodPrice() {
        return OrderGoodPrice;
    }

    public void setOrderGoodPrice(double orderGoodPrice) {
        OrderGoodPrice = orderGoodPrice;
    }

    public int getOrderGoodsNumber() {
        return OrderGoodsNumber;
    }

    public void setOrderGoodsNumber(int orderGoodsNumber) {
        OrderGoodsNumber = orderGoodsNumber;
    }

    public String getOrderGoodsLogo() {
        return OrderGoodsLogo;
    }

    public String getOrderGoodsName() {
        return OrderGoodsName;
    }

}
