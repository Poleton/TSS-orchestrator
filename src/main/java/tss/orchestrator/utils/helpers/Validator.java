package tss.orchestrator.utils.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.web3j.protocol.Web3j;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {



    public Boolean emailValidator(String email){


        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
    public Boolean cifValidator(String cif){

        Pattern pattern = Pattern.compile("[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[A-Z[0-9]]{1}");

        Matcher matcher = pattern.matcher(cif);

        return matcher.find();
    }
    /*/^(0x)?[0-9a-f]{40}$/


    public Boolean smartContractAddressValidator(SmartPolicyDTO smartPolicyDTO){
        Pattern pattern = Pattern.compile("^0x[a-fA-F0-9]{40}$");
        Matcher matcher_broker = pattern.matcher(smartPolicyDTO.getBrokerAddress());
        Matcher matcher_client = pattern.matcher(smartPolicyDTO.getClientAddress());
        Matcher matcher_insurance = pattern.matcher(smartPolicyDTO.getInsuranceAddress());


        return (matcher_broker.find() && matcher_client.find() && matcher_insurance.find());

    }*/
}
