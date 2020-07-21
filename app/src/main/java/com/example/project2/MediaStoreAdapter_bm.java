package com.example.project2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class MediaStoreAdapter_bm extends RecyclerView.Adapter<MediaStoreAdapter_bm.ViewHolder> {

    private ArrayList<String> mData;
    private Context context;

    public MediaStoreAdapter_bm(ArrayList<String> list, Context context) {
        this.mData = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_image_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String bmstring = mData.get(position);

//        Glide.with(context)
//                .load(mData(position))
//                .centerCrop()
//                .override(96, 96)
//                .into(holder.getImageView());
        holder.getImageView().setImageBitmap(BitmapConverter.StringToBitmap(bmstring));

    }
    //새로 만든 함수 ---희주
    public void addItem(String bmstring){
        mData.add(bmstring);
    }
    public void clear(){
        mData.clear();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.mediastoreImageView);
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }
}
