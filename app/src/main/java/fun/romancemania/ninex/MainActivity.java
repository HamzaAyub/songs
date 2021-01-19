package fun.romancemania.ninex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.design.widget.NavigationView;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentTransaction;
//import androidx.core.view.GravityCompat;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdView;/*
import com.google.android.gms.analytics.GoogleAnalytics;
import com.appstock.newhindisongs.analytics.Analytics;*/
import com.google.android.material.navigation.NavigationView;

import fun.romancemania.ninex.fragments.HomeFragment;
import fun.romancemania.ninex.json.JsonUtils;
//import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HTAG";
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    SharedPreferences preferences;
    private AdView mAdView;
    private EditText editSerach;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }






        //show admob banner ad

//        mAdView = (AdView) findViewById(R.id.adView);
//        mAdView.loadAd(new AdRequest.Builder().build());
//        mAdView.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdClosed() {
//                Log.d(TAG, "onAdClosed:  add Close");
//            }
//
//            @Override
//            public void onAdFailedToLoad(int error) {
//                Log.d(TAG, "onAdFailedToLoad: ");
//                mAdView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                Log.d(TAG, "onAdLeftApplication: ");
//            }
//
//            @Override
//            public void onAdOpened() {
//                Log.d(TAG, "onAdOpened: ");
//            }
//
//            @Override
//            public void onAdLoaded() {
//                Log.d(TAG, "onAdLoaded: ");
//                mAdView.setVisibility(View.VISIBLE);
//            }
//        });
        if (!(JsonUtils.isNetworkAvailable(this))) {
            Log.d(TAG, "onCreate: No Internet");
            noInternet("No Internt, Please Connect With A Network.");
            return;

        }


      /*  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer) ;*/

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame_container, new HomeFragment()).commit();

    /*    mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                //setTitle(menuItem.getTitle());

                if (menuItem.getItemId() == R.id.drawer_home) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, new HomeFragment()).commit();
                }
                if (menuItem.getItemId() == R.id.drawer_cat) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, new FragmentTabCategory()).commit();
                }

                if (menuItem.getItemId() == R.id.drawer_favorite) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, new FragmentFavorite()).commit();
                }

                if (menuItem.getItemId() == R.id.drawer_rate) {
                    final String appName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                    }
                }

                if (menuItem.getItemId() == R.id.drawer_more) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_app))));
                }
				
                if (menuItem.getItemId() == R.id.drawer_more2) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_app2))));
                }				

                if (menuItem.getItemId() == R.id.drawer_setting) {
                    Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(i);
                }

                return false;
            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

*/        // init analytics tracker
     //   ((Analytics) getApplication()).getTracker();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // analytics
// //       GoogleAnalytics.getInstance(this).reportActivityStart(this);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        // analytics
//  //      GoogleAnalytics.getInstance(this).reportActivityStop(this);
//    }
//
//    @Override
//    protected void onPause() {
//        mAdView.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAdView.resume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mAdView.destroy();
//        super.onDestroy();
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Toast.makeText(appContext, "BAck", Toast.LENGTH_LONG).show();
            /*AlertDialog.Builder alert = new AlertDialog.Builder(
                    MainActivity.this);*/
            AlertDialog.Builder alert;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                alert = new AlertDialog.Builder(this);
            }


            alert.setTitle(getString(R.string.app_name));
            alert.setIcon(R.mipmap.ic_launcher);


            alert.setMessage("Do You Want to Rate This App?");

            alert.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            finish();
                        }



                    });

            alert.setNegativeButton("Yes",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            final String appName = getPackageName();//your application package name i.e play store application url
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id="
                                                + appName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id="
                                                + appName)));
                            }
                        }
                    });


            //////////////////////////////////////////////////////////////////////////////////
//            alert.setMessage("Sure, You Want To Close This App?");
//
//            alert.setPositiveButton("Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//
//                            finish();
//                        }
//
//
//
//                    });
//
//            alert.setNegativeButton("No",
//                    new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            return;
//
//                        }
//                    });



            alert.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    public void noInternet(String msg){
       /* AlertDialog.Builder alert = new AlertDialog.Builder(
                MainActivity.this);
*/

        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        alert.setTitle(getString(R.string.app_name));
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage(msg);

        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        return;
                    }



                })
        .show();

    }
}
