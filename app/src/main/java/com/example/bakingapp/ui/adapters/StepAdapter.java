package com.example.bakingapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.Locale;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    ArrayList<Step> steps;

    private final StepSelectedListener mStepSelectedListener;

    public interface StepSelectedListener{
        void onStepSelected(Step step);
    }

    public void setSteps(ArrayList<Step> steps){
        this.steps = steps;
    }

    public StepAdapter(StepSelectedListener stepSelectedListener){
        mStepSelectedListener = stepSelectedListener;
        steps = new ArrayList<>();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if (position == 0) {
            holder.step.setText(steps.get(position).getShortDescription());
        } else {
            holder.step.setText(String.format(Locale.getDefault(),"%d: %s", position, steps.get(position).getShortDescription()));
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView step;
        StepViewHolder(@NonNull View itemView) {
            super(itemView);
            step = (TextView) itemView.findViewById(R.id.tv_step_short);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mStepSelectedListener.onStepSelected(steps.get(position));
        }
    }
}
