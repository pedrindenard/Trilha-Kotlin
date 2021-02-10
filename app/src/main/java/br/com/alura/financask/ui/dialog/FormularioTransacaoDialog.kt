package br.com.alura.financask.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import br.com.alura.financask.R
import br.com.alura.financask.delegate.TransacaoDelegate
import br.com.alura.financask.extension.converteParaCalendar
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.alura.financask.model.Tipo
import br.com.alura.financask.model.Transacao
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.util.*

abstract class FormularioTransacaoDialog(private val context: Context,
                                     private val viewGroup: ViewGroup) {

    abstract protected val tituloBotaoPositivo: String
    private val viewCriada = criaLayount()
    protected val campoValor: EditText = viewCriada.form_transacao_valor
    protected val campoCategoria: Spinner = viewCriada.form_transacao_categoria
    protected val campoData: EditText = viewCriada.form_transacao_data

    fun chama(tipo: Tipo, transacaoDelegate: TransacaoDelegate) {
        configuraCampoData()
        configuraCampoCategoria(tipo)
        configuraFormulario(tipo, transacaoDelegate)
    }

    private fun configuraFormulario(tipo: Tipo, transacaoDelegate: TransacaoDelegate) {

        val titulo = tituloPor(tipo)

        AlertDialog.Builder(context)
                .setTitle(titulo)
                .setView(viewCriada)
                .setPositiveButton(tituloBotaoPositivo
                ) { _, _ ->
                    val valorEmTexto = campoValor.text.toString()
                    val dataEmTexto = campoData.text.toString()
                    val categoriaEmTexto = campoCategoria.selectedItem.toString()

                    val valor = converteCampoValor(valorEmTexto)
                    val data = dataEmTexto.converteParaCalendar()

                    val transacaoCriada = Transacao(
                            tipo = tipo,
                            valor = valor,
                            data = data,
                            categoria = categoriaEmTexto
                    )
                    transacaoDelegate.delegate(transacaoCriada)
                }
                .setNegativeButton("Cancelar", null)
                .show()
    }

    abstract protected fun tituloPor(tipo: Tipo): Int

    private fun converteCampoValor(valorEmTexto: String) : BigDecimal {
        return try {
            BigDecimal(valorEmTexto)
        } catch (exception: NumberFormatException) {
            Toast.makeText(context, "Falha na conversão de valor", Toast.LENGTH_LONG).show()
            BigDecimal.ZERO
        }
    }

    private fun configuraCampoCategoria(tipo: Tipo) {

        val categorias = categoriasPor(tipo)

        val adapter = ArrayAdapter
                .createFromResource(
                        context,
                        categorias,
                        android.R.layout.simple_spinner_dropdown_item)

        campoCategoria.adapter = adapter
    }

    protected fun categoriasPor(tipo: Tipo): Int {
        if (tipo == Tipo.RECEITA) {
            return R.array.categorias_de_receita
        }
            return R.array.categorias_de_despesa
    }

    private fun configuraCampoData() {
        val hoje = Calendar.getInstance()
        val ano = hoje.get(Calendar.YEAR)
        val mes = hoje.get(Calendar.MONTH)
        val dia = hoje.get(Calendar.DAY_OF_MONTH)

        campoData.setText(hoje.formataParaBrasileiro())
        campoData.setOnClickListener {
            DatePickerDialog(context,
                    { _, ano, mes, dia ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(ano, mes, dia)
                        campoData.setText(dataSelecionada.formataParaBrasileiro())
                    }, ano, mes, dia)
            .show()
        }
    }

    private fun criaLayount() : View {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.form_transacao, viewGroup, false)
    }
}