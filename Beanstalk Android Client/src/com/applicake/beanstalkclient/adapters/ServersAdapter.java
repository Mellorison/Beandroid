package com.applicake.beanstalkclient.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Server;
import com.applicake.beanstalkclient.ServerEnvironment;
import com.applicake.beanstalkclient.activities.CreateNewServerActivity;
import com.applicake.beanstalkclient.activities.ModifyServerActivity;
import com.applicake.beanstalkclient.activities.ModifyServerEnvironmentProperties;
import com.applicake.beanstalkclient.permissions.ServerEnviromentsPermissions;
import com.applicake.beanstalkclient.utils.HttpRetriever;
import com.applicake.beanstalkclient.utils.HttpRetriever.HttpConnectionErrorException;
import com.applicake.beanstalkclient.utils.HttpRetriever.UnsuccessfulServerResponseException;
import com.applicake.beanstalkclient.utils.XmlParser;
import com.applicake.beanstalkclient.utils.XmlParser.XMLParserException;

public class ServersAdapter extends BaseExpandableListAdapter {

  private List<ServerEnvironment> mServersArray;
  private LayoutInflater mInflater;
  private SharedPreferences prefs;
  private Context mContext;
  private ServerEnviromentsPermissions permissions;

  public ServersAdapter(Context context, List<ServerEnvironment> serversArray) {
    this(context, serversArray, new ServerEnviromentsPermissions(context));
  }
  
  public ServersAdapter(Context context, List<ServerEnvironment> serversArray, ServerEnviromentsPermissions permissions) {
    this.mServersArray = serversArray;
    this.mContext = context;
    this.mInflater = LayoutInflater.from(context);
    this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    this.permissions = permissions;
  }

  @Override
  public int getGroupCount() {
    return mServersArray.size();
  }

  // this method checks if a list of servers for a certain server environment
  // has been downloaded or is being dowlnoaded. The list will always display
  // one additional field which is used as a "add new server" button

  @Override
  public int getChildrenCount(int groupPosition) {
    Log.d("xxx", "getting ChildrenCount for " + String.valueOf(groupPosition));

    ServerEnvironment serverEnvironment = mServersArray.get(groupPosition);

    int childrenCountToReturn = 0;
    
    // the list is loaded
    if (serverEnvironment.isDownloaded()) {
      if (mServersArray != null && serverEnvironment.getServerList() != null) {
        childrenCountToReturn =  serverEnvironment.getServerList().size() ;
      } else {
        childrenCountToReturn = 0;
      }

      // the list is being dowlnoaded
    } else if (serverEnvironment.isDownloading()) {
      childrenCountToReturn = 1;
      // the list has not been downloaded - start download
    } else {
      new DownloadServerListForEnvironmentTask(serverEnvironment).execute();
      childrenCountToReturn = 1;
    }
    
    if(permissions.hasFullDeploymentsAccessForRepository(serverEnvironment.getRepositoryId())) {
      childrenCountToReturn++;
    }
    
    return childrenCountToReturn;

  }

  @Override
  public Object getGroup(int groupPosition) {
    return mServersArray.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return mServersArray.get(groupPosition).getServerList().get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    // TODO Auto-generated method stub
    return false;
  }

