package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactAdapter(context: Context, private val contactList: MutableList<Contact>):
    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    // Chamada sempre que nova célula precisar ser mostrada (uma nova view)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Posição (importante) -> qual elemento de datasource preparar para mostrar
        val contact = contactList[position]

        //Inflar a célula
        var tileContactBinding: TileContactBinding? = null

        // ConverterView -> pode ser uma célular reciclável
        // Se não existe uma, o converterView "vem" com valor null (null -> inflar novo), senão, recebi célula

        var contactTileView = convertView
        if(contactTileView == null){
            // Inflar nova célula
            // Resgatar um system service apartir de contexto (construtor do adapter)
            // Quando inflamos, além do layout inflater precisamos passar um parent
            tileContactBinding = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = tileContactBinding.root

            // Aponta para componentes de dentro da célula
            val tileContactHolder = TileContactHolder(
                tileContactBinding.nameTextView,
                tileContactBinding.emailTextView
            )
            contactTileView.tag = tileContactHolder

        }

        // Tendo o object contact e tendo célula inflada (troca os valores)
        val holder = contactTileView.tag as TileContactHolder
        holder.nameTextView.text = contact.name
        holder.emailTextView.text = contact.email

        return contactTileView
    }

    private data class TileContactHolder(val nameTextView: TextView, val emailTextView: TextView){
        // Sempre que criar nova célular (ao inflar), cria-se esse objeto
    }

}