package Data;


import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chris on 06/03/17.
 * This class handles all the extraction of data from the Firebase database with an aim of providing
 * that information to the view
 */

public class DatabaseInformationQuerier {

    DatabaseReference database; // The firebase database
    static ArrayList<Parcelable> courseList = new ArrayList<>(); //The courses found in the current query (may not be needed)

    public DatabaseInformationQuerier(DatabaseReference database) {
        this.database = database;
    }



    public ArrayList<Parcelable> getSearchResults(){
        return courseList;
    }

    /**
     * This method is used because firebase is Async, this method extracts the information needed from the
     * course object creating a course object which can then be updated with the relevant details
     *
     * @param courses - The datasnapshot containing all the information about the courses
     * @param coursetype - The type of courses that are being searched
     */
    private void collectCourses(DataSnapshot courses, CourseTypes coursetype){
        courseList.clear();
        Iterator<DataSnapshot> data = courses.getChildren().iterator();
        while(data.hasNext()){
            DataSnapshot next = data.next();
            Course course = next.getValue(Course.class);
            Log.d("showing url as a test" , next.toString());
            courseList.add(course);
            //This is where the method is needed to pass the course data to the view
        }
    }

    /**
     * This method gets all the courses in the database that match the passed in name and coursetype
     *
     * @param name  - The name of the course that is being searched for
     * @param coursetype - The course type (taken from the enum of CourseTypes)
     * @return - An DataSnapshot object containing all matches
     *
     */
    public void getAllCoursesByCourseName(String name, final CourseTypes coursetype){
                Query query =  courseNameQuery(name, coursetype);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectCourses(dataSnapshot, coursetype);
                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    /**
     * This method gets a course from the database by looking up the key passed in (id)
     * @param id - The key where the course is held in the database
     * @param coursetype - The type of course (ie part time/full time)
     */
    public void getAParticularCourseByID(String id, final CourseTypes coursetype){
        Query query =  database.child(coursetype.getDatabaseRef()).child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectCourses(dataSnapshot, coursetype);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method filters the passed in datasnapshot by a further keyname/value
     * @param datain - The datasnapshot that is to be filtered
     * @param coursetype - The type of course (ie full or part time)
     * @param keyname - The key that will filter the data
     * @param valuetobematched - The value that if matched will be allowed to be sent to the view
     */
    public void collectFilteredCourses(DataSnapshot datain, CourseTypes coursetype, String keyname, String valuetobematched){
        courseList.clear();
        Iterator<DataSnapshot> data = datain.getChildren().iterator();
        while(data.hasNext()){
            DataSnapshot next = data.next();
            if(next.child(keyname).getValue().toString().startsWith(getStartAndFinishSearchIndexes(valuetobematched, 5)[0])){
                Course course = next.getValue(Course.class);
                courseList.add(course);
                Log.d("course name " , course.TITLE);
                Log.d("match found ", " we have found a match!");
            }
            //This is where the method is needed to pass the course data to the view
        }

    }

    /**
     * This method gives flexibility to the users searches, it does this by returning start and end search
     * words which can be applied in a database search meaning that close misses (ie putting in exact starts of the word
     * but missing the tail or specialisation ) will be picked up
     * @param current - The word to be amended
     * @param importantCharacters - The number of characters at the start which are needed to get  a match
     * @return - A two piece array with the start word indexed to 0 and the end word indexed to 1
     */
    public String[] getStartAndFinishSearchIndexes(String current, int importantCharacters){
        int lengthOfString = current.length();
        String starthere = current;
        if(lengthOfString > importantCharacters){
            starthere = current.substring(0,importantCharacters);
        }
        String end = current.substring(0,lengthOfString-2 ) + current.charAt(lengthOfString-1)+1;
        String [] startend = {starthere, end};
        return startend;
    }

    /**
     * This method queries the database with the passed course name and course type, it also gives an allowence of missing the
     * end of the course name
     * @param courseName - The course to be searched
     * @param coursetype - The type of course to be searched
     * @return - A query to by applied to the database
     */
    private  Query courseNameQuery(String courseName, CourseTypes coursetype){
        String [] searchWordCritera = getStartAndFinishSearchIndexes(courseName, 5);
        return database.child(coursetype.getDatabaseRef()).orderByChild("TITLE").startAt(searchWordCritera[0]).endAt(searchWordCritera[1]);
    }

    /**
     * This method retrieves from the database all close match courses to the passed in course and university name
     * these are the packaged as course objects and sent to the view in the collect filtered courses method
     * @param courseName - The name of the course to be searched
     * @param universityName - The name of the university to search by
     * @param coursetype - The type of course searched
     */
    public void getACourseByCoursenameAndUniversityName(String courseName, final String universityName, final CourseTypes coursetype){

                Query query =  courseNameQuery(courseName, coursetype);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        collectFilteredCourses(dataSnapshot,coursetype, "NAME", universityName);
                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    }
