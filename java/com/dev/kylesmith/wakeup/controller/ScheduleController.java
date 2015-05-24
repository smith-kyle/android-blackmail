package com.dev.kylesmith.wakeup.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;
import com.dev.kylesmith.wakeup.model.DB.DBContract;
import com.dev.kylesmith.wakeup.model.DrawerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kylesmith on 1/8/15.
 */
public class ScheduleController extends ListActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{
    SimpleCursorAdapter mAdapter;
    private static String[] DrawerItems;
    private ImageView drawer_photo;
    private ActionBarDrawerToggle DrawerToggle;
    private ImageButton AddFirstApptBtn;
    private Toolbar toolbar;
    private DBAccessor dbAccessor;
    private HashMap<String, Object> userInfo = new HashMap<>();
    DrawerLayout drawerLayout;
    ListView drawerItems;
    private static ArrayList<DrawerItem> MenuItems = new ArrayList<DrawerItem>();
    private static final String[] PROJECTION = {
            DBContract.Appointments._ID,
            DBContract.Appointments.COLUMN_NAME_TITLE,
            DBContract.Appointments.COLUMN_NAME_CREATED,
            DBContract.Appointments.COLUMN_NAME_LONGITUDE,
            DBContract.Appointments.COLUMN_NAME_LATITUDE,
            DBContract.Appointments.COLUMN_NAME_TIME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        createMenuItems();
        DrawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerItems = (ListView) findViewById(R.id.menu_list);
        //drawer_photo = (ImageView)findViewById(R.id.embarrassing_photo);
        toolbar = (Toolbar) findViewById(R.id.drawer_btn);
        AddFirstApptBtn = (ImageButton)findViewById(R.id.add_appt_btn);

        drawerItems.setAdapter(new DrawerItemsAdapter(MenuItems, this));
        drawerItems.setOnItemClickListener(new DrawerItemClickListener());
        DrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);


        drawerLayout.setDrawerListener(DrawerToggle);
        DrawerToggle.syncState();

        // Displayed when the list is loading
        ProgressBar progressBar = new ProgressBar(this);
        //progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f));
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);

        LinearLayout temp = (LinearLayout)findViewById(R.id.add_firstappt_layout);
        getListView().setEmptyView(temp);

        LinearLayout root = (LinearLayout) findViewById(R.id.schedule_layout);
        root.addView(progressBar);

        String[] fromColumns = {
                DBContract.Appointments.COLUMN_NAME_TITLE,
                DBContract.Appointments.COLUMN_NAME_TIME
        };

        int[] toViews = {R.id.appt_title, R.id.appt_time};

        mAdapter = new ScheduleCursorAdapter(this, R.layout.listitem_schedule, null,
                fromColumns, toViews);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

//        // Set embarrassing image in drawer
//        dbAccessor = new DBAccessor(this);
//        userInfo = dbAccessor.getUserInfo();
//        Uri embarrassingPhotoUri = Uri.parse((String)userInfo.get(Constants.MAPKEY_PHOTO_URI));
//        drawer_photo.setImageURI(embarrassingPhotoUri);
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader( this, null, PROJECTION, null, null, null )
        {
            @Override
            public Cursor loadInBackground()
            {
                DBAccessor dbAccessor = new DBAccessor(getApplicationContext());
                return dbAccessor.getAllAppointments();
            }
        };
    }


    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ApptDetailController.class);
        intent.putExtra(Constants.ID, id);
        startActivity(intent);
    }

    public void addAppt(View view){
        Intent intent = new Intent(this, NewApptController.class);
        startActivity(intent);
    }


    private class DrawerItemsAdapter extends ArrayAdapter<DrawerItem>{
        public DrawerItemsAdapter(ArrayList<DrawerItem> drawerItems1, Context context){
            super(context, 0, drawerItems1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater mInflator = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = mInflator.inflate(R.layout.listitem_drawer_item, null);
            }

            DrawerItem d = getItem(position);
            TextView label = (TextView)convertView.findViewById(R.id.drawer_item_label);
            label.setText(d.get_label());
            ImageView icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon);
            icon.setImageResource(d.get_icon());

            return convertView;
        }


    }


    private void DrawerItemClicked(int position){
        Intent intent;
        switch(position){
            case 0:
                intent = new Intent(getApplicationContext(), ScheduleController.class);
                break;
            case 1:
                intent = new Intent(getApplicationContext(), NewApptController.class);
                break;
            case 2:
                intent = new Intent(getApplicationContext(), SettingsController.class);
                break;
            case 3:
                intent = new Intent(getApplicationContext(), AboutController.class);
                break;
            default:
                intent = null;
                break;
        }
        startActivity(intent);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            DrawerItemClicked(position);
        }
    }


    private void createMenuItems(){
        if(MenuItems.isEmpty()) {
            MenuItems.add(new DrawerItem(R.drawable.ic_action_view_as_list, getResources().getString(R.string.drawer_item_home)));
            MenuItems.add(new DrawerItem(R.drawable.ic_action_new, getResources().getString(R.string.drawer_item_add)));
            MenuItems.add(new DrawerItem(R.drawable.ic_action_settings, getResources().getString(R.string.drawer_item_settings)));
            MenuItems.add(new DrawerItem(R.drawable.ic_action_about, getResources().getString(R.string.drawer_item_about)));
        }
    }
}
