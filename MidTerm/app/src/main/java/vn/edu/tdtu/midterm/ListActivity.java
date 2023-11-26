package vn.edu.tdtu.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Models> dataSource = new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseFirestore db;
    StudentAdapter adapter;
    ProgressDialog pd;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        showListData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, AddStudent.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showListData() {
        pd.setTitle("Loading data...");
        pd.show();

        db.collection("Document")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dataSource.clear();
                        pd.dismiss();

                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Models models = new Models(documentSnapshot.getString("id"),
                                    documentSnapshot.getString("number"),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("age"),
                                    documentSnapshot.getString("phone"));
                            dataSource.add(models);
                        }

                        adapter = new StudentAdapter(dataSource,ListActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int position){
        pd.setTitle("Deleting data...");
        pd.show();

        db.collection("Document").document(dataSource.get(position).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListActivity.this, "Deleting...", Toast.LENGTH_SHORT).show();
                        showListData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void search(String query) {
        pd.setTitle("Searching...");
        pd.show();

        db.collection("Document").whereEqualTo("search", query.toUpperCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dataSource.clear();
                        pd.dismiss();

                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Models models = new Models(documentSnapshot.getString("id"),
                                    documentSnapshot.getString("number"),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("age"),
                                    documentSnapshot.getString("phone"));
                            dataSource.add(models);
                        }

                        adapter = new StudentAdapter(dataSource,ListActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settingAction){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}