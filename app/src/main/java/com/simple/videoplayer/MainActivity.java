package com.simple.videoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private int playbackMode = 1;

    private static final int REQUEST_CODE = 6384;
    private static final String TAG = "VideoPlayer";
    private String savedPath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Create a simple button to start the file chooser process
        Button chooseButton = (Button) findViewById(R.id.chooseButton);
        chooseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            // Display the file chooser dialog
            showChooser();
            }
        });

        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedPath == null) {
                    showAlertDialog("Error", "Please choose a video for playback.");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("VIDEO_PATH", savedPath);
                intent.putExtra("PLAYBACK_MODE", playbackMode);
                startActivity(intent);
            }
        });

        // Create the mode dropdown spinner
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.modeSpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> modes = new ArrayList<String>();
        modes.add("Mode 1 - Fixed controls");
        modes.add("Mode 2 - Disappearing controls");
        modes.add("Mode 3 - Randomly appearing controls");
        modes.add("Mode 4 - Random colored controls");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        playbackMode = position+1;
        Log.i(TAG, "Updated play back mode to: "+playbackMode);
        if (playbackMode == 3) {
            showAlertDialog("Unsupported", "Mode 3 is not yet supported. No controls will be shown.");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, "Choose VideoActivity");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
            Log.e(TAG, "Unable to start file chooser activity.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        DisplayMetrics dm;
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        savedPath = FileUtils.getPath(this, uri);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}