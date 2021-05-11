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
