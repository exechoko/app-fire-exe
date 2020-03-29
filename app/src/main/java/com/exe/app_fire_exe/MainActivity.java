package com.exe.app_fire_exe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.exe.app_fire_exe.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nomP, domP, causaP, dniP, personaBuscada;
    ListView listV_personas;

    Button botonSignOut;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth;


    Persona personaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.txt_nombrePersona);
        domP = findViewById(R.id.txt_domiPersona);
        causaP = findViewById(R.id.txt_causaPersona);
        dniP = findViewById(R.id.txtDNIPersona);
        personaBuscada = findViewById(R.id.txt_Buscar);

        mAuth = FirebaseAuth.getInstance();
        botonSignOut = findViewById(R.id.btnSignOut);

        listV_personas = findViewById(R.id.lv_datosPersonas);



        inicializarFirebase();
        listarDatos();


        listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Persona) parent.getItemAtPosition(position);

                nomP.setText(personaSelected.getNombre());
                domP.setText(personaSelected.getDomicilio());
                causaP.setText(personaSelected.getCausa());
                dniP.setText(personaSelected.getDNI());

            }
        });


        //Filtrado por nombre
        personaBuscada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapterPersona.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Cerrar sesion
        botonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });



    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();

                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this,android.R.layout.simple_list_item_1,listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*    private  void ordenarLista(){
        databaseReference.child("Persona").c
    }*/

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

   /* private void consultarFirebase(){
        String nombre = nomP.getText().toString();
        //String perBuscada = new String();
        myQuery = databaseReference.child("Persona").orderByChild("nombre").equalTo(nombre);
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Object perBuscada = snapshot.child("apellido").getValue();
                    personaBuscada.setText("La persona es: " + perBuscada.toString());
                    Log.d("FirebaseQuery","Persona encontrada");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        String nombre = nomP.getText().toString();
        String domicilio = domP.getText().toString();
        String causa = causaP.getText().toString();
        String dni = dniP.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("") || domicilio.equals("") || causa.equals("") || dni.equals("")){
                    validation();
                }
                else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setDomicilio(domicilio);
                    p.setCausa(causa);
                    p.setDNI(dni);

                    databaseReference.child("Persona").child(p.getUid()).setValue(p);

                    Toast.makeText(this,"Agregado",Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save: {
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setDomicilio(domP.getText().toString().trim());
                p.setCausa(causaP.getText().toString().trim());
                p.setDNI(dniP.getText().toString().trim());

                databaseReference.child("Persona").child(p.getUid()).setValue(p);

                Toast.makeText(this,"Actualizado",Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_delete: {
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());

                databaseReference.child("Persona").child(p.getUid()).removeValue();

                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            /* //EN DESARROLLO BOTON DE BUSCAR (SEARCH)
            case R.id.icon_search: {
                //consultarFirebase();
                Toast.makeText(this, "Operacion en desarrollo", Toast.LENGTH_SHORT).show();
                break;
            }*/
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nomP.setText("");
        causaP.setText("");
        domP.setText("");
        dniP.setText("");
    }

    private void validation() {

        String nombre = nomP.getText().toString();
        String domicilio = domP.getText().toString();
        String causa = causaP.getText().toString();
        String dni = dniP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Required");
        } else if (domicilio.equals("")) {
            domP.setError("Required");
        } else if (causa.equals("")){
            causaP.setError("Required");
        } else if (dni.equals("")){
            dniP.setError("Required");
        }
    }


}
