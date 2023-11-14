package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source genérico
    private val contactList:MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ArrayAdapter<String> by lazy{
        ArrayAdapter(this,android.R.layout.simple_list_item_1,contactList.map{it.toString()})
    }

    private lateinit var contactActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setSupportActionBar(activityMainBinding.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        contactActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode === RESULT_OK){
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also {
                    if(contactList.any{it.id == contact.id}){

                    }else{
                        contactList.add(contact)
                        contactAdapter.add(contact.toString())
                    }
                    // Informar o adapter da modificação no datasource
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

        fillContacts()

        // Associar o listview ao adapter -> genérico
        activityMainBinding.contactsListView.adapter = contactAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.addContactMenuItem->{
                // Apenas chama a activity
                //startActivity(Intent(this, ContactActivity::class.java))

                // Chama activity aguardando um resultado
                contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java))

                true
            }
            else -> {false}
        }
    }

    private fun fillContacts(){
        for(i in 1..50){
            contactList.add(
                Contact(i,
                    "Nome $i",
                    "Endereço $i",
                    "Telefone $i",
                    "Email $i"
                )
            )
        }
    }


}