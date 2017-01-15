package com.application.norgevz.myticketon.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.norgevz.myticketon.DashboardScreen;
import com.application.norgevz.myticketon.R;
import com.application.norgevz.myticketon.global.MyApplication;
import com.application.norgevz.myticketon.models.Order;
import com.application.norgevz.myticketon.models.Ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by norgevz on 12/14/2016.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    public List<Ticket> data = new ArrayList<>();

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {
        void iconImageViewOnClick(View v, Ticket ticket, int position);
    }


    public TicketsAdapter(Context context, List<Ticket> _data, MyAdapterListener listener){
        inflater = LayoutInflater.from(context);
        this.data = _data;
        onClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = inflater.inflate(R.layout.custom_row, parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public void update(ArrayList<Ticket> datas){
        if(data != null){
            data.clear();
            data.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void updateListItem(int position){
        this.notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Ticket ticket = data.get(position);
        holder.showNameTextView.setText(ticket.showName);
        holder.theaterNameTextView.setText(ticket.theaterName);
        holder.startDateTextView.setText(parseDate(ticket.StartTime));
        int item = ticket.order.Printed ?
                R.drawable.unredeem_buttom_background : R.drawable.redeem_buttom_background;
        Drawable drawable = ContextCompat.getDrawable(MyApplication.getAppContext(), item);
        holder.redeemStateImageButton.setBackground(drawable);
    }

    private String parseDate(Date date){

        return String.format("%1$s %2$tB %2$td, %2$tY", "", date );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView showNameTextView;
        TextView theaterNameTextView;
        TextView startDateTextView;
        ImageButton redeemStateImageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.invalidate();
            showNameTextView = (TextView) itemView.findViewById(R.id.show_name_text_view);
            theaterNameTextView = (TextView) itemView.findViewById(R.id.theater_name_text_view);
            startDateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
            redeemStateImageButton = (ImageButton) itemView.findViewById(R.id.redeem_image_button);
            redeemStateImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPressed = getAdapterPosition();
            onClickListener.iconImageViewOnClick(v , data.get(itemPressed), itemPressed);
        }
    }
}
