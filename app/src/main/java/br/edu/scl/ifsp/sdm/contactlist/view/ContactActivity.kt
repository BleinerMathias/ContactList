package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {

    private val activityContactBinding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityContactBinding.root)

        setSupportActionBar(activityContactBinding.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let{received ->
            val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT,false)

            with(activityContactBinding){

                if(viewContact){
                    nameEditText.isEnabled = false
                    emailEditText.isEnabled = false
                    addressEditText.isEnabled = false
                    phoneEditText.isEnabled = false
                    saveButton.visibility = View.GONE
                }

                nameEditText.setText(received.name)
                emailEditText.setText(received.email)
                addressEditText.setText(received.address)
                phoneEditText.setText(received.phone)
            }
        }


        activityContactBinding.apply {
            saveButton.setOnClickListener{

                val contact = Contact(
                    receivedContact?.id?:hashCode(),
                    nameEditText.text.toString(),
                    addressEditText.text.toString(),
                    phoneEditText.text.toString(),
                    emailEditText.text.toString()
                )

                // Retornar o contato para main -> intent
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()

            }
        }

    }
}