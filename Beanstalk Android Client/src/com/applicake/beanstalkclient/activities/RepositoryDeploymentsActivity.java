package com.applicake.beanstalkclient.activities;

import java.util.ArrayList;
import java.util.List;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Release;
import com.applicake.beanstalkclient.Repository;
import com.applicake.beanstalkclient.Server;
import com.applicake.beanstalkclient.ServerEnvironment;
import com.applicake.beanstalkclient.Strings;
import com.applicake.beanstalkclient.adapters.ReleasesAdapter;
import com.applicake.beanstalkclient.adapters.ServersAdapter;
import com.applicake.beanstalkclient.utils.GUI;
import com.applicake.beanstalkclient.utils.HttpRetriever;
import com.applicake.beanstalkclient.utils.SimpleRetryDialogBuilder;
import com.applicake.beanstalkclient.utils.XmlParser;
import com.applicake.beanstalkclient.utils.HttpRetriever.HttpConnectionErrorException;
import com.applicake.beanstalkclient.utils.HttpRetriever.UnsuccessfulServerResponseException;
import com.applicake.beanstalkclient.utils.XmlParser.XMLParserException;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class RepositoryDeploymentsActivity extends BeanstalkActivity implements
    OnClickListener {

  boolean serverListLoaded = false;
  boolean releaseListLoaded = false;

  private ListView mReleasesList;
  private ExpandableListView mServersList;
  private ReleasesAdapter mReleasesAdapter;
  private ServersAdapter mServersAdapter;

  private ArrayList<Release> mReleaseArray = new ArrayList<Release>();
  private ArrayList<ServerEnvironment> mServersArray = new ArrayList<ServerEnvironment>();
  private Repository repository;
  private Button releasesButton;
  private Button serversButton;
  private View releasesLoadingFooterView;
  private View serversLoadingFooterView;
  private View serversAddNewFooterView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.repository_deployments_layout);

    repository = getIntent().getParcelableExtra(Constants.REPOSITORY);

    // set repository title and label color based on intent
    ((TextView) findViewById(R.id.repoTitle)).setText(repository.getTitle());
    findViewById(R.id.colorLabel).getBackground().setLevel(repository.getColorLabelNo());

    // create releases list view and tab switching button, attach loading and
    // footer and set button listeners
    releasesButton = (Button) findViewById(R.id.releases_button);
    releasesButton.setOnClickListener(this);
    releasesButton.setSelected(true);

    mReleasesList = (ListView) findViewById(R.id.releases_list);
    releasesLoadingFooterView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R.layout.releases_loading_footer, null, false);
    View releasesAddNewFooterView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R.layout.add_new_release_footer, null, false);
    releasesAddNewFooterView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CreateNewReleaseActivity.class);
        intent.putExtra(Constants.REPOSITORY_ID, String.valueOf(repository.getId()));
        intent.putExtra(Constants.REPOSITORY_TITLE, String.valueOf(repository.getTitle()));
        intent.putExtra(Constants.REPOSITORY_COLOR_NO, String.valueOf(repository.getColorLabelNo()));
        
        // add extras
        startActivityForResult(intent, 0);
        
      }
    });
    
    mReleasesList.addFooterView(releasesLoadingFooterView, null, false);
    mReleasesList.addFooterView(releasesAddNewFooterView, null, true);
    
    
    
    // releases list adapter
    mReleasesAdapter = new ReleasesAdapter(this, R.layout.releases_entry, mReleaseArray);
    mReleasesList.setAdapter(mReleasesAdapter);

    // create servers list view and tab switching button, attach "loading" and
    // "add new" footer and set button listeners
    mServersList = (ExpandableListView) findViewById(R.id.servers_list);
    serversLoadingFooterView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R.layout.environments_server_environments_loading_field, null, false);
    serversAddNewFooterView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R.layout.add_server_environment_footer, null, false);
    mServersList.addFooterView(serversLoadingFooterView, null, false);
    mServersList.addFooterView(serversAddNewFooterView);
    serversButton = (Button) findViewById(R.id.servers_button);
    serversButton.setOnClickListener(this);
    serversButton.setSelected(false);
    // servers list adapter
    mServersAdapter = new ServersAdapter(this, R.layout.environments_list_entry,
        mServersArray);
    mServersList.setAdapter(mServersAdapter);

    serversAddNewFooterView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),
            CreateNewServerEnvironmentActivity.class);
        intent.putExtra(Constants.REPOSITORY_ID, repository.getId());
        startActivity(intent);
      }
    });

    new DownloadReleaseListTask(this).execute();
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    switch (id) {
    case R.id.releases_button:
      mReleasesList.setVisibility(View.VISIBLE);
      mServersList.setVisibility(View.GONE);
      releasesButton.setSelected(true);
      serversButton.setSelected(false);
      break;

    case R.id.servers_button:
      if (!serverListLoaded) {
        new DownloadServerEnvironmentsListTask(this).execute();
      }
      mReleasesList.setVisibility(View.GONE);
      mServersList.setVisibility(View.VISIBLE);
      releasesButton.setSelected(false);
      serversButton.setSelected(true);
      break;
    }
  }

  // TODO change info dialog to list header
  public class DownloadReleaseListTask extends AsyncTask<String, Void, List<Release>> {

    private Context context;

    private String errorMessage;
    private String failMessage;
    private boolean failed = false;

    public DownloadReleaseListTask(Context context) {
      this.context = context;
    }


    @Override
    protected List<Release> doInBackground(String... params) {

      try {
        String releasesXml = HttpRetriever.getReleasesListForRepositoryXML(prefs,
            repository.getId());
        return XmlParser.parseReleasesList(releasesXml);

      } catch (UnsuccessfulServerResponseException e) {
        errorMessage = e.getMessage();
        return null;
      } catch (HttpConnectionErrorException e) {
        failMessage = Strings.networkConnectionErrorMessage;
      } catch (XMLParserException e) {
        failMessage = Strings.internalErrorMessage;
      }
      failed = true;
      return null;
    }

    @Override
    protected void onPostExecute(List<Release> result) {
      mReleasesList.removeFooterView(releasesLoadingFooterView);
     
      if (failed) {
        Log.d("xxx", failMessage);
        SimpleRetryDialogBuilder builder = new SimpleRetryDialogBuilder(context,
            failMessage) {

          @Override
          public void retryAction() {
            new DownloadReleaseListTask(context).execute();
          }

          @Override
          public void noRetryAction(DialogInterface dialog) {
            super.noRetryAction(dialog);
            finish();
          }

        };

        builder.displayDialog();
      } else {
        mReleaseArray.clear();
        if (result != null) {
          mReleaseArray.addAll(result);
          mReleasesAdapter.notifyDataSetChanged();
        }

        if (errorMessage != null)
          GUI.displayMonit(context, errorMessage);

      }
    }
  }

  public class DownloadServerEnvironmentsListTask extends
      AsyncTask<String, Void, List<ServerEnvironment>> {

    private Context context;

    private String failMessage;
    private boolean failed = false;

    public DownloadServerEnvironmentsListTask(Context context) {
      this.context = context;
    }

    @Override
    protected List<ServerEnvironment> doInBackground(String... params) {

      try {
        String serverEnvironmentsXml = HttpRetriever
            .getServerEnvironmentListForRepositoryXML(prefs, repository.getId());
        Log.d("xxx", serverEnvironmentsXml);
        return XmlParser.parseServerEnvironmentsList(serverEnvironmentsXml);

      } catch (UnsuccessfulServerResponseException e) {
        failMessage = e.getMessage();
        return null;
      } catch (HttpConnectionErrorException e) {
        failMessage = Strings.networkConnectionErrorMessage;
      } catch (XMLParserException e) {
        failMessage = Strings.internalErrorMessage;
      }
      failed = true;
      
      return null;
    }

    @Override
    protected void onPostExecute(List<ServerEnvironment> result) {
      Log.d("xxx", String.valueOf(result != null ? result.size() : 0));
      mServersList.removeFooterView(serversLoadingFooterView);
      if (failed) {
        Log.d("xxx", failMessage);
        SimpleRetryDialogBuilder builder = new SimpleRetryDialogBuilder(context,
            failMessage) {

          @Override
          public void retryAction() {
            new DownloadServerEnvironmentsListTask(context).execute();
          }

          @Override
          public void noRetryAction(DialogInterface dialog) {
            super.noRetryAction(dialog);
            finish();

          }

        };

        builder.displayDialog();
      } else {
        serverListLoaded = true;
        mServersArray.clear();
        if (result != null) {
          mServersArray.addAll(result);
          mServersAdapter.notifyDataSetChanged();
        }

        if (failMessage != null)
          GUI.displayMonit(context, failMessage);

      }
    }
  }

}
