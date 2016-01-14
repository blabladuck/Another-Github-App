package com.nosleep.githubclient.datalayer.storage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanjeev on 10/01/16.
 */
public class DataProviderContract {

    public static final String AUTHORITY = "com.nosleep.githubclient.datalayer.storage";

    public static final class Commits implements BaseColumns {

        private Commits() {
        }

        public static final String TABLE_NAME = "commits";
        private static final String SCHEME = "content://";
        private static final String PATH_COMMITS = "/commits";
        private static final String PATH_COMMIT_ID = "/commits/";

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_COMMITS);
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse(SCHEME + AUTHORITY + PATH_COMMIT_ID);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.git.commit";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.git.commit";


        public static final String SHA = "SHA";
        public static final String AUTHOR_NAME = "AUTHOR_NAME";
        public static final String AUTHOR_EMAIL = "AUTHOR_EMAIL";
        public static final String COMMITTER_NAME = "COMMITTER_NAME";
        public static final String COMMITTER_EMAIL = "COMMITTER_EMAIL";
        public static final String COMMIT_MESSAGE = "COMMIT_MESSAGE";
        public static final String COMMIT_DATE = "COMMIT_DATE";
        public static final String AUTHOR_AVATAR = "AUTHOR_AVATAR";
        public static final String COMMITTER_AVATAR = "COMMITTER_AVATAR";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";
        public static final int COMMIT_ID_PATH_POSITON = 1;
    }

}
