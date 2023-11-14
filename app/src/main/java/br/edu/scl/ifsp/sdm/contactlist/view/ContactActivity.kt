package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
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

        activityContactBinding.apply {
            saveButton.setOnClickListener{
                val contact = Contact(
                    hashCode(),
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