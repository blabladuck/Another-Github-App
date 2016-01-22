package com.nosleep.githubclient.home.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nosleep.githubclient.R;

import com.nosleep.githubclient.utils.CircleTransform;

/**
 * Created by ssub3 on 12/30/15.
 */
public class WelcomeUserFragment extends Fragment {

    public static final String EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    public static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    onWelcomeAnimationListener listener;
    private ImageView avatar;
    private TextView txtusername;

    public void displayWelcome(final String username, final String avatarurl) {
        Glide.with(getActivity().getApplicationContext()).load(avatarurl).transform(new CircleTransform(getContext())).into(avatar);
        txtusername.setText("Welcome! " + username);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onWelcomeAnimationComplete(avatarurl);
            }
        }, 4000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }

    public interface onWelcomeAnimationListener {
        void onWelcomeAnimationComplete(String avatarurl);
    }

    public static final WelcomeUserFragment newInstance(@Nullable Bundle args) {
        WelcomeUserFragment frag = new WelcomeUserFragment();
        frag.setArguments(args);
        return frag;
    }

    public void registerWelcomeAnimationListener(onWelcomeAnimationListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_welcomeuser, container, false);
        avatar = (ImageView) rootview.findViewById(R.id.avatar);
        txtusername = (TextView) rootview.findViewById(R.id.username);
        return rootview;
    }


}
