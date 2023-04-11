package com.example.buglyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buglyapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)//(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUP.setOnClickListener {
            val email =binding.SignupEmail.text.toString()
            val pass = binding.SignupPassword.text.toString()
            val confirmpass = binding.conformPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()){
                if( pass == confirmpass){

                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT ).show()

                        }
                    }


                } else {
                    Toast.makeText(this,"password is not match", Toast.LENGTH_SHORT ).show()

                }
            }else{
                Toast.makeText(this,"Empty Fields Are not Allowed", Toast.LENGTH_SHORT ).show()

            }
        }
    }
}