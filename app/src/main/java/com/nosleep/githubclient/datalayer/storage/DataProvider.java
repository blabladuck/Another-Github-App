package com.nosleep.githubclient.datalayer.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by Sanjeev on 10/01/16.
 */
public class DataProvider extends ContentProvider {

    private static final String TAG = "DataProvider";
    private static final int COMMITS = 1;
    private static final int COMMIT_ID = 2;
    private static final UriMatcher urimatcher;
    private static final HashMap<String, String> commitsProjectionMap;
    private CommitsSQLiteHelper commitsSQLiteHelper;

    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(DataProviderContract.AUTHORITY, "commits", COMMITS);
        urimatcher.addURI(DataProviderContract.AUTHORITY, "commits", COMMIT_ID);
        commitsProjectionMap = new HashMap<>();
        commitsProjectionMap.put(DataProviderContract.Commits.AUTHOR_EMAIL, "contactauthor");
        commitsProjectionMap.put(DataProviderContract.Commits.AUTHOR_NAME, "author");
        commitsProjectionMap.put(DataProviderContract.Commits.COMMIT_DATE, "commitdate");
        commitsProjectionMap.put(DataProviderContract.Commits.COMMIT_MESSAGE, "commitmessage");
        commitsProjectionMap.put(DataProviderContract.Commits.COMMITTER_EMAIL, "contactcommitter");
        commitsProjectionMap.put(DataProviderContract.Commits.COMMITTER_NAME, "committer");
        commitsProjectionMap.put(DataProviderContract.Commits.SHA, "sha");
        commitsProjectionMap.put(DataProviderContract.Commits._ID, "id");
    }


    @Override
    public boolean onCreate() {
        commitsSQLiteHelper = CommitsSQLiteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataProviderContract.Commits.TABLE_NAME);
        switch (urimatcher.match(uri)) {
            case COMMITS:
                queryBuilder.setProjectionMap(commitsProjectionMap);
                break;
            case COMMIT_ID:
                queryBuilder.setProjectionMap(commitsProjectionMap);
                queryBuilder.appendWhere(DataProviderContract.Commits._ID + "=" + uri.getPathSegments().get(DataProviderContract.Commits.COMMIT_ID_PATH_POSITON));
                break;
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = DataProviderContract.Commits.DEFAULT_SORT_ORDER;
        }

        Cursor c = queryBuilder.query(
                commitsSQLiteHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        c.getCount();

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (urimatcher.match(uri)) {

            case COMMITS:
                return DataProviderContract.Commits.CONTENT_TYPE;

            case COMMIT_ID:
                return DataProviderContract.Commits.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (urimatcher.match(uri) == COMMITS) {
            long id = commitsSQLiteHelper.getWritableDatabase().insert(DataProviderContract.Commits.TABLE_NAME, null, values);
            if (id > 0) {
                Uri newuri = ContentUris.withAppendedId(DataProviderContract.Commits.CONTENT_ID_URI_BASE, id);
                getContext().getContentResolver().notifyChange(newuri, null);
            }
        }
        return ContentUris.withAppendedId(DataProviderContract.Commits.CONTENT_ID_URI_BASE, -1);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = commitsSQLiteHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (urimatcher.match(uri)) {

            case COMMITS:
                count = db.delete(
                        DataProviderContract.Commits.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case COMMIT_ID:
                finalWhere =
                        DataProviderContract.Commits._ID +
                                " = " +
                                uri.getPathSegments().
                                        get(DataProviderContract.Commits.COMMIT_ID_PATH_POSITON);

                if (selection != null) {
                    finalWhere = finalWhere + " AND " + selection;
                }

                count = db.delete(
                        DataProviderContract.Commits.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = commitsSQLiteHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (urimatcher.match(uri)) {

            case COMMITS:
                count = db.update(
                        DataProviderContract.Commits.TABLE_NAME,
                        values,
                        where,
                        whereArgs
                );
                break;

            case COMMIT_ID:
                String noteId = uri.getPathSegments().get(DataProviderContract.Commits.COMMIT_ID_PATH_POSITON);
                finalWhere =
                        DataProviderContract.Commits._ID +
                                " = " +
                                uri.getPathSegments().
                                        get(DataProviderContract.Commits.COMMIT_ID_PATH_POSITON);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(
                        DataProviderContract.Commits.TABLE_NAME,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
