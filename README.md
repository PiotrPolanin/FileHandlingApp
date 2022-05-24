# FileHandlingApp
Application to work with csv files


Instruction how to run the application: <br>

A main method is included in Main class in a package com.company.filehanlingapp to run the application. <br>
The application enables store data, so database connection configuration is required. <br>
Default name of database schema is 'items_management'. <br>
You can find a file called hibernate.cfg.xml in subfolder META-INF in a resource folder. <br>
Please, change the values of properties in mentioned file according to your database engine that are listed below: <br>

<"hibernate.connection.url">jdbc:(your database connection)/items_management?serverTimezone=(Poland)"> <br>
<"hibernate.connection.username">(your database username connection)> <br>
<"hibernate.connection.password">(your database password connection)> <br>
<"hibernate.dialect">(your database dialect)> <br>

Features of FileHandlingApp

![img_1.png](img_1.png)

Main menu enables you to store, remove information about an item and calculate operations of supply and buy. <br> 

![img_2.png](img_2.png)

Clicking "Add" button you have access to: <br>
save data to database, <br>
calculate operations and export report to a file, <br>
import data from a file, <br>
add and remove item operations manually. <br>

![img_3.png](img_3.png)

Clicking "Report" you can check the state of supply and buy state of selected item from table (database). 