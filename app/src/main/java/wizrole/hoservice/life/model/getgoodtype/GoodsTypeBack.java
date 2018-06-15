package wizrole.hoservice.life.model.getgoodtype;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class GoodsTypeBack implements Serializable{

    /**
     * ResultCode
     ResultContent
     StoreCommodityType 	- 店铺商品类别(列表)
     TypeId		- 类别ID
     TypeName	- 类别名称
     CommodityList - 商品列表
     commodityNo 	- 商品主键ID
     CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     */
    public String ResultCode;
    public String ResultContent;
    public List<StoreCommodityType> StoreCommodityType;

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<StoreCommodityType> getStoreCommodityType() {
        return StoreCommodityType;
    }
}
