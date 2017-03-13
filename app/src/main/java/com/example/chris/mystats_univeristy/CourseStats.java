package com.example.chris.mystats_univeristy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import Data.Course;
import MPChart.UniversityStatsChartMaker;

public class CourseStats extends AppCompatActivity  {

    TabLayout tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stats);

        //This is the course object
        Course course = getIntent().getParcelableExtra("chosenCourse");

        //setContentView(R.layout.fragment_employ_stats);
        //BarChart chart1 = (BarChart) findViewById(R.id.bar1);
        //This is what we should be able to do but can't as I said last week these charts need to be able to deal with
        //null values for some statistics and haven't been tested properly as yet
        //chart1.setData(UniversityStatsChartMaker.getAvgSalaryFourtyMonths(course, chart1));
        //chart1.notifyDataSetChanged();
        //chart1.invalidate();


        //Log.d("etst" , String.valueOf(chart1.getYMax()));
        setContentView(R.layout.activity_course_stats);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        vp.setAdapter(new Adapter(getSupportFragmentManager(), this, course));

        tb = (TabLayout) findViewById(R.id.tabLayout);
        tb.setupWithViewPager(vp);

    }
}
