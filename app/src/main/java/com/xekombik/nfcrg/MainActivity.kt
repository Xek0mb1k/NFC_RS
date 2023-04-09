package com.xekombik.nfcrg

import android.content.Context
import android.nfc.NfcManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xekombik.nfcrg.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        val manager = getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter


        val newFragment = if(adapter == null || !adapter.isEnabled){
            ErrorFragment()
        }else{
            MainFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, newFragment)
            .commitNow()

        super.onResume()
    }
}