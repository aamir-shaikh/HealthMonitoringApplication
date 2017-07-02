package com.example.android.group4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*@Author: Aamir Shaikh         aamir.shaikh@asu.edu
*/

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    final String uploadFilePath = "/sdcard/CSE535_ASSIGNMENT2/";
    final String uploadFileName = "Group4.db";
    final String downloadFilePath = "/sdcard/CSE535_ASSIGNMENT2_EXTRA/";

    String upLoadServerUri = "https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php";

    long lastSaved = System.currentTimeMillis();
    int serverResponseCode = 0;
    Sensor accelerometer;
    SensorManager sensorManager;
    DataBaseHelper myDB;
    Boolean down = false;

    EditText IDText;
    EditText NameText;
    EditText ageText;
    String[] gender = new String[1];
    String tName = new String();

    boolean T = false;
    public GraphView graphView;
    public float newValues[];
    public float newValues2[];
    public float newValues3[];
    public Timer updateTimer;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        myDB = new DataBaseHelper(this, uploadFilePath);

        float[] values = new float[]{};
        float[] values2 = new float[]{};
        float[] values3 = new float[]{};
        String[] vertical_axis = new String[]{"3000", "2000", "1000", "0"};
        String[] horizontal_axis = new String[]{"0", "1000", "2000", "3000", "4000"};
        graphView = new GraphView(this, values, values2, values3, "", horizontal_axis, vertical_axis, GraphView.LINE);
        graphView.setScaleX(1.0f);
        graphView.setScaleY(1.0f);

        newValues = new float[10];
        newValues2 = new float[10];
        newValues3 = new float[10];

        final LinearLayout linear1 = new LinearLayout(this);
        final LinearLayout linear2 = new LinearLayout(this);
        final LinearLayout linear3 = new LinearLayout(this);
        final LinearLayout linear4 = new LinearLayout(this);
        final LinearLayout linear5 = new LinearLayout(this);

        linear1.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linear2.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lview2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linear1.addView(linear2);
        linear3.setOrientation(LinearLayout.HORIZONTAL);
        linear4.setOrientation(LinearLayout.HORIZONTAL);
        linear5.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lview3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear3);
        linear4.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lview4 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear4);

        setContentView(linear1, linLayoutParam);
        ViewGroup.LayoutParams lview5 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear5);
        linear5.setGravity(Gravity.CENTER);

        //Layout 2
        //Patient Information
        TextView patientID = new TextView(this);
        patientID.setText("Patient ID: ");
        patientID.setTextSize(17);
        patientID.setTextColor(Color.BLACK);

        IDText = new EditText(this);
        IDText.setEms(2);
        IDText.setTextSize(15);
        IDText.setHint("ID");


        TextView patientName = new TextView(this);
        patientName.setText("Name: ");
        patientName.setTextColor(Color.BLACK);
        patientName.setTextSize(17);

        NameText = new EditText(this);
        NameText.setEms(5);
        NameText.setTextSize(15);
        NameText.setHint("Name");

        patientID.setLayoutParams(lview2);
        linear2.addView(patientID);

        IDText.setLayoutParams(lview2);
        linear2.addView(IDText);

        patientName.setLayoutParams(lview2);
        linear2.addView(patientName);

        NameText.setLayoutParams(lview2);
        linear2.addView(NameText);

        //Layout 3
        TextView age = new TextView(this);
        age.setText("Age: ");
        age.setTextSize(17);

        ageText = new EditText(this);
        ageText.setTextSize(15);
        ageText.setEms(3);
        ageText.setHint("Age");
        ageText.setTextColor(Color.BLACK);

        TextView sexText = new TextView(this);
        sexText.setText("Sex:");
        sexText.setTextSize(17);
        sexText.setTextColor(Color.BLACK);

        age.setLayoutParams(lview3);
        linear3.addView(age);

        ageText.setLayoutParams(lview3);
        linear3.addView(ageText);

        sexText.setLayoutParams(lview3);
        linear3.addView(sexText);

        final RadioButton male = new RadioButton(this);
        male.setText("Male ");
        male.setTextSize(16);
        male.setLayoutParams(lview3);
        linear3.addView(male);

        final RadioButton female = new RadioButton(this);
        female.setText("Female ");
        female.setTextSize(16);
        female.setLayoutParams(lview3);
        linear3.addView(female);

        male.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gender[0] = "male";
                if (female.isChecked() == true)
                    female.setChecked(false);
            }
        });

        female.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gender[0] = "female";
                if (male.isChecked() == true)
                    male.setChecked(false);
            }
        });

        //Axis Legend and buttons
        final TextView axisText = new TextView(this);
        axisText.setText("Axis : ");
        axisText.setTextColor(Color.BLACK);
        axisText.setTextSize(17);
        axisText.setLayoutParams(lview5);
        linear5.addView(axisText);

        final TextView x = new TextView(this);
        x.setText("  X  ");
        x.setTextColor(Color.RED);
        x.setTextSize(16);
        x.setLayoutParams(lview5);
        linear5.addView(x);

        final TextView y = new TextView(this);
        y.setText("  Y  ");
        y.setTextColor(Color.GREEN);
        y.setTextSize(16);
        y.setLayoutParams(lview5);
        linear5.addView(y);

        final TextView z = new TextView(this);
        z.setText("  Z ");
        z.setTextColor(Color.BLUE);
        z.setTextSize(16);
        z.setLayoutParams(lview5);
        linear5.addView(z);

        //START button
        final Button start = new Button(this);
        start.setText("START");
        start.setWidth(100);
        start.setHeight(10);

        //STOP BUTTON
        Button stop = new Button(this);
        stop.setText("STOP");
        stop.setWidth(100);
        stop.setHeight(10);

        //UPLOAD button
        final Button upload = new Button(this);
        upload.setText("UPLOAD");
        upload.setWidth(100);
        upload.setHeight(10);

        //DOWNLOAD BUTTON
        final Button download = new Button(this);
        download.setText("DOWNLOAD");
        //download.setWidth(100);
        //download.setHeight(10);

        //CREATE DB Button
        Button dbButton= new Button(this);
        dbButton.setText("CREATE ENTRY");

        linear4.addView(dbButton, lview4);
        linear4.addView(start, lview4);
        linear4.addView(stop, lview4);
        linear5.addView(upload, lview5);
        linear5.addView(download, lview5);

        //START onClick
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setEnabled(false);
                download.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Boolean result = checkDetails();
                if (result || down) {
                    if(down){                                                                       //If Download button was pressed
                        tName = "";
                        start.setEnabled(false);
                        makeInterval(tName);                                                        //fetch last 10 seconds and display
                        graphView.invalidate();
                        graphView.setValues1(newValues);
                        graphView.setValues2(newValues2);
                        graphView.setValues3(newValues3);
                    }
                    else {                                                                          //If Download was not pressed, display a continuous graph
                        tName = NameText.getText().toString().replaceAll(" ", "") + "_" + IDText.getText().toString() + "_" + ageText.getText().toString() + "_" + gender[0];
                        if (!myDB.isTableExists(tName))
                            Toast.makeText(getApplicationContext(), "Please create an entry for this patient before starting!", Toast.LENGTH_LONG).show();
                        else {
                            if (myDB.countTableEntry() < 10)
                                Toast.makeText(getApplicationContext(), "Not enough data in the database. Please wait for atleast 10 seconds!", Toast.LENGTH_SHORT).show();
                            else {
                                start.setEnabled(false);
                                makeInterval(tName);
                                graphView.invalidate();
                                graphView.setValues1(newValues);
                                graphView.setValues2(newValues2);
                                graphView.setValues3(newValues3);
                                initialiseTimer();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter all of the fields! One or more fields are missing!", Toast.LENGTH_LONG).show();
                }

            }
        });

        //STOP onClick
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (start.isEnabled() == false) {
                    start.setEnabled(true);
                    if(!down) {
                        destroyTimer();
                    }
                    float[] neValues = new float[]{};
                    graphView.invalidate();
                    graphView.setValues1(neValues);
                    graphView.setValues2(neValues);
                    graphView.setValues3(neValues);

                }
                upload.setEnabled(true);
                download.setEnabled(true);

            }
        });

        //CREATE DB onClick
        dbButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NameText.getText().toString().matches(".*\\d+.*")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter only letters in Name details!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(IDText.getText().toString().matches(".*[a-z].*") || ageText.getText().toString().matches(".*[a-z].*")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter only numbers in Age and ID details!", Toast.LENGTH_LONG).show();
                    return;
                }
                down = false;
                boolean result = checkDetails();
                if (result) {

                    if (start.isEnabled() == false) {       //if the graph is in running state

                        Toast.makeText(getApplicationContext(),
                                "Please press the Stop button first and then enter new patient details!", Toast.LENGTH_LONG).show();
                    } else {
                        myDB.setTableName(IDText.getText().toString(), NameText.getText().toString().replaceAll(" ", ""), ageText.getText().toString(), gender[0]);
                        db = myDB.onCreat(db);
                        System.out.println(myDB.getTableName());
                        Toast.makeText(getApplicationContext(), "Database created.. Now tap RUN", Toast.LENGTH_SHORT).show();
                        if (T == false)
                            T = true;           //T==true when a database table exists
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all of the fields! One or more fields are missing!", Toast.LENGTH_LONG).show();
                }
            }

        });

        //UPLOAD onClick
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        String path = uploadFilePath + "" + uploadFileName;
                        Log.d("dbpath upload",path);
                        new UploadToServer().execute(path);
                    }
                }).start();
            }
        });

        //DOWNLOAD onClick
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down = true;
                new Thread(new Runnable() {
                    public void run() {
                        final DTask dTask = new DTask(MainActivity.this);
                        dTask.execute("https://impact.asu.edu/CSE535Spring17Folder/Group4.db");
                    }
                }).start();
            }
        });
        linear1.addView(graphView);
    }

    public void makeInterval(String tname) {
        if(!down){  //display continuous data from accelerometer
            Cursor Allrecords = myDB.getdata();
            Allrecords.moveToPosition(Allrecords.getCount() - 10);
            for (int i = 0; i < 10; i++) {
                newValues[i] = Allrecords.getFloat(0);
                newValues2[i] = Allrecords.getFloat(1);
                newValues3[i] = Allrecords.getFloat(2);
                Allrecords.moveToPosition(Allrecords.getCount() - 1 - i);
            }
        }
        else{       //after download is clicked, extract only last 10 seconds and display
            myDB = new DataBaseHelper(this, downloadFilePath);
            myDB.TableName = myDB.retrieve_Table_name();
            Log.i("uploadFile","Database name: " + myDB.TableName);
            Cursor Allrecords = myDB.getdata();
            //Allrecords.moveToPosition(val++);
            Allrecords.moveToPosition(Allrecords.getCount() - 10);
            //Allrecords.moveToFirst();
            for (int i = 0; i < 10; i++) {
                newValues[i] = Allrecords.getFloat(0);
                newValues2[i] = Allrecords.getFloat(1);
                newValues3[i] = Allrecords.getFloat(2);
                Allrecords.moveToPosition(Allrecords.getCount() - 1 - i);
            }
        }
    }


    public void initialiseTimer() {
        //initialise timer to generate data every second
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
                                                 @Override
                                                 public void run() {
                                                     makeInterval(tName);
                                                     runOnUiThread(new Runnable() {
                                                         public void run() {
                                                             graphView.invalidate();
                                                             graphView.setValues1(newValues);
                                                             graphView.setValues2(newValues2);
                                                             graphView.setValues3(newValues3);
                                                         }
                                                     });
                                                 }

                                             },
                1000,
                1000);

    }


    public void destroyTimer() {
        updateTimer.cancel();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(final SensorEvent event) {
        final String Table = myDB.getTableName();
        if (T == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((System.currentTimeMillis() - lastSaved) > 999) {
                        lastSaved = System.currentTimeMillis();
                        myDB.insertData(db, Table, event, event.values[0], event.values[1], event.values[2]);
                    }
                }
            },1000);
        }
    }

    public boolean checkDetails() {
        if(IDText.getText().toString().matches("") || NameText.getText().toString().matches("") || ageText.getText().toString().matches("") || (gender[0]==null))
            return false;
        else
            return true;

    }


    /******** DOWNLOAD TASK **********/

    private class DTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public DTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {

            InputStream input = null;
            OutputStream output = null;
            HttpsURLConnection connection = null;
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // Not implemented
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // Not implemented
                }
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");

                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpsURLConnection) url.openConnection();
                input = connection.getInputStream();
                output = new FileOutputStream(downloadFilePath + uploadFileName );
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                connection.connect();
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    output.write(data, 0, count);

                }
                System.out.print("Done");
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) {
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Downloading Complete! Now Tap Start to view graph", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();
                        output.close();
                    }
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    /******** UPLOAD TASK **********/

private class  UploadToServer extends AsyncTask<String,Void,Boolean>
{

    @Override
    protected Boolean doInBackground(String... params)
    {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        HttpURLConnection connection;
        InputStream input = null;
        DataOutputStream outputStream = null;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024;

        TrustManager[] tManager = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        } };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, tManager, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        URL serverUrl = null;
        try {
            serverUrl = new URL("https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php");

            File dbFile = new File(params[0]);
            connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            connection.setRequestProperty("uploaded_file", dbFile.getName());

            outputStream = new DataOutputStream(connection.getOutputStream());

            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name='uploaded_file';fileName='" +dbFile.getName()+"'" + lineEnd);
            outputStream.writeBytes(lineEnd);

            FileInputStream fileInputStream = new FileInputStream(dbFile);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                try {
                    outputStream.write(buffer);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                total.append(line).append('\n');
            }
            Log.i("uploadFile","Server Response is: " + total.toString() + ": " + serverResponseCode);

            //close the streams
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();

            //response code 200 corresponds to server status OK

            if (connection.getResponseCode() == 200) {

                return true;
            } else {

                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
        {
            Toast.makeText(MainActivity.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
            Log.d("upload","success");
        }
        else
        {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            Log.d("upload","error");
        }
    }
}
}