package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/8/25.
 * 科室 医生 搜索历史记录
 */

public class EditHostry implements Serializable{

    public String content;

    public EditHostry(){
        super();
    }
    public EditHostry(String content){
        this.content=content;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
