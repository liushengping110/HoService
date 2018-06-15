package wizrole.hoservice.life.model.getgoodtype;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by a on 2017/3/27.
 * 菜单细节
 */

public class FoodDetail implements Serializable {

    private String name;    //菜名
    private Bitmap ImagePath;   //图片
    public double price;    //价格
    public int monSaleNum;  //月销
    public String description;  //详细介绍
    public int yiOrder; //已点数量
    public int listNo;  //两个列表建立联系
    public FoodDetail(){
        super();
    }
    public FoodDetail(String name, double price, int monSaleNum, String description, int listNo) {
        this.name = name;
        this.monSaleNum=monSaleNum;
        this.price=price;
        this.description=description;
        this.listNo=listNo;
    }

    public int getListNo() {
        return listNo;
    }

    public void setListNo(int listNo) {
        this.listNo = listNo;
    }

    public int getYiOrder() {
        return yiOrder;
    }

    public void setYiOrder(int yiOrder) {
        this.yiOrder = yiOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMonSaleNum() {
        return monSaleNum;
    }

    public void setMonSaleNum(int monSaleNum) {
        this.monSaleNum = monSaleNum;
    }

    public Bitmap getImagePath() {
        return ImagePath;
    }

    public void setImagePath(Bitmap imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
