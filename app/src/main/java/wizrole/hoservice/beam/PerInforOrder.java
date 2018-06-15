package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/4/6.
 * 订单人详细信息
 */

public class PerInforOrder implements Serializable {

    public String name; //名字
    public String sex;
    public String tel;
    public String address;
    public int id;
    public int isSelect;    //0--未选中   1--选中

    public PerInforOrder(int id, String name, String sex, String tel, String address, int isSelect){
        this.name=name;
        this.id=id;
        this.sex=sex;
        this.tel=tel;
        this.address=address;
        this.isSelect=isSelect;
    }
    public PerInforOrder(String name, String sex, String tel, String address, int isSelect){
        this.name=name;
        this.sex=sex;
        this.tel=tel;
        this.address=address;
        this.isSelect=isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isSelect() {
        return isSelect;
    }

    public void setSelect(int select) {
        isSelect = select;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }
}
