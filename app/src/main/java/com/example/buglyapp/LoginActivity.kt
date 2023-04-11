package com.example.buglyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buglyapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)//(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textSignup.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val email =binding.LoginEmail.text.toString()
            val pass = binding.LoginPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){


                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this,HomeActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT ).show()

                        }
                    }



            }else{
                Toast.makeText(this,"Empty Fields Are not Allowed", Toast.LENGTH_SHORT ).show()

            }
        }
    }
}