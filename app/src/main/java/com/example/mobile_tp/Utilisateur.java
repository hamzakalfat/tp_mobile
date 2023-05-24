package com.example.mobile_tp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Utilisateur extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur);
        displayListView();
    }
    private void displayListView() {


        // The desired columns to be bound
        String[] columns = new String[] {
                BienDb.KEY_CODE,
                BienDb.KEY_NAME,
                BienDb.KEY_TYPE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.code,
                R.id.name,
                R.id.typeList,
        };

        // create an adapter from the SimpleCursorAdapter
        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.bien_info,
                null,
                columns,
                to,
                0);

        // get reference to the ListView
        ListView listView = (ListView) findViewById(R.id.bienList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemClickListener((listView1, view, position, id) -> {
            // Get the cursor, positioned to the corresponding row in the result set
            Cursor cursor = (Cursor) listView1.getItemAtPosition(position);

            // display the selected country
            String countryCode =
                    cursor.getString(cursor.getColumnIndexOrThrow(BienDb.KEY_CODE));
            Toast.makeText(getApplicationContext(),
                    countryCode, Toast.LENGTH_SHORT).show();

            String rowId =
                    cursor.getString(cursor.getColumnIndexOrThrow(BienDb.KEY_ROWID));

        });

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BienDb.KEY_ROWID,
                BienDb.KEY_CODE,
                BienDb.KEY_NAME,
                BienDb.KEY_TYPE};
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        dataAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_utilisateur, menu);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
