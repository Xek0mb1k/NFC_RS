package com.xekombik.nfcrg

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.NfcManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xekombik.nfcrg.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!


    private var adapter: NfcAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val animCircle = binding.animationCircle.background as AnimationDrawable


        val manager = context?.getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = manager.defaultAdapter

        binding.sendDataButton.setOnClickListener {
            sendData()
            animCircle.start()
        }

        binding.readDataButton.setOnClickListener {
            readData()
            animCircle.start()
        }


        return binding.root
    }

    private fun sendData() {
        binding.animationCircle.visibility = VISIBLE
        binding.modeTextView.visibility = VISIBLE
        binding.modeTextView.text = getText(R.string.data_sending)
        binding.editText.hint = "Enter text..."
        binding.editText.text
        adapter?.disableReaderMode(activity)

        val text = binding.editText.text.toString()
        val data = text.toByteArray(Charsets.UTF_8)
        val record = NdefRecord.createMime("text/plain", data)
        val message = NdefMessage(arrayOf(record))
        adapter?.setNdefPushMessage(message, context as Activity?)
    }

    private fun readData() {
        binding.animationCircle.visibility = VISIBLE
        binding.modeTextView.visibility = VISIBLE
        binding.modeTextView.text = getText(R.string.data_reading)
        binding.editText.hint = "Data will be here"

        adapter?.enableReaderMode(activity, ReaderCallback {},
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)


    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(activity, 0,
            Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val intentFilter = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        )
        adapter?.enableForegroundDispatch(activity, pendingIntent, intentFilter, null)
    }

    override fun onPause() {
        super.onPause()
        adapter?.disableForegroundDispatch(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}