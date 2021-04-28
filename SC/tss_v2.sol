pragma solidity >= 0.7.0 < 0.9.0;

contract smartInsurancePolicy {

    // Contract State
    enum State {Initialized, Funded, Activated, Deactivated};
    State contractState;

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
        uint256 type;
        uint256 lastUpdate;
    }

    // Conditions Parametrization
    uint256 public levelCount = 0;
    mapping (uint256 => ConditionLevel) public conditionLevels;
    
    struct ConditionLevel {
        uint256 ID;
        uint256 dataRangeMin;
        uint256 dataRangeMax;
        uint256 percentualWeight;
        uint256 excessTime;
    }

    // Constructor
    constructor (address _client, address _insurance, address _broker, uint256 premium, uint256 liability, uint256 inception, uint256 expiry, string memory scope) public {
    
        owner = msg.sender;
        client = _client;
        insurance = _insurance;
        broker = _broker;

        contractPremium = premium;
        contractLiability = liability;

        parameters = ContractParametrization(inception, 0, expiry, 0, scope);

        contractState = State.Initialized;
    }

    // Contract Activation Function
    function activateContract (uint256 activationT) public returns (bool) {
        // Esta funcion comprueba que la suma de las liabilities de shipments es igual a la contractLiability
        // Comprueba que la variable contractState sea State.Funded (variable que indica que las transacciones se han realizado)
        // Actualiza el activationTimestamp al timestamp passado como parametro
        // Devuelve true en caso de que se haya activado el SC
        // Actualiza contractState a State.Activated

        uint256 public amount = 0;

        for(uint256 i = 0; i < shipmentCount; i++) {
            amount += shipments[i].liability;
        }

        require (amount == contractLiability && contractState == State.Funded, 'Activation Requirements Not Completed');

        parameters.activationTimestamp = activationT;
        contractState = State.Activated;
        return true;
    }

    // Contract Deactivation Function
    function deactivateContract (uint256 deactivationT) public returns (uint256) {
        // Comprueba que la variable contractState sea State.Activated
        // Actualiza el deactivationTimestamp al timestamp passado como parametro
        // Llama a las funciones que toquen que realizaran los pagos a las addresses que toquen segun 
        // el calculo de la reserve obtenido, pagando tamb el premium
        // Actualiza contractState a State.Deactivated
        // Devuelve el resultado de la contractReserve

        require(contractState == State.Activated,'Deactivation Requirements Not Completed');
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

        shipments[shipmentCount] = Shipment(sID, sLiability, 0, 0)
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

    function addConditionLevel (uint256 lDepth, uint256 lType, uint256 lMin, uint256 lMax, uint256 lWeight) public {

        for(uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].sensorType == lType) {
                uint256 lID = (sensors[i].sensorID * 10) + lDepth;
                conditionLevels[levelCount] = ConditionLevel(lID, lMin, lMax, lWeight, 0);
                levelCount += 1;
            }       
        }
    }

    // Data Update Functions
    function updateSensor (uint256 sensorID, uint256 sensorData, uint256 dataTimestamp) public {

        for (uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].sensorID == sensorID) {   
                       
                for (uint256 k = 0; k < levelCount; k++) {
                    if ( (conditionLevels[k].dataRangeMin < sensorData) && (sensorData < conditionLevels[k].dataRangeMax) ) {
                        
                        if (sensors[i].lastUpdate) {
                            conditionLevel[k].excessTime += (dataTimestamp - sensors[i].sensorLastUpdate);
                            updateReserve(conditionLevel[k].ID, conditionLevel[k].excessTime, conditionLevel[k].percentualWeight);
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

            if(shipments[i].ID == shipmentID) {
                shipments[i].reserve += ((shipments[i].liability / shipments[i].numSensors) * (conditionExcessTime * conditionLevelWeight));
                
                if(shipments[i].reserve > shipments[i].liability) {
                    shipments[i].reserve = shipments[i].liability;
                }
            }
            contractReserve += shipments[i].reserve;
        }
    }

    // ID Extraction Function
    function extractDigit (uint256 number, uint256 target) public returns (uint256 extractedDigit) {

        uint8 digits = 0;
        uint256 aux = number;
        
        while (aux != 0) {
            aux /= 10;
            digits++;
        }
    
        require (target < digits, "Target outside number range");
        
        for (uint8 i = 0; i < target; i++) {
            number /= 10;
        }
        
        return extractedDigit = (number % 10);
    }

}
