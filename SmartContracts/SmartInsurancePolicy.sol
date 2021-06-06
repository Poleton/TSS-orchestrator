pragma solidity 0.8.1;

import './IERC20.sol';

contract SmartInsurancePolicy {
    
    event SensorAdded(int256 ID, int256 sensorType);
    event ConditionLevelAdded(int256 ID, int256 dataRangeMin, int256 dataRangeMax, uint256 percentualWeight, uint256 conditionLevelCount);
    
    event SensorUpdated(int256 levelID, int256 sensorType, int256 updatedData, int256 updatedDataExcess, uint256 levelExcessTime, uint256 contractReserve);
 
    // Contract State
    enum State {Initialized, Funded, Activated, Deactivated}
    State public contractState;
    
    bool public premiumFunded = false;
    bool public liabilityFunded = false;
    
    bool public liabilityPartition;

    // Contract Addresses
    address public owner;
    address public client;
    address public insurance;
    address public broker;
    
    // Contract Economic Parameters
    IERC20 contractCurrency;

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
    mapping(uint256 => Shipment) public shipments;
    
    struct Shipment {
        int256 ID;
        uint256 liability;
        uint256 reserve;
        uint256 numSensors;
    }

    // Sensors Parametrization
    uint256 public sensorCount = 0;
    mapping(uint256 => Sensor) public sensors;

    struct Sensor {
        int256 ID;
        int256 sensorType;
        uint256 lastUpdate;
    }

    // Conditions Parametrization
    uint256 public conditionLevelCount = 0;
    mapping(uint256 => ConditionLevel) public conditionLevels;
    
    struct ConditionLevel {
        int256 ID;
        int256 dataRangeMin;
        int256 dataRangeMax;
        uint256 percentualWeight;
        uint256 excessTime;
    }

    modifier stateIsInitialized {
        require(contractState == State.Initialized, 'Initialized State Required');
        _;
    }

    modifier stateIsFunded {
        require(contractState == State.Funded, 'Funded State Required');
        _;
        contractState = State.Activated;
    }

    modifier stateIsActivated {
        require(contractState == State.Activated, 'Activated State Required');
        _;
    }
    
    modifier validID(int256 _ID) {
        require(_ID >= 0, 'Not Valid ID');
        _;
    }

    modifier notExpired {
        if(block.timestamp >= parameters.expiryTimestamp) {
            deactivateContract(parameters.expiryTimestamp);
        }
        _;
    }

    constructor(address _contractCurrency, address _client, address _insurance, address _broker, uint256 _premium, uint256 _liability, uint256 _inception, uint256 _expiry, string memory _scope, bool _liabilityPartition) public {
    
        contractCurrency = IERC20(address(_contractCurrency));
        
        owner = msg.sender;
        client = _client;
        insurance = _insurance;
        broker = _broker;
        
        contractPremium = _premium;
        contractLiability = _liability;

        parameters = ContractParametrization(_inception, 0, _expiry, 0, _scope);
        
        liabilityPartition = _liabilityPartition;

        contractState = State.Initialized;
    }

    function activateContract(uint256 _activationTimestamp) public stateIsFunded notExpired {
        
        uint256 amount = 0;

        for(uint256 i = 0; i < shipmentCount; i++) {
            amount += shipments[i].liability;
        }

        require(amount == contractLiability, 'Activation Requirements Not Completed');

        parameters.activationTimestamp = _activationTimestamp;
        contractState = State.Activated;
    }

    function deactivateContract(uint256 _deactivationTimestamp) public stateIsActivated returns (uint256) {

        parameters.deactivationTimestamp = _deactivationTimestamp;
        
        // if(contractReserve > 0) {
        //    contractCurrency.transfer(broker, contractReserve);
        // }

        // contractCurrency.transfer(insurance, (contractPremium + (contractLiability - contractReserve)));
        
        contractState = State.Deactivated;
        return contractReserve;
    }

    function fundContract(uint256 _amount) external stateIsInitialized notExpired {
        contractState = State.Funded;
    }

    // function fundContract(uint256 _amount) external stateIsInitialized notExpired {
        
    //     require((msg.sender == client && _amount == contractPremium) || (msg.sender == insurance && _amount == contractLiability), 'Invalid Transaction');
        
    //     uint256 allowance = contractCurrency.allowance(msg.sender, address(this));
    //     require(allowance >= _amount, 'Insuficient Account Allowance');
        
    //     if(msg.sender == client) {
            
    //         require(!premiumFunded, 'Premium Already Funded');

    //         contractCurrency.transferFrom(msg.sender, address(this), _amount);
    //         premiumFunded = true;
        
    //     } else {
            
    //         require(!liabilityFunded, 'Insurance Already Funded');

    //         contractCurrency.transferFrom(msg.sender, address(this), _amount);
    //         liabilityFunded = true;
    //     }

    //     if(premiumFunded && liabilityFunded) {
    //         contractState = State.Funded;
    //     }
    // }

    function addShipment(int256 _ID, uint256 _liability) external stateIsInitialized validID(_ID) notExpired {
        
        shipments[shipmentCount] = Shipment(_ID, _liability, 0, 0);
        shipmentCount += 1;
    }

    function addSensor(int256 _ID, int256 _sensorType) external stateIsInitialized validID(_ID) notExpired {
        
        int256 shipmentID = extractDigit(_ID, 1);

        for(uint256 i = 0; i < shipmentCount; i++) {
            
            if(shipments[i].ID == shipmentID) {
                sensors[sensorCount] = Sensor(_ID, _sensorType, 0);
                sensorCount += 1;
                shipments[i].numSensors += 1;
            }
        } 
        emit SensorAdded(_ID, _sensorType);
    }

    function addConditionLevel(int256 _levelDepth, int256 _sensorType, int256 _dataRangeMin, int256 _dataRangeMax, uint256 _percentualWeight) external stateIsInitialized validID(_levelDepth) notExpired {
        
        for(uint256 i = 0; i < sensorCount; i++) {
            
            if(sensors[i].sensorType == _sensorType) {
                int256 _ID = (sensors[i].ID * 10) + _levelDepth;
                conditionLevels[conditionLevelCount] = ConditionLevel(_ID, _dataRangeMin, _dataRangeMax, _percentualWeight, 0);
                conditionLevelCount += 1;
                emit ConditionLevelAdded(_ID, _dataRangeMin, _dataRangeMax, _percentualWeight, conditionLevelCount);
            }       
        }
    }

    function updateSensor(int256 _ID, int256 _sensorData, uint256 _dataTimestamp) external stateIsActivated notExpired {

        int256 updatedLevelID = -1;
        int256 sensorType = -1;
        int256 updatedDataExcess = -1;
        uint256 levelExcessTime = 0;
        
        for(uint256 i = 0; i < sensorCount; i++) {
            
            if(sensors[i].ID == _ID) {
                
                sensorType = sensors[i].sensorType;
                       
                for(uint256 k = 0; k < conditionLevelCount; k++) {
                    
                    if(sensors[i].ID == (conditionLevels[k].ID/10)) {
                    
                        if( (conditionLevels[k].dataRangeMin <= _sensorData) && (_sensorData < conditionLevels[k].dataRangeMax) ) {
    
                            if(sensors[i].lastUpdate != 0) {
                                uint256 auxTime = conditionLevels[k].excessTime;
                                conditionLevels[k].excessTime += (_dataTimestamp - sensors[i].lastUpdate);
                                updateReserve(conditionLevels[k].ID, (conditionLevels[k].excessTime - auxTime), conditionLevels[k].percentualWeight);
                            } else {
                                sensors[i].lastUpdate = _dataTimestamp;
                            }
                            updatedLevelID = conditionLevels[k].ID;
                            updatedDataExcess = (_sensorData - conditionLevels[k].dataRangeMin);
                            levelExcessTime = conditionLevels[k].excessTime;
                        }
                    }
                }
                sensors[i].lastUpdate = _dataTimestamp;
            }       
        }
        emit SensorUpdated(updatedLevelID, sensorType, _sensorData, updatedDataExcess, levelExcessTime, contractReserve);
    }

    function updateReserve(int256 _ID, uint256 _excessTime, uint256 _percentualWeight) internal {

        int256 shipmentID = extractDigit(_ID, 2);
        contractReserve = 0;

        for(uint256 i = 0; i < shipmentCount; i++) {

            if(shipments[i].ID == shipmentID) {
                
                if(liabilityPartition) {
                    shipments[i].reserve += ((shipments[i].liability/shipments[i].numSensors) * ((_excessTime/60)*_percentualWeight)) / 100;
                }else{
                    shipments[i].reserve += (shipments[i].liability * ((_excessTime/60)*_percentualWeight)) / 100;
                }
                
                if(shipments[i].reserve > shipments[i].liability) {
                    shipments[i].reserve = shipments[i].liability;
                }
            }
            contractReserve += shipments[i].reserve;
        }
    }

    function extractDigit(int256 number, uint256 target) internal pure returns (int256 extractedDigit) {
        
        for(uint8 i = 0; i < target; i++) {
            number /= 10;
        }
        
        return extractedDigit = (number % 10);
    }
}
