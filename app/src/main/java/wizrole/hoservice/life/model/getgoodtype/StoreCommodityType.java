package wizrole.hoservice.life.model.getgoodtype;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class StoreCommodityType implements Serializable {
    /**
     * TypeId		- 类别ID
     TypeName	- 类别名称
     CommodityList - 商品列表
     */
    public String TypeId;
    public String TypeName;
    public List<CommodityList> CommodityList;
    /*自定义添加*/
    public boolean selected;
    public int selectNum;   //选中的数量
    public int listNo;  //左右两个列表联系

    public int getListNo() {
        return listNo;
    }

    public void setListNo(int listNo) {
        this.listNo = listNo;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTypeId() {
        return TypeId;
    }

    public String getTypeName() {
        return TypeName;
    }

    public List<CommodityList> getCommodityList() {
        return CommodityList;
    }
}
