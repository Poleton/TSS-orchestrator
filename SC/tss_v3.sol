pragma solidity >= 0.8.0 < 0.9.0;

contract smartInsurancePolicy {
 
    // Contract State
    enum State {Initialized, Funded, Activated, Deactivated}
    State contractState;
    
    // Contract Decimals
    uint8 public constant contractDecimals = 5;

    // Contract Addresses
    address public owner;
    address public client;
    address public insurance;
    address public broker;
    
    // Contract Monetary Parameters
    uint256 public contractPremium;
    uint256 public contractLiability;
    uint256 public contractReserve = 0;
    
    // Contract Parametrization
    ContractParametrization public parameters;

    struct ContractParametrization {
        uint256 inceptionTimestamp;
        uint256 activationTimestamp;
        uint256 expiryTimestamp;
        uint256 deactivationTimestamp;
        string territorialScope;
    }

    // Shipment Parametrization
    uint256 public shipmentCount = 0;
    mapping (uint256 => Shipment) public shipments;
    
    struct Shipment {
        uint256 ID;
        uint256 liability;
        uint256 reserve;
        uint256 numSensors;
    }

    // Sensors Parametrization
    uint256 public sensorCount = 0;
    mapping (uint256 => Sensor) public sensors;

    struct Sensor {
        uint256 ID;
        uint256 sensorType;
        uint256 lastUpdate;
    }

    // Conditions Parametrization
    uint256 public conditionLevelCount = 0;
    mapping (uint256 => ConditionLevel) public conditionLevels;
    
    struct ConditionLevel {
        uint256 ID;
        int256 dataRangeMin;
        int256 dataRangeMax;
        uint256 percentualWeight;
        uint256 excessTime;
    }

    // Constructor
    constructor (address _client, address _insurance, address _broker, uint256 premium, uint256 liability, uint256 inception, uint256 expiry, string memory scope) public {
    
        owner = msg.sender;
        client = _client;
        insurance = _insurance;
        broker = _broker;
        
        contractPremium = premium * (10 ** contractDecimals);
        contractLiability = liability * (10 ** contractDecimals);

        parameters = ContractParametrization(inception, 0, expiry, 0, scope);

        contractState = State.Initialized;
    }

    // Contract Activation Function
    function activateContract (uint256 activationT) public returns (bool) {
        
        uint256 amount = 0;

        for (uint256 i = 0; i < shipmentCount; i++) {
            amount += shipments[i].liability;
        }

        require (amount == contractLiability && contractState == State.Funded, 'Activation Requirements Not Completed');

        parameters.activationTimestamp = activationT;
        contractState = State.Activated;
        return true;
    }

    // Contract Deactivation Function
    function deactivateContract (uint256 deactivationT) public returns (uint256) {
        // Llama a las funciones que toquen que realizaran los pagos a las addresses que toquen segun 
        // el calculo de la reserve obtenido, pagando tamb el premium
        // Destruir contrato

        require (contractState == State.Activated , 'Deactivation Requirements Not Completed');
        parameters.deactivationTimestamp = deactivationT;
        //llamar funciones pagos
        contractState = State.Deactivated;
        return contractReserve;
    }

    // Contract Fund Function
    function fundContrat () public {
        // Funcion que recibe las transferencias de client y insurance, asegura que las cantidades son las correctas
        // Si las cantidades son correctas y el premium y liability se encuentran en el SC
        // se modifica contractState a State.Funded
    }

    // Contract Initialization Functions
    function addShipment (uint256 sID, uint256 sLiability) public {

        shipments[shipmentCount] = Shipment(sID, (sLiability * (10 ** contractDecimals)), 0, 0);
        shipmentCount += 1;
    }

    function addSensor (uint256 sID, uint256 sType) public {
        
        uint256 shipmentID = extractDigit(sID, 1);

        for (uint256 i = 0; i < shipmentCount; i++) {
            if (shipments[i].ID == shipmentID) {
                sensors[sensorCount] = Sensor(sID, sType, 0);
                sensorCount += 1;
                shipments[i].numSensors += 1;
            }
        } 
    }

    function addConditionLevel (uint256 lDepth, uint256 lType, int256 lMin, int256 lMax, uint256 lWeight) public {

        for(uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].sensorType == lType) {
                uint256 lID = (sensors[i].ID * 10) + lDepth;
                conditionLevels[levelCount] = ConditionLevel(lID, lMin, lMax, lWeight, 0);
                conditionLevelCount += 1;
            }       
        }
    }

    // Data Update Functions
    function updateSensor (uint256 sensorID, int256 sensorData, uint256 dataTimestamp) public {

        for (uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].ID == sensorID) {   
                       
                for (uint256 k = 0; k < conditionLevelCount; k++) {
                    if ( (conditionLevels[k].dataRangeMin <= sensorData) && (sensorData < conditionLevels[k].dataRangeMax) ) {
                        
                        if (sensors[i].lastUpdate != 0) {
                            uint256 auxTime = conditionLevels[k].excessTime;
                            conditionLevels[k].excessTime += (dataTimestamp - sensors[i].lastUpdate);
                            updateReserve(conditionLevels[k].ID, (conditionLevels[k].excessTime - auxTime), conditionLevels[k].percentualWeight);
                        } else {
                            sensors[i].lastUpdate = dataTimestamp;
                        }
                    }   
                }
                sensors[i].lastUpdate = dataTimestamp;
            }       
        }
    }

    // Reserve Update Function
    function updateReserve (uint256 conditionLevelID, uint256 conditionExcessTime, uint256 conditionLevelWeight) private {

        uint256 shipmentID = extractDigit(conditionLevelID, 2);
        contractReserve = 0;

        for (uint256 i = 0; i < shipmentCount; i++) {

            if (shipments[i].ID == shipmentID) {
                shipments[i].reserve += ((shipments[i].liability/shipments[i].numSensors) * ((conditionExcessTime/60)*conditionLevelWeight)) / 100;
                
                if (shipments[i].reserve > shipments[i].liability) {
                    shipments[i].reserve = shipments[i].liability;
                }
            }
            contractReserve += shipments[i].reserve;
        }
    }

    // ID Extraction Function
    function extractDigit (uint256 number, uint256 target) public returns (uint256 extractedDigit) {

        // uint8 digits = 0;
        // uint256 aux = number;
        
        // while (aux != 0) {
        //    aux /= 10;
        //    digits++;
        // }
    
        // require (target < digits, "Target outside number range");
        
        for (uint8 i = 0; i < target; i++) {
            number /= 10;
        }
        
        return extractedDigit = (number % 10);
    }

}
