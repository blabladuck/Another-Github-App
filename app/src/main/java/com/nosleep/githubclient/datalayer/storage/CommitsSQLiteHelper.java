package com.nosleep.githubclient.datalayer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sanjeev on 09/01/16.
 */
class CommitsSQLiteHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "gitcache.db";
    private static final int DATABASE_VERSION = 1;

    private static CommitsSQLiteHelper helper;
    private SQLiteDatabase db;

    public static synchronized CommitsSQLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new CommitsSQLiteHelper(context);
        }
        return helper;
    }

    private final String CREATE_COMMIT_TABLE = "CREATE TABLE IF NOT EXISTS " + DataProviderContract.Commits.TABLE_NAME + " (\n" +
            "\t`" + DataProviderContract.Commits._ID + "`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`" + DataProviderContract.Commits.SHA + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.AUTHOR_NAME + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.AUTHOR_EMAIL + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.AUTHOR_AVATAR + "`\tBLOB,\n" +
            "\t`" + DataProviderContract.Commits.COMMITTER_NAME + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.COMMITTER_EMAIL + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.COMMITTER_AVATAR + "`\tBLOB,\n" +
            "\t`" + DataProviderContract.Commits.COMMIT_MESSAGE + "`\tTEXT,\n" +
            "\t`" + DataProviderContract.Commits.COMMIT_DATE + "`\tTEXT\n" +
            ");";

    private final String DELETE_COMMIT_TABLE = "DROP TABLE IF EXISTS " + DataProviderContract.Commits.TABLE_NAME + "";


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

}
