package com.carecorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;


public class JournalEditorActivity extends AppCompatActivity {

    //Save button, Exit button
    Button btnSave, btnExit;
    //Editable text entry box
    EditText textEntryBox;
    //Check to save before exiting, set to true in case user does not edit.
    boolean hasBeenSaved = true;
    //Convert file passed in from JournalActivity to a string
    String text = "";
    //Bundle extras = getIntent().getExtras();

    //text = (String) savedInstanceState.getSerializable("text"); // extras.getString("text");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Prep
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_editor_activity);
        //Intent intent = new Intent(JournalEditorActivity.this, JournalActivity.class);
        //Links
        btnSave = findViewById(R.id.btnSave);
        btnExit = findViewById(R.id.btnExit);
        textEntryBox = findViewById(R.id.textEntryBox);

        try {
            //text = getIntent().getExtras().getString("text");
            text = getIntent().getStringExtra("text");
        }
        catch(NullPointerException e) {
            text = " ";
        }
        //Puts the editable text into the text entry box
        textEntryBox.setText(text, TextView.BufferType.EDITABLE);

        //Separate variables needed in case user does not save.
        String outText = " ";

        //Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieves the text from the text box
                text = textEntryBox.getText().toString();
                //Confirms to user save has been made
                Toast.makeText(JournalEditorActivity.this, "Entry saved", Toast.LENGTH_SHORT).show();
                //Marks entry saved
                hasBeenSaved = true;
            }
        });

        //Exit button
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if saved
                if (hasBeenSaved) {
                    //Exit back to Journal Activity and return text
                    Toast.makeText(JournalEditorActivity.this,
                            "Exiting", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JournalEditorActivity.this, JournalRecyclerMain.class);
                    intent.putExtra("text", text);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}

