//package com.example.trynfc;
//
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//
//public class LoginActivityRec extends AppCompatActivity  {
//
//    private EditText etUname, etPass;
//    private Button btnlogin;
//    private TextView tvcon;
//    private PreferenceHelper preferenceHelper;
//    Spinner spinner;
//
//    LinearLayout loginForm;
//
//    private NfcAdapter mNfcAdapter;
//    String activity;
//    String type = "normal";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_rec);
//
//        loginForm = findViewById(R.id.loginForm);
//
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        activity = getIntent().getStringExtra("activity");
//        type = getIntent().getStringExtra("type");
//
//
////         Check if UserResponse is Already Logged In
//        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
//            Intent intent = new Intent(getApplicationContext(), SessionCustomer.class);
//            startActivity(intent);
//        } else {
//            loginForm.setVisibility(View.VISIBLE);
//        }
//
////          check nfc
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "This device doesn't support nfc", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
//        if (!mNfcAdapter.isEnabled()) {
//            Toast.makeText(this, "This device doesn't support nfc", Toast.LENGTH_LONG).show();
//        }
//
//
//        preferenceHelper = new PreferenceHelper(this);
//
//        etUname = (EditText) findViewById(R.id.etusername);
//        etPass = (EditText) findViewById(R.id.etpassword);
//
//        btnlogin = (Button) findViewById(R.id.btn);
////        tvcon = (TextView) findViewById(R.id.tvcon);
//
////        tvcon.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(LoginActivityRec.this, LoginActivity.class);
////                startActivity(intent);
////                LoginActivityRec.this.finish();
////            }
////        });
//
////        btnlogin.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                loginUser();
////            }
////        });
//
//
//
//    }
//
//
////    @Override
////    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////        // On selecting a spinner item
////        String item = parent.getItemAtPosition(position).toString();
////        long idd = parent.getItemIdAtPosition((int) id);
////
////        // Showing selected spinner item
////        Toast.makeText(parent.getContext(), "Selected: " + item + idd, Toast.LENGTH_LONG).show();
////    }
//
//
//    private void enableForegroundDispatch() {
//        Intent intent = new Intent(this, Card.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                intent, 0);
//        IntentFilter[] intentFilter = new IntentFilter[]{};
//        String[][] techList = new String[][]{{
//                android.nfc.tech.Ndef.class.getName()}, {
//                android.nfc.tech.NdefFormatable.class.getName()}};
//        if (Build.DEVICE.matches(".*generic.*")) {
//            techList = null;
//        }
//        mNfcAdapter.enableForegroundDispatch(this, pendingIntent,
//                intentFilter, techList);
//    }
//    private long toDec(byte[] bytes) {
//        long result = 0;
//        long factor = 1;
//        for (byte aByte : bytes) {
//            long value = aByte & 0xffl;
//            result += value * factor;
//            factor *= 256l;
//        }
//        return result;
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        String action = intent.getAction();
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//            Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            if(tag == null){
//                Toast.makeText(this, "something is empty", Toast.LENGTH_SHORT).show();
//            }
//            else{
//
//                final byte[] tagId = tag.getId();
//                final long toDec = toDec(tagId);
//                loginUser(toDec(tagId));
//
//                Toast.makeText(this, Long.toString(toDec), Toast.LENGTH_SHORT).show();
//            }
//        }
//        else{
//            Toast.makeText(this,
//                    "something is wrong",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    private void loginUser(final long toDec) {
//
//        final String username = etUname.getText().toString().trim();
//        final String password = etPass.getText().toString().trim();
//
//
////        final String sport = String.valueOf(spinner.getSelectedItemId()+1);
////        final String sport = idd;
//
//
////                Log.d("Spinnerrrr", String.valueOf(spinner.getSelectedItemId()));
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(LoginInterfaceRec.LOGINURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        LoginInterfaceRec api = retrofit.create(LoginInterfaceRec.class);
//
//        Call<String> call = api.getUserLogin(Long.toString(toDec));
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
////                Log.i("Responsestring", response.body());
//                //Toast.makeText()
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Log.i("onSuccess", response.body().toString());
//
////                        String jsonresponse = response.body().toString();
//
//                        Gson gson = new Gson();
//                        User jsonresponse = new User();
//                        jsonresponse = gson.fromJson(response.body(), User.class);
//
//                        parseLoginData(response.body());
//
////                        Intent tes = new Intent(LoginActivity.this, MainActivity.class);
//////                        tes.putExtra ("MyData", editText1.getText().toString());
////                        startActivity(tes);
//
//                    } else {
//                        Toast.makeText( LoginActivityRec.this,"Nothing returned",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private void parseLoginData(String response){
//        try {
//            JSONObject jsonObject = new JSONObject(response.toString());
//
////            if (jsonObject.getString("status").equals(0)) {
//
//            saveInfo(response);
//
//            Toast.makeText(LoginActivityRec.this, "Successful logged in!", Toast.LENGTH_SHORT).show();
//            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
//            Intent intent = new Intent(getApplicationContext(), SessionCustomer.class);
////            Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
//
//
//            preferenceHelper.putName(jsonObject.getString("name"));
//            preferenceHelper.putHobby(jsonObject.getString("id"));
//            preferenceHelper.putHobby(jsonObject.getString("post_id"));
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            this.finish();
////            }
////            else {
////                Toast.makeText(LoginActivity.this, "Invalid credetials!", Toast.LENGTH_SHORT).show();
////            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void saveInfo(String response){
//
//        preferenceHelper.putIsLogin(true);
//        try {
//            JSONObject jsonObject = new JSONObject(response.toString());
//            if (jsonObject.getString("status").equals("0")) {
//                JSONArray dataArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < jsonObject.length(); i++) {
//
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    preferenceHelper.putName(dataobj.getString("name"));
//                    preferenceHelper.putHobby(dataobj.getString("hobby"));
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//}