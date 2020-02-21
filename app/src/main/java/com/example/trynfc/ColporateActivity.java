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

public class ColporateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private PreferenceHelper preferenceHelper;
    Button button,  btnlogout;
    EditText phone;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    List<Colporate> colporate = new ArrayList();
    List<Entity> enties = new ArrayList();
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
        setContentView(R.layout.colporate);

        p = new ProgressDialog(this);
        p.setMessage("Loading ...");
        p.setCancelable(false);

        //        colporate
        preferenceHelper = new PreferenceHelper(this);


        loginForm = findViewById(R.id.loginForm);
//         Check if UserResponse is Already Logged In

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
//        spinner3 = (Spinner) findViewById(R.id.spinner3);
        phone = (EditText) findViewById(R.id.phone);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
//        spinner2.setOnItemSelectedListener(this);
        p.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColporateInterface.COLPORATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ColporateInterface ColporateInterface = retrofit.create(ColporateInterface.class);

        Call<List<Entity>> call = ColporateInterface.getEntities();

        call.enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                p.dismiss();
                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                enties = response.body();

//                Log.d("mmmmmmmmmmm", String.valueOf(2));

                ArrayAdapter<Entity> adapter = new ArrayAdapter<Entity>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, enties);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final Entity entity = (Entity) parent.getSelectedItem();
//                        displayUserData(entity);

                        p.show();
                        Retrofit retrofit2 = new Retrofit.Builder()
                                .baseUrl(ColporateInterface.COLPORATE)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ColporateInterface colporateInterface = retrofit2.create(ColporateInterface.class);

//                        Call<List<Sport>> call2 = sportInterface.getSports(category.getId());

                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("id", entity.getId());

                        Call<List<Colporate>> call2 = ColporateInterface.getColpolates(parameters);

                        call2.enqueue(new Callback<List<Colporate>>() {
                            @Override
                            public void onResponse(Call<List<Colporate>> call, Response<List<Colporate>> response) {
                                p.dismiss();
                                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                colporate = response.body();

                                Log.d("sportssssssssss", sport.toString());

                                ArrayAdapter<Colporate> adapter = new ArrayAdapter<Colporate>(getApplicationContext(),
                                        android.R.layout.simple_spinner_item, colporate);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spinner2.setAdapter(adapter);

                                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        final Colporate colp= (Colporate) parent.getSelectedItem();
//                                        displayColpoData(colp);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<List<Colporate>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                                Toast.makeText(ColporateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                Toast.makeText(ColporateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ColporateInterface.COLPORATE)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                ColporateInterface api = retrofit.create(ColporateInterface.class);

                Call<String> call = api.postCorporate( colporate.get((int) spinner.getSelectedItemId()).getId());

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

//                                    Log.d("onSessionnmmmmmmm", session.getString("category")) ;

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
//



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
                Toast.makeText(ColporateActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            }
        });
//        end colporate

      }


    public void getSelectedUser(View v) {
        Entity entity = (Entity) spinner.getSelectedItem();
        displayUserData(entity);
    }

    private void displayUserData(Entity entity) {
        String name = entity.getName();
        String id = entity.getId();
        String CategoryData = "Name: " + name + "\nId: " + id;
//         categoryId = id;
        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }

    private void displayColpoData(Colporate colp) {
        String name = colp.getFirstName();
        String id = colp.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;

//         categoryId = id;

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
                SessionManagement sessionManagement = new SessionManagement(ColporateActivity.this);
                sessionManagement.removeSession();
                Intent tes = new Intent(ColporateActivity.this, Login.class);
                startActivity(tes);
                return true;
            case R.id.session:
                Intent tes2 = new Intent(ColporateActivity.this, SessionCustomer.class);
                startActivity(tes2);
                return true;
            case R.id.commuted:
                Intent tes3 = new Intent(ColporateActivity.this, CommutedActivity.class);
                startActivity(tes3);
                return true;
            case R.id.colporate:
                Intent tes4 = new Intent(ColporateActivity.this, ColporateActivity.class);
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