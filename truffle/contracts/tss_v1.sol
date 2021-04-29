pragma solidity >= 0.7.0 < 0.9.0;

contract insurance_policy {

    // Conditions Parametrization
    uint256 public levelCount = 0;
    mapping (uint256 => ConditionLevel) public conditionLevels;
    
    struct ConditionLevel {
        uint256 levelID;
        uint256 dataRangeMin;
        uint256 dataRangeMax;
        uint256 percentualWeight;
        uint256 levelExcessTime;
    }

    // Sensors Parametrization
    uint256 public sensorCount = 0;
    mapping (uint256 => Sensor) public sensors;

    struct Sensor {
        uint256 sensorID;
        uint256 sensorType;
        uint256 sensorUpdatePeriodicity;
        uint256 sensorLastUpdate;
    }

    // Contract Parametrization
    ContractParametrization public parameters;

    struct ContractParametrization {
        uint256 inceptionTimestamp;
        uint256 activationTimestamp;
        uint256 expiryTimestamp;
        uint256 deactivationTimestamp;
        string territorialScope;
    }

    // Constructor
    constructor(uint256 inception, uint256 expiry, string memory scope) public {
        parameters = ContractParametrization(inception, 0, expiry, 0, scope); 
    }

    // Contract Initialization Functions
    function addSensor(uint256 sID, uint256 sType, uint256 sUpdatePeriodicity) public {
        
        sensors[sensorCount] = Sensor(sID, sType, sUpdatePeriodicity, 0);
        sensorCount += 1;
    }

    function addLevel(uint256 lDepth, uint256 lType, uint256 lMin, uint256 lMax, uint256 lWeight) public {

        for(uint256 i = 0; i < sensorCount; i++) {

            if (sensors[i].sensorType == lType) {
                uint256 _levelID = (sensors[i].sensorID * 10) + lDepth;
                conditionLevels[levelCount] = ConditionLevel(_levelID, lMin, lMax, lWeight, 0);
                levelCount += 1;
            }       
        }
    }

    // Data Update Functions
    function updateSensor(uint256 sensorID, uint256 sensorData, uint256 newDataTime) public {

        for(uint256 i = 0; i < sensorCount; i++) {

            if (sensors[i].sensorID == sensorID) {          
                for(uint256 j = 0; j < levelCount; j++) {

                    if ((sensorData < conditionLevels[j].dataRangeMax) && (sensorData > conditionLevels[j].dataRangeMin)) {
                        if (sensors[i].sensorLastUpdate == 0) {
                            sensors[i].sensorLastUpdate = newDataTime;
                        } else {
                            conditionLevels[j].levelExcessTime = conditionLevels[j].levelExcessTime + (newDataTime - sensors[i].sensorLastUpdate);
                        }
                    }       
                }
               sensors[i].sensorLastUpdate = newDataTime;
            }       
        }
    }

    // Calculo reserve
    // Activacion SC (transferencia tokens del asegurado y aseguradora)
    // Inicio Shipment / Final Shipment
    // Requirements en otras funciones para proteger

    // function getSensor(uint256 sensorID) public view returns (uint256,uint256,uint256) {
    //     for(uint256 i = 0; i < sensorCount; i++) {
    //         if (sensors[i].sensorID == sensorID) {              
    //             return (sensors[i].sensorType,sensors[i].sensorUpdatePeriodicity,sensors[i].sensorLastUpdate);
    //         }
    //     }
    // }

    // function getLevel(uint256 levelID) public view returns (uint256,uint256,uint256,uint256,uint256) {
    //     for(uint256 i = 0; i < levelCount; i++) {
    //         if (conditionLevels[i].levelID == levelID) {              
    //             return (conditionLevels[i].levelID,conditionLevels[i].dataRangeMin,conditionLevels[i].dataRangeMax,conditionLevels[i].percentualWeight,conditionLevels[i].levelExcessTime);
    //         }
    //     }
    // }

}

// Data Types

// Temperature -> 1
// Acceleration -> 2
// Humidity -> 3
// Pressure -> 4

// Example:
// 2 Temperature Sensors + 1 Pressure Sensor
// 3 Temperature Levels
//     0-5 C -> 1% -> 0
//     6-20 C -> 3% -> 1
//     21-70 C -> 10% -> 2
// 2 Pressure Levels
//     1000-2500 P -> 1% -> 0
//     2501-10000 P -> 10% -> 1

