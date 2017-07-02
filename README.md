# HealthMonitoringApplication
**Author: Aamir Shaikh**

Mobile Computing Assignment - 1

###Description
A Health Monitoring Application that generates 3 axial continuous graph based on the device's accelerometer data. You can upload the file to any server as well as download a file from the server.

**To run this project:**
- Unzip the folder.
- Import folder HealthMonitoring_UI on Android Studio. / Or simply open the project on Android Studio.
- Ensure that you run this on an Android device so as to accomoodate database storage of graph coordinates. If you plan on running this on an emulator, change the database file path in DatabaseHelper.java file. 
- Create a database for every new patient before starting. You may download any previously uploaded filr to run without entering data. You may need to provide server information in MainActivity.java before you use upload or download functions.

**Source code contains three java files:**
- *MainActivity.java* - The main class contains UI plotting and placement of text fields, boxes and buttons as well as graph plot area. Threads and Timers are implemented to display a continous graph cycle.
- *GraphView.java* - This file contains the code for displaying graph (Author: Arno den Hond).
- *DataBaseHelper.java* - Used to perform all CRUD operations for incoming accelerometer data.

###Screenshot
![untitled](https://user-images.githubusercontent.com/19358241/27766802-f74db924-5e9a-11e7-8273-d473de377e32.png)

