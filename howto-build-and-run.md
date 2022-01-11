**Authors**

The authors of this source code are:
 * José Antonio Parejo Maestre

**Building**

This is a maven-java project, you will need the following pre-requisites to build this project:
  * A valid JAVA JDK installation. The authors have used JDK 11 to develop this project, but any version later tha 1.8 should be enough. You can download the openjdk from: https://openjdk.java.net/install/ 
  * A maven installation. The authros have used maven 3 to develop this project. Please follow the instructions available in the following link to install maven in your computer: https://maven.apache.org/install.html

Once you have installed both tools, you should be able to build the project using the following command: 
```
maven install
```

after executing suh command, you should see a message stating that the installation has been successfull.

**Running**

First, you must ensure that your environment meets the requirements stated above. The main example of this project can be executed by typing the following command:
```
mvn compile exec:java
```
You should see a result similar to this:
```
[INFO] --- exec-maven-plugin:3.0.0:java (default-cli) @ graspprqosawarebinding ---
Binding [chosenProviders={Notfication Sevice=Commited Use, Cloud Storage Service=Google Drive base paid plan, Payment & Billing Service=Business card}]:0.7640731503791482
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
**How to adapt this example to other scenarios**
