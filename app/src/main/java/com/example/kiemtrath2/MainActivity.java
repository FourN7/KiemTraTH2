package com.example.kiemtrath2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static  final String PROVIDER_NAME="com.example.kiemtrath.BookProvider";
    static final String URL="content://"+PROVIDER_NAME+"/books";
    static  final Uri CONTENT_URI=Uri.parse(URL);

    static  final String  ID="id";
    static  final  String NAME="name";
    static  final  String AUTHOR="author";

    Button buttonExit, buttonSelect, buttonSave, buttonUpdate, buttonDelete;
    EditText editTextID, editTextName, editTextGrade;
    GridView gridView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonExit = findViewById(R.id.buttonExit);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSelect = findViewById(R.id.buttonSelect);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        editTextGrade = findViewById(R.id.editTextGrade);
        gridView = findViewById(R.id.gridView);

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAll();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }
    private void getAll(){
        arrayList=new ArrayList<>();
        ContentResolver contentResolver =getContentResolver();
        Cursor cursor=contentResolver.query(CONTENT_URI, null,null,null,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                int id =cursor.getInt(cursor.getColumnIndex(ID));
                String name=cursor.getString(cursor.getColumnIndex(NAME));
                String author=cursor.getString(cursor.getColumnIndex(AUTHOR));
                arrayList.add(String.valueOf(id));
                arrayList.add(name);
                arrayList.add(author);
            }while (cursor.moveToNext());
            arrayAdapter =new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
            gridView.setAdapter(arrayAdapter);
            editTextID.setText("");
            editTextName.setText("");
            editTextGrade.setText("");
        }
    }
    private  void save(){
        String id =editTextID.getText().toString();
        String name=editTextName.getText().toString();
        String author =editTextGrade.getText().toString();
        if(id.isEmpty()){
            Toast.makeText(MainActivity.this,"Chua nhap id",Toast.LENGTH_LONG).show();
        } else if (name.isEmpty()){
            Toast.makeText(MainActivity.this,"Chua nhap name",Toast.LENGTH_LONG).show();
        } else  if (author.isEmpty()){
            Toast.makeText(MainActivity.this,"Chua nhap author",Toast.LENGTH_LONG).show();
        } else  {
            ContentResolver contentResolver =getContentResolver();
            ContentValues contentValues=new ContentValues();
            contentValues.put(ID,Integer.parseInt(id));
            contentValues.put(NAME,name);
            contentValues.put(AUTHOR,author);
            Uri newBooks=contentResolver.insert(CONTENT_URI,contentValues);
            Toast.makeText(MainActivity.this,"Them Thanh Cong",Toast.LENGTH_LONG).show();
        }
    }
    private void update(){
        String id = editTextID.getText().toString();
        String name = editTextName.getText().toString();
        String author = editTextGrade.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(MainActivity.this, "Chưa nhập id", Toast.LENGTH_LONG).show();
        } else if (name.isEmpty()) {
            Toast.makeText(MainActivity.this, "Chưa nhập name", Toast.LENGTH_LONG).show();
        } else if (author.isEmpty()) {
            Toast.makeText(MainActivity.this, "Chưa nhập grade", Toast.LENGTH_LONG).show();
        } else {
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(NAME, name);
            contentValues.put(AUTHOR, author);
            String whereClause = "id = ?";
            String whereClauseArgs[] = {id};
            int updateCount = contentResolver.update(CONTENT_URI, contentValues, whereClause, whereClauseArgs);
            if (updateCount > 0) {
                Toast.makeText(MainActivity.this, "Cập nhật thành công " + updateCount, Toast.LENGTH_LONG).show();
            }
            getAll();
        }
    }
    private void delete(){
        String id = editTextID.getText().toString();
        ContentResolver contentResolver = getContentResolver();
        String whereClause = "id = ?";
        String whereClauseArgs[] = {id};
        int deleteCount = contentResolver.delete(CONTENT_URI, whereClause, whereClauseArgs);
        Toast.makeText(getApplicationContext(), "Xóa thành công " + deleteCount, Toast.LENGTH_LONG).show();
        getAll();
    }
}
