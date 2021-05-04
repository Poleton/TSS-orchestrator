pragma solidity >= 0.8.0 < 0.9.0;

interface IERC20 {

    function totalSupply() external view returns (uint256);
    function balanceOf(address account) external view returns (uint256);
    function allowance(address owner, address spender) external view returns (uint256);

    function transfer(address recipient, uint256 amount) external returns (bool);
    function approve(address spender, uint256 amount) external returns (bool);
    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
}

library SafeMath {
    
    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        
        assert(b <= a);
        return a - b;
    }

    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }
}

contract smartInsurancePolicy {


    // INDEXED EVENTS??
    
    event Reserve(address indexed from, uint256 reserve);
    //event Reserve(address indexed from, uint256 ID, uint256 reserve);

    modifier checkStateIni {
        require(contractState == State.Initialized);
        _;
    }

    modifier checkStateFund {
        require(contractState == State.Funded);
        _;
        contractState = State.Activated;
    }

    modifier checkStateAct {
        require(contractState == State.Activated);
        _;
    }

    modifier notExpired {
        if(block.timestamp >= parameters.expiryTimestamp) {
            deactivateContract(parameters.expiryTimestamp);
        }
        contractState == State.Activated;
    }
 
    // Contract State
    enum State {Initialized, Funded, Activated, Deactivated}
    State public contractState;
    bool public clientFunded = false;
    bool public insuranceFunded = false;

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
        
        contractPremium = premium;
        contractLiability = liability;

        parameters = ContractParametrization(inception, 0, expiry, 0, scope);

        contractState = State.Initialized;
    }

    // Contract Activation Function
    function activateContract (uint256 activationT) public checkStateFund notExpired returns (bool) {
        
        uint256 amount = 0;

        for (uint256 i = 0; i < shipmentCount; i++) {
            amount += shipments[i].liability;
        }

        require (amount == contractLiability, 'Activation Requirements Not Completed');

        parameters.activationTimestamp = activationT;
        //contractState = State.Activated;
        return true;
    }

    // Contract Deactivation Function
    function deactivateContract (uint256 deactivationT) public checkStateAct returns (uint256 contractReserve) {
        // Llama a las funciones que toquen que realizaran los pagos a las addresses que toquen segun 
        // el calculo de la reserve obtenido, pagando tamb el premium
        // Destruir contrato

        parameters.deactivationTimestamp = deactivationT;
        //llamar funciones pagos
        contractState = State.Deactivated;
        return contractReserve;
    }

    // Contract Fund Function
    function fundContract (string memory currency, uint256 amount) public {
        // Funcion que recibe las transferencias de client y insurance, asegura que las cantidades son las correctas
        // Si las cantidades son correctas y el premium y liability se encuentran en el SC
        // se modifica contractState a State.Funded
        require ((msg.sender == client && amount == contractPremium && currency == contractCurrency) || (msg.sender == insurance && amount == contractLiability && currency == contractCurrency), 'Invalid Transaction');

        if (msg.sender == client) {
            require (clientFunded, 'Client Already Funded');

            IERC20(contractAddressERC20[currency]).transferFrom(_msgSender(), address(this), amount);
            emit DepositERC20Token(currency, _msgSender(), amount);

            clientFunded = true;
        } else {
            require (insuranceFunded, 'Insurance Already Funded');

            IERC20(contractAddressERC20[currency]).transferFrom(_msgSender(), address(this), amount);
            emit DepositERC20Token(currency, _msgSender(), amount);

            insuranceFunded = true;
        }

        if (clientFunded && insuranceFunded) {
            contractState = State.Funded
        }
        
    }

    function depositERC20Token(string memory tokenName, uint256 amount) public {

        IERC20(contractAddressERC20[tokenName]).approve(address(this), amount);
        IERC20(contractAddressERC20[tokenName]).transferFrom(_msgSender(), address(this), amount);
        emit DepositERC20Token(tokenName, _msgSender(), amount);
    }

    // Contract Initialization Functions
    function addShipment (uint256 _ID, uint256 _liability) public checkStateIni notExpired {
        
        shipments[shipmentCount] = Shipment(_ID, (_liability * (10 ** contractDecimals)), 0, 0);
        shipmentCount += 1;
    }

    function addSensor (uint256 _ID, uint256 _sensorType) public checkStateIni notExpired {
        
        uint256 shipmentID = extractDigit(_ID, 1);

        for (uint256 i = 0; i < shipmentCount; i++) {
            if (shipments[i].ID == shipmentID) {
                sensors[sensorCount] = Sensor(_ID, _sensorType, 0);
                sensorCount += 1;
                shipments[i].numSensors += 1;
            }
        } 
    }

    function addConditionLevel (uint256 lDepth, uint256 _sensorType, int256 _dataRangeMin, int256 _dataRangeMax, uint256 _percentualWeight) public checkStateIni notExpired {

        for(uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].sensorType == _sensorType) {
                uint256 _ID = (sensors[i].ID * 10) + lDepth;
                conditionLevels[conditionLevelCount] = ConditionLevel(_ID, _dataRangeMin, _dataRangeMax, _percentualWeight, 0);
                conditionLevelCount += 1;
            }       
        }
    }

    // Data Update Functions
    function updateSensor (uint256 _ID, int256 sensorData, uint256 dataTimestamp) public checkStateAct notExpired {

        for (uint256 i = 0; i < sensorCount; i++) {
            if (sensors[i].ID == _ID) {   
                       
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
    function updateReserve (uint256 _ID, uint256 _excessTime, uint256 _percentualWeight) private checkStateAct notExpired {

        uint256 shipmentID = extractDigit(_ID, 2);
        contractReserve = 0;

        for (uint256 i = 0; i < shipmentCount; i++) {

            if (shipments[i].ID == shipmentID) {
                shipments[i].reserve += ((shipments[i].liability/shipments[i].numSensors) * ((_excessTime/60)*_percentualWeight)) / 100;
                
                if (shipments[i].reserve > shipments[i].liability) {
                    shipments[i].reserve = shipments[i].liability;
                }
            }
            contractReserve += shipments[i].reserve;
            emit Reserve(msg.sender, contractReserve);
            //emit Reserve(msg.sender, _ID, contractReserve);
        }
    }

    // ID Extraction Function
    function extractDigit (uint256 number, uint256 target) internal returns (uint256 extractedDigit) {

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


//cambiar nombres   OK
//eventos
//si expira el updatesensor deact   OK
