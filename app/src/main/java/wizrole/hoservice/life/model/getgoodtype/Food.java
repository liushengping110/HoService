package wizrole.hoservice.life.model.getgoodtype;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017/3/1.
 */
public class Food implements Serializable {
    private String sortName;    //店铺名称
    public String image_logo;   //店铺图标
    public int ToaNum;  //月售
    public int peisong ;    //配送费
    public int evaluationNum;   //评价
    public String manJian;  //满减
    public int MoneySong;       //多少起送
    private List<FoodType> foodTypeList;    //店铺内所有菜单集合

    public Food(){
        super();
    }
    public Food(String sortName, int ToaNum, int peisong, int evaluationNum, String manJian, int moneySong,
                List<FoodType> foodTypeList){
        this.sortName=sortName;
        this.ToaNum=ToaNum;
        this.peisong=peisong;
        this.evaluationNum=evaluationNum;
        this.manJian=manJian;
        this.MoneySong=moneySong;
        this.foodTypeList=foodTypeList;
    }

    public int getMoneySong() {
        return MoneySong;
    }

    public void setMoneySong(int moneySong) {
        MoneySong = moneySong;
    }

    public int getToaNum() {
        return ToaNum;
    }

    public void setToaNum(int toaNum) {
        ToaNum = toaNum;
    }

    public String getImage_logo() {
        return image_logo;
    }

    public void setImage_logo(String image_logo) {
        this.image_logo = image_logo;
    }

    public void setEvaluationNum(int evaluationNum) {
        this.evaluationNum = evaluationNum;
    }

    public void setManJian(String manJian) {
        this.manJian = manJian;
    }

    public int getEvaluationNum() {
        return evaluationNum;
    }

    public String getManJian() {
        return manJian;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public List<FoodType> getFoodTypeList() {
        return foodTypeList;
    }

    public void setFoodTypeList(List<FoodType> foodTypeList) {
        this.foodTypeList = foodTypeList;
    }

    public int getPeisong() {
        return peisong;
    }

    public void setPeisong(int peisong) {
        this.peisong = peisong;
    }
}
