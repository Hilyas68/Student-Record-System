package com.hcodestudio.studentrecordsystem.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hassan on 11/27/2017.
 */

public class RecordContract  {

    static final String CONTENT_AUTHORITY = "com.hcodestudio.studentrecordsystem";
    static final String BASE_PATH = "records";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri RECORDS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(BASE_PATH).build();

    public static Uri buildRecipeUriWithId(int id) {
        return RECORDS_CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
    }

    public static final class RecordsEntry implements BaseColumns {

        public static final String TABLE_NAME = "student_record";
        public static final String ROLL_NUMBER = "roll_no";
        public static final String RECORD_ID = "record_id";
        public static final String STUDENT_NAME = "student_name";
        public static final String STUDENT_MARK = "student_mark";

        public static final String[] ALL_COLUMNS =
                {ROLL_NUMBER, RECORD_ID, STUDENT_NAME, STUDENT_MARK};
    }
}
