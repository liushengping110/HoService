package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/9.
 * 何人执笔？
 * 妇产科学
 */

public class ObstetricsGynecology  implements Serializable{
    public String ChapterName;   //章节名
    public String ChapterUrl;   //pdf下载地址

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public void setChapterUrl(String chapterUrl) {
        ChapterUrl = chapterUrl;
    }

    public String getChapterName() {

        return ChapterName;
    }

    public String getChapterUrl() {
        return ChapterUrl;
    }
}
