package com.applicake.beanstalkclient.adapters;

import java.util.List;

import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Server;
import com.applicake.beanstalkclient.ServerEnvironment;
import com.applicake.beanstalkclient.utils.HttpRetriever;
import com.applicake.beanstalkclient.utils.XmlParser;
import com.applicake.beanstalkclient.utils.HttpRetriever.HttpConnectionErrorException;
import com.applicake.beanstalkclient.utils.HttpRetriever.UnsuccessfulServerResponseException;
import com.applicake.beanstalkclient.utils.XmlParser.XMLParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ServersAdapter extends BaseExpandableListAdapter {

  private List<ServerEnvironment> mServersArray;
  private LayoutInflater mInflater;
  private SharedPreferences prefs;

  public ServersAdapter(Context context, int i, List<ServerEnvironment> serversArray) {
    this.mServersArray = serversArray;
    mInflater = LayoutInflater.from(context);
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
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
    if (serverEnvironment.isDownloaded()) {

      if (mServersArray != null && serverEnvironment.getServerList() != null) {
        return serverEnvironment.getServerList().size() + 1;
      }
      else
        return 1;

    } else if (serverEnvironment.isDownloading()) {

      return 1;
      
    } else {
      new DownloadServerListForEnvironmentTask(serverEnvironment).execute();
      return 1;
    }

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

    ServerEnvironment serverEnvironment = mServersArray.get(groupPosition);

    if (serverEnvironment != null) {
      TextView environmentName = (TextView) view.findViewById(R.id.environment_name);
      TextView branchName = (TextView) view.findViewById(R.id.branch_name);
      TextView automatic = (TextView) view.findViewById(R.id.automatic);

      environmentName.setText(serverEnvironment.getName());
      branchName.setText(serverEnvironment.getBranchName());
      // TODO change to automatic/manual
      automatic.setText(String.valueOf(serverEnvironment.isAutomatic()));
    }

    return view;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {

    View view;
    if (!isLastChild) {
      view = mInflater.inflate(R.layout.environments_server_list_entry, null);
    // temporary
      Server server = mServersArray.get(groupPosition).getServerList().get(childPosition);

      if (server != null) {
        TextView environmentName = (TextView) view.findViewById(R.id.environment_name);
        TextView branchName = (TextView) view.findViewById(R.id.branch_name);
        TextView automatic = (TextView) view.findViewById(R.id.automatic);

        environmentName.setText(server.getName());
        branchName.setText(String.valueOf(server.getRevision()));
        // TODO change to automatic/manual
        automatic.setText(server.getEnvitonmentName());
        // TODO Auto-generated method stub

      }
    } else{
      view = mInflater.inflate(R.layout.environments_add_server_footer, null);
//      view.setOnClickListener(l)
    }
  
    
    return view;

  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    // TODO Auto-generated method stub

    return false;
  }

  @Override
  public void onGroupExpanded(int groupPosition) {
    // TODO Auto-generated method stub
    super.onGroupExpanded(groupPosition);
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
      Log.d("xxx", "downloading server list failed");
      return null;
    }

    @Override
    protected void onPostExecute(List<Server> result) {
      Log.d("xxx", "server list size: " + (result != null ? result.size() : 0));
      if (result != null) {

        mServerEnvironment.setServerList(result);
        mServerEnvironment.setDownloaded(true);
        mServerEnvironment.setDownloading(false);
        notifyDataSetChanged();

      }

    }

  }

}
