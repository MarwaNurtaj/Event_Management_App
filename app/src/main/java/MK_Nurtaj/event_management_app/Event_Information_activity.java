package MK_Nurtaj.event_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Event_Information_activity extends AppCompatActivity {

    private EditText etName, etPlace, etDateTime, etCapacity, etBudget, etEmail, etPhone, etDescription;
    private RadioButton rdIndoor, rdOutdoor, rdOnline;

    private Button cancelBtn,shareBtn,saveBtn;
    private String existingKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);

        etName = findViewById(R.id.etName);
        etPlace = findViewById(R.id.etPlace);
        etDateTime = findViewById(R.id.etDateTime);
        etCapacity = findViewById(R.id.etCapacity);
        etBudget = findViewById(R.id.etBudget);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);

        rdIndoor = findViewById(R.id.rdIndoor);
        rdOutdoor = findViewById(R.id.rdOutdoor);
        rdOnline = findViewById(R.id.rdOnline);

        cancelBtn = findViewById(R.id.cancelBtn);
        shareBtn = findViewById(R.id.shareBtn);
        saveBtn = findViewById(R.id.saveBtn);

        Intent i = getIntent();
        existingKey = i.getStringExtra("EventKey");
        if (existingKey != null && !existingKey.isEmpty()) {
            initializeFormWithExistingData(existingKey);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Event_Information_activity.this, "Share btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
        private void initializeFormWithExistingData(String eventKey){

            String value = Util.getInstance().getValueByKey(this, eventKey);
            System.out.println("Key: " + eventKey + "; Value: " + value);

            if (value != null) {
                String[] fieldValues = value.split("-::-");
                String name = fieldValues[0];
                String place = fieldValues[1];
                String dateTime = fieldValues[2];
                String capacity = fieldValues[3];
                String budget = fieldValues[4];
                String email = fieldValues[5];
                String phone = fieldValues[6];
                String description = fieldValues[7];
                String eventType = fieldValues[8];

                etName.setText(name);
                etPlace.setText(place);
                etDateTime.setText(dateTime);
                etCapacity.setText(capacity);
                etBudget.setText(budget);
                etEmail.setText(email);
                etPhone.setText(phone);
                etDescription.setText(description);

                if (eventType.equalsIgnoreCase("INDOOR")) {
                    rdIndoor.setChecked(true);
                } else if (eventType.equalsIgnoreCase("OUTDOOR")) {
                    rdOutdoor.setChecked(true);
                } else if (eventType.equalsIgnoreCase("ONLINE")) {
                    rdOnline.setChecked(true);
                }
            }
        }

    private void saveData() {
        String name=etName.getText().toString();
        String place=etPlace.getText().toString();
        String datetime=etDateTime.getText().toString();
        String capacity=etCapacity.getText().toString();
        String budget=etBudget.getText().toString();
        String email=etEmail.getText().toString();
        String phone=etPhone.getText().toString();
        String description=etDescription.getText().toString();
        boolean isIndoorChecked = rdIndoor.isChecked();
        boolean isOutdoorChecked = rdOutdoor.isChecked();
        boolean isOnlineChecked = rdOnline.isChecked();

        System.out.println("Evnt Name"+ name);
        System.out.println("Evnt Place"+ place);
        System.out.println("Evnt Datetime"+ datetime);
        System.out.println("Evnt Capacity"+ capacity);
        System.out.println("Evnt Budget"+ budget);
        System.out.println("Evnt Email"+ email);
        System.out.println("Evnt Phone"+ phone);
        System.out.println("Evnt Description"+ description);
        System.out.println("Evnt Indoor"+ isIndoorChecked);
        System.out.println("Evnt Outdoor"+ isOutdoorChecked);
        System.out.println("Evnt Online"+ isOnlineChecked);
        //
        String eventType = isIndoorChecked ? "Indoor" : isOutdoorChecked ? "Outdoor" : isOnlineChecked ? "Online" : "Undefined";

        String key = name + "-::-" + datetime + "-::-" + email;
        String value =name + "-::-" + place + "-::-" + datetime + "-::-" + capacity + "-::-" + budget + "-::-" + email + "-::-" + phone + "-::-" + description + "-::-" + eventType;

        if (existingKey != null) {
                key = existingKey;
        }

        Util.getInstance().setKeyValue(this, key, value);
        System.out.println("Data Saved Success");

        Toast.makeText(this, "Event Information has been Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}