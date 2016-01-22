package com.nosleep.githubclient.home.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nosleep.githubclient.R;
import com.nosleep.githubclient.business.MyRepos;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssub3 on 1/5/16.
 */
public class ReposFragment extends Fragment {
    private ReposAdapter reposAdapter;

    interface ReposActionListener {
        void onRepoRefreshPulled();

        void onRepoViewCreated();

        void onRepoViewClicked(String repo, String owner);
    }

    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;

    public static final ReposFragment newInstance(Bundle args) {
        ReposFragment fragment = new ReposFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_repos, container, false);
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((ReposActionListener) getActivity()).onRepoRefreshPulled();
            }
        });
        recyclerView = (RecyclerView) refreshLayout.findViewById(R.id.repos_list);
        reposAdapter = new ReposAdapter(new ArrayList<MyRepos.RepoInfo>(0), (ReposActionListener) getActivity());
        recyclerView.setAdapter(reposAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return refreshLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        ((ReposActionListener) getActivity()).onRepoViewCreated();
    }


    private static class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {
        private List<MyRepos.RepoInfo> repoInfoList;
        private WeakReference<ReposActionListener> listenerWeakReference;

        ReposAdapter(List<MyRepos.RepoInfo> repoInfoList, ReposActionListener listener) {
            this.repoInfoList = repoInfoList;
            this.listenerWeakReference = new WeakReference<ReposActionListener>(listener);
        }

        public void setRepoInfoList(List<MyRepos.RepoInfo> repoInfoList) {
            this.repoInfoList = repoInfoList;
        }

        @Override
        public ReposAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View repoView = inflater.inflate(R.layout.item_repo, parent, false);
            return new ViewHolder(repoView);
        }

        @Override
        public void onBindViewHolder(ReposAdapter.ViewHolder holder, int position) {
            MyRepos.RepoInfo repoInfo = repoInfoList.get(position);
            holder.repoInfo = repoInfo;
            holder.title.setText(repoInfo.name);
            holder.description.setText(repoInfo.owner);
        }

        @Override
        public int getItemCount() {
            return repoInfoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView title;

            public TextView description;

            public MyRepos.RepoInfo repoInfo;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                title = (TextView) itemView.findViewById(R.id.note_detail_title);
                description = (TextView) itemView.findViewById(R.id.note_detail_description);
            }

            @Override
            public void onClick(View v) {
                if (listenerWeakReference.get() != null) {
                    listenerWeakReference.get().onRepoViewClicked(repoInfo.name, repoInfo.owner);
                }
            }
        }
    }

    public void displayRepos(List<MyRepos.RepoInfo> repoInfoList) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                ;
            }
        });
        reposAdapter.setRepoInfoList(repoInfoList);
        reposAdapter.notifyDataSetChanged();
    }
}
