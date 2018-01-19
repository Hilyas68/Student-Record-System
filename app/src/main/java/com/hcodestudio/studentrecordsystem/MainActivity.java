package com.hcodestudio.studentrecordsystem;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import me.toptas.fancyshowcase.FancyShowCaseView;

import static com.hcodestudio.studentrecordsystem.data.RecordContract.RECORDS_CONTENT_URI;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.ROLL_NUMBER;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.STUDENT_MARK;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.STUDENT_NAME;

public class MainActivity extends AppCompatActivity {

    TextInputEditText rollno, name, marks;
    FloatingActionButton add, delete, edit, update, view;
    //String recordFilter;
    Uri uri = RECORDS_CONTENT_URI;
    Cursor cursor;
    ShowcaseView.Builder showcaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollno = (TextInputEditText) findViewById(R.id.rollno);
        name = (TextInputEditText) findViewById(R.id.name);
        marks = (TextInputEditText) findViewById(R.id.mark);

        add = (FloatingActionButton) findViewById(R.id.add);
        delete = (FloatingActionButton) findViewById(R.id.delete);
        edit = (FloatingActionButton) findViewById(R.id.edit);
        update = (FloatingActionButton) findViewById(R.id.update);
        view = (FloatingActionButton) findViewById(R.id.view);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.container);
        constraintLayout.invalidate();

        final FloatingActionsMenu actionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_action);
        //actionsMenu.bringToFront();
