pragma solidity ^0.8.1;

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

interface IERC20 {
    
    function totalSupply() external view returns (uint256);
    function balanceOf(address account) external view returns (uint256);
    function allowance(address owner, address spender) external view returns (uint256);

    function transfer(address recipient, uint256 amount) external returns (bool);
    function approve(address spender, uint256 amount) external returns (bool);
    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
    
    function mint(address account, uint256 amount) external;
    function burn(address account, uint256 amount) external;
    
    function setCurrencyPrice(uint256 price) external;
    function getCurrencyPrice() external view returns (uint256);
}

contract TSSDollar is IERC20 {

    string public constant name = "The Telematics Smart Shipments Dollar";
    string public constant symbol = "TSSD";
    uint8 public constant decimals = 18;

    event Approval(address indexed owner, address indexed spender, uint256 amount);
    event Transfer(address indexed from, address indexed to, uint256 amount);

    mapping(address => uint256) balances;

    mapping(address => mapping (address => uint256)) allowed;

    uint256 totalSupply_ = 1000000000000000000000000;
    uint256 maxSupply_ = 10000000000000000000000000;

    address public contractOwner;
    
    uint256 currencyPrice;

    using SafeMath for uint256;

    modifier onlyOwner {
        
        require(tx.origin == contractOwner);
        _;
    }

    constructor() public {

        contractOwner = tx.origin;
        currencyPrice = 1000;
        balances[msg.sender] = totalSupply_;
    }

    function totalSupply() external override view returns (uint256) {
        
        return totalSupply_;
    }

    function maxSupply() external view returns (uint256) {
        
        return maxSupply_;
    }

    function balanceOf(address account) external override view returns (uint256) {
        
        return balances[account];
    }

    function transfer(address receiver, uint256 amount) external override returns (bool) {
        
        require(amount <= balances[msg.sender]);
        
        balances[msg.sender] = balances[msg.sender].sub(amount);
        balances[receiver] = balances[receiver].add(amount);
        
        emit Transfer(msg.sender, receiver, amount);
        return true;
    }

    function approve(address delegate, uint256 amount) external override returns (bool) {
        
        allowed[tx.origin][delegate] = amount;
        
        emit Approval(tx.origin, delegate, amount);
        return true;
    }

    function allowance(address owner, address delegate) external override view returns (uint) {
        
        return allowed[owner][delegate];
    }

    function transferFrom(address owner, address buyer, uint256 amount) external override returns (bool) {
        
        require(amount <= balances[owner]);
        require(amount <= allowed[owner][msg.sender]);

        balances[owner] = balances[owner].sub(amount);
        allowed[owner][msg.sender] = allowed[owner][msg.sender].sub(amount);
        balances[buyer] = balances[buyer].add(amount);
        
        emit Transfer(owner, buyer, amount);
        return true;
    }

    function mint(address account, uint256 amount) external override onlyOwner {
        
        require(account != address(0));
        require(totalSupply_ + amount <= maxSupply_);

        totalSupply_ = totalSupply_.add(amount);
        balances[account] = balances[account].add(amount);
        emit Transfer(address(0), account, amount);
    }

    function burn(address account, uint256 amount) external override onlyOwner {
        
        require(account != address(0));
        require(amount <= balances[account]);

        totalSupply_ = totalSupply_.sub(amount);
        balances[account] = balances[account].sub(amount);
        emit Transfer(account, address(0), amount);
    }
    
    function setCurrencyPrice(uint256 price) external override onlyOwner {
        
        currencyPrice = price;
    }
    
    function getCurrencyPrice() external override view returns (uint256) {
        
        return currencyPrice;
    }
}

