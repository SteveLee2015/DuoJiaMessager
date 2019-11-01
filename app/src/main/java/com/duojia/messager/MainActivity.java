package com.duojia.messager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> permissions = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
        }
    }


}
