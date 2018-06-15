package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/9/14.
 * 院级新闻集合
 */

public class HosNews  implements Serializable{

    public String News_Img;
    public String News_title;
    public String News_msg;
    public String News_SamllImg;

    public String getNews_SamllImg() {
        return News_SamllImg;
    }

    public void setNews_Img(String news_Img) {
        News_Img = news_Img;
    }

    public void setNews_title(String news_title) {
        News_title = news_title;
    }

    public void setNews_msg(String news_msg) {
        News_msg = news_msg;
    }

    public String getNews_Img() {

        return News_Img;
    }

    public String getNews_title() {
        return News_title;
    }

    public String getNews_msg() {
        return News_msg;
    }
}