contract TSSDollarDEX {

    event Bought(uint256 amount);
    event Sold(uint256 amount);

    IERC20 public contractCurrency;

    uint256 public priceCurrency;

    constructor() public {
        
        contractCurrency = new TSSDollar();
        priceCurrency = contractCurrency.getCurrencyPrice();
    }
    
    fallback() external payable {
        buy();
    }

    function buy() public payable {
        
        uint256 amountTobuy = msg.value * priceCurrency;
        uint256 dexBalance = contractCurrency.balanceOf(address(this));
        
        require(amountTobuy > 0, 'Insuficient ETH Sent Quantity');
        require(amountTobuy <= dexBalance, 'Insuficient DEX Balance');
        
        contractCurrency.transfer(tx.origin, amountTobuy);
        
        emit Bought(amountTobuy);
    }
    
    function buyAndApprove(address _delegate) public payable {
        
        buy();
        contractCurrency.approve(_delegate, msg.value * priceCurrency);
    }

    function sell(uint256 _amount) public {
        
        require(_amount > 0, 'Insuficient Sent Token Quantity');
        
        uint256 _allowance = contractCurrency.allowance(msg.sender, address(this));
        
        require(_allowance >= _amount, 'Insuficient Account Allowance');
        
        contractCurrency.transferFrom(msg.sender, address(this), _amount);
        payable(msg.sender).transfer(_amount/priceCurrency);
        
        emit Sold(_amount);
    }
    
    function balanceOf(address _account) external view returns (uint256) {
        
        return contractCurrency.balanceOf(_account);
    }
    
    function approve(address _delegate, uint256 _amount) external returns (bool) {
        
        return contractCurrency.approve(_delegate, _amount);
    }
    
    function allowance(address _owner, address _delegate) external view returns (uint) {
        
        return contractCurrency.allowance(_owner, _delegate);
    }
    
    function mint(address _account, uint256 _amount) external {
        
        contractCurrency.mint(_account, _amount);
    }
    
    function burn(address _account, uint256 _amount) external {
        
        contractCurrency.burn(_account, _amount);
    }
    
    function setCurrencyPrice(uint256 _price) external {
        
        contractCurrency.setCurrencyPrice(_price);
        priceCurrency = contractCurrency.getCurrencyPrice();
    }
}

