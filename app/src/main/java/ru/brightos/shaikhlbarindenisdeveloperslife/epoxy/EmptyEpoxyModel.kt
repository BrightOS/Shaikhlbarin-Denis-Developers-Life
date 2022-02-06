package ru.brightos.shaikhlbarindenisdeveloperslife.epoxy

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.brightos.shaikhlbarindenisdeveloperslife.R
import ru.brightos.shaikhlbarindenisdeveloperslife.util.KotlinHolder

@EpoxyModelClass(layout = R.layout.layout_empty)
abstract class EmptyEpoxyModel : EpoxyModelWithHolder<EmptyEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var text: String

    @EpoxyAttribute
    var code = 0

    override fun bind(holder: Holder) {
        var kol = 0

        holder.text.text = text
        holder.code.text = "$code"

        holder.root.setOnClickListener {
            kol++
            if (kol % 3 == 0) {
                when (kol / 3) {
                    0 -> Toast.makeText(
                        holder.root.context,
                        "У пользователя нет постов. Давай, понажимай ещё.",
                        Toast.LENGTH_LONG
                    ).show()
                    1 -> Toast.makeText(
                        holder.root.context,
                        "Перестань, пожалуйста.",
                        Toast.LENGTH_SHORT
                    ).show()
                    2 -> Toast.makeText(
                        holder.root.context,
                        "Мне, знаешь ли, не очень приятно...",
                        Toast.LENGTH_SHORT
                    ).show()
                    3 -> Toast.makeText(
                        holder.root.context,
                        "Хватит на меня нажимать! >:(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    inner class Holder : KotlinHolder() {
        val root by bind<MaterialCardView>(R.id.root_empty)
        val code by bind<TextView>(R.id.code)
        val text by bind<TextView>(R.id.empty_text)
    }
}