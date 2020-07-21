package com.example.project2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class GalleryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, MediaStoreAdapter.OnClickThumbListener, MediaStoreAdapter_upload.OnClickThumbListener_up{

    // 로그인 액티비티로부터 넘겨받은 로그인 정보
    String user_name;
    String user_email;

    // 레트로핏 사용을 위한 변수...
    Retrofit retrofitClient  = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService = retrofitClient.create(IMyService.class);

    public GalleryFragment(String user_name, String user_email) {
        Log.d("sequence","Gallery Fragment Constructor");
        this.user_name = user_name;
        this.user_email = user_email;
    }

    private final static int READ_EXTERNAL_STORAGE_PERMMISSION_RESULT = 0;
    private final static int MEDIASTORE_LOADER_ID = 0;
    private RecyclerView mThumbnailRecyclerView;
    private MediaStoreAdapter mMediaStoreAdapter;
    private MediaStoreAdapter_upload mMediaStoreAdapter_upload;
    private MediaStoreAdapter_bm mMediaStoreAdapter_bm;
    private ArrayList<String> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.gallery_layout,container,false);
        //-----------------------------------Tab2----------------------------------------------
        mThumbnailRecyclerView = (RecyclerView) view.findViewById(R.id.thumbnailRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 3);
        mThumbnailRecyclerView.setLayoutManager(gridLayoutManager);
        mMediaStoreAdapter = new MediaStoreAdapter(this.getActivity(), this, user_name, getActivity());
        mMediaStoreAdapter_upload = new MediaStoreAdapter_upload(this.getActivity(), this, user_name, getActivity());
        mMediaStoreAdapter_bm = new MediaStoreAdapter_bm(this.getActivity(),this, list);

        //phone 버튼 클릭시 핸드폰의 이미지 보이기
        Button phone_button = view.findViewById(R.id.phone_button);
        phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThumbnailRecyclerView.removeAllViewsInLayout();
                mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);
                checkReadExternalStoragePermission();
            }
        });
        //phone -> cloud 버튼 클릭시 핸드폰의 이미지 서버에 업로드
        Button toServer_button = view.findViewById(R.id.toServer_button);
        toServer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThumbnailRecyclerView.removeAllViewsInLayout();
                mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);
                checkReadExternalStoragePermission();
            }
        });

        //cloud 버튼 클릭시 서버에서 이미지 가져오기
        Button download_button = view.findViewById(R.id.download_button);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에서 이미지 가져오기

                compositeDisposable.add(iMyService.get_cloud_bitmap(user_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void accept(String response) throws Exception {

                                Log.e("get_cloud_bitmap result",response);
                                mMediaStoreAdapter_bm.clear();

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    Log.e("one element" ,jsonArray.get(i).toString());
                                    String bmstring = jsonArray.getJSONObject(i).getString("bmstring");

                                    if(bmstring.equals("null")) {
                                    }else{
                                        mMediaStoreAdapter_bm.addItem(bmstring);
                                        Log.e("end",jsonArray.getJSONObject(i).getString("bmstring"));
                                    }

                                }
                                mThumbnailRecyclerView.removeAllViewsInLayout();
                                mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter_bm);
                                Log.e("adapter","FIN");

                            }
                        }));

            }
        });

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

    @Override
    public void OnClickImage_up(Uri imageUri) {
        Intent fullScreenIntent = new Intent(getContext(), FullScreenImageActivity.class);
        fullScreenIntent.setData(imageUri);
        startActivity(fullScreenIntent);
    }

    @Override
    public void OnClickVideo_up(Uri videoUri) {
        Intent videoPlayIntent = new Intent(getContext(), VideoPlayActivity.class);
        videoPlayIntent.setData(videoUri);
        startActivity(videoPlayIntent);
    }

//    @Override
//    public void OnClickImage_bm(String bmstring) {
//        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView_bm);
//        fullScreenImageView.setImageBitmap(BitmapConverter.StringToBitmap(bmstring));
//    }
//
//    @Override
//    public void OnClickVideo_bm(String bmstring) {
//    }
}