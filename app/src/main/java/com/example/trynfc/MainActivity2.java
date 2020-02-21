package com.example.trynfc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {
    private TextView textViewResult;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private PreferenceHelper preferenceHelper;

    static private ArrayList<TagWrapper> tags = new ArrayList<TagWrapper>();
    static private int currentTagIndex = -1;
    Button button;
    EditText editText1;
    TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        preferenceHelper = new PreferenceHelper(this);
//
//        //        redirect
//        if(!SaveSharedPreference.getLoggedStatus(getApplicationContext())){
//            Intent intent = new Intent(getApplicationContext(), LoginActivityRec.class);
//            startActivity(intent);
//        } else {
//            mainForm.setVisibility(View.VISIBLE);
//        }


        textViewResult = findViewById(R.id.text_view_result);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://gym.etouchsol.net/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPosts();
//        getComments();

//        tvname = (TextView) findViewById(R.id.tvname);
//
//
//
//        tvname.setText(" Your id "+preferenceHelper.getPostId());

    }

    private void getPosts() {
//        final TagWrapper tagWrapper = tags.get(currentTagIndex);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gym.etouchsol.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        String myData = getIntent().getStringExtra("MyData");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("payment", myData);
        parameters.put("sport_id", preferenceHelper.getPostId());
        Log.d("sport id jjjjjj", preferenceHelper.getPostId());
        Log.d("card id jjjjjj", myData);
//        parameters.put("_order", "desc");

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Log.d("onResponse: ", response.body().toString());
                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "Message: " + post.getCustomer_id() + "\n";


                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

//    public void fetch(View view) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        getPosts();
//    }

//    private void getComments() {
//        Call<List<Comment>> call = jsonPlaceHolderApi
//                .getComments("https://jsonplaceholder.typicode.com/posts/1/comments");
//
//        call.enqueue(new Callback<List<Comment>>() {
//            @Override
//            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//
//                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }
//
//                List<Comment> comments = response.body();
//
//                for (Comment comment : comments) {
//                    String content = "";
//                    content += "ID: " + comment.getId() + "\n";
//                    content += "Post ID: " + comment.getPostId() + "\n";
//                    content += "Name: " + comment.getName() + "\n";
//                    content += "Email: " + comment.getEmail() + "\n";
//                    content += "Text: " + comment.getText() + "\n\n";
//
//                    textViewResult.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Comment>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
//            }
//        });
//    }

//            public void fetch(View view) {
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://jsonplaceholder.typicode.com/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//                getPosts();
//
//            }
}