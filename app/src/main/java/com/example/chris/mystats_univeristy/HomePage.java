package com.example.chris.mystats_univeristy;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Data.CourseTypes;
import Data.DatabaseInformationQuerier;
import GPS.RadiusChecker;


public class HomePage extends MenuViewActivity  {

    private Button sim;
    private EditText searchedCourse;
    private EditText searchedLocation;
    private MenuViewActivity current = this;
    private SeekBar radiusBar;
    private TextView radiusDisplay;
    int sizeOfRadius = 0;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private void updateRadius(int progress){
        sizeOfRadius = progress*10;
        radiusDisplay.setText(String.valueOf(sizeOfRadius) + " km");
        Log.d("The current size of the rad is " , String.valueOf(sizeOfRadius));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //This creates the database querier with the database, this will need to be passed to other activities that require
         final DatabaseInformationQuerier databaseInfomationQuerier = new DatabaseInformationQuerier(database);

        radiusBar = (SeekBar) findViewById(R.id.radiusBar);
        sizeOfRadius = radiusBar.getProgress();
        radiusDisplay = (TextView) findViewById(R.id.radiusText);
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sim = (Button) findViewById(R.id.simSearch);
        searchedCourse = (EditText) findViewById(R.id.courseNameEntered);
        searchedLocation = (EditText) findViewById(R.id.locationNameEntered);
        final RadioGroup courseTypeButton = (RadioGroup) findViewById(R.id.coursetype);
        final Geocoder loc = new Geocoder(this);


        Log.d("i should be true " , String.valueOf(loc.isPresent()));
        sim.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                int selectedid = courseTypeButton.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) (findViewById(selectedid));

                CourseTypes courseType = button.getText().equals("full time courses") ? CourseTypes.FULL_TIME : CourseTypes.PART_TIME;
                Log.d("cours type ", String.valueOf(courseTypeButton.getCheckedRadioButtonId()));
                //Create the intent to start another activity
                Intent intent = new Intent(view.getContext(), SearchResults.class);
                databaseInfomationQuerier.setIntent(intent);
                databaseInfomationQuerier.setCurrent(current);
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = loc.getFromLocationName(searchedLocation.getText().toString(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses.size() >0){
                    RadiusChecker.getHitsAroundLocation(100, addresses.get(0).getLongitude()
                                                            , addresses.get(0).getLatitude()
                                                            , searchedCourse.getText().toString(),
                                                            databaseInfomationQuerier,courseType);
                }
                else {
                    databaseInfomationQuerier.getAllCoursesByCourseName(searchedCourse.getText().toString(), courseType); //need to add an option to select the course type

                }
            }
        });




    }

}
