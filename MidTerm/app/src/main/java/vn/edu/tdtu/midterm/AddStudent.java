package vn.edu.tdtu.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStudent extends AppCompatActivity {
    EditText edtStudentNumbers, edtName, edtAge, edtPhone;
    Button btnSave, btnShow;
    ProgressDialog pd;
    FirebaseFirestore db;
    String sid, stnum, sname, sage, sphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtStudentNumbers = findViewById(R.id.edtStudentNumbers);
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtPhone = findViewById(R.id.edtPhone);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //update
            sid = bundle.getString("sid");
            stnum = bundle.getString("snumber");
            sname = bundle.getString("sname");
            sage = bundle.getString("sage");
            sphone = bundle.getString("sphone");

            edtStudentNumbers.setText(stnum);
            edtName.setText(sname);
            edtAge.setText(sage);
            edtPhone.setText(sphone);
        }

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null){
                    //input data
                    String pid = sid;
                    String stdnumber = edtStudentNumbers.getText().toString().trim();
                    String name = edtName.getText().toString().trim();
                    String age = edtAge.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    
                    updateData(pid, stdnumber, name, age, phone);
                }
                else{
                    //input data
                    String stdnumber = edtStudentNumbers.getText().toString().trim();
                    String name = edtName.getText().toString().trim();
                    String age = edtAge.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();

                    uploadData(stdnumber, name, age, phone);
                }
            }
        });

        btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudent.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateData(String pid, String stdnumber, String name, String age, String phone) {
        pd.setTitle("Updating Data...");
        pd.show();

        db.collection("Document").document(pid)
                .update("number", stdnumber, "search", stdnumber.toUpperCase(), "name", name, "age", age, "phone", phone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(AddStudent.this, "Updating...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddStudent.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String stdnumber, String name, String age, String phone) {
        pd.setTitle("Adding Student to Firestore");
        pd.show();

        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();
        //put id of data
        doc.put("id", id);
        doc.put("number", stdnumber);
        doc.put("search", stdnumber.toUpperCase());
        doc.put("name", name);
        doc.put("age", age);
        doc.put("phone",phone);

        db.collection("Document").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when data success
                        pd.dismiss();
                        Toast.makeText(AddStudent.this, "Uploaded Successfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when data error
                        pd.dismiss();
                        Toast.makeText(AddStudent.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}