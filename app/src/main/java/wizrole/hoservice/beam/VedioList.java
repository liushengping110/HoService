package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/10.
 * 何人执笔？
 */

public class VedioList implements Serializable{

    public String VedioTitle;	//视频名称
    public String VedioUrl	;//视频地址
    public String VedioImgUrl;	//缩略图地址

    public void setVedioTitle(String vedioTitle) {
        VedioTitle = vedioTitle;
    }

    public void setVedioUrl(String vedioUrl) {
        VedioUrl = vedioUrl;
    }

    public void setVedioImgUrl(String vedioImgUrl) {
        VedioImgUrl = vedioImgUrl;
    }

    public String getVedioTitle() {

        return VedioTitle;
    }

    public String getVedioUrl() {
        return VedioUrl;
    }

    public String getVedioImgUrl() {
        return VedioImgUrl;
    }
}
