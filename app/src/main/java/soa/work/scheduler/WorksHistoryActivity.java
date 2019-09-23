package soa.work.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static soa.work.scheduler.Constants.USER_ACCOUNTS;
import static soa.work.scheduler.Constants.WORKS_POSTED;

public class WorksHistoryActivity extends AppCompatActivity {

    @BindView(R.id.history_recycler_view)
    RecyclerView historyRecyclerView;
    @BindView(R.id.no_history)
    TextView noHistoryTextView;

    private WorkersHistoryAdapter workersHistoryAdapter;
    private ArrayList<IndividualWork> workList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_history);

        ButterKnife.bind(this);

        workersHistoryAdapter = new WorkersHistoryAdapter(workList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setAdapter(workersHistoryAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userAccounts = database.getReference(USER_ACCOUNTS);
        DatabaseReference userAccount = userAccounts.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference worksPosted = userAccount.child(WORKS_POSTED);

        worksPosted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                workList.clear();
                if (dataSnapshot.getChildrenCount() == 0) {
                    noHistoryTextView.setVisibility(View.VISIBLE);
                    workersHistoryAdapter.notifyDataSetChanged();
                    return;
                }
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    workList.add(item.getValue(IndividualWork.class));
                }
                noHistoryTextView.setVisibility(View.GONE);
                workersHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WorksHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
