# TSS-orchestrator
This is a block from the TSS project which consists in:

1. **API:**
    - Login (Spring-REST)
    - User Interface (Spring-REST)
    - IoT (Spring-REST)  
    - Blockchain (Web3)
    

2. **DATABASE:** (mySQL)
    - Users
    - User Policies
    - User Smart Policies
    - User Smart Policies Alerts
    

***

## SETTING UP

### MySQL environment

To install MySQL follow [this](https://myaccount.google.com/u/0/lesssecureapps?pli=1).  

> **Additional info for installation:**   
> - It is not necessary to get everything, there may be some features that could not be 
    possible to install depending on which software you already have (like MySQL for visual studio code) 
> - You may have to configure the server after the installation (not like the guide), just open the MySQL 
    Installer again and click reconfigure in the server row. Now you can go and follow the guide. 
> - The root user & password are important for accessing to the command line client later for example,
    but it does not have to be related to the project.

### MySQL database

Using the _MySQL Command Line Client_ (once accessed with your root pass):

    create database db_orchestrator; 
    create user 'tss'@'%' identified by 'tss1234'; 
    grant all on db_orchestrator.* to 'tss'@'%'; 

1. Creates the new database called "db_orchestrator".
2. Creates the user "tss" with "tss1234" as its password.
3. Gives all privileges to the new user on the newly created database.

>All names and pass are customizable but please, make sure you change it in properties file.



In the _src/main/resources/application.properties_ file:

    spring.datasource.url = jdbc:mysql://localhost:3306/db_orchestrator
    spring.datasource.username = tss
    spring.datasource.password = tss1234
    spring.jpa.hibernate.ddl-auto = update
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

To recreate the database use:

    drop database db_orchestrator;
    create database db_orchestrator;
    grant all on db_orchestrator.* to 'tss'@'%';

### Ethereum Blockchain Simulation

We will be using the _Truffle Suite_ environment:

For the blockchain simulation [install Ganache](https://www.trufflesuite.com/ganache).

We are using this configuration:

    GAS PRICE: 200000000
    GAS LIMIT: 6721975
    HOSTNAME: 127.0.0.1
    PORT NUMBER: 7545


#### Using your own Contracts

This step is not necessary if you just want to use our implementation. 
Otherwise, if you are willing to use a different smart contract:

1. Install web3j CLI:
   
        $ curl -L get.web3j.io | sh && source ~/.web3j/source.sh        

2. Install truffle:

        $ npm install truffle -g

3. Create a truffle project or use the already created in _/TSS-orchestrator/truffle/_.

        $ truffle init 

4. Put your _contract.sol_ in the _/TSS-orchestrator/truffle/contracts/_ and compile
   (you should be at the truffle project directory):

        $ truffle compile

5. A _yourContract.json_ should have been generated in the
   _/TSS-orchestrator/truffle/build/contracts/_, so now convert it into a Wrapper (.java)
   
        $ web3j generate truffle -t path/to/yourContract.json -o path/to/src/main/java/tss/orchestrator/models/contracts -p tss.orchestrator.models.contracts 

6. Finally, modify the methods at the BlockChainServiceImpl class to use your contract.


****

## FIRST TIME RUNNING

You will have to create by default the users, this is made automatically adding
in the _src/main/resources/data.sql_:

    insert into user values (1,'{accountAddress}', 'user1', '1234', '{privateKey}');

>Put all necessary User values.  
>Change the _{accountAddress}_ and _{privateKey}_ to your Ganache's
> first account address and private key.

Uncomment the last line in the _src/main/resources/application.properties_:

    spring.datasource.initialization-mode=always

>Comment this line again after the first execution.

To run the project use this command in your coding environment terminal:

    mvnw spring-boot:run

****

## API LOGIC
### SPRING-CLASSES

#### CONTROLLERS
_The requests managers._
  - **Interface:**
    
        @RequestMapping("/examplepath")
        public interface ExampleRestControllerApi {

            @GetMapping("/get")
            public List<E> getExample();
    
            @PostMapping("/post")
            public ResponseEntity<Object> postExample(@RequestBody ModelDTO modelDTO);
        }

  - **Class:**

        @RestController
        public class ExampleRestController implements ExampleRestControllerApi {

            @Override
            public List<E> getExample(){
                //...
            }

            @Override
            public ResponseEntity<Object> postExample(@RequestBody ModelDTO modelDTO){
                //...
            }
        }  

  - **Useful Code:**  
    - Get info from a request path _/path/{id}_
          
          @PathVariable int id

    - Get info from a request parameter (int) _key1=value1_ & (string) _key2=value2_

          @RequestParam int key1, @RequestParam String key2

    - Map json to an object from the body of a request

          @RequestBody ModelDTO modelDTO

    >There are a lot of requests types but here we will mainly use get and post.  
      
    
#### MODELS  
_The data._
  - **Data Transfer Object:**
        
        public class ModelDTO {
            //variables
            
            //setters & getters            
        }

    >Used for direct parsing from json to Java operative class

  - **Model:**
    
        public class Model {
            //variables
    
            public Model(){
            }
    
            public Model (ModelDTO modelDTO){
                super();
                this.variable = modelDTO.getVariable();
            }

            //setters & getters 
        }

    >From the conversion of DTO
    
  - **Model:**  
     
        @Entity
        public class Model {
            @Id
            @GeneratedValue
            private Integer id;
            //variables

            public Model(){
            }

            public Model (ModelDTO modelDTO){
                super();
                this.variable = modelDTO.getVariable();
            }
    
            //setters & getters 
        }

    >Attached to a database table.

#### SERVICES
_The internal logic._
  - **Table repository:**

        @Repository
        public interface ModelRepository extends JpaRepository<Model, Integer> {
        
        }
    
    >It will be autocompleted.
    

### WEB3
_For the communication with the blockchain._

### SENSORS DATA

The path to send the sensor's data is _/blockchain/api/sensors_ and 
the format of the json is:

    {
        "userId": "{userId}",
        "smartPolicyId": "{smartPolicyId}",
        "sensorData": {
            "{sensorId}": "{sensorValue}",
            "{sensorId}": "{sensorValue}",
            ...
        },
        "dataTimeStamp": "{dataTimeStamp}"
    }

>The *sensorId* should follow:  
> - 1st Digit : Shipment ID, 2nd Digit : Sensor ID.  
> 
>The *dataTimeStamp* should be epoch format in seconds.

