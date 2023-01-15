package com.example.grupmesajuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    // Login sayfasındaki özellikleri atamak için alanlar oluşturuldu
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button loginToSignUpButton;

    // Firebaseden kullanıcı kimliği doğrulama işlemleri için alan oluşturuldu
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Butonlara ve yazı giriş alanlarına Login sayfasındaki buton ve yazı giriş alanları id'leri vasıtasıyla atandı
        emailEditText = findViewById(R.id.loginPage_emailEditText);
        passwordEditText = findViewById(R.id.loginPage_passwordEditText);
        loginButton = findViewById(R.id.loginPage_loginButton);
        loginToSignUpButton = findViewById(R.id.loginPage_signUpButton);

        // Tek bir nesne oluşturulması ve tekrar çağrılırsa aynı nesnenin döndürülmesi sağlandı
        firebaseAuth = FirebaseAuth.getInstance();

        // Giriş Yap butonuna tıklandığında yapılacak işlemler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arayüzdeki yazı giriş alanlarına girilen email ve şifre alanları String değişkenlere atandı
                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                // Eğer şifre veya email alanı boş bırakılmışsa
                if(email.isEmpty() || password.isEmpty()) {
                    // Bilgilendirme mesajı verildi
                    Toast.makeText(LoginActivity.this, "Email veya şifre alanları boş bırakılamaz", Toast.LENGTH_SHORT).show();
                    // Eğer bu alanlardan biri veya her ikisi de boş bırakılmışsa işlemlere devam etme
                    return;
                }

                // Girilen Email ve Şifrenin kontrolü sağlandı
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    // Girişin başarılı olup olmaması bir task ile gönderildi
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Giriş başarılıysa
                        if(task.isSuccessful()) {
                            // Giriş başarılı mesajı oluşturuldu ve Ana Sayfaya Gidildi
                            Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                });
            }
        });

        // Kayıt Olma sayfasına yönlendirme
        loginToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Giriş Yap sayfasından Kayıt Ol sayfasına gidildi
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}