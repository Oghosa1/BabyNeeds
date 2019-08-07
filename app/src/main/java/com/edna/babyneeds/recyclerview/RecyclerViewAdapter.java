package com.edna.babyneeds.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edna.babyneeds.R;
import com.edna.babyneeds.data.DatabaseHandler;
import com.edna.babyneeds.model.Items;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Items> mItemsList;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Context context, List<Items> itemsList) {
        this.mContext = context;
        this.mItemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View lView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(lView, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //int i represent the position of the item

        Items lItems = mItemsList.get(i);
        viewHolder.ItemName.setText(MessageFormat.format("Item: {0}", lItems.getItemName()));
        viewHolder.ItemColor.setText(MessageFormat.format("Color: {0}", lItems.getItemColor()));
        viewHolder.ItemDate.setText(MessageFormat.format("Added on: {0}", lItems.getDateItemAdded()));
        viewHolder.ItemSize.setText(MessageFormat.format("Size: {0} ", lItems.getItemSize()));
        viewHolder.ItemQuant.setText(MessageFormat.format("Quantity: {0}", lItems.getItemQuant()));

    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int id;
        public TextView ItemName;
        public TextView ItemColor;
        public TextView ItemSize;
        public TextView ItemQuant;
        public TextView ItemDate;
        public Button DeleteButton;
        public Button SaveButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            mContext = ctx;
            ItemName = itemView.findViewById(R.id.item_name_list);
            ItemColor = itemView.findViewById(R.id.item_color_list);
            ItemSize = itemView.findViewById(R.id.item_size_list);
            ItemQuant = itemView.findViewById(R.id.item_quant_list);
            ItemDate = itemView.findViewById(R.id.item_date_list);
            DeleteButton = itemView.findViewById(R.id.delete_button);
            SaveButton = itemView.findViewById(R.id.edit_button);

            DeleteButton.setOnClickListener(this);
            SaveButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
            Items lItems = mItemsList.get(position);
            switch (v.getId()) {
                case R.id.delete_button:
                    //Delete Item
                    deleteItem(lItems.getId());
                    break;
                case R.id.edit_button:
                    //Edit Item
                    editItem(lItems);
                    break;
            }
        }

        private void editItem(final Items newItem) {
            //Todo: Populate the popup with object data
           // Items lItems = mItemsList.get(getAdapterPosition());


            mBuilder = new AlertDialog.Builder(mContext);
            mLayoutInflater = LayoutInflater.from(mContext);
            View lView = mLayoutInflater.inflate(R.layout.popup, null);

            Button mPopupSaveButton;
            final EditText mPopupBabyItem;
            final EditText mPopupQuanatity;
            final EditText mPopupColor;
            final EditText mPopupSize;
            TextView lTitle;

            mPopupBabyItem = lView.findViewById(R.id.baby_Item_Name_Popup);
            mPopupQuanatity = lView.findViewById(R.id.baby_Quantity_Popup);
            mPopupSize = lView.findViewById(R.id.baby_Size_Popup);
            mPopupColor = lView.findViewById(R.id.baby_Color_Popup);
            lTitle = lView.findViewById(R.id.title);
            mPopupSaveButton = lView.findViewById(R.id.save_Button_Popup);

            mPopupSaveButton.setText(R.string.update_text);
            lTitle.setText(R.string.edit_item);

            mPopupBabyItem.setText(newItem.getItemName());
            mPopupQuanatity.setText(String.valueOf(newItem.getItemQuant()));
            mPopupColor.setText(newItem.getItemColor());
            mPopupSize.setText(String.valueOf(newItem.getItemSize()));


            mBuilder.setView(lView);
            mDialog = mBuilder.create();
            mDialog.show();

            mPopupSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //First Instantiate the database
                    DatabaseHandler lHandler = new DatabaseHandler(mContext);

                    //Update Items
                    newItem.setItemName(mPopupBabyItem.getText().toString());
                    newItem.setItemSize(Integer.parseInt(mPopupSize.getText().toString()));
                    newItem.setItemQuant(Integer.parseInt(mPopupQuanatity.getText().toString()));
                    newItem.setItemColor(mPopupColor.getText().toString());

                    lHandler.updateItem(newItem);
                    notifyItemChanged(getAdapterPosition(), newItem); //Important!!!!!!!!!!!!!!!!!!

                    mDialog.dismiss();
                }
            });


        }

        private void deleteItem(final int id) {
            mBuilder = new AlertDialog.Builder(mContext);
            mLayoutInflater = LayoutInflater.from(mContext);
            View lView = mLayoutInflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = lView.findViewById(R.id.confirmation_no);
            Button yesButton = lView.findViewById(R.id.confirmation_yes);

            mBuilder.setView(lView);
            mDialog = mBuilder.create();
            mDialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler lDatabaseHandler = new DatabaseHandler(mContext);
                    lDatabaseHandler.deleteItem(id);
                    mItemsList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    mDialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();

                }
            });


        }
    }
}
