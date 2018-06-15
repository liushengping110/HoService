package wizrole.hoservice.life.model.getgoodtype;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017/3/1.
 * 店铺菜单种类信息
 */
public class FoodType implements Serializable {
    public String name;//分类名
    public List<FoodDetail> foodDetails;    //分类名下的详细菜品
    public boolean selected;
    public int selectNum;   //选中的数量
    public int listNo;  //左右两个列表联系
    public FoodType(){
        super();
    }
    public FoodType(String name, List<FoodDetail> foodDetails, int listNo){
        this.foodDetails=foodDetails;
        this.name=name;
        this.listNo=listNo;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FoodDetail> getFoodDetails() {
        return foodDetails;
    }

    public void setFoodDetails(List<FoodDetail> foodDetails) {
        this.foodDetails = foodDetails;
    }
}
