package com.applicake.beanstalkclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.Release;
import com.applicake.beanstalkclient.Repository;
import com.applicake.beanstalkclient.permissions.CanReleaseFromRepositoryFilter;
import com.applicake.beanstalkclient.tasks.BeanstalkAsyncTask;
import com.applicake.beanstalkclient.tasks.ResponseHandler;
import com.applicake.beanstalkclient.utils.HttpRetriever;
import com.applicake.beanstalkclient.utils.XmlParser;
import com.applicake.beanstalkclient.utils.HttpRetriever.HttpConnectionErrorException;
import com.applicake.beanstalkclient.utils.HttpRetriever.UnsuccessfulServerResponseException;

public class OverallRepoReleaseFragment extends SpecifiedRepoReleasesListFragment {

  
  private static final int REPOSITORY_ADD_RELEASE = 0x999;
  
  @Override
  public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
    final Release release = mReleaseArray.get(position);
    ResponseHandler<Repository> handler = new ResponseHandler<Repository>() {
      @Override
      public void onResult(Repository result) {
        startReleaseDetails(release, result);
      }
    };
    new DownloadRepositoryDetailsTask(release.getRepositoryId(), getActivity(), handler).execute();
  }
  
  @Override
  public void onClick(View v) {
    Intent intent = new Intent(getActivity(), RepositoriesActivity.class);
    intent.setAction(Intent.ACTION_PICK);
    intent.putExtra(Constants.FILTER_CLASS, CanReleaseFromRepositoryFilter.class);
    startActivityForResult(intent, REPOSITORY_ADD_RELEASE);
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == REPOSITORY_ADD_RELEASE && resultCode == RepositoriesActivity.RESULT_REPOSITORY) {
      Repository repository = data.getParcelableExtra(Constants.REPOSITORY);
      startAddNewRelease(repository);
    }
  }
  
  @Override
  protected String getReleasesListXML() throws UnsuccessfulServerResponseException,
      HttpConnectionErrorException {
    return HttpRetriever.getReleasesListForAllRepositories(prefs);
  }

  public class DownloadRepositoryDetailsTask extends
      BeanstalkAsyncTask<Void, Void, Repository> {

    private int repositoryId;
    private ResponseHandler<Repository> responseHandler;

    public DownloadRepositoryDetailsTask(int repositoryId, Activity activity) {
      super(activity);
      this.repositoryId = repositoryId;
    }

    public DownloadRepositoryDetailsTask(int repositoryId, Activity activity,
        ResponseHandler<Repository> responseHandler) {
      this(repositoryId, activity);
      this.responseHandler = responseHandler;
    }
    
    @Override
    protected String getProgressDialogTip() {
      return "Getting repository data";
    }

    @Override
    protected Repository trueDoInBackground(Void... params) throws Throwable {
      String repositoryXML = HttpRetriever.getRepositoryXML(prefs, repositoryId);
      Repository repository = XmlParser.parseRepository(repositoryXML);
      return repository;
    }
    
    @Override
    protected boolean finishActivityAfterError() {
      return false;
    }

    @Override
    protected void trueOnPostExceute(Repository result) {
      if (responseHandler != null) {
        responseHandler.onResult(result);
      }
    }

    @Override
    protected void performTaskAgain() {
      new DownloadRepositoryDetailsTask(repositoryId, getActivity(),
          responseHandler).execute();
    }
  }

}
