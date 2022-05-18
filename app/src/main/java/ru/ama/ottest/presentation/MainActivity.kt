package ru.ama.ottest.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.HtmlCompat
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
                   showPopupText(findViewById(R.id.menu_help),getString(R.string.ma_menu_help))
				   //Toast.makeText(this,  "menu_help", Toast.LENGTH_SHORT).show()
                   true
				   }
               R.id.menu_about -> {
                   showAlert("",getString(R.string.ma_menu_about))
                  // showPopupText(findViewById(R.id.menu_about),"о пиложении")
				   Toast.makeText(this,  "menu_about", Toast.LENGTH_SHORT).show()
                   true
				   }
               R.id.menu_share -> {
                   sharetext("Тесты","приложение для тестов",false)
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
        popupWindow.animationStyle = R.style.dialog_animation_addslovoFU
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding2: ItemCoinInfoBinding= ItemCoinInfoBinding.inflate(layoutInflater)
        popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.nulldr))
        binding2.tvMenuHelp.linksClickable = true
        binding2.tvMenuHelp.autoLinkMask = Linkify.WEB_URLS
        binding2.tvMenuHelp.text = HtmlCompat.fromHtml(txt,HtmlCompat.FROM_HTML_MODE_LEGACY)
        //binding2.tvMenuHelp.text=txt
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = binding2.root
        popupWindow.showAsDropDown(anchor)

    }
 fun sharetext(
            textZagol: String,
            textBody: String,
            isEmail: Boolean
        ) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

            if (isEmail) {
                sharingIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf("10ama@mail.ru")
                )
                sharingIntent.type = "message/rfc822"
            } else
                sharingIntent.type = "text/plain"
				sharingIntent.putExtra(
                android.content.Intent.EXTRA_SUBJECT,
                textZagol
            )
            sharingIntent.putExtra(
                android.content.Intent.EXTRA_TEXT, 
                textBody
            )
            val d = Intent.createChooser(
                sharingIntent,
                "использовать"
            )
            startActivity(d)
        }
  fun showAlert(titl: String, mes: String) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(titl)
            builder.setCancelable(true)
            builder.setMessage(mes)

            builder.setPositiveButton("ok") { dialogInterface, i -> dialogInterface.dismiss() }
            val dialoga = builder.create()
            dialoga.window!!.attributes .windowAnimations = R.style.dialog_animation_pd
            dialoga.show()
        }


    private fun launchFirstScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseTestFragment.newInstance())
            .commit()
    }
}
