package com.example.grupmesajuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    // Splash sayfasındaki özellikleri atamak için alanlar oluşturuldu
    Button girisYapButonu;
    Button kayitOlButonu;

    // Firebaseden kullanıcı kimliği doğrulama işlemleri için alan oluşturuldu
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Butonlara splash sayfasındaki butonlar id'leri vasıtasıyla atıldı
        girisYapButonu = findViewById(R.id.splashScreen_girisYapButonu);
        kayitOlButonu = findViewById(R.id.splashScreen_kayitOlButonu);

        // Tek bir nesne oluşturulması ve tekrar çağrılırsa aynı nesnenin döndürülmesi sağlandı
        firebaseAuth = FirebaseAuth.getInstance();

        // Kayıt olma butonuna tıklandığında yapılacak işlemler
        kayitOlButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Splash sayfasından Kayıt Ol sayfasına gidildi
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        });

        // Giriş Yap butonuna tıklandığında yapılacak işlemler
        girisYapButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Splash sayfasından Giriş Yap sayfasına gidildi
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });

        // Eğer mevcut bir kullanıcı girişi yapılmışsa
        if(firebaseAuth.getCurrentUser() != null) {
            // Splash sayfasından direkt Ana Sayfaya gönder
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }
}