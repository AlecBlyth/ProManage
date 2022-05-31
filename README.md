# ProManage
ProManage is a university project that attempts to bring Agile methodology to various industries with the use of software. The client will be given a JSON file packaged with their software which will customise the application based on their respective industry. 
## Tech Stack
This project is written mostly in Java and CSS. The framework being used is JavaFX. The companion program is written in C++ and can be found here, [JSON Creator](https://github.com/AlecBlyth/JSONCreator-UniWork) The companion program creates a JSON file in a format that is read and used by the JavaFX program 
## Libraries used  
-	[Bluebub]( https://github.com/b3z/bluebub)
-	[Jfoenix-9.0.1]( https://github.com/sshahine/JFoenix)
-	[Json-simple-1.1.1]( https://github.com/fangyidong/json-simple)
-	[Medusa-11.5]( https://github.com/HanSolo/Medusa)
-	[Mysql-connector-java-8.0.22.jar]( https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.22)
## Installation / Usage 
Currently unable to release as a JAR file as issues persist when attempting to run said jar file. 
It does however run when compiled by Intellji IDEa. 
-	Setup a local MySQL server 
- Download [JavaFX 13]( https://gluonhq.com/products/javafx/) 
-	Import database files into MySQL server, or create new databases with same structure and names. 
-	In your IDE, set the SDK to [Java13]( https://openjdk.java.net/projects/jdk/13/)
-	Set VM options to --module-path "PATH TO JAVAFX13" --add-modules javafx.controls,javafx.fxml 
-	Set Main class to “GUI_classes.proManage”
-	Run and compile 

## License
[MIT](https://choosealicense.com/licenses/mit/)
