package com.example.tonghung.contentproviderdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnContacts, btnCalllog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnContacts = (Button) findViewById(R.id.btn_contacts);
        btnCalllog = (Button) findViewById(R.id.buttonCalllog);
        listView = (ListView) findViewById(R.id.lv);

        readContacts();
        showCallLogs();
    }

    private void readContacts() {
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();

                Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    list.add(name);
                    c.moveToNext();
                }
                c.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, list);

                listView.setAdapter(adapter);
            }
        });
    }

    private void showCallLogs() {
        btnCalllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] projection = new String[]{
                        CallLog.Calls.DATE,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.DURATION
                };

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) !=
                        PackageManager
                        .PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.DURATION +
                        "<?", new String[]{"30"}, CallLog.Calls.DATE + " Asc");

                List<String> listCalllogs = new ArrayList<>();
                while(cursor.moveToNext()){
                    String value = "";
                    for(int i=0; i< cursor.getColumnCount(); i++){
                        value+= cursor.getString(i) + " - ";
                    }
                    listCalllogs.add(value);
                }

                cursor.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout
                        .simple_list_item_1, listCalllogs);
                listView.setAdapter(adapter);
            }
        });
    }
}
