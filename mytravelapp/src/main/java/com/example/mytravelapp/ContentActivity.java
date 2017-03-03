package com.example.mytravelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentActivity extends AppCompatActivity {
    databaseHelper mydb;
    public static final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private static int RESULT_LOAD_IMG = 2;
    TextView todayDate;
    private ArrayList<DiaryDetails> diaryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        SharedPreferences setting = getSharedPreferences("usersettings", Context.MODE_PRIVATE);
        String userID = setting.getString("ID", "");
        mydb = new databaseHelper(this);

        Cursor res = mydb.viewAllDiaryRecords(userID);
        while (res.moveToNext())
        {
            // Add All Diary Records into the array list
            diaryList.add(new DiaryDetails(res.getInt(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5)));

        }
        ArrayAdapter<DiaryDetails> adapter = new diaryArrayAdapter(this, 0, diaryList);

        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);

        // Button to create new diary entry
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                //Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                  //      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                //startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                Intent intent = new Intent(ContentActivity.this, NewDiary.class);
                startActivity(intent);
            }
        });

        //add event listener so we can handle clicks
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiaryDetails diaries = diaryList.get(position);

                Intent intent = new Intent(ContentActivity.this, DetailsActivity.class);
                intent.putExtra("diaryID", diaries.getDiary_id());
                intent.putExtra("diaryTitle", diaries.getDiary_title());
                intent.putExtra("diaryLocation", diaries.getDiary_location());
                intent.putExtra("diaryImg", diaries.getDiary_image());
                intent.putExtra("diaryInfo", diaries.getDiary_info());
                intent.putExtra("diaryDate", diaries.getDiary_date());

                startActivity(intent);
            }
        };

        //set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav__bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Write your code
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //custom ArrayAdapter
    class diaryArrayAdapter extends ArrayAdapter<DiaryDetails> {

        private Context context;
        private List<DiaryDetails> diaryList;

        //constructor, call on creation
        public diaryArrayAdapter(Context context, int resource, ArrayList<DiaryDetails> objects) {
            super(context, resource, objects);

            this.context = context;
            this.diaryList = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            DiaryDetails diary = diaryList.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_diary_layout, null);

            TextView description = (TextView) view.findViewById(R.id.dDescription);
            TextView title = (TextView) view.findViewById(R.id.dTitle);
            ImageView image = (ImageView) view.findViewById(R.id.dImage);

            //set address and description
            String dTitle = diary.getDiary_date();
            title.setText("Date : " + dTitle);
            //display trimmed excerpt for description
            int descriptionLength = diary.getDiary_title().length();
            if(descriptionLength >= 100){
                String descriptionTrim = diary.getDiary_title().substring(0, 100) + "...";
                description.setText("Title : " + descriptionTrim);
            }else{
                description.setText("Title : " + diary.getDiary_title());
            }

            //get the image associated with this property
            String imgstr = diary.getDiary_image().toString();
            Bitmap decodeImage = decodeToBase64(imgstr);
            //int imageID = context.getResources().getIdentifier(property.getImage(), "drawable", context.getPackageName());
            image.setImageBitmap(decodeImage);

            return view;
        }
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
