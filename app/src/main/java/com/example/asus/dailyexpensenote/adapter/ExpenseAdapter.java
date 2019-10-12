package com.example.asus.dailyexpensenote.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.dailyexpensenote.R;
import com.example.asus.dailyexpensenote.activity.MainActivity;
import com.example.asus.dailyexpensenote.database.MyDBHelper;
import com.example.asus.dailyexpensenote.fragment.BottomSheetFragment;
import com.example.asus.dailyexpensenote.fragment.ExpensesFragment;
import com.example.asus.dailyexpensenote.model_class.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpenseAdapter() {

    }

    public ExpenseAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_design,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        Expense expense = expenseList.get(position);

        viewHolder.expenseTypeTV.setText(expense.getExpenseType());
        viewHolder.expenseDateTV.setText(expense.getExpenseDate());
        viewHolder.expenseAmountTV.setText("৳ "+expense.getExpenseAmount());

        //recycler view item click event to show details
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(context).inflate(R.layout.fragment_bottom_sheet, null);

                BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(view);
                dialog.show();
            }
        });

        //recycler view more button click action
        viewHolder.moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,viewHolder.moreIV);
                popupMenu.inflate(R.menu.edit_menu_item);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.updateOptionId:
                                //update option click action
                                Toast.makeText(context, ""+item, Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.deleteOptionId:
                                //delete option click action
                                Cursor cursor = ExpensesFragment.myDBHelper.getData("SELECT id FROM expense");
                                List<Integer> id = new ArrayList<>();

                                while (cursor.moveToNext()){
                                    id.add(cursor.getInt(0));
                                }
                                showDeleteDialog(id.get(position));

                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    //show delete dialog to delete
    private void showDeleteDialog( final int rowId) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setTitle("Warning!");
        deleteDialog.setMessage("Are you sure to delete?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ExpensesFragment.myDBHelper.deleteDataFromDatabase(rowId);
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(context, "Exception: "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView expenseTypeTV,expenseDateTV,expenseAmountTV;
        private ImageView moreIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTypeTV = itemView.findViewById(R.id.expenseTypeTVId);
            expenseDateTV = itemView.findViewById(R.id.expenseDateTVId);
            expenseAmountTV = itemView.findViewById(R.id.expenseAmountTVId);
            moreIV = itemView.findViewById(R.id.moreIVId);
        }
    }
}
