package com.ashford.tictactoe

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

/*Things to do
        -Create a Player
        -Assign a value to each button when clicked
        -Make the button unclickable after its been clicked
        -Alternate player value after a button has been pressed
        -Check to see if win condition is met
        -If win, restart game by reseting all values and emptying boxes, increment score of winner
        -if tie, reset game and all values
        -When a player gets 5 wins reset game
*/

class MainActivity : AppCompatActivity() {
    lateinit var mp: MediaPlayer
    lateinit var animDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animDrawable = appBackground.background as AnimationDrawable

        //I do not own this music. Music from No Straight Roads. No Rights Reserved.
        mp = MediaPlayer.create(this, R.raw.dj_sub_supernova)
        mp.isLooping = true

        //Draw background and set animation attributes
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(5000)
    }

    override fun onPause() {
        super.onPause()
        mp.pause()
        animDrawable.stop()
    }

    override fun onResume() {
        super.onResume()
        animDrawable.start()
        mp.start()
    }


    //When a button is clicked assign a cell value to the button
    fun buClick(view: View) {

        val buSelected = view as Button
        var cellID = 0
        when (buSelected.id) {
            R.id.button -> cellID = 1
            R.id.button2 -> cellID = 2
            R.id.button3 -> cellID = 3
            R.id.button4 -> cellID = 4
            R.id.button5 -> cellID = 5
            R.id.button6 -> cellID = 6
            R.id.button7 -> cellID = 7
            R.id.button8 -> cellID = 8
            R.id.button9 -> cellID = 9
        }
        Log.d("buClick", buSelected.id.toString())
        Log.d("buClick, CellID: ", cellID.toString())

        playGame(cellID, buSelected)
    }

    //Global Variables
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var activePlayer = 1
    var player1WinCount = 0
    var player2WinCount = 0
    var playCount = 0

    private fun playGame(cellID: Int, buSelected: Button) {
        if(activePlayer == 1) {
            buSelected.text = "X"
            player1.add(cellID)
            activePlayer = 2
            playCount++
            buSelected.isEnabled = false
            if(playCount != 5){
                autoPlay()
            }
        } else if(activePlayer == 2 && playCount != 5){
            buSelected.text = "O"
            player2.add(cellID)
            activePlayer = 1
            buSelected.isEnabled = false
        }

        //Check for the winner
        checkWinner()
    }

    private fun checkWinner() {
        var winner = -1


        //Row 1
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }

        //Row 2
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }

        //Row 3
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }

        //Column 1
        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }

        //Column 2
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }

        //Column 3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }

        //Diagonal 1
        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2
        }

        //Diagonal 2
        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2
        }

        if(winner == 1){
            player1WinCount++
        } else if(winner == 2){
            player2WinCount++
        }

        when{
            player1WinCount == 5 -> {
                Toast.makeText(this, "Player 1 wins the game!", Toast.LENGTH_LONG).show()
                Thread.sleep(1500)
                restartRound()
            }
            player2WinCount == 5 -> {
                Toast.makeText(this, "Player 2 wins the game!", Toast.LENGTH_LONG).show()
                Thread.sleep(1500)
                restartRound()
            }

        }

        when {
            winner == 1 -> {
                Toast.makeText(this, "Player 1 wins the round!", Toast.LENGTH_SHORT).show()
                player1Wins.text = "P1: $player1WinCount"
                Thread.sleep(1000)
                restartRound()
            }
            winner == 2 -> {
                Toast.makeText(this, "Player 2 wins the round!", Toast.LENGTH_SHORT).show()
                player2Wins.text = "P2: $player2WinCount"
                Thread.sleep(1000)
                restartRound()
            }
            playCount == 5 -> {
                Toast.makeText(this, "TIE!", Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
                restartRound()
            }
        }


    }

    private fun autoPlay() {

        var emptyCellsList = ArrayList<Int>()
        for (cellID in 1..9) {
            if (!(player1.contains(cellID) || player2.contains((cellID)))) {
                emptyCellsList.add(cellID)
            }
        }

        val rand = Random()
        val randIndex = rand.nextInt(emptyCellsList.size)
        val cellID = emptyCellsList[randIndex]

        var buSelected: Button?
        buSelected = when (cellID) {
            1 -> button
            2 -> button2
            3 -> button3
            4 -> button4
            5 -> button5
            6 -> button6
            7 -> button7
            8 -> button8
            9 -> button9
            else -> {
                button
            }

        }

        playGame(cellID, buSelected)
    }

    private fun restartRound() {
        activePlayer = 1
        playCount = 0
        player1.clear()
        player2.clear()

        if(player1WinCount == 5){
            player1WinCount = 0
            player2WinCount = 0
            player1Wins.text= "P1: $player1WinCount"
            player2Wins.text= "P2: $player2WinCount"
        } else if(player2WinCount == 5){
            player1WinCount = 0
            player2WinCount = 0
            player1Wins.text= "P1: $player1WinCount"
            player2Wins.text= "P2: $player2WinCount"
        }

        for (cellID in 1..9) {
            var buSelected: Button? = when (cellID) {
                1 -> button
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {
                    button
                }
            }
            buSelected!!.isEnabled = true
            buSelected.text = ""

        }
    }
}

