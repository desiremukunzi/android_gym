package com.example.trynfc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CommutedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private PreferenceHelper preferenceHelper;
    Button button,  btnlogout;
    EditText phone;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    List<Commuted> commuted = new ArrayList();
    List<Sport> sport = new ArrayList();
    List<MemberShip> memberships = new ArrayList();
    LinearLayout sessionForm;
    int id;
    LinearLayout loginForm;
    JSONObject session = new JSONObject();
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commited);

        p = new ProgressDialog(this);
        p.setMessage("Loading ...");
        p.setCancelable(false);

//        commited
        preferenceHelper = new PreferenceHelper(this);


        loginForm = findViewById(R.id.loginForm);
//         Check if UserResponse is Already Logged In

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        phone = (EditText) findViewById(R.id.phone);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
//        spinner2.setOnItemSelectedListener(this);
        p.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommutedInterface.COMMUTED)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CommutedInterface commutedInterface = retrofit.create(CommutedInterface.class);

        Call<List<Commuted>> call = commutedInterface.getCommuted();

        call.enqueue(new Callback<List<Commuted>>() {
            @Override
            public void onResponse(Call<List<Commuted>> call, Response<List<Commuted>> response) {
                p.dismiss();
                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                commuted = response.body();

                ArrayAdapter<Commuted> adapter = new ArrayAdapter<Commuted>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, commuted);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final Commuted commuted = (Commuted) parent.getSelectedItem();
//                        displayUserData(commuted);

                        p.show();
                        Retrofit retrofit2 = new Retrofit.Builder()
                                .baseUrl(SportInterface.SPORT)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        SportInterface sportInterface = retrofit2.create(SportInterface.class);

//                        Call<List<Sport>> call2 = sportInterface.getSports(category.getId());

                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("id", commuted.getId());

                        Call<List<Sport>> call2 = sportInterface.getSports(parameters);

                        call2.enqueue(new Callback<List<Sport>>() {
                            @Override
                            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                                p.dismiss();
                                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                sport = response.body();

                                Log.d("sportssssssssss", sport.toString());

                                ArrayAdapter<Sport> adapter = new ArrayAdapter<Sport>(getApplicationContext(),
                                        android.R.layout.simple_spinner_item, sport);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spinner2.setAdapter(adapter);

                                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        final Sport sport = (Sport) parent.getSelectedItem();
//                                        displaySportUserData(sport);

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            @Override
                            public void onFailure(Call<List<Sport>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                                Toast.makeText(CommutedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            @Override
            public void onFailure(Call<List<Commuted>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                Toast.makeText(CommutedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(CommutedInterface.COMMUTED)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                CommutedInterface api = retrofit.create(CommutedInterface.class);
                Call<String> call = api.postCommited( commuted.get((int) spinner.getSelectedItemId()).getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        p.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String res = response.body();

//                                  Log.d("jhhhhhhhhhhh", res);
//                                try {
                                try {
                                    session = new JSONObject(response.body());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                session = response.body();
                                Log.d("onSessionnnnn", String.valueOf(response.body())) ;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                Printer printer = new Printer();
                try {

                    String str = "\n-----------------------------\n" +
                            "Name:"+session.getString("name").toString()+"\n" +
                            "Sport:"+session.getString("sport").toString()+"\n" +
                            "Expiration date:"+session.getString("expiration date").toString()+"\n" +
                            "Message:"+session.getString("message").toString()+"\n" +
                            "-----------------------------\n\n\n\n";

                    printer.printText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(CommutedActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            }
        });
//        end commited
    }

    public void getSelectedUser(View v) {
        Commuted commuted = (Commuted) spinner.getSelectedItem();
        displayUserData(commuted);
    }

    private void displayUserData(Commuted commuted) {
        String name = commuted.getFirstName();
        String id = commuted.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;
        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }

    private void displaySportUserData(Sport sport) {
        String name = sport.getName();
        String id = sport.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;

        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                // Set LoggedIn status to false
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                //this method will remove session and open login screen
                SessionManagement sessionManagement = new SessionManagement(CommutedActivity.this);
                sessionManagement.removeSession();
                Intent tes = new Intent(CommutedActivity.this, Login.class);
                startActivity(tes);
                return true;
            case R.id.session:
                Intent tes2 = new Intent(CommutedActivity.this, SessionCustomer.class);
                startActivity(tes2);
                return true;
            case R.id.commuted:
                Intent tes3 = new Intent(CommutedActivity.this, CommutedActivity.class);
                startActivity(tes3);
                return true;
            case R.id.colporate:
                Intent tes4 = new Intent(CommutedActivity.this, ColporateActivity.class);
                startActivity(tes4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
