package wizrole.hoservice.life.model.getgoodtype;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class CommodityList implements Serializable {
    /**
     * commodityNo 	- 商品主键ID
     CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     */
    public int yiOrder=0; //已点数量
    public int listNo;  //两个列表建立联系
    public String commodityNo;
    public String CommodityName;
    public String CommodityPic;
    public String CommodityContent;
    public String CommodityAmt;

    public void setYiOrder(int yiOrder) {
        this.yiOrder = yiOrder;
    }

    public void setListNo(int listNo) {
        this.listNo = listNo;
    }

    public int getYiOrder() {
        return yiOrder;
    }

    public int getListNo() {
        return listNo;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public String getCommodityName() {
        return CommodityName;
    }

    public String getCommodityPic() {
        return CommodityPic;
    }

    public String getCommodityContent() {
        return CommodityContent;
    }

    public String getCommodityAmt() {
        return CommodityAmt;
    }
}
