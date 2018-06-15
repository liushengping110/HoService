package wizrole.hoservice.beam;

/**
 * Created by a on 2017/3/10.
 */

public class TarItemDetail {

    public String Index;    //编号
    public String Category;    //项目类别
    public String ItemName;    //项目名称
    public String Qty;    //数量
    public String UOM;    //单位
    public String Price;    //单价
    public String TotalAmt;    //总价
    public String DisAmt;
    public String PayorAmt;
    public String PatShareAmt;
    public String BillDate;    //开单日期
    public String BillTime;    //开单时间
    public String InsuRatio;
    public String InsuClass;
    public String ItemChargeBasis;

    public String getIndex() {
        return Index;
    }

    public String getCategory() {
        return Category;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public void setDisAmt(String disAmt) {
        DisAmt = disAmt;
    }

    public void setPayorAmt(String payorAmt) {
        PayorAmt = payorAmt;
    }

    public void setPatShareAmt(String patShareAmt) {
        PatShareAmt = patShareAmt;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public void setBillTime(String billTime) {
        BillTime = billTime;
    }

    public void setInsuRatio(String insuRatio) {
        InsuRatio = insuRatio;
    }

    public void setInsuClass(String insuClass) {
        InsuClass = insuClass;
    }

    public void setItemChargeBasis(String itemChargeBasis) {
        ItemChargeBasis = itemChargeBasis;
    }

    public String getItemName() {
        return ItemName;

    }

    public String getQty() {
        return Qty;
    }

    public String getUOM() {
        return UOM;
    }

    public String getPrice() {
        return Price;
    }

    public String getTotalAmt() {
        return TotalAmt;
    }

    public String getDisAmt() {
        return DisAmt;
    }

    public String getPayorAmt() {
        return PayorAmt;
    }

    public String getPatShareAmt() {
        return PatShareAmt;
    }

    public String getBillDate() {
        return BillDate;
    }

    public String getBillTime() {
        return BillTime;
    }

    public String getInsuRatio() {
        return InsuRatio;
    }

    public String getInsuClass() {
        return InsuClass;
    }

    public String getItemChargeBasis() {
        return ItemChargeBasis;
    }
}
