package com.hcodestudio.studentrecordsystem.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.hcodestudio.studentrecordsystem.data.RecordContract.BASE_PATH;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.CONTENT_AUTHORITY;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RECORDS_CONTENT_URI;
import static com.hcodestudio.studentrecordsystem.data.RecordContract.RecordsEntry.TABLE_NAME;

/**
 * Created by hassan on 11/27/2017.
 */

public class RecordProvider extends ContentProvider {

    // Constant to identify the requested operation
    private static final int RECORD = 1;
    private static final int RECORD_ID = 2;

    public static final String CONTENT_ITEM_TYPE = "Record";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public SQLiteDatabase database;
    private RecordDBOpenHelper helper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, BASE_PATH, RECORD);
        uriMatcher.addURI(CONTENT_AUTHORITY, BASE_PATH + "/#", RECORD_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        helper = new RecordDBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        final SQLiteDatabase db = helper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case RECORD_ID:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECORD:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        RECORD_ID + " DESC");
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        Uri returnUri;
        long id;

        switch (sUriMatcher.match(uri)) {
            case RECORD_ID:
                id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECORDS_CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case RECORD:
                id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECORDS_CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // final SQLiteDatabase db = helper.getWritableDatabase();

        // return db.delete(TABLE_NOTES, selection, selectionArgs);

        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted = 0; // starts as 0

        // COMPLETED (2) Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {

            case RECORD:
                tasksDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            // Handle the single item case, recognized by the ID included in the URI path
            case RECORD_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return tasksDeleted;
    }

//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        return database.delete(TABLE_NOTES, selection, selectionArgs);
//    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return database.update(TABLE_NAME,
          //      contentValues, selection, selectionArgs);
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted = 0; // starts as 0

        // COMPLETED (2) Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {

            case RECORD:
                tasksDeleted = db.update(TABLE_NAME,contentValues, selection, selectionArgs);
                break;
            // Handle the single item case, recognized by the ID included in the URI path
            case RECORD_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.update(TABLE_NAME, contentValues, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return tasksDeleted;
    }

    }


