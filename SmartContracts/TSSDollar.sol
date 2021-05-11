pragma solidity 0.8.1;

import './IERC20.sol';

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

    using SafeMath for uint256;

    modifier onlyOwner {
        require(tx.origin == contractOwner);
        _;
    }

    constructor() public {

        contractOwner = tx.origin;
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
}
