package com.example.trynfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class Card extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    String activity;
    String type = "normal";
//    Storage s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_rec);
//        s = new Storage(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        activity = getIntent().getStringExtra("activity");
        type = getIntent().getStringExtra("type");
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support nfc", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "This device doesn't support nfc", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatch();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            onNewIntent(getIntent());
        }
    }
    private void enableForegroundDispatch() {
        Intent intent = new Intent(this, Card.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        String[][] techList = new String[][]{{
                android.nfc.tech.Ndef.class.getName()}, {
                android.nfc.tech.NdefFormatable.class.getName()}};
        if (Build.DEVICE.matches(".*generic.*")) {
            techList = null;
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent,
                intentFilter, techList);
    }
    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
                Toast.makeText(this, "something is empty", Toast.LENGTH_SHORT).show();
            }
            else{

                final byte[] tagId = tag.getId();
                final long toDec = toDec(tagId);
                Toast.makeText(this, Long.toString(toDec), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,
                    "something is wrong",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