  // downloads server list for particular environment
  public void downloadChildList(int groupId) {

  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
      ViewGroup parent) {
    View view;
    if (convertView == null)
      view = mInflater.inflate(R.layout.environments_list_entry, null);
    else
      view = convertView;

    final ServerEnvironment serverEnvironment = mServersArray.get(groupPosition);

    if (serverEnvironment != null) {
      TextView environmentName = (TextView) view.findViewById(R.id.environment_name);
      // TextView branchName = (TextView) view.findViewById(R.id.branch_name);
      TextView automatic = (TextView) view.findViewById(R.id.automatic);
      TextView editServerEnvironmentButton = (TextView) view
          .findViewById(R.id.edit_server_environment_button);

      if (permissions != null && permissions.hasAccessForThisEnvironment(serverEnvironment)) {
        editServerEnvironmentButton.setVisibility(View.VISIBLE);
        editServerEnvironmentButton.setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View v) {
            Log.d("xxx",
                "Server environment in OnClickListener" + serverEnvironment.toString());
            Intent intent = new Intent(mContext, ModifyServerEnvironmentProperties.class);
            intent.putExtra(Constants.SERVER_ENVIRONMENT, serverEnvironment);
            ((Activity) mContext).startActivityForResult(intent, 0);
          }
        });
      } else {
        editServerEnvironmentButton.setVisibility(View.GONE);
      }

      environmentName.setText(serverEnvironment.getName());
      // branchName.setText(serverEnvironment.getBranchName());
      automatic
          .setText(mContext.getString(serverEnvironment.isAutomatic() ? R.string.automatic
              : R.string.manual));

    }

    return view;
  }

  @Override
  public View getChildView(final int groupPosition, int childPosition,
      boolean isLastChild, View convertView, ViewGroup parent) {

    View view;

    final ServerEnvironment serverEnvironment = mServersArray.get(groupPosition);
    
    if (isLastChild && permissions.hasFullDeploymentsAccessForRepository(serverEnvironment.getRepositoryId())) {
      view = mInflater.inflate(R.layout.environments_add_server_footer, null);
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Log.d("xxx", "create new server for " + serverEnvironment.getName());
          // tests
          Intent intent = new Intent(mContext, CreateNewServerActivity.class);
          intent.putExtra(Constants.SERVER_ENVIRONMENT, serverEnvironment);

          ((Activity) mContext).startActivityForResult(intent, 0);

        }
      });
    } else if (mServersArray.get(groupPosition).isDownloading()) {
      view = mInflater.inflate(R.layout.environments_servers_loading_field, null);
    }

    else {
      view = mInflater.inflate(R.layout.environments_server_list_entry, null);
      // temporary
      final Server server = mServersArray.get(groupPosition).getServerList()
          .get(childPosition);

      if (server != null) {
        TextView serverName = (TextView) view.findViewById(R.id.server_name);

        serverName.setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View v) {
            Intent intent = new Intent(mContext, ModifyServerActivity.class);
            intent.putExtra(Constants.SERVER_ENVIRONMENT,
                mServersArray.get(groupPosition));
            intent.putExtra(Constants.SERVER, server);

            ((Activity) mContext).startActivityForResult(intent, 0);
          }
        });

        serverName.setText(server.getName());
      }
    }

    return view;

  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    // TODO Auto-generated method stub

    return false;
  }

  public class DownloadServerListForEnvironmentTask extends
      AsyncTask<Void, Void, List<Server>> {

    private ServerEnvironment mServerEnvironment;

    public DownloadServerListForEnvironmentTask(ServerEnvironment serverEnvironment) {
      mServerEnvironment = serverEnvironment;
    }

    @Override
    protected void onPreExecute() {
      mServerEnvironment.setDownloading(true);
      super.onPreExecute();
    }

    @Override
    protected List<Server> doInBackground(Void... params) {
      try {
        String serverListXml = HttpRetriever.getServerListForEnviromentXML(prefs,
            mServerEnvironment.getRepositoryId(), mServerEnvironment.getId());
        Log.d("xxx", serverListXml);
        return XmlParser.parseServerList(serverListXml);

      } catch (HttpConnectionErrorException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (UnsuccessfulServerResponseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (XMLParserException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return null;

    }

    @Override
    protected void onPostExecute(List<Server> result) {
      Log.d("xxx", "server list size: " + (result != null ? result.size() : 0));

      // TODO add retry feature
      mServerEnvironment.setServerList(result);
      mServerEnvironment.setDownloaded(true);
      mServerEnvironment.setDownloading(false);

      notifyDataSetChanged();

    }

  }

}
