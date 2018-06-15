package wizrole.hoservice.life.preserent.getgoodtype;


import wizrole.hoservice.life.model.getgoodtype.GoodsTypeBack;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public interface GoodsTypeInterface {

    void getGoodsTypeSucc(GoodsTypeBack goodsTypeBack);
    void getGoodsTypeFail(String msg);
}
