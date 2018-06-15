package wizrole.hoservice.beam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 */

public class DiseaseName implements Serializable{

    public String DiseaseDetailName;//病种名
    public String DisOverview	;	//概述
    public String    DisCause	;	//病因
    public String  DisSymptom	;	//症状
    public String      DisCheck;		//检查
    public String  DisTreatment	;	//治疗
    public String      DisComplications;	//并发症
    public String DisProphylaxis	;	//预防
    public String      DisDiet		;	//饮食

    public void setDiseaseDetailName(String diseaseDetailName) {
        DiseaseDetailName = diseaseDetailName;
    }

    public void setDisOverview(String disOverview) {
        DisOverview = disOverview;
    }

    public void setDisCause(String disCause) {
        DisCause = disCause;
    }

    public void setDisSymptom(String disSymptom) {
        DisSymptom = disSymptom;
    }

    public void setDisCheck(String disCheck) {
        DisCheck = disCheck;
    }

    public void setDisTreatment(String disTreatment) {
        DisTreatment = disTreatment;
    }

    public void setDisComplications(String disComplications) {
        DisComplications = disComplications;
    }

    public void setDisProphylaxis(String disProphylaxis) {
        DisProphylaxis = disProphylaxis;
    }

    public void setDisDiet(String disDiet) {
        DisDiet = disDiet;
    }

    public String getDiseaseDetailName() {

        return DiseaseDetailName;
    }

    public String getDisOverview() {
        return DisOverview;
    }

    public String getDisCause() {
        return DisCause;
    }

    public String getDisSymptom() {
        return DisSymptom;
    }

    public String getDisCheck() {
        return DisCheck;
    }

    public String getDisTreatment() {
        return DisTreatment;
    }

    public String getDisComplications() {
        return DisComplications;
    }

    public String getDisProphylaxis() {
        return DisProphylaxis;
    }

    public String getDisDiet() {
        return DisDiet;
    }
}
