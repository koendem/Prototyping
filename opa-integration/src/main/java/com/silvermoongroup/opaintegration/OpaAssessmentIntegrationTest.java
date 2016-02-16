package com.silvermoongroup.opaintegration;

import com.oracle.opa.AttributeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koen on 16.02.16.
 */
public class OpaAssessmentIntegrationTest {

    public static void main(String[] args){

        System.out.println("args.length = " + args.length);
        String ruleBase = "computerloss_rules";

        if (args.length == 2) {

            // get an opa integration object.
            OpaAssessmentIntegration opa = new OpaAssessmentIntegration(args[0], Integer.parseInt(args[1]),ruleBase);

            // build input and output.
            OpaAttribute base_premiumAttribute = new OpaAttribute("base_premium");
            List<OpaAttribute> inputs = buildInputParameters();

            AttributeType result = opa.runOpaRules(base_premiumAttribute, inputs);

            System.out.println("Value of base_premium : " + result.getNumberVal());

        } else {
            System.out.println("Provide the ip address and opaServerPort as the first and the second argument respectively. \n" +
                    "Example:\n" +
                    OpaAssessmentIntegration.class.getSimpleName() + " 192.168.10.25 7003");
            return;
        }



    }

    private static List<OpaAttribute> buildInputParameters() {
        List<OpaAttribute> inputParameters = new ArrayList<OpaAttribute>();

        OpaAttribute deductible = new OpaAttribute("deductible", "100", OpaAttributeType._NUMBER);
        OpaAttribute insured_value = new OpaAttribute("insured_value", "1100", OpaAttributeType._NUMBER);
        OpaAttribute premium_rate = new OpaAttribute("premium_rate", "0.05", OpaAttributeType._NUMBER);

        inputParameters.add(deductible);
        inputParameters.add(insured_value);
        inputParameters.add(premium_rate);

        return inputParameters;
    }

}
