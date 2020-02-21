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

public class SessionCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private PreferenceHelper preferenceHelper;
    Button button,  btnlogout;
    EditText phone;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    List<Category> categories = new ArrayList();
    List<Sport> sport = new ArrayList();
    List<MemberShip> memberships = new ArrayList();
    LinearLayout sessionForm;
    int id;
    LinearLayout loginForm;
    JSONObject session = new JSONObject();
    ProgressDialog p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_customer);

        preferenceHelper = new PreferenceHelper(this);

        p = new ProgressDialog(this);
        p.setMessage("Loading ...");
        p.setCancelable(false);

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


//  ---------- -----------------------------------------------categories------------------------------------------

        p.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CategoryInterface.CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);

        Call<List<Category>> call = categoryInterface.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                p.dismiss();
                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                 categories = response.body();

//                Log.d("mmmmmmmmmmm", String.valueOf(2));

                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       final Category category = (Category) parent.getSelectedItem();
//                        displayUserData(category);

                        p.show();
                        Retrofit retrofit2 = new Retrofit.Builder()
                                .baseUrl(SportInterface.SPORT)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        SportInterface sportInterface = retrofit2.create(SportInterface.class);

//                        Call<List<Sport>> call2 = sportInterface.getSports(category.getId());



                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("id", category.getId());

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


                                        p.show();
                                        Retrofit retrofit2 = new Retrofit.Builder()
                                                .baseUrl(MemberShipInterface.MEMBERSHIP)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        MemberShipInterface memberShipInterface = retrofit2.create(MemberShipInterface.class);

//                        Call<List<Sport>> call2 = sportInterface.getSports(category.getId());

                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("id", sport.getId());

                                        Call<List<MemberShip>> call = memberShipInterface.getMemberships(parameters);

                                        call.enqueue(new Callback<List<MemberShip>>() {
                                            @Override
                                            public void onResponse(Call<List<MemberShip>> call, Response<List<MemberShip>> response) {
                                                p.dismiss();
                                                if (!response.isSuccessful()) {
//                    Toast.makeText(SessionCustomer.this, response.code(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                memberships = response.body();

                                                Log.d("sportssssssssss", memberships.toString());

                                                ArrayAdapter<MemberShip> adapter = new ArrayAdapter<MemberShip>(getApplicationContext(),
                                                        android.R.layout.simple_spinner_item, memberships);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                spinner3.setAdapter(adapter);

                                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        final MemberShip memberships = (MemberShip) parent.getSelectedItem();
//                                                        displayMembershipUserData(memberships);


                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<List<MemberShip>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                                                Toast.makeText(SessionCustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<List<Sport>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                                Toast.makeText(SessionCustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                Toast.makeText(SessionCustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                p.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MemberShipInterface.MEMBERSHIP)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                MemberShipInterface api = retrofit.create(MemberShipInterface.class);

                Call<String> call = api.getSession(phone.getText().toString(), categories.get((int) spinner.getSelectedItemId()).getId(), sport.get((int) spinner.getSelectedItemId()).getId(), memberships.get((int) spinner3.getSelectedItemId()).getId(), preferenceHelper.getHobby());

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
                            "Category:"+session.getString("category").toString()+"\n" +
                            "Sport:"+session.getString("sport").toString()+"\n" +
                            "Membership:"+session.getString("membership").toString()+"\n" +
                            "Amount:"+session.getString("amount").toString()+"\n" +
                            "Phone number:"+session.getString("telephone").toString()+"\n" +
                            "-----------------------------\n\n\n\n";

                    printer.printText(str);

                    Toast.makeText(SessionCustomer.this, "Successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

//                btnlogout = (Button) findViewById(R.id.btn);
//                btnlogout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Set LoggedIn status to false
//                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
//                    preferenceHelper.clearPostId();
//                    Intent tes = new Intent(SessionCustomer.this, Login.class);
//                    startActivity(tes);
//
//                    // Logout
////                logout();
//                }
//            });

    }

        public void logout(View view) {
            //this method will remove session and open login screen
            SessionManagement sessionManagement = new SessionManagement(SessionCustomer.this);
            sessionManagement.removeSession();

//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//        moveToLogin();
        }

    public void getSelectedUser(View v) {
        Category category = (Category) spinner.getSelectedItem();
        displayUserData(category);
    }

    private void displayUserData(Category category) {
        String name = category.getName();
        String id = category.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;

//         categoryId = id;

        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }

    private void displaySportUserData(Sport sport) {
        String name = sport.getName();
        String id = sport.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;

//         categoryId = id;

        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }

    private void displayMembershipUserData(MemberShip member) {
        String name = member.getName();
        String id = member.getId();

        String CategoryData = "Name: " + name + "\nId: " + id;

//         categoryId = id;

        Toast.makeText(this, CategoryData, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        long idd = parent.getItemIdAtPosition((int) id);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item + idd, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //        redirect
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext()) && preferenceHelper.getPostId().equals("1") || preferenceHelper.getPostId().equals("2") || preferenceHelper.getPostId().equals("3") || preferenceHelper.getPostId().equals("4")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //        redirect
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext()) && preferenceHelper.getPostId().equals("1") || preferenceHelper.getPostId().equals("2") || preferenceHelper.getPostId().equals("3") || preferenceHelper.getPostId().equals("4")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

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
                        SessionManagement sessionManagement = new SessionManagement(SessionCustomer.this);
                        sessionManagement.removeSession();
                        Intent tes = new Intent(SessionCustomer.this, Login.class);
                        startActivity(tes);
                return true;
            case R.id.session:
                Intent tes2 = new Intent(SessionCustomer.this, SessionCustomer.class);
                startActivity(tes2);
                return true;
            case R.id.commuted:
                Intent tes3 = new Intent(SessionCustomer.this, CommutedActivity.class);
                startActivity(tes3);
                return true;
            case R.id.colporate:
                Intent tes4 = new Intent(SessionCustomer.this, ColporateActivity.class);
                startActivity(tes4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
