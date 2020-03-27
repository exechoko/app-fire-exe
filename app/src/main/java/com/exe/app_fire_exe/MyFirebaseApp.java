package com.exe.app_fire_exe;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //para hacer correcta la persistencia de datos
        //luego agregar en el manifest
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
