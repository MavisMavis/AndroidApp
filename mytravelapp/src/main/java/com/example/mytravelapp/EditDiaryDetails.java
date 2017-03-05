package com.example.mytravelapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditDiaryDetails extends AppCompatActivity {
    String id, title, location, img, info, date;
    EditText mDateView;
    EditText mTitleView;
    EditText mLocationView;
    TextView mImageView;
    TextView mImageDecodeView;
    EditText mDiaryView;
    String imgDecodableString;
    public String userChoosenTask;
    Button btnImageUpload;
    Button btnNewDiary;
    DatePickerDialog datePickerDialog;
    databaseHelper mydb;
    ImageView imgvw;
    private static int RESULT_LOAD_IMG = 2;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static final String MyPREFERENCES = "ProfileImage";
    public static final String ProfilePicture = "profilePicture";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary_details);
        // initiate the DB handler
        mydb = new databaseHelper(this);

        //collect our intent and populate our layout
        Intent intent = getIntent();

        id = intent.getStringExtra("diaryID");
        title = intent.getStringExtra("diaryTitle");
        location = intent.getStringExtra("diaryLocation");
        img = intent.getStringExtra("diaryImg");
        info = intent.getStringExtra("diaryInfo");
        date = intent.getStringExtra("diaryDate");

        // initiate the date picker and a button
        mDateView = (EditText) findViewById(R.id.edit_diary_date);
        mDateView.setText(date);
        // perform click event on edit text
        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditDiaryDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                mDateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // initiate the diary title
        mTitleView = (EditText) findViewById(R.id.edit_diary_title);
        mTitleView.setText(title);
        // initiate the diary location
        mLocationView = (EditText) findViewById(R.id.edit_diary_location);
        mLocationView.setText(location);
        // initiate the diary image
        mImageView = (TextView) findViewById(R.id.editImageViewDescription);
        mImageDecodeView = (TextView) findViewById(R.id.editImageDecodeView);
        // initiate image button
        btnImageUpload = (Button) findViewById(R.id.btnEditImageUpload);
        btnImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                //Intent intent = new Intent(ContentActivity.this, NewDiary.class);
                //startActivity(intent);
            }
        });

        // initiate the diary entry
        mDiaryView = (EditText) findViewById(R.id.edit_diary_diary);
        mDiaryView.setText(info);
        // initiate the button to create new diary entry
        btnNewDiary = (Button) findViewById(R.id.btnEditDiary);
        btnNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDiary();
            }
        });
    }

    // function to insert a new diary entry
    private void updateDiary() {
        // Store values of the new diary entry
        String date = mDateView.getText().toString();
        String title = mTitleView.getText().toString();
        String location = mLocationView.getText().toString();
        String image = mImageDecodeView.getText().toString();
        if(mImageView.getText().toString().contains("Example")){
            image = img;
        }

        String diary = mDiaryView.getText().toString();
        int success = mydb.editDiaryRecord(id, title, location, image, diary, date);

        if(success > 0){
            Toast.makeText(EditDiaryDetails.this, "Edit Diary Entry Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditDiaryDetails.this, ContentActivity.class);
            startActivity(intent);
        }
    }

    //Checking current Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == RESULT_LOAD_IMG)
            {
                try {
                    // When an Image is picked
                    if (resultCode == RESULT_OK && null != data) {
                        // Get the Image from data
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        String encodeImage = getStringImage(bm);
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        //Toast.makeText(this, "Image : "+ encodeImage, Toast.LENGTH_LONG).show();
                        mImageView.setText(selectedImage.toString());
                        mImageDecodeView.setText(encodeImage);


                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    //Check user selection, and handle permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Use Camera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    //Camera Intent and Gallery Intent methods
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    //If select from Gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                String encodeImage = getStringImage(bm);

                //share preference

                //Open SP Editor and write into it via putString method
                SharedPreferences.Editor editor = sharedpreferences.edit();

                //the SP key-value to your write (putString)
                editor.putString(ProfilePicture, encodeImage);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgvw.setImageBitmap(bm);
    }


    //If use Camera
    private void onCaptureImageResult(Intent data) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String encodeImage = getStringImage(thumbnail);
        //share preference

        //Open SP Editor and write into it via putString method
        SharedPreferences.Editor editor = sharedpreferences.edit();

        //the SP key-value to your write (putString)
        editor.putString(ProfilePicture, encodeImage);
        editor.commit();

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgvw.setImageBitmap(thumbnail);
    }

    //convert image bitmap to String (using Base 64)
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //convert String to Bitmap format
    public static Bitmap decodeToBase64(String input)
    {
        byte[] decodeByte = Base64.decode(input, 0);

        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}
