package br.com.alura.financask.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.alura.financask.R
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.alura.financask.model.Tipo
import br.com.alura.financask.model.Transacao
import br.com.alura.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import kotlinx.android.synthetic.main.resumo_card.*
import java.math.BigDecimal
import java.util.*

class ListaTransacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        val transacoes : List<Transacao> = transaçõesDeExemplo()

        adicionaReceitaNoResumo(transacoes)

        configuraLista(transacoes)
    }

    private fun adicionaReceitaNoResumo(transacoes: List<Transacao>) {
        var totalReceita = BigDecimal.ZERO
        for (transacao in transacoes) {
            if (transacao.tipo == Tipo.RECEITA) {
                totalReceita = totalReceita.plus(transacao.valor)
            }
        }
        resumo_card_receita.text = totalReceita.formataParaBrasileiro()
    }

    private fun configuraLista(transacoes: List<Transacao>) {
        lista_transacoes_listview.adapter = ListaTransacoesAdapter(transacoes, this)
    }

    private fun transaçõesDeExemplo(): List<Transacao> {
        return listOf(
                Transacao(valor = BigDecimal(20.5), tipo = Tipo.DESPESA, data = Calendar.getInstance()),
                Transacao(valor = BigDecimal(100.0), tipo = Tipo.RECEITA, categoria = "Economia"),
                Transacao(valor = BigDecimal(200.0), tipo = Tipo.DESPESA, data = Calendar.getInstance()),
                Transacao(valor = BigDecimal(500.0), tipo = Tipo.RECEITA, categoria = "Prêmio")
        )
    }
}