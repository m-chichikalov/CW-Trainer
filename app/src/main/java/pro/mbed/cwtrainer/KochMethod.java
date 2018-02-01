package pro.mbed.cwtrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

/**
 * Created by m_chichi on 22.01.2018.
 */

public class KochMethod {
    private int lesson;
    Random rnd;
    char[][] exerciseArray;

    private String lessonsOrder = "KMURESNAPTLWI.JZ=FOY,VG5/Q92H38B?47C1D60X";

    public KochMethod () {

        exerciseArray = new char[5][6]; //HARDCODE
        this.lesson = 6; // HARDCODE
        rnd = new Random();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        lesson = sharedPref.getInt(asdfa afs  df, 0);

    }

    /*
    * I'm gonna use this method for changing lesson.
    * (onChangingSettings or user change lesson manually or
    *  after completed privies lesson )
    */
    public void setLesson (int lesson) {
        this.lesson = lesson;
    }

    /*
    * Generate array of characters for exercise
    * (should I specify length of this array??? )
    */
    public void generateExercise (CwPlayer cw) {
        String characters = getCharactersInLesson();
        char[] charactersArray = characters.toCharArray();

//        char[][] exerciseArray = new char[5][6]; //HARDCODE
        for (int i=0; i < 5; i++) {
            for (int j=0; j < 5; j++) {
                exerciseArray[i][j] = charactersArray[rnd.nextInt(lesson+1)];
                cw.feed( (byte) exerciseArray[i][j]);
            }
            exerciseArray[i][5] = ' ';
            cw.feed( (byte) ' ');
        }
    }

    public char[][] getExerciseArray() {
        return exerciseArray;
    }

    /*
    * Evaluate result. Compare generated array and users array...
    */
    public void checkResult (char[][] underTestArray) {

    }

    public void getError () {

    }

    /**
     * * return set of characters as a string
     *
     */
    public String getCharactersInLesson () {
        return this.lessonsOrder.substring(0, lesson+1);
    }



}
