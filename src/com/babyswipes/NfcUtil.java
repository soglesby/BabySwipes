package com.babyswipes;

import java.io.IOException;
import java.nio.charset.Charset;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

public class NfcUtil {
    private static final String TAG = "NfcUtil";

    public static boolean programTag(Tag tag, String payload) {
        // record to launch app when tag is scanned
        NdefRecord appRecord = NdefRecord.createApplicationRecord("com.babyswipes");
        
        // record that contains our custom tag data
        byte[] mimeBytes = new String("application/com.babyswipes").getBytes(Charset.forName("US-ASCII"));
        NdefRecord swipeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, 
                                                new byte[0], payload.getBytes());
        NdefMessage message = new NdefMessage(new NdefRecord[] { swipeRecord, appRecord});
        
        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
    
                if (!ndef.isWritable()) {
                    Log.d(TAG, "Read-only tag.");
                    return false;
                }
                
                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    Log.d(TAG, "Tag doesn't have enough free space.");
                    return false;
                }
    
                ndef.writeNdefMessage(message);
                Log.d(TAG, "Tag written successfully.");
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        Log.d(TAG, "Tag written successfully!\nClose this app and scan tag.");
                        return true;
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to format tag to NDEF.");
                        return false;
                    }
                } else {
                    Log.d(TAG, "Tag doesn't appear to support NDEF format.");
                    return false;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to write tag");
        }
    
        return false;
    }
}
