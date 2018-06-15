package wizrole.hoservice.life.model.evaluation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class CommentList implements Serializable{

    public String CommentHostName;		//	- 评价人姓名
    public String CommentHostHeadPic;		//- 评价人头像
    public String CommentTime;				//- 评价时间
    public String CommentContent;			//- 评价内容
    public List<String> CommentPicList;			//- 评价图片(List<String>)

    public String getCommentHostName() {
        return CommentHostName;
    }

    public String getCommentHostHeadPic() {
        return CommentHostHeadPic;
    }

    public String getCommentTime() {
        return CommentTime;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public List<String> getCommentPicList() {
        return CommentPicList;
    }
}
