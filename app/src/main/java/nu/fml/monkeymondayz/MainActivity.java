package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Toast;



public class MainActivity extends Activity {
    // more efficient than HashMap for mapping integers to objects
    SparseArray<Group> groups = new SparseArray<Group>();

//    MediaPlayer mySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {

                PackageManager p = getPackageManager();
                boolean hasAccelerometer = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
                boolean hasGyroscope = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
                boolean hasLight = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
                boolean hasMicrophone = p.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
                boolean hasGPSLocation = p.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
                boolean hasNetworkLocation = p.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);

                SharedPreferences settings = getSharedPreferences(Constants.AVAILABLE_SENSORS, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putBoolean(Constants.PREF_ACCELEROMETER, hasAccelerometer);
                editor.putBoolean(Constants.PREF_GYROSCOPE, hasGyroscope);
                editor.putBoolean(Constants.PREF_LIGHT, hasLight);
                editor.putBoolean(Constants.PREF_MICROPHONE, hasMicrophone);
                editor.putBoolean(Constants.PREF_LOCATION_GPS, hasGPSLocation);
                editor.putBoolean(Constants.PREF_LOCATION_NETWORK, hasNetworkLocation);

                editor.commit();
                return null;
            }

            public void onPostExecute(Void result) {
                handler.post(new Runnable() {
                    public void run() {
                        setContentView(R.layout.activity_main);
                        setupList();
                    }
                });
            }

        }.execute();




    }

    private void startMonkey() {
        Intent monkey = new Intent(this, MonkeyFinderService.class);
        startService(monkey);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Stoppar notificationen, kan innebära problems
    public static void CancelNotification(Context ctx) {
        NotificationManager notifManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CancelNotification(this);
    }

    //LISTVIEW
    private void setupList() {
        final SharedPreferences pref = getSharedPreferences("tell",0);
        boolean showAlert = pref.getBoolean("show",true);

        createData();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,
                groups);
        listView.setAdapter(adapter);

        if (showAlert) {
            View checkBoxView = View.inflate(this, R.layout.splashmessage, null);
            CheckBox box = (CheckBox) checkBoxView.findViewById(R.id.checkDont);
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor e = pref.edit();
                    e.putBoolean("show",!isChecked);
                    e.commit();
                }
            });

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage(getString(R.string.str_guide));
            b.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startMonkey();
                }
            });
            b.setView(checkBoxView);

            b.create().show();
        }else{
            startMonkey();
        }
    }

    public void createData() {

        Group groupAsfalt = new Group("Asfalt");
        Group groupSkog = new Group("Skog");
        Group groupVatten = new Group("Vatten");

        groupAsfalt.children.add("Asfaltsapa");
        groupAsfalt.children.add("Vanligapa");
        groupAsfalt.children.add("Marknäraapa");
        groups.append(0, groupAsfalt);


        groupSkog.children.add("Oskarsapa");
        groupSkog.children.add("Grönapa");
        groupSkog.children.add("Grönareapa");
        groupSkog.children.add("Grönasteapa");
        groups.append(1, groupSkog);


        groupVatten.children.add("Vattenapa");
        groupVatten.children.add("Koiapa");
        groupVatten.children.add("Gäddapa");
        groups.append(2, groupVatten);
        }


    }
