package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/5/12.
 * 健康知识
 */

public class HealthKnow implements Serializable{

    public String title;
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
