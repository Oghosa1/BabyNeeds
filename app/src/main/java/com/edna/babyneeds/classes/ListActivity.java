package com.edna.babyneeds.classes;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edna.babyneeds.R;
import com.edna.babyneeds.data.DatabaseHandler;
import com.edna.babyneeds.model.Items;
import com.edna.babyneeds.recyclerview.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mViewAdapter;
    private List<Items> mItemsList;
    private DatabaseHandler mDatabaseHandler;
    private FloatingActionButton mFloatingActionButton;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;

    private Button mPopupSaveButton;
    private EditText mPopupBabyItem;
    private EditText mPopupQuanatity;
    private EditText mPopupColor;
    private EditText mPopupSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = findViewById(R.id.Recycler_View);
        mFloatingActionButton = findViewById(R.id.fab);

        mDatabaseHandler = new DatabaseHandler(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemsList = new ArrayList<>();
        //Get Items from Database
        mItemsList = mDatabaseHandler.getAllItems();

        for (Items lItems: mItemsList) {
            Log.d("List", "onCreate: List of items :  " + lItems.getItemName());
        }

        mViewAdapter = new RecyclerViewAdapter(this, mItemsList);
        mRecyclerView.setAdapter(mViewAdapter);
        mViewAdapter.notifyDataSetChanged();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePopDialog();
            }
        });

    }

    private void CreatePopDialog() {
        mBuilder = new AlertDialog.Builder(this);
        View lView = getLayoutInflater().inflate(R.layout.popup, null);

        mPopupBabyItem = lView.findViewById(R.id.baby_Item_Name_Popup);
        mPopupQuanatity = lView.findViewById(R.id.baby_Quantity_Popup);
        mPopupSize = lView.findViewById(R.id.baby_Size_Popup);
        mPopupColor = lView.findViewById(R.id.baby_Color_Popup);

        mPopupSaveButton = lView.findViewById(R.id.save_Button_Popup);


        mBuilder.setView(lView);
        mDialog = mBuilder.create();
        mDialog.show();

        mPopupSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopupBabyItem.getText().toString().isEmpty() &&
                        !mPopupQuanatity.getText().toString().isEmpty() &&
                        !mPopupSize.getText().toString().isEmpty()) {
                    saveItem(v);
                }else {
                    Snackbar.make(v, "Empty Field only allowed for colors", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }
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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();

            }
        }, 1500);
    }
}
