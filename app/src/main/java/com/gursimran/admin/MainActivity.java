package com.gursimran.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ImageView appImage;
    Button registerBtn;
    EditText appName;
    Spinner spinner;
    Uri imageUri;
    String ImageUrl,Category,AppName;
    String[] AppC={"Select a category","App","Game","Book"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Inisilization


        appImage=findViewById(R.id.image_gallery);
        registerBtn=findViewById(R.id.register_btn);
        appName=findViewById(R.id.app_name);
        spinner=findViewById(R.id.spinner);



        //on click on image view and taking image from gallery
        appImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery= new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,1);
            }
        });
        //Setting spinner
        ArrayAdapter adp=new ArrayAdapter(this,android.R.layout.simple_spinner_item,AppC);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                }
                else{

                    Category = AppC[position];
                    Toast.makeText(MainActivity.this, Category + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Select a file", Toast.LENGTH_SHORT).show();


            }
        });



        // code for register button
      registerBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              uploadtoFirebase();
          }
      });


    }

    private void uploadtoFirebase() {
        //Code for Progress Dialog
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploader");
        progressDialog.show();

        AppName=appName.getText().toString();
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference uploader=firebaseStorage.getReference(AppName + ".png");
        uploader.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference reference=firebaseDatabase.getReference(Category);
                        dataholder dh=new dataholder(AppName,Category,uri.toString());
                        reference.child(AppName).setValue(dh);
                        Toast.makeText(MainActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        appName.setText("");
                        appImage.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float prog=100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading : "+(int)prog+" %" );
            }
        });




    }
    //method for setting image in ImageView


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null)
        {
           imageUri=data.getData();
           appImage.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Select an Image", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}