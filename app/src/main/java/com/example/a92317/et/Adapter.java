package com.example.a92317.et;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Emo> mEmoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView sentence, time;
        ImageView label;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            sentence = (TextView) view.findViewById(R.id.sentence);
            time = (TextView) view.findViewById(R.id.time);
            label = (ImageView) view.findViewById(R.id.label);
        }
    }

    public Adapter(List<Emo> emoList) {
        mEmoList = emoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        view.getBackground().setAlpha(204);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Emo emo = mEmoList.get(position);
        holder.sentence.setText(emo.getSentence());
        holder.time.setText(emo.getTime());
        holder.label.setImageResource(MainActivity.Icon.valueOf(emo.getLabel()).getIconRes());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return mEmoList.size();
    }

    public void remove(int position) {
        mEmoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mEmoList.size() - position);
    }

    public void add(int position, Emo emo) {
        mEmoList.add(position, emo);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mEmoList.size() - position);
    }

    public void change(int position, Emo emo) {
        mEmoList.remove(position);
        mEmoList.add(position, emo);
        notifyItemChanged(position);
    }
}
