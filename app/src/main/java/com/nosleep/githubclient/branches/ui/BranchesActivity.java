package com.nosleep.githubclient.branches.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nosleep.githubclient.R;
import com.nosleep.githubclient.branches.BranchesContract;
import com.nosleep.githubclient.branches.presenter.BranchesPresenter;
import com.nosleep.githubclient.business.BusinessInjector;
import com.nosleep.githubclient.business.MyBranch;
import com.nosleep.githubclient.commits.ui.CommitsActivity;
import com.nosleep.githubclient.utils.MasterTemplateActivity;

import java.util.ArrayList;
import java.util.List;

public class BranchesActivity extends MasterTemplateActivity implements BranchesContract.BranchesView, BranchViewClickListener {

    private static final String TAG = BranchesActivity.class.getSimpleName();
    public static final String EXTRA_REPO = "EXTRA_REPO";
    public static final String EXTRA_OWNER = "EXTRA_OWNER";
    private BranchesContract.UserAction userAction;
    private RecyclerView recyclerView;
    private BranchesInfoAdapter adapter;
    private ImageView profileImageView;
    private String repoName;
    private String repoOwner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        this.repoName = getIntent().getStringExtra(EXTRA_REPO);
        this.repoOwner = getIntent().getStringExtra(EXTRA_OWNER);
        if (this.repoName == null || this.repoName.isEmpty() || this.repoOwner == null || this.repoOwner.isEmpty()) {
            throw new IllegalArgumentException("Unexpected parameters received");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setTitle("Branches");
        ab.setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new BranchesInfoAdapter(new ArrayList<MyBranch.BranchInfo>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BusinessInjector businessInjector = BusinessInjector.getInstance(this);
        userAction = new BranchesPresenter(this, businessInjector.getBranchesBusiness());
        userAction.getBranches(repoName, repoOwner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBranches(List<MyBranch.BranchInfo> branchInfoList) {
        adapter.setBranchInfoList(branchInfoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToCommitScreen(String repoName, String repoOwner, String branchName) {
        Intent intent = new Intent(this, CommitsActivity.class);
        intent.putExtra(CommitsActivity.EXTRA_REPO, repoName);
        intent.putExtra(CommitsActivity.EXTRA_BRANCH, branchName);
        intent.putExtra(CommitsActivity.EXTRA_OWNER, repoOwner);
        startActivity(intent);
    }

    @Override
    public void onBranchClicked(MyBranch.BranchInfo branchInfo) {
        userAction.onBranchViewClicked(repoName, repoOwner, branchInfo.name);
    }

    private static class BranchesInfoAdapter extends RecyclerView.Adapter<BranchesInfoAdapter.ViewHolder> {

        private final BranchViewClickListener branchViewClickListener;
        private List<MyBranch.BranchInfo> branchInfoList;


        BranchesInfoAdapter(List<MyBranch.BranchInfo> branchInfoList, BranchViewClickListener branchViewClickListener) {
            this.branchInfoList = branchInfoList;
            this.branchViewClickListener = branchViewClickListener;
        }

        public void setBranchInfoList(List<MyBranch.BranchInfo> branchInfoList) {
            this.branchInfoList = branchInfoList;
        }

        @Override
        public BranchesInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View repoView = inflater.inflate(R.layout.item_repo, parent, false);
            return new ViewHolder(repoView);
        }

        @Override
        public void onBindViewHolder(BranchesInfoAdapter.ViewHolder holder, int position) {
            MyBranch.BranchInfo branchInfo = branchInfoList.get(position);
            holder.title.setText(branchInfo.name);
            holder.description.setText(branchInfo.url);
        }

        @Override
        public int getItemCount() {
            return branchInfoList.size();
        }

        public void onBranchClick(int adapterPosition) {
            MyBranch.BranchInfo branchInfo = getBranchInfo(adapterPosition);
            branchViewClickListener.onBranchClicked(branchInfo);
        }

        private MyBranch.BranchInfo getBranchInfo(int position) {
            return branchInfoList.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public TextView description;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.note_detail_title);
                description = (TextView) itemView.findViewById(R.id.note_detail_description);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBranchClick(getAdapterPosition());
                    }
                });
            }
        }
    }

}
