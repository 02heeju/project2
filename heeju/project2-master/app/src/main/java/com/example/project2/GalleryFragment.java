package com.example.project2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import java.io.File;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;



public class GalleryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, MediaStoreAdapter.OnClickThumbListener{

    // 로그인 액티비티로부터 넘겨받은 로그인 정보
    String user_name;
    String user_email;

    public GalleryFragment(String user_name, String user_email) {
        Log.d("sequence","Gallery Fragment Constructor");
        this.user_name = user_name;
        this.user_email = user_email;
    }

    private final static int READ_EXTERNAL_STORAGE_PERMMISSION_RESULT = 0;
    private final static int MEDIASTORE_LOADER_ID = 0;
    private RecyclerView mThumbnailRecyclerView;
    private MediaStoreAdapter mMediaStoreAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.gallery_layout,container,false);
        //-----------------------------------Tab2----------------------------------------------
        mThumbnailRecyclerView = (RecyclerView) view.findViewById(R.id.thumbnailRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 3);
        mThumbnailRecyclerView.setLayoutManager(gridLayoutManager);
        mMediaStoreAdapter = new MediaStoreAdapter(this.getActivity(), this, user_name, getActivity());
        mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);

        checkReadExternalStoragePermission();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case READ_EXTERNAL_STORAGE_PERMMISSION_RESULT:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkReadExternalStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Start cursor loader
                getLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this.getContext(), "App needs to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_PERMMISSION_RESULT);
            }
        } else {
            // Start cursor loader
            getLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return new CursorLoader(
                getActivity(),
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mMediaStoreAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMediaStoreAdapter.changeCursor(null);
    }

    @Override
    public void OnClickImage(Uri imageUri) {
        Intent fullScreenIntent = new Intent(getContext(), FullScreenImageActivity.class);
        fullScreenIntent.setData(imageUri);
        startActivity(fullScreenIntent);
    }

    @Override
    public void OnClickVideo(Uri videoUri) {
        Intent videoPlayIntent = new Intent(getContext(), VideoPlayActivity.class);
        videoPlayIntent.setData(videoUri);
        startActivity(videoPlayIntent);

    }
}