Project 1 Unit 6
Kelly Cheng (ID: kuangchc)

********* HOW TO USE THE APPLICATION **********
1. Run Driver class. The main method for testing database functionalities is in this class.
Neither the server nor the client needs to be run for this test to work.

For Unit 6, I have added the driver package and the database package.

In the database package, in both DatabaseOperation and CreateDatabase class,
there are configurations for connection to the DB and the username and pw need to be altered.

The database package contains the DatabaseOperation class which has methods
for performing create, update, and delete on the database. These methods
take in an Automobile object as a parameter and get info from using Automobile methods.

The way to access these DB operations is still through the BuildAuto API:
DatabaseOperation instances are created in ProxyAutomobile, where the static a1 can be 
set from searching through LHM and passed into DatabaseOperation methods.

**************** MY DATABASE ******************
Has 4 tables: 

mysql> show tables;
+----------------------+
| Tables_in_automobiles|
+----------------------+
| models               |
| options              |
| optionsets           |
| relations            |
+----------------------+
4 rows in set (0.00 sec)

The cars only have two option sets - Transmission and brakes.
Since optionsets are known, the two optionsets are set upon DB creation. 
The program does not save optionset names into the database.
Options are not shared between car models to allow more customization. Eg. Car A’s 
ABS brakes might cost less than Car B’s standard brakes.

More details on the tables:

mysql> DESCRIBE models;
+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| model_id  | int(11)      | NO   | PRI | NULL    | auto_increment |
| name      | varchar(100) | YES  |     | NULL    |                |
| make      | varchar(40)  | YES  |     | NULL    |                |
| model     | varchar(100) | YES  |     | NULL    |                |
| baseprice | float        | YES  |     | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+
5 rows in set (0.01 sec)

mysql> DESCRIBE options;
+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| option_id | int(11)      | NO   | PRI | NULL    | auto_increment |
| name      | varchar(100) | YES  |     | NULL    |                |
| price     | float        | YES  |     | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+
3 rows in set (0.00 sec)

mysql> DESCRIBE optionsets;
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| opset_id | int(11)      | NO   | PRI | NULL    | auto_increment |
| name     | varchar(100) | YES  |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+
2 rows in set (0.00 sec)

mysql> DESCRIBE relations;
+-------------+---------+------+-----+---------+----------------+
| Field       | Type    | Null | Key | Default | Extra          |
+-------------+---------+------+-----+---------+----------------+
| relation_id | int(11) | NO   | PRI | NULL    | auto_increment |
| model_id    | int(11) | YES  | MUL | NULL    |                |
| opset_id    | int(11) | YES  | MUL | NULL    |                |
| option_id   | int(11) | YES  | MUL | NULL    |                |
+-------------+---------+------+-----+---------+----------------+
4 rows in set (0.00 sec)

********** Input files for testing *************
-Toyota Prius.txt
-Ford_Focus_Wagon_ZTW.txt
The database as the main function is executed is shown in test_output.txt

******** Notes on the the other stuff **********
There’s another main method in the Server class which is in the server package.
That main method runs the server.

How to run the server/client/servlet part of the project (AKA Unit 5 stuff):
1. Run server. 
   It’s in the server project.
   The port is set to 4442. Main method is in Server class.
2. Run client. (Need to manually change IP address in Client class)
   It’s in the Project1Unit5 project.
3. In client’s console, type ’s’ to send properties file to server.
   Command instructions will be shown on the console window too: 
   <<Commands>>: s(send properties), a(list all models),
	 c(choose a model to configure):
  After typing ’s’, you’ll be prompted to type a file name. There are two files available: 
  -prius.properties
  -beetle.properties
4. Run DisplayModels in servlet package in project Project1Unit6 with Tomcat server. 
   Or run the whole project. If the whole project is run, http://localhost:XXXX/Project1Unit6/ 
   will redirect to http://localhost:XXXX/Project1Unit6/index.html,
   which has a link to the actual app. (XXXX=port)
5. The cars only have two option sets - Transmission and brakes.

CONTENTS:

-Project: Server
src
  |-server
  |-adapter
  |-util
  |-model
  |-scale
  |-exception
-Project: Project1Unit6
src
  |-client
  |-adapter
  |-client
  |-servlet
  |-util
  |-model
  |-scale
  |-exception
  |-prius.properties
  |-beetle.properties
WebContent
  |-index.html
  |-result.jsp
-Class Diagram
-Screenshots of webpages
-Test Output

[1] Server

server package - 
Server uses defaultSocketClient to handle sessions.

adapter package - 
Has BuildAuto class, which is the API. BuildAuto exposes 8 methods: buildAuto, printAuto, updateOptionSetName, updateOptionPrice, fix, buildFromProp, listAll, serializeAuto
buildFromProp, listAll, serializeAuto are declared in AutoServer interface and were added in Unit 4.
Abstract class proxyAutomobile contains an instance variable a1 of type Automobile. Variable a1 is used for handling all operations on Automobile and LHM of Automobiles (which is stored in proxyAutomobile as well).

util package -
In charge of file IO. Contains populate class, which is in charge of reading a text file and parsing into automotive’s variables.
Populate and Serialize were updated to accommodate Properties objects/files.

model package - 
has Automobile class. Each Automobile class contains an instance of OptionSet and respective Options.

scale package -
has EditOption class. EditOption extends Thread. There are two constructors where you can pass in the stuff you want to edit. You can either edit the options name or an option’s price, and  EditOption will differentiate between what you want to edit by which constructor you call.

exception package - 
Contains custom exception AutoException
AutoException calls helper function fix1to10 to fix exceptions. It also logs error messages in a .txt file called myLog.txt
List of custom exceptions and fixes:
fix1 - for AutoException (errno=1)
	 * Fixes: Missing price in text file.
	 * Fixes by: asks user for correct price and changes input text file
fix2 - for AutoException (errno=2)
	 * Fixes: Format of first line is wrong.
	 * Fixes by: asks user for correct info and changes input text file
fix3 - for AutoException (errno=3)
	 * Fixes: Missing filename or wrong filename
	 * Fixes by: asks user for correct filename
fix4 - for AutoException (errno=4)
	 * Fixes: Can't find optionset in automobile class
	 * Fixes by: asks user for correct optionset name
fix5 - for AutoException (errno=5)
	 * Fixes: Can't find option in automobile class
	 * Fixes by: asks user for correct option name

[2] Project1Unit6
servlet package -
DisplayModels makes the page that displays a list of all available models. It creates a client instance and calls client methods to send commands to the server (equivalent to pressing ‘a’)
ConfigureModel makes the page that lets the user choose options for the selected model. Creates a client instance that sends command to the server (equivalent to pressing ‘c’).

client package - 
Client class extends DefaultSocketClient, overrides handleSession().

adapter package -
Has BuildAuto class, which is the API. BuildAuto exposes 7 methods: buildAuto, printAuto, updateOptionSetName, updateOptionPrice, fix, buildFromProp, deserialize
buildFromProp, deserialize are added to the CreateAuto interface

[3] test_output.txt - outputs of test cases using 3 different input files.
I copy-pasted the console output to this file.
The input files are all properties files and are saved in the Client project:   
  |-prius.properties
  |-beetle.properties

