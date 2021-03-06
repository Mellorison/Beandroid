package com.applicake.beanstalkclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Repository;

public class RepositoryDeploymentsActivity extends BeanstalkActivity implements OnClickListener {
  
  private boolean activityStartedForSpecificRepository;
    
  private SpecifiedRepoReleasesListFragment releasesFragment;
  private SpecificRepoServerEnviromentsFragment serversFragment;
  
  private Button releasesButton;
  private Button serversButton;
  
  public static Intent generateIntentForSpecificRepo(Context context, Repository repository) {
    Intent intent = new Intent(context, RepositoryDeploymentsActivity.class);
    intent.putExtra(Constants.REPOSITORY, repository);
    return intent;
  }
  
  public static Intent generateIntentForOverallRepositories(Context context) {
    Intent intent = generateIntentForSpecificRepo(context, Repository.generateFakeRepositoryForOverall());
    intent.putExtra(Constants.OVERALL_REPOS, true);
    return intent;
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activityStartedForSpecificRepository = !getIntent().getBooleanExtra(Constants.OVERALL_REPOS, false);
    
    setContentView(R.layout.new_repository_deployments_layout);
        
    releasesButton = (Button) findViewById(R.id.releases_button);
    releasesButton.setOnClickListener(this);
    releasesButton.setSelected(true);
    
    serversButton = (Button)findViewById(R.id.servers_button);
    serversButton.setOnClickListener(this);
    serversButton.setSelected(false);
    
    Repository repository = (Repository)getIntent().getParcelableExtra(Constants.REPOSITORY);
    ((TextView) findViewById(R.id.repoName)).setText(repository.getTitle());
    findViewById(R.id.colorLabel).getBackground().setLevel(repository.getColorLabelNo());

    setupReleasesFragment();
  }
  
  private void setupReleasesFragment() {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if(releasesFragment == null) {
      if(activityStartedForSpecificRepository) {
        releasesFragment = new SpecifiedRepoReleasesListFragment();
      } else {
        releasesFragment = new OverallRepoReleaseFragment();
      }
    }
    transaction.replace(R.id.fragment_container, releasesFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
  
  private void setupServersFragment() {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if(serversFragment == null) {
      if(activityStartedForSpecificRepository) {
        serversFragment = new SpecificRepoServerEnviromentsFragment();
      } else {
        serversFragment = new OverallRepoServerEnviromentsListFragment();
      }
    }
    transaction.replace(R.id.fragment_container, serversFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onBackPressed() {
    finish();
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    view.setSelected(true);
    switch (id) {
    case R.id.releases_button:
      setupReleasesFragment();
      serversButton.setSelected(false);
      break;
    case R.id.servers_button:
      setupServersFragment();
      releasesButton.setSelected(false);
      break;
    }
  }

}
