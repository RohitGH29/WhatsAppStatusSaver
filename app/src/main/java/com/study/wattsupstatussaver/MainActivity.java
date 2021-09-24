package com.study.wattsupstatussaver;

import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int requestCode=1;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Adapter adapter;

    File[] files;
    ArrayList<ModelClass> fileslist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        swipeRefreshLayout =findViewById(R.id.swipeRecyclerView);
        checkPermission();




      swipeRefreshLayout.setOnRefreshListener(() -> {
          swipeRefreshLayout.setRefreshing(true);
          setuplayout();
          {
              new Handler().postDelayed(() -> {
                  swipeRefreshLayout.setRefreshing(false);
                  //Toast.makeText(MainActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();

              },2000);
          }
      });

    }


    @SuppressLint("NotifyDataSetChanged")
    private void setuplayout() {

        fileslist.clear();
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

       recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new Adapter(MainActivity.this,getData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

    private ArrayList<ModelClass> getData() {

        ModelClass f;
        String targetpath= Environment.getExternalStorageDirectory().getAbsolutePath()+Constant.FOLDER_NAME+"Media/.Statuses";
        File targetdir=new File(targetpath);
        files=targetdir.listFiles();
        assert files != null;
        for (File file : files) {
            f = new ModelClass();
            f.setUri(Uri.fromFile(file));
            f.setPath(file.getAbsolutePath());
            f.setFilename(file.getName());
            if (!f.getUri().toString().endsWith(".nomedia")) {

                fileslist.add(f);
            }
        }
              return  fileslist;
    }

    private void checkPermission() {

        if(SDK_INT>23)
        {
           if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
           {
               //Permission already allow.
               setuplayout();

           }
           else {

               ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);


               Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

               // ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
           }

        }
        else 
            {
                Toast.makeText(getApplicationContext(), "Already", Toast.LENGTH_SHORT).show();
                setuplayout();
            }


    }





}
