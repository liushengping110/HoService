package wizrole.hoservice.beam;

/**
 * Created by liushengping on 2017/11/24/024.
 * 何人执笔？
 * 检验报告详细结果
 */

public class ReportMsgDetail {

    public String  MsgNo ; //编号
    public String  MsgName; //名称
    public String  MsgResult ;//结果
    public String  MsgUnit; //单位
    public String  MsgResultPrompt; // 提示
    public String  MsgNormal; //正常值
    public String  MsgCode; //代号


    public String getMsgNo() {
        return MsgNo;
    }

    public String getMsgName() {
        return MsgName;
    }

    public String getMsgResult() {
        return MsgResult;
    }

    public String getMsgUnit() {
        return MsgUnit;
    }

    public String getMsgResultPrompt() {
        return MsgResultPrompt;
    }

    public String getMsgNormal() {
        return MsgNormal;
    }

    public String getMsgCode() {
        return MsgCode;
    }
}
