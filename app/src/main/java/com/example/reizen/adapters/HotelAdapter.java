package com.example.reizen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.reizen.R;
import com.example.reizen.interfaces.OnClickListeners;
import com.example.reizen.models.HotelModel;
import com.example.reizen.models.PlaceModel;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder>  {
    Context context;
    ArrayList<HotelModel> list;

    OnClickListeners onClickListeners;

    public HotelAdapter(Context context, ArrayList<HotelModel> list, OnClickListeners listener) {
        this.context = context;
        this.list = list;
        onClickListeners = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HotelModel hotelModel = list.get(position);

        holder.placeName.setText(hotelModel.getName());
        holder.location.setText(hotelModel.getLocation());

        Glide.with(context)
                .load(hotelModel.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading) // Placeholder image
                        .error(R.drawable.broken_image) // Error image in case of loading failure
                )
                .into(holder.placeImageIV);

        holder.placeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListeners.onClick(hotelModel);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView placeName,location;
        ImageView placeImageIV;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.placeNameTV);
            location = itemView.findViewById(R.id.placeLocationTV);
            placeImageIV = itemView.findViewById(R.id.placeImageIV);



        }
    }
}