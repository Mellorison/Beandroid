package com.applicake.beanstalkclient.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.SyncService;

public abstract class BeanstalkActivity extends FragmentActivity {
  // on create

  protected SharedPreferences prefs;
  protected String currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    currentUser = getIntent().getStringExtra(Constants.USER_TYPE);
    if(currentUser == null) {
      currentUser = prefs.getString(Constants.USER_TYPE, null);
    }
  }

  // inflate menu
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  // options menu handling
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.settings:
      startActivity(new Intent(this, ApplicationSettingsActivity.class));
      return true;
    case R.id.logout:
      logoutWrapper();
      return true;
    default:
      return true;
    }
  }

  public void clearCredentials() {
    Editor editor = prefs.edit();
    editor.putBoolean(Constants.CREDENTIALS_STORED, false);
    editor.putString(Constants.USER_ACCOUNT_DOMAIN, "");
    editor.putString(Constants.USER_LOGIN, "");
    editor.putString(Constants.USER_PASSWORD, "");
    editor.commit();
  }

  private void logoutWrapper() {
    if (prefs.getBoolean(Constants.AUTO_UPDATE_NOTIFICATION_SERVICE, false)) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder
          .setTitle("The nofitcation service is ON")
          .setMessage(
              "If you logout the notifications will be disabled. \n Do you want to continue?")
          .setPositiveButton("Yes", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              SyncService.stopService(getApplicationContext());
              logout();
            }
          }).setNegativeButton("No", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
      builder.show();

    } else {
      logout();

    }
  }

  public void logout() {
    clearCredentials();
    setResult(Constants.CLOSE_ALL_ACTIVITIES);
    finish();
  }

  // synchronizing killing all activities with exit action
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (resultCode) {
    case Constants.CLOSE_ALL_ACTIVITIES:
      setResult(Constants.CLOSE_ALL_ACTIVITIES);
      finish();
      break;
    case Constants.CLEAR_STACK_UP_TO_HOME:
      setResult(Constants.CLEAR_STACK_UP_TO_HOME);
      finish();
      break;
    default:
      super.onActivityResult(requestCode, resultCode, data);
    }

  }

  @Override
  protected void onDestroy() {
    cancelAllDownloadTasks();
    super.onDestroy();

  }

  protected void cancelAllDownloadTasks() {

  }

  // action bar handling
  public void onRepositoriesButtonClick(View v) {
    Intent intent = new Intent(getApplicationContext(), RepositoriesActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    setResult(Constants.CLEAR_STACK_UP_TO_HOME);
    startActivityForResult(intent, 0);
    if (!(this instanceof HomeActivity))
      finish();
  }

  public void onUsersButtonClick(View v) {
    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    setResult(Constants.CLEAR_STACK_UP_TO_HOME);
    startActivityForResult(intent, 0);
    if (!(this instanceof HomeActivity))
      finish();
  }

  public void onDeploymentButtonClick(View v) {
    Intent intent = RepositoryDeploymentsActivity.generateIntentForOverallRepositories(this);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    setResult(Constants.CLEAR_STACK_UP_TO_HOME);
    startActivity(intent);
    if(!(this instanceof HomeActivity)) {
      finish();
    }
  }

  public void onHomeButtonClick(View v) {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    setResult(Constants.CLEAR_STACK_UP_TO_HOME);
    startActivityForResult(intent, 0);
    if (!(this instanceof HomeActivity))
      finish();
  }

}
