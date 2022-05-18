package ru.ama.ottest.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ActivityMainBinding
import ru.ama.ottest.databinding.ItemCoinInfoBinding
import ru.ama.ottest.databinding.ItemResultBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(binding.root)
        if (savedInstanceState == null) {
            launchFirstScreen()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.menu_main_activity, menu)
       /* val adapterm : ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,  getResources().getStringArray(R.array.menu_main_activity))
        val m_ddl = menu?.findItem(R.id.menu_ma) as MenuItem
        var ddl=  m_ddl.actionView as Spinner
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ddl.setAdapter(adapterm)
        ddl.setSelection(0)*/
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
               R.id.menu_help -> {
                   showPopupText(findViewById(R.id.menu_help),"помощь")
				   //Toast.makeText(this,  "menu_help", Toast.LENGTH_SHORT).show()
                   true
				   }
               R.id.menu_about -> {
                  // showPopupText(findViewById(R.id.menu_about),"о пиложении")
				   Toast.makeText(this,  "menu_about", Toast.LENGTH_SHORT).show()
                   true
				   }
               R.id.menu_share -> {
                  // showPopupText(findViewById(R.id.menu_share),"поделиться")
				   Toast.makeText(this,  "menu_share", Toast.LENGTH_SHORT).show()
                   true
				   }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menu.add( "Item 1")
        popupMenu.menu.add( "Item 2")
        popupMenu.menu.add( "Item 3")
        popupMenu.setOnMenuItemClickListener { item ->
            Toast.makeText(view.context, item.title.toString() + "clicked", Toast.LENGTH_SHORT)
                .show()
            true
        }
        popupMenu.show()
    }

    fun showPopupText(anchor: View, txt:String) {
        val popupWindow = PopupWindow(application)
         //   popupWindow.animationStyle = R.style.dialog_animation_addslovoFU
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding2: ItemCoinInfoBinding= ItemCoinInfoBinding.inflate(layoutInflater)
    //    val view = inflater.inflate(R.layout.item_coin_info, null)
     //   val rv = view.findViewById<View>(R.id.tvMenuHelp) as TextView
        popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.nulldr))
        /*val adapterm : ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,  getResources().getStringArray(R.array.menu_main_activity))
        rv.adapter = adapterm*/
        binding2.tvMenuHelp.text=txt
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = binding2.root
        popupWindow.showAsDropDown(anchor)

    }

    private fun launchFirstScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseTestFragment.newInstance())
            .commit()
    }
}
