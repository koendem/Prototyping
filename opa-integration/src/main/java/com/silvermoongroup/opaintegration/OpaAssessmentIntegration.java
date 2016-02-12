package com.silvermoongroup.opaintegration;

import com.oracle.opa.AssessRequest;
import com.oracle.opa.AttributeType;
import com.oracle.opa.GlobalInstanceType;
import com.oracle.opa.OutcomeStyleEnum;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;

import java.math.BigDecimal;


/**
 * Created by koen on 11.02.16.
 *
 *       <typ:assess-request>
             <typ:global-instance>
                 <!--Zero or more repetitions:-->
                 <typ:attribute id="base_premium" outcome-style="value-only" />
                 <typ:attribute id="deductible">
                     <typ:number-val>100</typ:number-val>
                 </typ:attribute>
                 <typ:attribute id="insured_value">
                     <typ:number-val>1100</typ:number-val>
                 </typ:attribute>
                 <typ:attribute id="premium_rate">
                     <typ:number-val>0.05</typ:number-val>
                 </typ:attribute>
             </typ:global-instance>
         </typ:assess-request>

 *
 */
public class OpaAssessmentIntegration {


    public static String endpointURL = "http://192.168.10.25:7003/lunos_rules/determinations-server/assess/soap/generic/12.2.1/computerloss_rules";

    public static void main(String[] args){
        System.out.println("com.silvermoongroup.opaintegration.OpaAssessmentIntegration");

        AssessRequest request = new AssessRequest();
        GlobalInstanceType globalInstance = new GlobalInstanceType();
        request.setGlobalInstance(globalInstance);

        AttributeType base_premium_attribute = new AttributeType();
        base_premium_attribute.setOutcomeStyle(OutcomeStyleEnum.VALUE_ONLY);
        base_premium_attribute.setId("base_premium");
        globalInstance.getAttribute().add(base_premium_attribute);

        AttributeType deductible_attribute = new AttributeType();
        deductible_attribute.setId("deductible");
        deductible_attribute.setNumberVal(new BigDecimal("100"));
        globalInstance.getAttribute().add(deductible_attribute);

        AttributeType insured_value_attribute = new AttributeType();
        insured_value_attribute.setId("insured_value");
        insured_value_attribute.setNumberVal(new BigDecimal("1100"));
        globalInstance.getAttribute().add(insured_value_attribute);

        AttributeType premium_rate_attribute = new AttributeType();
        premium_rate_attribute.setId("premium_rate");
        premium_rate_attribute.setNumberVal(new BigDecimal("0.05"));
        globalInstance.getAttribute().add(premium_rate_attribute);


        //--------now call the webservice.
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.oracle.opa");

        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);

        template.setDefaultUri(endpointURL);
        StringResult result = (StringResult) template.marshalSendAndReceive(request);
        System.out.println(result);


    }
}
