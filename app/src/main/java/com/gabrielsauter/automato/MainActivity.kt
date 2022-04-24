package com.gabrielsauter.automato

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var txtView: TextView
    private lateinit var txtViewTransition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editTextPalavra)
        button = findViewById(R.id.buttonValidar)
        txtView = findViewById(R.id.txtViewPalavra)
        txtViewTransition = findViewById(R.id.txtViewTransitions)

        button.setOnClickListener {
            if(listener()){
                txtView.text = "Palavra Aceita"
            }else{
                txtView.text = "Palavra Rejeitada"
            }
        }
    }

    private fun listener() : Boolean{
        txtViewTransition.text = ""
        val word = editText.text.toString()

        val initialState = 0
        val finalState = 0
        val transitionTable = arrayOf(
                  //L   G   S   P   V   N   F   D   E   T   C   Z
            arrayOf(1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), //Desligado = 0)
            arrayOf(-1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), //Ligado = 1)
            arrayOf(-1, -1, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1), //CalculandoRota = 2)
            arrayOf(-1, -1, -1, 4, -1, 5, -1, -1, -1, -1, -1, -1),  //SensorObstaculo = 3)
            arrayOf(-1, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, -1), //Parar = 4)
            arrayOf(-1, -1, -1, -1, -1, -1, 6,  7,  8,  9,  10, -1),//VerRotaInstante = 5)
            arrayOf(-1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1),//Frente = 6)
            arrayOf(-1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1),//Direita = 7)
            arrayOf(-1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1),//Esquerda = 8)
            arrayOf(-1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1),//Tras = 9)
            arrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0), //Estacionar = 10)
        )

        var state = initialState
        var i = 0
        var index = 0
        for(c: Char in word){
            if(c!='l' && c!='g' && c!='s' && c!='p' && c!='v' && c!='n' && c!='f'&& c!='d'&& c!='e'&& c!='t'&& c!='c'&& c!='z') return false
            when(c){
                'l'-> i=0
                'g'-> i=1
                's'-> i=2
                'p'-> i=3
                'v'-> i=4
                'n'-> i=5
                'f'-> i=6
                'd'-> i=7
                'e'-> i=8
                't'-> i=9
                'c'-> i=10
                'z'-> i=11
            }
            state = transitionTable[state][i]
            if(state == -1){
                txtViewTransition.append("Erro\n")
                return false
            }
            when(state){
                0 -> txtViewTransition.append("Estacionar,Z->Desligado\n")
                1 -> txtViewTransition.append("Desligado,L->Ligado\n")
                2 -> txtViewTransition.append("Ligado,G->CalculandoRota\n")
                3 -> {
                    if(i==2)
                        txtViewTransition.append("CalculandoRota,S->SensorObstaculo\n")
                    else if(i==4 && word[index-1] == 'p')
                        txtViewTransition.append("PararFreiar,V->SensorObstaculo\n")
                    else if(i==4 && word[index-1] == 'f')
                        txtViewTransition.append("Frente,V->SensorObstaculo\n")
                    else if(i==4 && word[index-1] == 'd')
                        txtViewTransition.append("Direita,V->SensorObstaculo\n")
                    else if(i==4 && word[index-1] == 'e')
                        txtViewTransition.append("Esquerda,V->SensorObstaculo\n")
                    else if(i==4 && word[index-1] == 't')
                        txtViewTransition.append("Tras,V->SensorObstaculo\n")
                }
                4 -> txtViewTransition.append("SensorObstaculo,P->PararFreiar\n")
                5 -> txtViewTransition.append("SensorObstaculo,N->VerRotaDoInstante\n")
                6 -> txtViewTransition.append("VerRotaDoInstante,F->Frente\n")
                7 -> txtViewTransition.append("VerRotaDoInstante,D->Direita\n")
                8 -> txtViewTransition.append("VerRotaDoInstante,E->Esquerda\n")
                9 -> txtViewTransition.append("VerRotaDoInstante,T->Tras\n")
                10 -> txtViewTransition.append("VerRotaDoInstante,C->Estacionar\n")
            }
            index++
        }
        return state == finalState
    }
}