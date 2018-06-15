package wizrole.hoservice.beam;

/**
 * Created by a on 2017/9/19.
 * 药品详情
 */

public class MedItem {

    public String ChargeStandard;
    public int SerialNo;
    public String MedCate;
    public String ItemCode;
    public String Uom;
    public double Price;
    public String ItemDesc;
    public String PricesNo;
    public String Factory;
    public String Level;
    public String DrugForm;
    public String InsureSign;


    public void setChargeStandard(String chargeStandard) {
        ChargeStandard = chargeStandard;
    }

    public void setSerialNo(int serialNo) {
        SerialNo = serialNo;
    }

    public void setMedCate(String medCate) {
        MedCate = medCate;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public void setUom(String uom) {
        Uom = uom;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public void setPricesNo(String pricesNo) {
        PricesNo = pricesNo;
    }

    public void setFactory(String factory) {
        Factory = factory;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public void setDrugForm(String drugForm) {
        DrugForm = drugForm;
    }

    public void setInsureSign(String insureSign) {
        InsureSign = insureSign;
    }

    public String getChargeStandard() {

        return ChargeStandard;
    }

    public int getSerialNo() {
        return SerialNo;
    }

    public String getMedCate() {
        return MedCate;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getUom() {
        return Uom;
    }

    public double getPrice() {
        return Price;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public String getPricesNo() {
        return PricesNo;
    }

    public String getFactory() {
        return Factory;
    }

    public String getLevel() {
        return Level;
    }

    public String getDrugForm() {
        return DrugForm;
    }

    public String getInsureSign() {
        return InsureSign;
    }
}
