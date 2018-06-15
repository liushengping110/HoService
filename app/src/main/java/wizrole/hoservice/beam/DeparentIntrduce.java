package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/9/7.
 */

public class DeparentIntrduce implements Serializable{

    public String DeparentId;
    public String DeparentName;
    public String DeparentIntrduce;

    public void setDeparentId(String deparentId) {
        DeparentId = deparentId;
    }

    public void setDeparentName(String deparentName) {
        DeparentName = deparentName;
    }

    public void setDeparentIntrduce(String deparentIntrduce) {
        DeparentIntrduce = deparentIntrduce;
    }

    public String getDeparentId() {

        return DeparentId;
    }

    public String getDeparentName() {
        return DeparentName;
    }

    public String getDeparentIntrduce() {
        return DeparentIntrduce;
    }
}