// ADDING SENSORS
// addSensor(sID = 2, sType = 1, sUpdatePeriodicity = 5)
// Sensor {
//         sensorID = 2
//         sensorType = 1
//         sensorUpdatePeriodicity = 5
//         sensorLastUpdate = 0
// }

// addSensor(sID = 5, sType = 1, sUpdatePeriodicity = 5)
// Sensor {
//         sensorID = 5
//         sensorType = 1
//         sensorUpdatePeriodicity = 5
//         sensorLastUpdate = 0
// }

// addSensor(sID = 1, sType = 4, sUpdatePeriodicity = 5)
// Sensor {
//         sensorID = 1
//         sensorType = 4
//         sensorUpdatePeriodicity = 5
//         sensorLastUpdate = 0
// }

// ADDING TEMPERATURE LEVELS
// addLevel(lDepth = 0, lType = 1, lMin = 0, lMax = 5, lWeight = 1)
// ConditionLevel {
//     levelID = 20; // 2 * 10 + 0
//     dataRangeMin = 0;
//     dataRangeMax = 5;
//     percentualWeight = 1;
//     levelExcessTime = 0;
// }
// ConditionLevel {
//     levelID = 50; // 5 * 10 + 0
//     dataRangeMin = 0;
//     dataRangeMax = 5;
//     percentualWeight = 1;
//     levelExcessTime = 0;
// }

// addLevel(lDepth = 1, lType = 1, lMin = 6, lMax = 20, lWeight = 3)
// ConditionLevel {
//     levelID = 21; 2 * 10 + 1
//     dataRangeMin = 6;
//     dataRangeMax = 20;
//     percentualWeight = 3;
//     levelExcessTime = 0;
// }
// ConditionLevel {
//     levelID = 51; 5 * 10 + 1
//     dataRangeMin = 6;
//     dataRangeMax = 20;
//     percentualWeight = 3;
//     levelExcessTime = 0;
// }

// addLevel(lDepth = 2, lType = 1, lMin = 21, lMax = 70, lWeight = 10)
//ConditionLevel {
//    levelID = 22; 
//    dataRangeMin = 21;
//    dataRangeMax = 70;
//    percentualWeight = 10;
//    levelExcessTime = 0;
//}
//ConditionLevel {
//    levelID = 52;
//    dataRangeMin = 21;
//    dataRangeMax = 70;
//    percentualWeight = 10;
//    levelExcessTime = 0;
//}


// ADDING PRESSURE LEVELS
// addLevel(lDepth = 0, lType = 4, lMin = 1000, lMax = 2500, lWeight = 1)
// ConditionLevel {
//     levelID = 10; 1 * 10 + 0
//     dataRangeMin = 1000;
//     dataRangeMax = 2500;
//     percentualWeight = 1;
//     levelExcessTime = 0;
// }
// addLevel(lDepth = 1, lType = 4, lMin = 2501, lMax = 10000, lWeight = 10)
// ConditionLevel {
//     levelID = 11; 1 * 10 + 1
//     dataRangeMin = 2501;
//     dataRangeMax = 10000;
//     percentualWeight = 10;
//     levelExcessTime = 0;
// }


// UPDATING TEMPERATURE LEVELS
// UPDATE 1
// updateSensor(sensorID = 2, sensorData = 2, newDataTime = 1619525000)
// Sensor {
//         sensorID = 2
//         sensorType = 1
//         sensorUpdatePeriodicity = 5
//         sensorLastUpdate = 1619525000
// }
// ConditionLevel {
//     levelID = 20;
//     dataRangeMin = 0;
//     dataRangeMax = 5;
//     percentualWeight = 1;
//     levelExcessTime = 0;
// }

// UPDATE 2
// updateSensor(sensorID = 2, sensorData = 8, newDataTime = 1619525600)
// Sensor {
//         sensorID = 2
//         sensorType = 1
//         sensorUpdatePeriodicity = 5
//         sensorLastUpdate = 1619525600
// }
// ConditionLevel {
//     levelID = 20;
//     dataRangeMin = 0;
//     dataRangeMax = 5;
//     percentualWeight = 1;
//     levelExcessTime = 0;
// }
// ConditionLevel {
//     levelID = 21;
//     dataRangeMin = 6;
//     dataRangeMax = 20;
//     percentualWeight = 3;
//     levelExcessTime = 600;
// }
