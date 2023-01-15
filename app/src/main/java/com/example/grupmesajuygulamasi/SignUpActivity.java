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

public class SignUpActivity extends AppCompatActivity {
    // Sign Up sayfasındaki özellikleri atamak için alanlar oluşturuldu
    EditText emailEditText;
    EditText passwordEditText;
    Button signUpToLoginButton;
    Button signUpButton;

    // Firebase'a kullanıcı kaydı yapmak için alan oluşturuldu
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Butonlara ve yazı giriş alanlarına SignUp sayfasındaki buton ve yazı giriş alanları id'leri vasıtasıyla atandı
        emailEditText = findViewById(R.id.signUpPage_emailEditText);
        passwordEditText = findViewById(R.id.signUpPage_passwordEditText);
        signUpButton = findViewById(R.id.signUpPage_signUpButton);
        signUpToLoginButton = findViewById(R.id.signUpPage_loginButton);

        // Tek bir nesne oluşturulması ve tekrar çağrılırsa aynı nesnenin döndürülmesi sağlandı
        firebaseAuth = FirebaseAuth.getInstance();

        // Kayıt Ol butonuna tıklandığında yapılacak işlemler
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arayüzdeki yazı giriş alanlarına girilen email ve şifre alanları String değişkenlere atandı
                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                // Eğer şifre veya email alanı boş bırakılmışsa
                if(email.isEmpty() || password.isEmpty()) {
                    // Bilgilendirme mesajı verildi
                    Toast.makeText(SignUpActivity.this, "Email veya şifre alanları boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                    // Eğer bu alanlardan biri veya her ikisi de boş bırakılmışsa işlemlere devam etme
                    return;
                }

                // Girilen şifre ve email alanları ile kayıt oluşturma işlemi
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    // Kayıtın başarılı olup olmaması bir task ile gönderildi
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Kayıt başarılıysa
                        if(task.isSuccessful()) {
                            // Kayıt başarılı mesajı oluşturuldu ve Ana Sayfaya Gidildi
                            Toast.makeText(SignUpActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                    }
                });
            }
        });

        // Girş Yap sayfasına yönlendirme
        signUpToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kayıt Ol sayfasından Giriş Yap sayfasına gidildi
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}