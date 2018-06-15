package wizrole.hoservice.life.model.storesearch;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class MerchantsHos {
    public String content;

    public MerchantsHos(){
        super();
    }
    public MerchantsHos(String content){
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
