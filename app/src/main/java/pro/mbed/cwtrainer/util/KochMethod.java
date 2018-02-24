package pro.mbed.cwtrainer.util;

import java.util.Random;

/**
 * Created by m_chichi on 22.01.2018.
 */

public class KochMethod {
    private int lesson;
    Random rnd;
    private String exerciseStr;
    private String userRawStr;


    private String lessonsOrder = "KMURESNAPTLWI.JZ=FOY,VG5/Q92H38B?47C1D60X";

    public KochMethod () {
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
    public void generateExercise () {
        String characters = getCharactersInLesson();
        char[] charactersArray = characters.toCharArray();
        StringBuilder str = new StringBuilder();
        str.append("    ");
        for (int i=0; i < 5; i++) {
            for (int j=0; j < 5; j++) {
                str.append(charactersArray[rnd.nextInt(lesson+1)]);
            }
            str.append(' ');
        }
        exerciseStr = str.toString();
    }

    @Override
    public String toString() {
        if (exerciseStr == null) exerciseStr= "";
        return exerciseStr;
    }

    /*
        * Evaluate result. Compare generated array and users array...
        */
    public void checkResult () {

    }

    public String getUserRawStr() {
        if (userRawStr == null) userRawStr = "";
        return userRawStr;
    }

    public void setUserRawStr (String str) {
        userRawStr = str;
    }

    public int getError () {
        return apply(exerciseStr, userRawStr);
    }

    private int apply(CharSequence first, CharSequence second) {
        int distance = 0;
        int secondInd = 0;
        int firstInd;

        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }

        int fLen = first.length();
        int sLen = second.length();

        if (fLen == 0) return sLen;
        if (sLen == 0) return fLen;

        if (first.equals(second)) return 0;


        if (fLen > sLen) {
            CharSequence temp = first;
            first = second;
            second = temp;

            int tmp = fLen;
            fLen = sLen;
            sLen = tmp;
        }

        for (firstInd = 0; (secondInd < sLen); ) {
            if (firstInd < fLen) {
                if (first.charAt(firstInd) == second.charAt(secondInd)) {
                    firstInd++;
                    secondInd++;
                } else {
                    secondInd++;
                    distance++;
                    if (firstInd + 1 < fLen && secondInd < sLen) {
                        if (first.charAt(firstInd + 1) == second.charAt(secondInd)) {
                            firstInd++;
                        }
                    }
                }
            } else {
                distance++;
                secondInd ++;
            }
        }
//        if (fLen != firstInd && fLen != sLen) { distance += (fLen - firstInd); }
//        return distance + (sLen - secondInd);
//        if (extraAtTheEnd) {
//            distance += ((fLen - firstInd) + (sLen - secondInd));
//        }
        return distance;
    }

    /**
     * * return set of characters as a string
     *
     */
    public String getCharactersInLesson () {
        return this.lessonsOrder.substring(0, lesson+1);
    }



}
