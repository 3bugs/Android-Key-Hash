package com.example.keyhash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String debugKeyHash = getDebugHashKey();
        ((TextView) findViewById(R.id.key_hash_text_view)).setText(debugKeyHash);

        new AlertDialog.Builder(this)
                .setTitle("Debug Key Hash")
                .setMessage(debugKeyHash)
                .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("key_hash", debugKeyHash);
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(MainActivity.this, "copy แล้ว", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private String getDebugHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.keyhash",
                    PackageManager.GET_SIGNATURES
            );
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        return "";
    }
}
