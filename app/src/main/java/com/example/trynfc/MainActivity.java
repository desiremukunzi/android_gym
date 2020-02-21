package com.example.trynfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    static private ArrayList<TagWrapper> tags = new ArrayList<TagWrapper>();
    static private int currentTagIndex = -1;

    private NfcAdapter adapter = null;
    private PendingIntent pendingIntent = null;

    private TextView currentTagView;
    private ExpandableListView expandableListView;

    private float touchDownX, touchUpX;

    private PreferenceHelper preferenceHelper;
    Button button;
    private Button btnlogout;
    LinearLayout mainForm;
//    EditText editText1;
    TextView tvname;
    @Override
    public void onCreate(final Bundle savedState) {
        super.onCreate(savedState );
        preferenceHelper = new PreferenceHelper(this);


//        redirect
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext()) || preferenceHelper.getPostId()==""){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
//        editText1 = findViewById(R.id.editText1);
        tvname = (TextView) findViewById(R.id.tvname);

        currentTagView = (TextView) findViewById(R.id.currentTagView);
        currentTagView.setText("Loading...");


        adapter = NfcAdapter.getDefaultAdapter(this);

//        tvname.setText("Welcome "+preferenceHelper.getName() +" \n Your id "+preferenceHelper.getPostId());

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Printer printer = new Printer();
//                try {
//                    printer.printText(editText1.getText().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });



        btnlogout = (Button) findViewById(R.id.btn);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set LoggedIn status to false
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                preferenceHelper.clearPostId();
                Intent tes = new Intent(MainActivity.this, Login.class);
                startActivity(tes);

                // Logout
//                logout();
            }
        });
    }



    public void logout(View view) {
        //this method will remove session and open login screen
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();
        preferenceHelper.clearPostId();

//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//        moveToLogin();
    }


//    @Override
//    public void onCreateOptionsMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_example, menu);
//    }



    @Override
    public void onResume() {
        super.onResume();

        //        redirect
//        if(!SaveSharedPreference.getLoggedStatus(getApplicationContext()) || preferenceHelper.getPostId()==""){
//            Intent intent = new Intent(getApplicationContext(), LoginActivityRec.class);
//            startActivity(intent);
//        }

        if (!adapter.isEnabled()) {
            Utils.showNfcSettingsDialog(this);
            return;
        }

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            currentTagView.setText("Scan a tag");
        }

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        getPosts();

