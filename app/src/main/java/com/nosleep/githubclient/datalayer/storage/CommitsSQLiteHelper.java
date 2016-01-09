package com.nosleep.githubclient.datalayer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sanjeev on 09/01/16.
 */
public class CommitsSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gitcache.db";
    private static final String TABLE_NAME = "commits";
    private static final int DATABASE_VERSION = 1;

    private static CommitsSQLiteHelper helper;
    private SQLiteDatabase db;

    public static synchronized CommitsSQLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new CommitsSQLiteHelper(context);
        }
        return helper;
    }

    private final String CREATE_COMMIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`sha`\tTEXT UNIQUE,\n" +
            "\t`author`\tTEXT,\n" +
            "\t`contactauthor`\tTEXT,\n" +
            "\t`committer`\tTEXT,\n" +
            "\t`contactcommitter`\tTEXT,\n" +
            "\t`commitmessage`\tTEXT,\n" +
            "\t`commitdate`\tTEXT\n" +
            ");";

    private final String DELETE_COMMIT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "";


    public CommitsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMMIT_TABLE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_COMMIT_TABLE);
        db.execSQL(CREATE_COMMIT_TABLE);
    }

    public long insert(ContentValues values) {
        if (values != null & values.size() > 0) {
            return db.insert(TABLE_NAME, null, values);
        }
        return -1l;
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        if (values != null & values.size() > 0) {
            return db.update(TABLE_NAME, values, whereClause, whereArgs);
        }
        return 0;
    }

    public int delete(String whereClause, String[] whereArgs) {
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }


}
