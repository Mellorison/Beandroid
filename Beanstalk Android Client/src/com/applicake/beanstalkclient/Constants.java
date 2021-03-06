package com.applicake.beanstalkclient;

public interface Constants {

  public static final String SHARED_PREFERENCES = "shared_preferences";

  // current user saved data

  public static final String CREDENTIALS_STORED = "user_credentials_stored";
  public static final String USER_LOGIN = "user_login";
  public static final String USER_ACCOUNT_DOMAIN = "user_account_domain";
  public static final String USER_PASSWORD = "user_password";

  // activity result codes

  public static final int CLOSE_ALL_ACTIVITIES = 1996;
  public static final int CLEAR_STACK_UP_TO_HOME = 314;

  // names for extras passed via intents
  public static final String SERVER_ENVIRONMENT = "server_environment";
  public static final String SERVER = "server";
  public static final String CHANGESET_ENTRY = "changeset_entry";
  public static final String CHANGEDFILES_ARRAYLIST = "changed_files_array";
  public static final String CHANGEDDIRS_ARRAYLIST = "changed_dirs_array";
  public static final String COMMIT_USERNAME = "commit_username";
  public static final String COMMIT_MESSAGE = "commit_message";
  public static final String COMMIT_REPOSITORY_ID = "commit_repository_id";
  public static final String COMMIT_REVISION_ID = "commit_revision_id";
  public static final String REPOSITORY = "repository";
  public static final String REPOSITORY_ID = "repository_id";
  public static final String REPOSITORY_NAME = "repository_name";
  public static final String REPOSITORY_COLOR_NO = "repository_color_number";
  public static final String REPOSITORY_TITLE = "repository_title";
  public static final String USER = "user_parcel";
  public static final String USER_ID = "user_id";
  public static final int REFRESH_ACTIVITY = 313;
  public static final String PERMISSION = "permission";
  public static final String ACCOUNT_PLAN = "account_plan_preference";
  public static final String RELEASE = "release";

  public static final String RETURN_RESULT_WHEN_CLICK = "return_result_when_click";
  
  public static final String USER_TIMEZONE = "account timezone";

  public static final String USER_TYPE = "user_type";

  public static final String NUMBER_OF_REPOS_AVAILABLE = "number_of_repos_available";

  public static final String NUMBER_OF_USERS_AVAILABLE = "number_of_users_available";

  public static final String COMMIT_REPOSIOTRY_NAME = "commit_reposiotry_name";

  public static final String COMMIT_REPOSIOTRY_LABEL = "commit_repostory_label";

  // preferences and misc

  // settings used for notifications
  public static final String RECENT_CHANGESET_ID = "recent_changeset_id";
  public static final int NOTIFICATION_ID = 90210;
  public static final String LAST_NOTIFIED_CHANGESET_ID = "last_notified_changeset_id";
  public static final String AUTO_UPDATE_NOTIFICATION_SERVICE = "auto_update_notification_service";
  public static final String AUTO_UPDATE_NOTIFICATION_SERVICE_DELAY = "auto_update_notification_service_delay";
  public static final String AUTO_UPDATE_NOTIFICATION_SERVICE_CUSTOM_LED = "auto_update_notification_service_custom_led";



  public static final String OVERALL_REPOS = "overall";
  
  public static final String PLAN_ID = "plan_id";
  public static final String PLAN = "plan";
  
  public static final String DEFAULT_CURRENCY_SYMBOL = "$";
  
  public static final String FILTER_CLASS = "filter_class";
    

}
