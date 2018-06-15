package wizrole.hoservice.interface_base;

/**
 * Created by on 2017/2/22.
 * 请求后返回数据
 */

public interface ReturnData {

    void Success(String result);

    void Lose(String result);
}