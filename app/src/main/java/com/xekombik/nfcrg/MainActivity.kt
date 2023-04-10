package com.xekombik.nfcrg

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.xekombik.nfcrg.databinding.ActivityMainBinding
import android.nfc.NdefRecord
import android.nfc.tech.*

import android.os.Parcelable


class MainActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {
    private lateinit var binding: ActivityMainBinding

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var intentFiltersArray: Array<IntentFilter>? = null
    private var techListsArray: Array<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)


        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef)

        techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    }

    override fun onResume() {
        super.onResume()

        val intentFilters = arrayOf(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        )

        val techList = arrayOf(
            arrayOf(NfcA::class.java.name),
            arrayOf(NfcB::class.java.name),
            arrayOf(NfcF::class.java.name),
            arrayOf(
                NfcV::class.java.name
            ),
            arrayOf(IsoDep::class.java.name),
            arrayOf(MifareClassic::class.java.name),
            arrayOf(MifareUltralight::class.java.name),
            arrayOf(Ndef::class.java.name),
            arrayOf(NdefFormatable::class.java.name)
        )

        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, intentFilters, techList)


        val manager = getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter
        nfcAdapter?.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_F, null)

        val newFragment = if (adapter == null || !adapter.isEnabled) {
            ErrorFragment()
        } else {
            MainFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, newFragment)
            .commitNow()



    }

    override fun onPause() {
        super.onPause()

        nfcAdapter!!.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val id = tag?.id
            Toast.makeText(this, "Tag ID: ${ByteArrayToHexString(id)}", Toast.LENGTH_LONG).show()
        }
    }

    private fun ByteArrayToHexString(inarray: ByteArray?): String {
        var i: Int
        var j = 0
        val hex = StringBuilder(inarray!!.size * 2)
        while (j < inarray.size) {
            i = inarray[j].toInt() and 0xff
            if (i <= 0xf) hex.append('0')
            hex.append(Integer.toHexString(i))
            ++j
        }
        return hex.toString()
    }

    override fun onTagDiscovered(tag: Tag?) {
        val tagId = tag?.id.toString()
        Log.d("NFC", "Tag ID : $tagId")
    }
}
