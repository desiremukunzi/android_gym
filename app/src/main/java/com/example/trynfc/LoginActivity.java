package com.example.trynfc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etUname, etPass;
    private Button btnlogin;
    private TextView tvrec;
    private PreferenceHelper preferenceHelper;
    Spinner spinner;

    LinearLayout loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginForm = findViewById(R.id.loginForm);


//         Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            loginForm.setVisibility(View.VISIBLE);
        }

        preferenceHelper = new PreferenceHelper(this);

        etUname = (EditText) findViewById(R.id.etusername);
        etPass = (EditText) findViewById(R.id.etpassword);

        btnlogin = (Button) findViewById(R.id.btn);
        tvrec = (TextView) findViewById(R.id.tvrec);

        tvrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Login.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        // Spinner element
         spinner = (Spinner) findViewById(R.id.spinnerlogin);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        long idd = spinner.getSelectedItemId();

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("gym");
        categories.add("pool");
        categories.add("sauna");
        categories.add("massage");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

       // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        long idd = parent.getItemIdAtPosition((int) id);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item + idd, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loginUser() {

        final String username = etUname.getText().toString().trim();
        final String password = etPass.getText().toString().trim();
        final String sport = String.valueOf(spinner.getSelectedItemId()+1);
//        final String sport = idd;


//                Log.d("Spinnerrrr", String.valueOf(spinner.getSelectedItemId()));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGINURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        LoginInterface api = retrofit.create(LoginInterface.class);

        Call<String> call = api.getUserLogin(username,password,sport);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.i("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

//                        String jsonresponse = response.body().toString();

                        Gson gson = new Gson();
                        User jsonresponse = new User();
                        jsonresponse = gson.fromJson(response.body(), User.class);

                        parseLoginData(response.body());

//                        Intent tes = new Intent(LoginActivity.this, MainActivity.class);
////                        tes.putExtra ("MyData", editText1.getText().toString());
//                        startActivity(tes);

                    } else {
                        Toast.makeText( LoginActivity.this,"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void parseLoginData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response.toString());

//            if (jsonObject.getString("status").equals(0)) {

            saveInfo(response);

            Toast.makeText(LoginActivity.this, "Successful logged in!", Toast.LENGTH_SHORT).show();
            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);


            preferenceHelper.putName(jsonObject.getString("name"));
            preferenceHelper.putHobby(jsonObject.getString("id"));
            preferenceHelper.putPostId(jsonObject.getString("post_id"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
//            }
//            else {
//                Toast.makeText(LoginActivity.this, "Invalid credetials!", Toast.LENGTH_SHORT).show();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveInfo(String response){

        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.getString("status").equals("0")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonObject.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putName(dataobj.getString("name"));
                    preferenceHelper.putHobby(dataobj.getString("hobby"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}