//        showTag();

        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //        redirect
        if(!SaveSharedPreference.getLoggedStatus(getApplicationContext()) || preferenceHelper.getPostId()==""){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //        redirect
//        if(!SaveSharedPreference.getLoggedStatus(getApplicationContext()) || preferenceHelper.getHobby()=="2"){
//            Intent intent = new Intent(getApplicationContext(), LoginActivityRec.class);
//            startActivity(intent);
//        }
        adapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("onNewIntent", "Discovered tag with intent " + intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = Utils.bytesToHex(tag.getId());
        TagWrapper tagWrapper = new TagWrapper(tagId);

        ArrayList<String> misc = new ArrayList<String>();
        misc.add("scanned at: " + Utils.now());

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        String tagData = "";

        if (rawMsgs != null) {

            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord cardRecord = msg.getRecords()[0];
            try {
                tagData = readRecord(cardRecord.getPayload());
            } catch (UnsupportedEncodingException e) {
                Log.e("TagScan", e.getMessage());
                return;
            }
        }

        misc.add("tag data: " + tagData);
        tagWrapper.techList.put("Misc", misc);

//        for (String tech : tag.getTechList()) {
//            tech = tech.replace("android.nfc.tech.", "");
//            List<String> info = getTagInfo(tag, tech);
//            tagWrapper.techList.put("Technology: " + tech, info);
//        }

        if (tags.size() == 1) {
            Toast.makeText(this, "Swipe right to see previous tags", Toast.LENGTH_LONG).show();
        }

        tags.add(tagWrapper);
        currentTagIndex = tags.size() - 1;
        showTag();

//        editText1.setText(tagData);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
        Intent tes = new Intent(MainActivity.this, MainActivity2.class);
//                tes.putExtra ("MyData", editText1.getText().toString());
        tes.putExtra("MyData", tagData);
        startActivity(tes);

//            }
//        });
    }

    String readRecord(byte[] payload) throws UnsupportedEncodingException {
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 63;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }


    private void showPreviousTag() {
        if (--currentTagIndex < 0) currentTagIndex = tags.size() - 1;

        showTag();
    }

    private void showNextTag() {
        if (++currentTagIndex >= tags.size()) currentTagIndex = 0;

        showTag();
    }


//    private void getPosts() {
//        if (tags.size() == 0) return;
//
//        final TagWrapper tagWrapper = tags.get(currentTagIndex);
//
//        currentTagView.setText("Tag " + tagWrapper.getId() +
//                " (" + (currentTagIndex+1) + "/" + tags.size() + ")");
//
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("userId", "1");
//        parameters.put("_sort", "id");
//        parameters.put("_order", "desc");
//
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
//
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//
//                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }
//
//                List<Post> posts = response.body();
//
//                for (Post post : posts) {
//                    String content = "";
//                    content += "ID: " + post.getId() + "\n";
//                    content += "User ID: " + post.getUserId() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
//                    content += "Text: " + post.getText() + "\n\n";
//
//                    textViewResult.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
//            }
//        });
//    }

    private void showTag() {
        if (tags.size() == 0) return;

        final TagWrapper tagWrapper = tags.get(currentTagIndex);
        final TagTechList techList = tagWrapper.techList;
//        final ArrayList<String> expandableListTitle = new ArrayList<String>(techList.keySet());
//
//        expandableListView.setAdapter(
//                new CustomExpandableListAdapter(this, expandableListTitle, techList));
//
//        final int count = expandableListView.getCount();
//        for (int i = 0; i < count; i++) expandableListView.expandGroup(i);

        currentTagView.setText("Tag " + tagWrapper.getId() +
                " (" + (currentTagIndex+1) + "/" + tags.size() + ")");
    }



//    private final List<String> getTagInfo(final Tag tag, final String tech) {
//        List<String> info = new ArrayList<String>();
//
//        switch (tech) {
//            case "NfcA":
//                info.add("aka ISO 14443-3A");
//
//                NfcA nfcATag = NfcA.get(tag);
//                info.add("atqa: " + Utils.bytesToHexAndString(nfcATag.getAtqa()));
//                info.add("sak: " + nfcATag.getSak());
//                info.add("maxTransceiveLength: " + nfcATag.getMaxTransceiveLength());
//                break;
//
//            case "NfcF":
//                info.add("aka JIS 6319-4");
//
//                NfcF nfcFTag = NfcF.get(tag);
//                info.add("manufacturer: " + Utils.bytesToHex(nfcFTag.getManufacturer()));
//                info.add("systemCode: " + Utils.bytesToHex(nfcFTag.getSystemCode()));
//                info.add("maxTransceiveLength: " + nfcFTag.getMaxTransceiveLength());
//                break;
//
//            case "NfcV":
//                info.add("aka ISO 15693");
//
//                NfcV nfcVTag = NfcV.get(tag);
//                info.add("dsfId: " + nfcVTag.getDsfId());
//                info.add("responseFlags: " + nfcVTag.getResponseFlags());
//                info.add("maxTransceiveLength: " + nfcVTag.getMaxTransceiveLength());
//                break;
//
//            case "Ndef":
//                Ndef ndefTag = Ndef.get(tag);
//                NdefMessage ndefMessage = null;
//
//                try {
//                    ndefTag.connect();
//                    ndefMessage = ndefTag.getNdefMessage();
//                    ndefTag.close();
//
//                    for (final NdefRecord record : ndefMessage.getRecords()) {
//                        final String id = record.getId().length == 0 ? "null" : Utils.bytesToHex(record.getId());
//                        info.add("record[" + id + "].tnf: " + record.getTnf());
//                        info.add("record[" + id + "].type: " + Utils.bytesToHexAndString(record.getType()));
//                        info.add("record[" + id + "].payload: " + Utils.bytesToHexAndString(record.getPayload()));
//                    }
//
//                    info.add("messageSize: " + ndefMessage.getByteArrayLength());
//
//                } catch (final Exception e) {
//                    e.printStackTrace();
//                    info.add("error reading message: " + e.toString());
//                }
//
//                HashMap<String, String> typeMap = new HashMap<String, String>();
//                typeMap.put(Ndef.NFC_FORUM_TYPE_1, "typically Innovision Topaz");
//                typeMap.put(Ndef.NFC_FORUM_TYPE_2, "typically NXP MIFARE Ultralight");
//                typeMap.put(Ndef.NFC_FORUM_TYPE_3, "typically Sony Felica");
//                typeMap.put(Ndef.NFC_FORUM_TYPE_4, "typically NXP MIFARE Desfire");
//
//                String type = ndefTag.getType();
//                if (typeMap.get(type) != null) {
//                    type += " (" + typeMap.get(type) + ")";
//                }
//                info.add("type: " + type);
//
//                info.add("canMakeReadOnly: " + ndefTag.canMakeReadOnly());
//                info.add("isWritable: " + ndefTag.isWritable());
//                info.add("maxSize: " + ndefTag.getMaxSize());
//                break;
//
//            case "NdefFormatable":
//                info.add("nothing to read");
//
//                break;
//
//            case "MifareUltralight":
//                MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
//                info.add("type: " + mifareUltralightTag.getType());
//                info.add("tiemout: " + mifareUltralightTag.getTimeout());
//                info.add("maxTransceiveLength: " + mifareUltralightTag.getMaxTransceiveLength());
//                break;
//
//            case "IsoDep":
//                info.add("aka ISO 14443-4");
//
//                IsoDep isoDepTag = IsoDep.get(tag);
//                info.add("historicalBytes: " + Utils.bytesToHexAndString(isoDepTag.getHistoricalBytes()));
//                info.add("hiLayerResponse: " + Utils.bytesToHexAndString(isoDepTag.getHiLayerResponse()));
//                info.add("timeout: " + isoDepTag.getTimeout());
//                info.add("extendedLengthApduSupported: " + isoDepTag.isExtendedLengthApduSupported());
//                info.add("maxTransceiveLength: " + isoDepTag.getMaxTransceiveLength());
//                break;
//
//            default:
//                info.add("unknown tech!");
//        }
//
//        return info;
//    }
}