var DEX = artifacts.require("./TSSDollarDEX.sol");

module.exports = function(deployer) {
   deployer.deploy(DEX);
};