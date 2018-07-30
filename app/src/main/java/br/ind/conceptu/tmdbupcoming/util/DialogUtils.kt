package br.ind.conceptu.tmdbupcoming.util

import android.app.AlertDialog
import android.content.Context
import br.ind.conceptu.tmdbupcoming.R

class DialogUtils {
    companion object {
        fun showDialogWithDoubleAction(context: Context, title:String, message:String, positiveTitle:String, negativeTitle:String, actionPositive:Runnable, actionNegative:Runnable?){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton(positiveTitle, { dialog, _ ->
                actionPositive.run()
                dialog.dismiss()
            })
            dialogBuilder.setNegativeButton(negativeTitle, { dialog, _ ->
                actionNegative?.run()
                dialog.dismiss()
            })

            val dialog = dialogBuilder.create()
            dialog.show()
        }

        fun showRetryDialogWithAction(context: Context, title:String, message:String, action:Runnable){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton(context.getString(R.string.retry), { dialog, _ ->
                action.run()
                dialog.dismiss()
            })

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }
}