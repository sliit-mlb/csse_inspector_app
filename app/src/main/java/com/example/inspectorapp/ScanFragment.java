package com.example.inspectorapp;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Scanner;

public class ScanFragment extends Fragment {

    CodeScanner codeScanner;
    CodeScannerView scannerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scan, container, false);

        final Activity activity = getActivity();

        scannerView = view.findViewById(R.id.scanner_view);

        codeScanner = new CodeScanner(activity,scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String resul = result.getText().toString();
                        int resInt = Integer.parseInt(resul);
                        if(resInt<9999) {
                            Toast.makeText(getActivity().getApplicationContext(), "Valid User", Toast.LENGTH_LONG).show();
                        }else{
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.custom_toast_layout));
                            Toast toast = new Toast(getActivity().getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                codeScanner.startPreview();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //codeScanner.startPreview();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getActivity(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    /*@Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }*/
}
