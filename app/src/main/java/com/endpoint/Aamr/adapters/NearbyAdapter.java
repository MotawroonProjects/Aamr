package com.endpoint.Aamr.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.Aamr.R;
import com.endpoint.Aamr.activities_fragments.activity_home.client_home.fragments.fragment_home.Fragment_Client_Store;
import com.endpoint.Aamr.activities_fragments.activity_home.client_home.fragments.fragment_home.Fragment_Search;
import com.endpoint.Aamr.models.PlaceModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyHolder> {

    public int i;
    private List<PlaceModel> placeModelList;
    private Context context;
    private Fragment fragment;
    private double user_lat = 0.0, user_lng = 0.0;

    public NearbyAdapter(List<PlaceModel> placeModelList, Context context, Fragment fragment, double user_lat, double user_lng, int i) {
        this.placeModelList = placeModelList;
        this.context = context;
        this.fragment = fragment;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.i = i;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        PlaceModel placeModel = placeModelList.get(position);
        holder.BindData(placeModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceModel placeModel = placeModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Client_Store) {
                    Fragment_Client_Store fragment_client_store = (Fragment_Client_Store) fragment;
                    fragment_client_store.setItemData(placeModel);

                } else if (fragment instanceof Fragment_Search) {
                    // Fragment_Search fragment_search = (Fragment_Search) fragment;
                    //fragment_search.setItemData(placeModel);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return placeModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name, tv_address, tv_rate, tv_state, tv_distance;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_distance = itemView.findViewById(R.id.tv_distance);


        }

        public void BindData(PlaceModel placeModel) {
//            if (placeModel.getPhotosList()!=null&&placeModel.getPhotosList().size()>0)
//            {
//                String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+placeModel.getPhotosList().get(0).getPhoto_reference()+"&key="+context.getResources().getString(R.string.map_api_key);
//                Log.e("lllll",url);
//                Picasso.with(context).load(Uri.parse(url)).fit().into(image);
//
//            }else
//            {
//                Picasso.with(context).load(Uri.parse(placeModel.getIcon())).fit().into(image);
//
//            }
            if (i == 0) {
                Picasso.with(context).load(R.drawable.flag_ng).fit().into(image);
            } else if (i == 1) {
                Picasso.with(context).load(R.drawable.flag_id).fit().into(image);

            } else if (i == 2) {
                Picasso.with(context).load(R.drawable.flag_th).fit().into(image);


            } else if (i == 3) {
                Picasso.with(context).load(R.drawable.flag_ac).fit().into(image);


            } else if (i == 4) {
                Picasso.with(context).load(R.drawable.flag_af).fit().into(image);


            } else if (i == 5) {
                Picasso.with(context).load(R.drawable.flag_ae).fit().into(image);


            } else if (i == 6) {
                Picasso.with(context).load(R.drawable.flag_an).fit().into(image);


            } else if (i == 7) {
                Picasso.with(context).load(R.drawable.flag_gs).fit().into(image);


            }
            tv_name.setText(placeModel.getName());
            tv_address.setText(placeModel.getAddress());
            tv_rate.setText(String.valueOf(placeModel.getRating()));
            double distance = SphericalUtil.computeDistanceBetween(new LatLng(user_lat, user_lng), new LatLng(placeModel.getLat(), placeModel.getLng()));
            tv_distance.setText(String.format("%.2f", (distance / 1000)) + " " + context.getString(R.string.km));

            if (placeModel.isOpenNow()) {

                tv_state.setText(R.string.active);
                tv_state.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                tv_state.setText(R.string.inactive);
                tv_state.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            }
        }
    }
}
