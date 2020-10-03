package com.example.txchat20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class cha extends AppCompatActivity {
    String mt = "";
    int choose = 0;
    String activeuser = "";
    ArrayList<String> messages = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    EditText chatEditText;
    Button a;


    public void time(View view) {
        choose = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Time(in mins)");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mt = input.getText().toString();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();


    }

    public void srndChat(View view) {
        if (choose == 0) {
            chatEditText = (EditText) findViewById(R.id.messageEditText);
            if (chatEditText.getText().toString().matches("")) {
                Toast.makeText(cha.this, "Kuch Enter to kar", Toast.LENGTH_LONG).show();
            } else {
                ParseObject message = new ParseObject("Message");
                message.put("sender", ParseUser.getCurrentUser().getUsername());
                message.put("recipient", activeuser);
                message.put("message", chatEditText.getText().toString());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(cha.this, "sent", Toast.LENGTH_SHORT).show();
                            messages.add((chatEditText.getText().toString()));
                            arrayAdapter.notifyDataSetChanged();
                            chatEditText.setText("");
                        }

                    }
                });
            }
        }

    else if(choose==1)

    {
        chatEditText=(EditText)findViewById(R.id.messageEditText) ;
        choose=0;
        if(chatEditText.getText().toString().matches("")){
            Toast.makeText(cha.this,"Enter to Kar kuch",Toast.LENGTH_LONG).show();
        }
        final String sw=chatEditText.getText().toString();
        chatEditText.setText("");



        int k=Integer.parseInt(mt);
        k=k*60*1000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ParseObject message = new ParseObject("Message");
                message.put("sender", ParseUser.getCurrentUser().getUsername());
                message.put("recipient", activeuser);
                message.put("message", sw);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(cha.this, "sent", Toast.LENGTH_SHORT).show();
                            messages.add((sw));
                            arrayAdapter.notifyDataSetChanged();

                        }

                    }
                });


            }
        }, k);
        choose=0;

    }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha);

        a=(Button)findViewById(R.id.button2);
        a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                time(view);
                return true;
            }
        });
        Intent intent=getIntent();
        activeuser=intent.getStringExtra("username");
        setTitle("Talking to "+activeuser);
        ListView chatListView=(ListView)findViewById(R.id.Lview);


        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
        chatListView.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient",activeuser);
        ParseQuery<ParseObject> query2=new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("sender",activeuser);
        query2.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());
        List<ParseQuery<ParseObject>> queries=new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        ParseQuery<ParseObject> query=ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        messages.clear();
                        for(ParseObject message:objects){
                            String messageContent=message.getString("message");
                            if(!(message.getString("sender").equals(ParseUser.getCurrentUser().getUsername()))){
                                messageContent="🔰 "+messageContent;
                            }
                            messages.add(messageContent);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}