package soa.work.scheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static soa.work.scheduler.Constants.CARPENTER;
import static soa.work.scheduler.Constants.ELECTRICIAN;
import static soa.work.scheduler.Constants.MECHANIC;
import static soa.work.scheduler.Constants.PAINTER;
import static soa.work.scheduler.Constants.PLUMBER;

public class WorkersHistoryAdapter extends RecyclerView.Adapter<WorkersHistoryAdapter.ViewHolder> {

    private ItemCLickListener itemCLickListener;
    private List<IndividualWork> list;

    public WorkersHistoryAdapter(List<IndividualWork> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IndividualWork work = list.get(position);
        holder.workDescriptionTextView.setText(work.getWork_description());
        holder.createdAtTextView.setText(work.getCreated_date());

        switch (work.getWork_category()) {
            case PAINTER:
                holder.categoryImageView.setImageResource(R.drawable.ic_painter);
                break;
            case CARPENTER:
                holder.categoryImageView.setImageResource(R.drawable.ic_carpenter);
                break;
            case PLUMBER:
                holder.categoryImageView.setImageResource(R.drawable.ic_plumber);
                break;
            case MECHANIC:
                holder.categoryImageView.setImageResource(R.drawable.ic_mechanic);
                break;
            case ELECTRICIAN:
                holder.categoryImageView.setImageResource(R.drawable.ic_electrician);
        }

        if (work.getWork_completed()) {
            holder.completedStatusTextView.setText("Completed");
        } else {
            holder.completedStatusTextView.setText("Not Completed");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void setItemClickListener(ItemCLickListener itemClickListener) {
        this.itemCLickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.category_imageView)
        ImageView categoryImageView;
        @BindView(R.id.work_description_text_view)
        TextView workDescriptionTextView;
        @BindView(R.id.created_at_text_view)
        TextView createdAtTextView;
        @BindView(R.id.completed_status_text_view)
        TextView completedStatusTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemCLickListener != null) {
                itemCLickListener.onItemClick(list.get(getAdapterPosition()));
            }
        }
    }

    public interface ItemCLickListener {
        void onItemClick(IndividualWork work);
    }
}