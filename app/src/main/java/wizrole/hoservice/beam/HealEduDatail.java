package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/9/27.
 * 何人执笔？
 * 健康教育
 */

public class HealEduDatail implements Serializable{

    public String Heal_img;
    public String Heal_title;
    public String Heal_content;

    public void setHeal_img(String heal_img) {
        Heal_img = heal_img;
    }

    public void setHeal_title(String heal_title) {
        Heal_title = heal_title;
    }

    public void setHeal_content(String heal_content) {
        Heal_content = heal_content;
    }

    public String getHeal_img() {

        return Heal_img;
    }

    public String getHeal_title() {
        return Heal_title;
    }

    public String getHeal_content() {
        return Heal_content;
    }
}