contract smartInsurancePolicy {
 
    // Contract State
    enum State {Initialized, Funded, Activated, Deactivated}
    State public contractState;
    
    bool public premiumFunded = false;
    bool public liabilityFunded = false;

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
        uint256 ID;
        uint256 liability;
        uint256 reserve;
        uint256 numSensors;
    }

    // Sensors Parametrization
    uint256 public sensorCount = 0;
    mapping(uint256 => Sensor) public sensors;

    struct Sensor {
        uint256 ID;
        uint256 sensorType;
        uint256 lastUpdate;
    }

    // Conditions Parametrization
    uint256 public conditionLevelCount = 0;
    mapping(uint256 => ConditionLevel) public conditionLevels;
    
    struct ConditionLevel {
        uint256 ID;
        int256 dataRangeMin;
        int256 dataRangeMax;
        uint256 percentualWeight;
        uint256 excessTime;
    }
    
    // INDEXED EVENTS??
    event Reserve(address indexed from, uint256 reserve);
    //event Reserve(address indexed from, uint256 ID, uint256 reserve);

    modifier stateIsInitialized {
        require(contractState == State.Initialized);
        _;
    }

    modifier stateIsFunded {
        require(contractState == State.Funded);
        _;
        contractState = State.Activated;
    }

    modifier stateIsActivated {
        require(contractState == State.Activated);
        _;
    }

    modifier notExpired {
        if(block.timestamp >= parameters.expiryTimestamp) {
            deactivateContract(parameters.expiryTimestamp);
        }
        _;
    }

    constructor(address _contractCurrency, address _client, address _insurance, address _broker, uint256 _premium, uint256 _liability, uint256 _inception, uint256 _expiry, string memory _scope) public {
    
        contractCurrency = IERC20(address(_contractCurrency));
        
        owner = msg.sender;
        client = _client;
        insurance = _insurance;
        broker = _broker;
        
        contractPremium = _premium;
        contractLiability = _liability;

        parameters = ContractParametrization(_inception, 0, _expiry, 0, _scope);

        contractState = State.Initialized;
    }

    function activateContract(uint256 _activationTimestamp) public stateIsFunded notExpired returns (bool) {
        
        uint256 amount = 0;

        for(uint256 i = 0; i < shipmentCount; i++) {
            amount += shipments[i].liability;
        }

        require(amount == contractLiability, 'Activation Requirements Not Completed');

        parameters.activationTimestamp = _activationTimestamp;
        contractState = State.Activated;

        return true;
    }

    function deactivateContract(uint256 _deactivationTimestamp) public stateIsActivated returns (uint256) {

        parameters.deactivationTimestamp = _deactivationTimestamp;
        
        if(contractReserve > 0) {
            contractCurrency.transfer(broker, contractReserve);
        }

        contractCurrency.transfer(insurance, (contractPremium + (contractLiability - contractReserve)));
        
        contractState = State.Deactivated;
        return contractReserve;
    }

    function fundContract(uint256 _amount) external stateIsInitialized notExpired {
        
        require((msg.sender == client && _amount == contractPremium) || (msg.sender == insurance && _amount == contractLiability), 'Invalid Transaction');
        
        uint256 allowance = contractCurrency.allowance(msg.sender, address(this));
        require(allowance >= _amount, 'Insuficient Account Allowance');
        
        if(msg.sender == client) {
            
            require(!premiumFunded, 'Premium Already Funded');

            contractCurrency.transferFrom(msg.sender, address(this), _amount);
            premiumFunded = true;
        
        } else {
            
            require(!liabilityFunded, 'Insurance Already Funded');

            contractCurrency.transferFrom(msg.sender, address(this), _amount);
            liabilityFunded = true;
        }

        if(premiumFunded && liabilityFunded) {
            contractState = State.Funded;
        }
    }

    function addShipment(uint256 _ID, uint256 _liability) external stateIsInitialized notExpired {
        
        shipments[shipmentCount] = Shipment(_ID, _liability, 0, 0);
        shipmentCount += 1;
    }

    function addSensor(uint256 _ID, uint256 _sensorType) external stateIsInitialized notExpired {
        
        uint256 shipmentID = extractDigit(_ID, 1);

        for(uint256 i = 0; i < shipmentCount; i++) {
            
            if(shipments[i].ID == shipmentID) {
                sensors[sensorCount] = Sensor(_ID, _sensorType, 0);
                sensorCount += 1;
                shipments[i].numSensors += 1;
            }
        } 
    }

    function addConditionLevel(uint256 _levelDepth, uint256 _sensorType, int256 _dataRangeMin, int256 _dataRangeMax, uint256 _percentualWeight) external stateIsInitialized notExpired {

        for(uint256 i = 0; i < sensorCount; i++) {
            
            if(sensors[i].sensorType == _sensorType) {
                uint256 _ID = (sensors[i].ID * 10) + _levelDepth;
                conditionLevels[conditionLevelCount] = ConditionLevel(_ID, _dataRangeMin, _dataRangeMax, _percentualWeight, 0);
                conditionLevelCount += 1;
            }       
        }
    }

    function updateSensor(uint256 _ID, int256 _sensorData, uint256 _dataTimestamp) external stateIsActivated notExpired {

        for(uint256 i = 0; i < sensorCount; i++) {
            
            if(sensors[i].ID == _ID) {   
                       
                for(uint256 k = 0; k < conditionLevelCount; k++) {
                    
                    if( (conditionLevels[k].dataRangeMin <= _sensorData) && (_sensorData < conditionLevels[k].dataRangeMax) ) {
                        
                        if(sensors[i].lastUpdate != 0) {
                            uint256 auxTime = conditionLevels[k].excessTime;
                            conditionLevels[k].excessTime += (_dataTimestamp - sensors[i].lastUpdate);
                            updateReserve(conditionLevels[k].ID, (conditionLevels[k].excessTime - auxTime), conditionLevels[k].percentualWeight);
                        } else {
                            sensors[i].lastUpdate = _dataTimestamp;
                        }
                    }   
                }
                sensors[i].lastUpdate = _dataTimestamp;
            }       
        }
    }

    function updateReserve(uint256 _ID, uint256 _excessTime, uint256 _percentualWeight) internal {

        uint256 shipmentID = extractDigit(_ID, 2);
        contractReserve = 0;

        for(uint256 i = 0; i < shipmentCount; i++) {

            if(shipments[i].ID == shipmentID) {
                
                shipments[i].reserve += ((shipments[i].liability/shipments[i].numSensors) * ((_excessTime/60)*_percentualWeight)) / 100;
                
                if(shipments[i].reserve > shipments[i].liability) {
                    shipments[i].reserve = shipments[i].liability;
                }
            }
            contractReserve += shipments[i].reserve;
            
            //emit Reserve(msg.sender, contractReserve);
            //emit Reserve(msg.sender, _ID, contractReserve);
        }
    }

    function extractDigit(uint256 number, uint256 target) internal pure returns (uint256 extractedDigit) {
        
        for(uint8 i = 0; i < target; i++) {
            number /= 10;
        }
        
        return extractedDigit = (number % 10);
    }
}
