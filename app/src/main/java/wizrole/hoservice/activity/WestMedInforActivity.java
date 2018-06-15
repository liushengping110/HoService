package wizrole.hoservice.activity;

import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.WestMedInfor;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 西药详细信息
 */

public class WestMedInforActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.MedGenericName)TextView MedGenericName;
    @BindView(R.id.MedAttend)TextView MedAttend;
    @BindView(R.id.MedIndication)TextView MedIndication;
    @BindView(R.id.MedUsage)TextView MedUsage;
    @BindView(R.id.MedContraindication)TextView MedContraindication;
    @BindView(R.id.MedAdverseReaction)TextView MedAdverseReaction;
    @BindView(R.id.MedConsideration)TextView MedConsideration;
    @BindView(R.id.MedSpecPopulationMed)TextView MedSpecPopulationMed;
    @BindView(R.id.MedDrugInteraction)TextView MedDrugInteraction;
    @BindView(R.id.MedPharmacologicalEffects)TextView MedPharmacologicalEffects;
    @BindView(R.id.MedConstituent)TextView MedConstituent;
    @BindView(R.id.MedValidityPeriod)TextView MedValidityPeriod;
    @BindView(R.id.MedProductionEnterprise)TextView MedProductionEnterprise;
    @BindView(R.id.MedPackingSpecification)TextView MedPackingSpecification;
    @Override
    protected int getLayout() {
        return R.layout.activity_westmedinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        WestMedInfor infor=(WestMedInfor) getIntent().getSerializableExtra("westMed");
        if(!infor.getMedGenericName().equals("")){
            MedGenericName.setText(Html.fromHtml(infor.getMedGenericName()));
        }
        if(!infor.getMedAttend().equals("")){
            MedAttend.setText(infor.getMedAttend());
        }
        if(!infor.getMedIndication().equals("")){
            MedIndication.setText(infor.getMedIndication());
        }
        if(!infor.getMedUsage().equals("")){
            MedUsage.setText(infor.getMedUsage());
        }
        if(!infor.getMedContraindication().equals("")){
            MedContraindication.setText(infor.getMedContraindication());
        }
        if(!infor.getMedAdverseReaction().equals("")){
            MedAdverseReaction.setText(infor.getMedAdverseReaction());
        }
        if(!infor.getMedConsideration().equals("")){
            MedConsideration.setText(infor.getMedConsideration());
        }
        if(!infor.getMedSpecPopulationMed().equals("")){
            MedSpecPopulationMed.setText(infor.getMedSpecPopulationMed());
        }
        if(!infor.getMedDrugInteraction().equals("")){
            MedDrugInteraction.setText(infor.getMedDrugInteraction());
        }
        if(!infor.getMedPharmacologicalEffects().equals("")){
            MedPharmacologicalEffects.setText(infor.getMedPharmacologicalEffects());
        }
        if(!infor.getMedConstituent().equals("")){
            MedConstituent.setText(infor.getMedConstituent());
        }
        if(!infor.getMedValidityPeriod().equals("")){
            MedValidityPeriod.setText(infor.getMedValidityPeriod());
        }
        if(!infor.getMedProductionEnterprise().equals("")){
            MedProductionEnterprise.setText(infor.getMedProductionEnterprise());
        }
        if(!infor.getMedPackingSpecification().equals("")){
            MedPackingSpecification.setText(infor.getMedPackingSpecification());
        }

    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
