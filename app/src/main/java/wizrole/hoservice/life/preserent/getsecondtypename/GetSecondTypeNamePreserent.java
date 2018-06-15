package wizrole.hoservice.life.preserent.getsecondtypename;


import wizrole.hoservice.life.model.getsecondtypename.GetSecondTypeNameBack;
import wizrole.hoservice.life.model.getsecondtypename.GetSecondTypeNameBackInterface;
import wizrole.hoservice.life.model.getsecondtypename.GetSecondTypeNameHttp;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 */

public class GetSecondTypeNamePreserent implements GetSecondTypeNameBackInterface {

    public GetSecondTypeNameInterface getSecondTypeNameInterface;
    public GetSecondTypeNameHttp getSecondTypeNameHttp;
    public GetSecondTypeNamePreserent(GetSecondTypeNameInterface getSecondTypeNameInterface){
        this.getSecondTypeNameInterface=getSecondTypeNameInterface;
        this.getSecondTypeNameHttp=new GetSecondTypeNameHttp(this);
    }

    /**
     *
     * @param name--商铺类别名称  如：餐饮美食
     */
    public void GetSecondTypeName(String name){
        getSecondTypeNameHttp.getSecondTypeName(name);
    }
    @Override
    public void Succ(Object o) {
        getSecondTypeNameInterface.getSecondTypeNameSucc((GetSecondTypeNameBack)o);
    }

    @Override
    public void Fail(String msg) {
        getSecondTypeNameInterface.getSecondTypeNameFail(msg);
    }
}
