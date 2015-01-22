package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    ArrayList<Task> items;
    TaskAdapter itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    private TodoTaskDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        db = new TodoTaskDatabase(this);
        readItems();
        itemsAdapter = new TaskAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        EditText etDueDate = (EditText) findViewById(R.id.etDueDate);
        etDueDate.addTextChangedListener(etDueDateWatcher);

    }

    private TextWatcher etDueDateWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditText etDueDate = (EditText) findViewById(R.id.etDueDate);
            Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==2 && before ==0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                    etDueDate.setText("");
                } else {
                    working+="/";
                    etDueDate.setText(working);
                    etDueDate.setSelection(working.length());
                }
            }
            if (working.length()==5 && before ==0) {
                if (Integer.parseInt(working.substring(3)) < 1 || Integer.parseInt(working.substring(3))>31) {
                    isValid = false;
                    etDueDate.setText(working.substring(0,3));
                    etDueDate.setSelection(3);
                } else {
                    working+="/";
                    etDueDate.setText(working);
                    etDueDate.setSelection(working.length());
                }
            }
            else if (working.length()==8 && before ==0) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                sdf.set2DigitYearStart(new GregorianCalendar(1970, 1, 1).getTime());
                try {
                    cal.setTime(sdf.parse(working));// all done
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String reportDate = sdf.format(Calendar.getInstance().getTime());
                reportDate = sdf.format(cal.getTime());
                if (Calendar.getInstance().getTime().after(cal.getTime()) ) {
                    isValid = false;
                    etDueDate.setText(working.substring(0,6));
                    etDueDate.setSelection(6);
                }
            } else if (working.length()!=8) {
                btnAddItem.setEnabled(false);
            }

            if (!isValid && (working.length()==0 || working.length()==3 ||
                    working.length()==5 || working.length()==8)) {
                etDueDate.setError("Enter a valid date: MM/DD/YY");

            } else {
                etDueDate.setError(null);
            }

            if(isValid && working.length() == 8)
                btnAddItem.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                               View item, int pos, long id) {
                    // first parameter is the context, second is the class of the activity to launch
                    Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("item", (Task) lvItems.getItemAtPosition(pos));
                        i.putExtra("originalPos", pos);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Task saveItem = (Task) data.getSerializableExtra("saveItem");
            int originalPos = data.getExtras().getInt("originalPos");
            if(null != saveItem) {
                items.remove(originalPos);
                itemsAdapter.insert(saveItem, originalPos);
            }
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>();
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        EditText etDueDate = (EditText) findViewById(R.id.etDueDate);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(new Task(etNewItem.getText().toString(), etDueDate.getText().toString()));
        etNewItem.setText("");
        etDueDate.setText("");
        writeItems();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