//
        final RelativeLayout obstructorView = (RelativeLayout) findViewById(R.id.trans);
        obstructorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (obstructorView.getVisibility() == View.VISIBLE) {
                    obstructorView.bringToFront();
                    actionsMenu.collapse();

                    return true;
                }
                return false;
            }
        });

           actionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
               @Override
               public void onMenuExpanded() {
                   if (obstructorView.getVisibility() == View.INVISIBLE)
                       obstructorView.setVisibility(View.VISIBLE);
                       obstructorView.bringToFront();
                       actionsMenu.bringToFront();
               }

               @Override
               public void onMenuCollapsed() {
                   if (obstructorView.getVisibility() == View.VISIBLE)
                       obstructorView.setVisibility(View.INVISIBLE);
               }
           });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsMenu.collapse();
                cursor = getContentResolver().query(uri,
                        null,
                        ROLL_NUMBER + " = " + DatabaseUtils.sqlEscapeString(rollno.getText().toString()),
                        null,
                        null);

                if(!rollno.getText().toString().trim().isEmpty() && !name.getText().toString().trim().isEmpty()
                        && !marks.getText().toString().trim().isEmpty()  && cursor.getCount() == 0 ){

                    saveReord(rollno.getText().toString().trim(),
                            name.getText().toString().trim(),
                            marks.getText().toString().trim());

                    Snackbar.make(view, "Record Added Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                }else if(cursor.getCount() == 0 ) {
                        Snackbar.make(view, "Please fill the Blank(s)", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        Snackbar.make(view, "Roll no already Exist!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsMenu.collapse();
                cursor = getContentResolver().query(uri,
                        null,
                        ROLL_NUMBER + " = " + DatabaseUtils.sqlEscapeString(rollno.getText().toString()),
                        null,
                        null);

                if (!rollno.getText().toString().trim().isEmpty() && cursor.getCount() != 0) {
                    deleteRecord(rollno.getText().toString().trim());
                    Snackbar.make(view, "Record deleted Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                }else {
                    Snackbar.make(view, "Wrong Roll Number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsMenu.collapse();

                    Uri uri = RECORDS_CONTENT_URI;

                     cursor = getContentResolver().query(uri,
                            null,
                           ROLL_NUMBER + " = " + DatabaseUtils.sqlEscapeString(rollno.getText().toString()),
                            null,
                            null);

                if (!rollno.getText().toString().trim().isEmpty() && cursor.getCount() != 0 && cursor.moveToFirst()) {

                    rollno.setText(cursor.getString(cursor.getColumnIndex(ROLL_NUMBER)));
                    name.setText(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                    marks.setText(cursor.getString(cursor.getColumnIndex(STUDENT_MARK)));

//                    Snackbar.make(view, "Student Name Added Successfully", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(view, "Wrong Roll No", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsMenu.collapse();
                cursor = getContentResolver().query(uri,
                        null,
                        ROLL_NUMBER + " = " + DatabaseUtils.sqlEscapeString(rollno.getText().toString()),
                        null,
                        null);

                if(!rollno.getText().toString().trim().isEmpty() && !name.getText().toString().trim().isEmpty()
                        && !marks.getText().toString().trim().isEmpty()  && cursor.getCount() != 0 ){

                    updateRecord(rollno.getText().toString().trim(),
                            name.getText().toString().trim(),
                            marks.getText().toString().trim(), rollno.getText().toString().trim());

                    Snackbar.make(view, "Record Updated Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                }else {
                    Snackbar.make(view, "Please fill the Blank(s)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsMenu.collapse();
                cursor = getContentResolver().query(uri,
                        null,
                        null,
                        null,
                        ROLL_NUMBER + " ASC");

                if(cursor.getCount() == 0){
                    showMessage("Error" , "No Record Found");
                    return;
                }

                StringBuffer stringBuffer = new StringBuffer();
                while (cursor.moveToNext()){
                    stringBuffer.append("Roll no: " + cursor.getString(1) + "\n");
                    stringBuffer.append("Name: " + cursor.getString(2) + "\n");
                    stringBuffer.append("Marks: " + cursor.getString(3) + "\n");
                    stringBuffer.append("\n");
                }

                showMessage("Student Details", stringBuffer.toString());

            }
        });


        //Make code run once
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean(getString(R.string.first_run), false)) {

            new FancyShowCaseView.Builder(this)
                    .focusOn(add)
                    .title("Start Recording:\n fill in the student records, then click on the plus button to " +
                            "Add, Modify, Delete, Update and View All saved records. \n\n Enjoy!!!")
                    .titleSize(20,1)
                    .build().show();


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.first_run), true);
            editor.apply();
        }

    }//close of OnCreate()

    private void rePositioningShowcaseViewBtn() {
        // Implementation
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
// This aligns button to the bottom left side of screen
        lps.addRule(RelativeLayout.CENTER_IN_PARENT);
        //lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // Set margins to the button, we add 16dp margins here
        int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        Target target = new ViewTarget(R.id.multiple_action, this);
        showcaseView =  new ShowcaseView.Builder(this, false);
        showcaseView.setTarget(target)
                .setContentTitle("Start Recording")
                .setContentText("You have to fill in the student records, then click on the plus button to " +
                        "Add  Modify Delete  Update and View All saved records. \n\n Enjoy!!! ")
                .setStyle(1)
                .hideOnTouchOutside()
                .build()
                .setButtonPosition(lps);
    }




    private void showMessage(String title, String s) {
        Builder builder = new Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setMessage(s);
        builder.show();
    }

    private void clearText() {
        rollno.setText(" ");
        name.setText(" ");
        marks.setText(" ");
        rollno.requestFocus();
    }

    private void saveReord(String rollno, String name, String mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROLL_NUMBER, rollno );
        contentValues.put(STUDENT_NAME, name );
        contentValues.put(STUDENT_MARK, mark );
        getContentResolver().insert(RECORDS_CONTENT_URI, contentValues);
        setResult(RESULT_OK);
    }

    private void updateRecord(String rollno, String name, String mark, String recordFilter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROLL_NUMBER, rollno );
        contentValues.put(STUDENT_NAME, name );
        contentValues.put(STUDENT_MARK, mark );

        getContentResolver().update(RECORDS_CONTENT_URI, contentValues, recordFilter, null);
        setResult(RESULT_OK);

    }

    private void deleteRecord(String recordFilter) {
        getContentResolver().delete(RECORDS_CONTENT_URI,
                recordFilter, null);
        setResult(RESULT_OK);
    }
}
