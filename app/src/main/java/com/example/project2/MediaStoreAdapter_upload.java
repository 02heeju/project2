package com.example.project2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class MediaStoreAdapter_upload extends RecyclerView.Adapter<MediaStoreAdapter_upload.ViewHolder> {


    private Cursor mMediaStoreCursor;
    private final Context context;
    private OnClickThumbListener_up mOnClickThumbListener;
    private String user_name;
    private Activity GalleryFragment_ac;


    public MediaStoreAdapter_upload(Context context, OnClickThumbListener_up mOnClickThumbListener, String user_name, Activity GalleryFragment_ac) {
        Log.e("MediaStoreAdapter", "context" + context.toString());
        this.context = context;
        this.mOnClickThumbListener = mOnClickThumbListener;
        this.user_name = user_name;
        this.GalleryFragment_ac = GalleryFragment_ac;
    }

    public interface OnClickThumbListener_up {
        void OnClickImage_up(Uri imageUri);
        void OnClickVideo_up(Uri videoUri);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_image_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context)
                .load(getUriFromMediaStore(position))
                .centerCrop()
                .override(96, 96)
                .into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        return (mMediaStoreCursor == null) ? 0 : mMediaStoreCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.mediastoreImageView);
            mImageView.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        @Override
        public void onClick(View v) {
            getOnClickUri(getAdapterPosition());
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private Uri getUriFromMediaStore(int position) {
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);

        String dataString = mMediaStoreCursor.getString(dataIndex);
        Uri mediaUri = Uri.parse("file://" + dataString);
        return mediaUri;
    }

    private void getOnClickUri(int position) {
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);
        String dataString = mMediaStoreCursor.getString(dataIndex);
        String authorities = context.getPackageName() + ".fileprovider";
        Uri mediaUri = FileProvider.getUriForFile(context, authorities, new File(dataString));

        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                mOnClickThumbListener.OnClickImage_up(mediaUri);
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                mOnClickThumbListener.OnClickVideo_up(mediaUri);
                break;
            default:
        }
    }
}
