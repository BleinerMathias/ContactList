package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source genérico
    private val contactList:MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactRvAdapter by lazy{
        ContactRvAdapter(contactList)
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
                contact?.also {newOrEditedContact ->
                    if(contactList.any{it.id == newOrEditedContact.id}){
                        contactList.indexOfFirst { it.id == newOrEditedContact.id }.also {position->
                            contactList[position] = newOrEditedContact
                        }
                    }else{
                        contactList.add(newOrEditedContact)
                    }
                    // Informar o adapter da modificação no datasource
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

        fillContacts()

        // Associar o recyclerView ao adapter
        activityMainBinding.contactsRecyclerView.adapter = contactAdapter

        // Gerenciador de layout para determinar a orientação dos componentes
        activityMainBinding.contactsRecyclerView.layoutManager = LinearLayoutManager(this)



        // Listener
     /*   activityMainBinding.contactsRecyclerView.set { _, _, position, _ ->
            startActivity(Intent(this,ContactActivity::class.java).apply {
                putExtra(EXTRA_CONTACT,contactList[position])
                putExtra(EXTRA_VIEW_CONTACT,true)
            })
        }*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.addContactMenuItem->{
                // Chama activity aguardando um resultado
                contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> {false}
        }
    }

    // Criar menu de contexto
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    // Tratar clique do menu de contexto
    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Para obter a posicão, é necessário fazer um cast
        val position = (item.menuInfo as AdapterContextMenuInfo).position

        return when(item.itemId){
            R.id.removeContactMenuItem ->{
                contactList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, getString(R.string.contact_removed), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMenuItem -> {
                val contact = contactList[position]
                contactActivityResultLauncher.launch(
                        Intent(this, ContactActivity::class.java).apply {
                        putExtra(EXTRA_CONTACT,contact)
                    }
                )
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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