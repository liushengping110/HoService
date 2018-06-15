package wizrole.hoservice.life.model.getallstore.bean;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class StoreList implements Serializable{

    /**
     * StoreNo   -店铺id
     StoreName		- 店铺名称
     StorePlace		- 店铺位置
     StorePhone		- 店铺电话
     StoreType		- 店铺类别
     StoreLogoPic	- 店铺logo图片
     StorePayPic		- 店铺收款码
     CompanyName  - 营业执照单位名称
     LegalPerson	 - 法人
     CompanyAddress  - 营业执照单位地址
     LicenseValidity - 有效期
     LicenseNo		- 执照证件编号
     CreditCode		- 社会信用代码
     WeChatPic		- 微信收款码
     */

    public String StoreNo;// 	 - 店铺主键id;
    public String StoreName;
    public String StorePlace;
    public String StorePhone;
    public String StoreType;
    public String StoreLogoPic;
    public String StorePayPic;
    public String CreditCode;
    public String LicenseNo;
    public String LicenseValidity;
    public String CompanyAddress;
    public String LegalPerson;
    public String CompanyName;
    public String WeChatPic;

    public String getStoreNo() {
        return StoreNo;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getStorePlace() {
        return StorePlace;
    }

    public String getStorePhone() {
        return StorePhone;
    }

    public String getStoreType() {
        return StoreType;
    }

    public String getStoreLogoPic() {
        return StoreLogoPic;
    }

    public String getStorePayPic() {
        return StorePayPic;
    }

    public String getCreditCode() {
        return CreditCode;
    }

    public String getLicenseNo() {
        return LicenseNo;
    }

    public String getLicenseValidity() {
        return LicenseValidity;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public String getLegalPerson() {
        return LegalPerson;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getWeChatPic() {
        return WeChatPic;
    }
}
