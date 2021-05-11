pragma solidity ^0.8.1;

import './IERC20.sol';
import './TSSDollar.sol';

contract TSSDollarDEX {

    event Bought(uint256 amount);
    event Sold(uint256 amount);

    IERC20 public contractCurrency;

    uint256 public priceCurrency;

    constructor() public {
        
        contractCurrency = new TSSDollar();
        priceCurrency = 1000;
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
    
    function buyAndApprove(address _delegate) external payable {
        
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
    
    function mint(address _account, uint256 _amount) public {
        
        contractCurrency.mint(_account, _amount);
    }
    
    function mintAndApprove(address _account, uint256 _amount, address _delegate) external {
        
        mint(_account, _amount);
        contractCurrency.approve(_delegate, _amount); 
    }
    
    function burn(address _account, uint256 _amount) public {
        
        contractCurrency.burn(_account, _amount);
    }
    
    function setCurrencyPrice(uint256 _price) external {
        
        require(msg.sender == contractCurrency.contractOwner);
        
        priceCurrency = _price;
    }
}
