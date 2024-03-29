package soa.work.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static soa.work.scheduler.Constants.CARPENTER;
import static soa.work.scheduler.Constants.ELECTRICIAN;
import static soa.work.scheduler.Constants.MECHANIC;
import static soa.work.scheduler.Constants.PAINTER;
import static soa.work.scheduler.Constants.PHONE_NUMBER;
import static soa.work.scheduler.Constants.PLUMBER;
import static soa.work.scheduler.Constants.USER_ACCOUNTS;
import static soa.work.scheduler.Constants.WORK_CATEGORY;

public class ChooseWorkCategoryActivity extends AppCompatActivity  {

    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    private ArrayList<Category> categories = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_work_category);

        ButterKnife.bind(this);

        categories.add(new Category(MECHANIC, R.drawable.ic_mechanic));
        categories.add(new Category(PLUMBER, R.drawable.ic_plumber));
        categories.add(new Category(ELECTRICIAN, R.drawable.ic_electrician));
        categories.add(new Category(CARPENTER, R.drawable.ic_carpenter));
        categories.add(new Category(PAINTER, R.drawable.ic_painter));


        CategoryRecyclerViewAdapter categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(categories);
        categoryRecyclerViewAdapter.setItemClickListener(this::askPhoneNumber);
        categoriesRecyclerView.setAdapter(categoryRecyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        categoriesRecyclerView.setLayoutManager(manager);
    }

    private void askPhoneNumber(Category category) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Contact Info is required");
        alertDialog.setMessage("Enter Phone number");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("DONE",
                (dialog, which) -> {
                    String phoneNumber = input.getText().toString();
                    if (phoneNumber.length() < 10) {
                        Toast.makeText(ChooseWorkCategoryActivity.this, "Phone number is not valid", Toast.LENGTH_SHORT).show();
                        askPhoneNumber(category);
                    } else {
                        FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
                        DatabaseReference currentUserAccount = databaseRef.getReference(USER_ACCOUNTS).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        currentUserAccount.child(WORK_CATEGORY).setValue(category.getCategoryTitle());
                        currentUserAccount.child(PHONE_NUMBER).setValue(phoneNumber);
                        OneSignal.sendTag(WORK_CATEGORY, category.getCategoryTitle());
                        startActivity(new Intent(ChooseWorkCategoryActivity.this, WorkersActivity.class));
                        finish();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChooseWorkCategoryActivity.this, MainActivity.class));
    }
}


