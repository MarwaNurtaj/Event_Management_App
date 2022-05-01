package MK_Nurtaj.event_management_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button createNewBtn,historyBtn,exitBtn;
    private ListView lvEvents;
    private ArrayList<Event> events;
    private CustomEventAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        createNewBtn = findViewById(R.id.createNewBtn);
        historyBtn = findViewById(R.id.historyBtn);
        exitBtn = findViewById(R.id.exitBtn);

        lvEvents = findViewById(R.id.lvEvents);

        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,Event_Information_activity.class);
                startActivity(intent);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "History Button Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,   My_Attendance_Activity.class);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadData();


    }
    private void loadData(){
        events = new ArrayList<>();
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0) {
            return;
        }
        //events = new Event[rows.getCount()];
        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String eventData = rows.getString(1);
            String[] fieldValues = eventData.split("-::-");

            String name = fieldValues[0];
            String place = fieldValues[1];
            String dateTime = fieldValues[2];
            String capacity = fieldValues[3];
            String budget = fieldValues[4];
            String email = fieldValues[5];
            String phone = fieldValues[6];
            String description = fieldValues[7];
            String eventType = fieldValues[8];

            Event e = new Event(key, name, place, dateTime, capacity, budget, email, phone, description, eventType);
            events.add(e);
        }
        db.close();
        adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);

        //handle the click on an event-list item
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                System.out.println(position);

                Intent i = new Intent(MainActivity.this, Event_Information_activity.class);
                i.putExtra("EventKey", events.get(position).key);
                startActivity(i);
            }
        });


        lvEvents.setOnItemLongClickListener((parent, view, position, id) -> {
        String message = "Do you want to delete event - " + events.get(position).name + " ?";
        System.out.println(message);
        showDialog(message, "Delete Event", events.get(position).key);
        return true;
    });
    }

    private void showDialog(String message, String title, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Util.getInstance().deleteByKey(MainActivity.this, key);
                dialog.cancel();
                loadData();
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("@MainActivity.onRestart");
        loadData();
    }
}