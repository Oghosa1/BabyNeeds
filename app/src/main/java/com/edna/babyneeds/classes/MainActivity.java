package com.edna.babyneeds.classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.edna.babyneeds.R;
import com.edna.babyneeds.data.DatabaseHandler;
import com.edna.babyneeds.model.Items;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private Button mPopupSaveButton;
    private EditText mPopupBabyItem;
    private EditText mPopupQuanatity;
    private EditText mPopupColor;
    private EditText mPopupSize;
    private DatabaseHandler mDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHandler = new DatabaseHandler(this);

        byPassActivity();

        // Check if Item was saved
        List<Items> lItemsList = mDatabaseHandler.getAllItems();
        for (Items lItems: lItemsList){
            Log.d("Main", "Item: " + lItems.getItemName());
            Log.d("Main", "Color: " + lItems.getItemColor());
            Log.d("Main", "Date: " + lItems.getDateItemAdded());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {
        if (mDatabaseHandler.getItemCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void createPopupDialog() {
        mBuilder = new AlertDialog.Builder(this);
        View lView = getLayoutInflater().inflate(R.layout.popup, null);

        mPopupBabyItem = lView.findViewById(R.id.baby_Item_Name_Popup);
        mPopupQuanatity = lView.findViewById(R.id.baby_Quantity_Popup);
        mPopupSize = lView.findViewById(R.id.baby_Size_Popup);
        mPopupColor = lView.findViewById(R.id.baby_Color_Popup);

        mPopupSaveButton = lView.findViewById(R.id.save_Button_Popup);

        mPopupSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save Each baby Item to Database
                if (!mPopupBabyItem.getText().toString().isEmpty() &&
                        !mPopupQuanatity.getText().toString().isEmpty() &&
                        !mPopupSize.getText().toString().isEmpty()) {
                    saveItem(v);
                }else {
                    Snackbar.make(v, "Empty Field only allowed for colors", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(lView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    // TODO: Save Each baby Item to Database
    private void saveItem(View view) {
        Items lItems = new Items();

        String newItem = mPopupBabyItem.getText().toString().trim();
        String newColor = mPopupColor.getText().toString().trim();
        int newQuant = Integer.parseInt(mPopupQuanatity.getText().toString().trim());
        int newSize = Integer.parseInt(mPopupSize.getText().toString().trim());

        lItems.setItemName(newItem);
        lItems.setItemColor(newColor);
        lItems.setItemQuant(newQuant);
        lItems.setItemSize(newSize);

        mDatabaseHandler.addItem(lItems);

        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                // TODO: Once saved move to the next screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();

            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
