package wizrole.hoservice.life.model.goodssearch;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class SearchGoodsHis {
    public String content;

    public SearchGoodsHis(){
        super();
    }
    public SearchGoodsHis(String content){
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
