package com.silvermoongroup.opaintegration;

import com.oracle.opa.AssessRequest;
import com.oracle.opa.AttributeType;
import com.oracle.opa.GlobalInstanceType;
import com.oracle.opa.OutcomeStyleEnum;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by koen on 13.02.16.
 */
public class OpaAssessRequestBuilder {


    /**
     * constructs an assess-request like the following example:
     *
     <typ:assess-request>
         <typ:global-instance>
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

     * @return
     * @param unknown
     * @param inputValues
     */
    public AssessRequest constructOpaAssessRequest(OpaAttribute unknown, List<OpaAttribute> inputValues) {


        AssessRequest request = new AssessRequest();
        GlobalInstanceType globalInstance = new GlobalInstanceType();
        request.setGlobalInstance(globalInstance);

        AttributeType unknown_Attribute = new AttributeType();
        unknown_Attribute.setOutcomeStyle(OutcomeStyleEnum.VALUE_ONLY);
        unknown_Attribute.setId(unknown.getName());
        globalInstance.getAttribute().add(unknown_Attribute);

        for(OpaAttribute input : inputValues){
            AttributeType attributeType = new AttributeType();
            attributeType.setId(input.getName());
            populateAttributeTypeValue(attributeType, input);
            globalInstance.getAttribute().add(attributeType);
        }

        return request;
    }

    private void populateAttributeTypeValue(AttributeType attributeType, OpaAttribute input) {
        switch (input.getValueType()){
            case _NUMBER:
            case _CURRENCY:
                attributeType.setNumberVal(new BigDecimal(input.getValue()));
                break;
            case _BOOLEAN:
                attributeType.setBooleanVal(new Boolean(input.getValue()));
                break;
            case _DATE:
                attributeType.setDateVal(populateDate(input));
                break;
            case _TEXT:
                break;
            default:
                throw new RuntimeException("Unsupported AttributeType :"+input.getValueType());
        }

    }

    private XMLGregorianCalendar populateDate(OpaAttribute input) {


        DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date= null;
        XMLGregorianCalendar xmlDate = null;
        try {
            date = df.parse( input.getValue());
            GregorianCalendar cal = new GregorianCalendar();

            cal.setTime(date);
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.getTimeZone().LONG);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot convert inputValue to Date : "+ input.getValue());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Cannot convert inputValue to Date : "+ input.getValue());
        }

        return xmlDate;
    }
}
