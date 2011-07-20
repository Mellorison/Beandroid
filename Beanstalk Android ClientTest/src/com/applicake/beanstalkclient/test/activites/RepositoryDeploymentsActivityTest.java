package com.applicake.beanstalkclient.test.activites;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.applicake.beanstalkclient.Constants;
import com.applicake.beanstalkclient.R;
import com.applicake.beanstalkclient.Repository;
import com.applicake.beanstalkclient.enums.ColorLabels;
import com.applicake.beanstalkclient.activities.RepositoryDeploymentsActivity;

/**
 * These are tests of layout and point-click UI interaction.
 */
public class RepositoryDeploymentsActivityTest extends
    ActivityInstrumentationTestCase2<RepositoryDeploymentsActivity> {

  private static final int COLOR = 1;
  private static final String TITLE = "title";
  private TextView mTitle;
  private View mColor;
  private RepositoryDeploymentsActivity mActivity;
  private ListView mReleases;
  private ListView mServers;
  private Button mReleasesTab;
  private Button mServersTab;
  private Adapter mReleasesAdapter;

  public RepositoryDeploymentsActivityTest() {
    super("com.applicake.beanstalkclient", RepositoryDeploymentsActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    Intent intent = new Intent();
    
    Repository repository = new Repository();
    repository.setId(0);
    repository.setTitle(TITLE);
    repository.setColorLabel(ColorLabels.getLabelFromNumber(COLOR));
    
    intent.putExtra(Constants.REPOSITORY, repository);

    setActivityIntent(intent);

    mActivity = getActivity();
    
    // a hack that unlocks devices screen in order to enable correct executon of tests
    // TODO remove permission from Application AndroidManifest.xml before release
    KeyguardManager mKeyGuardManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
    KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("RepositoryDeploymentsActivity");
    mLock.disableKeyguard();

    mTitle = (TextView) mActivity.findViewById(R.id.repoTitle);
    mColor = mActivity.findViewById(R.id.colorLabel);
    mReleases = (ListView) mActivity.findViewById(R.id.releases_list);
    mServers = (ListView) mActivity.findViewById(R.id.servers_list);
    mReleasesTab = (Button) mActivity.findViewById(R.id.releases_button);
    mServersTab = (Button) mActivity.findViewById(R.id.servers_button);
    mReleasesAdapter = mReleases.getAdapter();
  }

  @SmallTest
  public void testPreconditions() {
    // check if there are views for title and label
    assertNotNull("can't find title view", mTitle);
    assertNotNull("can't find color label view", mColor);

    // check if there are views for release releases and servers list
    assertNotNull("can't find releases view", mReleases);
    assertNotNull("can't find servers view", mServers);

    // check tab buttons
    assertNotNull("can't find releases tab", mReleasesTab);
    assertNotNull("can't find servers tab", mServersTab);

    // check if adapter for releases is set
    assertNotNull("releases adapter not set", mReleasesAdapter);
  }

  public void testTitleUi() {
    // test color-label size...
    int size = (int) mActivity.getResources().getDimension(R.dimen.color_label_size);
    assertEquals("wrong label size", size, mColor.getWidth());
    assertEquals("wrong label size", size, mColor.getHeight());
    // ...and color
    assertEquals("wrong label color", COLOR, mColor.getBackground().getLevel());

    // test repo title
    assertEquals("wrong title", TITLE, mTitle.getText());

    // test relative location between title and color
    int[] colorLocation = new int[2], titleLocation = new int[2];
    mColor.getLocationInWindow(colorLocation);
    mTitle.getLocationInWindow(titleLocation);
    // is color to the left?
    assertFalse("color label not to the left of title",
        colorLocation[0] + mColor.getWidth() > titleLocation[0]);
    // is it vertically aligned?
    assertFalse("color label above title",
        colorLocation[1] + mColor.getHeight() < titleLocation[1]);
    assertFalse("color label below title",
        colorLocation[1] > titleLocation[1] + mTitle.getHeight());
  }
  
  public void testTabsUi() {
    // at first releases should be visible and servers hidden
    assertReleasesVisible();

    // do nothing (tap active)
    TouchUtils.tapView(this, mReleasesTab);
    assertReleasesVisible();

    // do nothing (tap inactive but cancel)
    TouchUtils.touchAndCancelView(this, mServersTab);
    assertReleasesVisible();

    // switch tabs
    TouchUtils.tapView(this, mServersTab);
    assertServersVisible();

    // do nothing (tap active)
    TouchUtils.tapView(this, mServersTab);
    assertServersVisible();

    // do nothing (tap inactive but cancel)
    TouchUtils.touchAndCancelView(this, mReleasesTab);
    assertServersVisible();

    // switch tabs
    TouchUtils.tapView(this, mReleasesTab);
    assertReleasesVisible();
  }

  public void testReleasesList() {

  }

  private void assertReleasesVisible() {
    assertTrue("releases not visible", mReleases.isShown());
    assertFalse("servers visible", mServers.isShown());
  }

  private void assertServersVisible() {
    assertFalse("releases visible", mReleases.isShown());
    assertTrue("servers not visible", mServers.isShown());
  }

}