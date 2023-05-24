package com.example.mobile_tp;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BienEdit extends Activity implements OnClickListener{

    private Spinner typeList;
    private Button save, delete;
    private String mode;
    private EditText code, name;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        // get the values passed to the activity from the calling activity
        // determine the mode - add, update or delete
        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        // get references to the buttons and attach listeners
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);


        // create a dropdown for users to select various continents
        typeList = (Spinner) findViewById(R.id.typeList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeList.setAdapter(adapter);

        // if in add mode disable the delete option
        if(mode.trim().equalsIgnoreCase("add")){
            delete.setEnabled(false);
        }
        // get the rowId for the specific country
        else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadBienInfo();
        }

    }

    public void onClick(View v) {

        // get values from the spinner and the input text fields
        String myType = typeList.getSelectedItem().toString();
        String myCode = code.getText().toString();
        String myName = name.getText().toString();

        // check for blanks
        if(myCode.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please ENTER bien code", Toast.LENGTH_LONG).show();
            return;
        }

        // check for blanks
        if(myName.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please ENTER bien name", Toast.LENGTH_LONG).show();
            return;
        }


        switch (v.getId()) {
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(BienDb.KEY_CODE, myCode);
                values.put(BienDb.KEY_NAME, myName);
                values.put(BienDb.KEY_TYPE, myType);

                // insert a record
                if(mode.trim().equalsIgnoreCase("add")){
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                }
                // update a record
                else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

            case R.id.delete:
                // delete a record
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                finish();
                break;

            // More buttons go here (if any) ...

        }
    }

    // based on the rowId get all information from the Content Provider
    // about that country
    private void loadBienInfo(){

        String[] projection = {
                BienDb.KEY_ROWID,
                BienDb.KEY_CODE,
                BienDb.KEY_NAME,
                BienDb.KEY_TYPE};
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myCode = cursor.getString(cursor.getColumnIndexOrThrow(BienDb.KEY_CODE));
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(BienDb.KEY_NAME));
            String myContinent = cursor.getString(cursor.getColumnIndexOrThrow(BienDb.KEY_TYPE));
            code.setText(myCode);
            name.setText(myName);
            typeList.setSelection(getIndex(typeList, myContinent));
        }


    }

    // this sets the spinner selection based on the value
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

}
