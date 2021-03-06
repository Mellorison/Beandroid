package com.applicake.beanstalkclient.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Repository;
import com.applicake.beanstalkclient.ServerEnvironment;
import com.applicake.beanstalkclient.Strings;
import com.applicake.beanstalkclient.adapters.ServersAdapter;
import com.applicake.beanstalkclient.permissions.PermissionsData;
import com.applicake.beanstalkclient.permissions.PermissionsPersistenceUtil;
import com.applicake.beanstalkclient.utils.GUI;
import com.applicake.beanstalkclient.utils.HttpRetriever;
import com.applicake.beanstalkclient.utils.HttpRetriever.HttpConnectionErrorException;
import com.applicake.beanstalkclient.utils.HttpRetriever.UnsuccessfulServerResponseException;
import com.applicake.beanstalkclient.utils.SimpleRetryDialogBuilder;
import com.applicake.beanstalkclient.utils.XmlParser;
import com.applicake.beanstalkclient.utils.XmlParser.XMLParserException;

public class SpecificRepoServerEnviromentsFragment extends Fragment implements OnClickListener {

  private Repository repository;
  protected SharedPreferences prefs;
  private List<ServerEnvironment> enviromentsList = new ArrayList<ServerEnvironment>();
  private ServersAdapter mAdapter;
  private View serversLoadingFooterView;
  private View serversAddNewFooterView;
  private ExpandableListView mServersList;

  @Override
  public void onCreate(android.os.Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.repository = getActivity().getIntent().getParcelableExtra(Constants.REPOSITORY);
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
        .getApplicationContext());
    PermissionsPersistenceUtil util = new PermissionsPersistenceUtil(getActivity());
    try {
      PermissionsData data = util.readStoredPermissionsData();
      Log.w("aaaaa", data.getUser().toString());
    } catch(Exception e) {
      
    }
    this.mAdapter = new ServersAdapter(getActivity(), enviromentsList);
  }

  @Override
  public void onResume() {
    super.onResume();
    downloadEnviroments();
  }
  
  protected void downloadEnviroments() {
    new DownloadServerEnvironmentsListTask(getActivity(), repository.getId()).execute();
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mServersList = (ExpandableListView) inflater.inflate(
        R.layout.enviroment_servers_list, null);
    serversLoadingFooterView = inflater.inflate(
        R.layout.environments_server_environments_loading_field, null, false);
    serversAddNewFooterView = inflater.inflate(R.layout.add_server_environment_footer,
        null, false);

    mServersList.addFooterView(serversLoadingFooterView, null, false);
    mServersList.addFooterView(serversAddNewFooterView);

    mServersList.setAdapter(mAdapter);

    
    serversAddNewFooterView.setOnClickListener(this);

    return mServersList;
  }
  
  @Override
  public void onClick(View view) {
    startAddNewServer(repository);
  }
    
  protected void startAddNewServer(Repository repository) {
    Intent intent = new Intent(getActivity(), CreateNewServerActivity.class);
    intent.putExtra(Constants.REPOSITORY_ID, repository.getId());
    startActivity(intent);
  }


  public class DownloadServerEnvironmentsListTask extends
      AsyncTask<String, Void, List<ServerEnvironment>> {

    private Context context;

    private String failMessage;
    private boolean failed = false;

    private List<Integer> listOfRepositoriesIdsToDownload;

    public DownloadServerEnvironmentsListTask(Context context, int singleRepoId) {
      this.context = context;
      listOfRepositoriesIdsToDownload = new ArrayList<Integer>();
      listOfRepositoriesIdsToDownload.add(singleRepoId);
    }

    public DownloadServerEnvironmentsListTask(Context context,
        List<Integer> repositoriesIds) {
      this.context = context;
      this.listOfRepositoriesIdsToDownload = repositoriesIds;
    }

    @Override
    protected List<ServerEnvironment> doInBackground(String... params) {

      try {
        List<ServerEnvironment> enviroments = new ArrayList<ServerEnvironment>();
        for (Integer singleRepoId : listOfRepositoriesIdsToDownload) {
          String serverEnvironmentsXml = HttpRetriever
              .getServerEnvironmentListForRepositoryXML(prefs, singleRepoId.intValue());
          Log.d("xxx", serverEnvironmentsXml);
          enviroments
              .addAll(XmlParser.parseServerEnvironmentsList(serverEnvironmentsXml));
        }
        return enviroments;

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
            new DownloadServerEnvironmentsListTask(context,
                listOfRepositoriesIdsToDownload).execute();
          }

          @Override
          public void noRetryAction(DialogInterface dialog) {
            super.noRetryAction(dialog);
            getActivity().finish();
          }

        };

        builder.displayDialog();
      } else {
        enviromentsList.clear();
        if (result != null) {
          enviromentsList.addAll(result);
          mAdapter.notifyDataSetChanged();
        }

        if (failMessage != null)
          GUI.displayMonit(context, failMessage);

      }
    }
  }

}
