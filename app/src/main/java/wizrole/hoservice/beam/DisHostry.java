package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/12.
 * 何人执笔？
 * 疾病搜索历史纪录
 */

public class DisHostry implements Serializable{

    public String content;

    public DisHostry(){
        super();
    }
    public DisHostry(String content){
       this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
