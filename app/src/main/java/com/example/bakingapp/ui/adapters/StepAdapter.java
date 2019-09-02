package com.example.bakingapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.Locale;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    ArrayList<Steps> steps;

    public void setSteps(ArrayList<Steps> steps){
        this.steps = steps;
    }

    public StepAdapter(){
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

    class StepViewHolder extends RecyclerView.ViewHolder {
        TextView step;
        StepViewHolder(@NonNull View itemView) {
            super(itemView);
            step = (TextView) itemView.findViewById(R.id.tv_step_short);
        }
    }
}
