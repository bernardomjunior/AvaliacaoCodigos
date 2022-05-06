package com.android.starter

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.starter.model.Program
import com.android.starter.model.Program.BINARYTREE
import com.android.starter.model.Program.FANNKUCH
import com.android.starter.model.Program.FASTA
import com.android.starter.model.Program.KNUCLEOTIDE
import com.android.starter.model.Program.MANDELBROT
import com.android.starter.model.Program.NBODY
import com.android.starter.model.Program.PIDIGITS
import com.android.starter.model.Program.REGEX
import com.android.starter.model.Program.REVCOMP
import com.android.starter.model.Program.SPECTRAL
import com.android.starter.network.Repository
import com.android.starter.programs.Binarytrees
import com.android.starter.programs.Fannkuchredux
import com.android.starter.programs.Fasta
import com.android.starter.programs.Knucleotide
import com.android.starter.programs.Mandelbrot
import com.android.starter.programs.Nbody
import com.android.starter.programs.Pidigits
import com.android.starter.programs.RegexRedux
import com.android.starter.programs.RevComp
import com.android.starter.programs.Spectral
import kotlinx.coroutines.runBlocking
import java.io.File

class MainActivity : AppCompatActivity() {

    private val repository = Repository()
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 23
        )
        runBlocking {
            startProgram(repository.started())
            repository.logData()
            file?.delete()
            repository.done()
        }
    }

    private fun startProgram(program: Program) {
        when (program) {
            FANNKUCH -> Fannkuchredux().runCode(params[program]!!)
            BINARYTREE -> Binarytrees().runCode(params[program]!!)
            FASTA -> {
                file = File(getExternalFilesDir(null), "Output.txt")
                Fasta().runCode(params[program]!!, file)
            }
            MANDELBROT -> {
                file = File(getExternalFilesDir(null), "Output.txt")
                Mandelbrot().runCode(params[program]!!, file)
            }
            NBODY -> Nbody().runCode(params[program]!!)
            PIDIGITS -> Pidigits().runCode(params[program]!!)
            SPECTRAL -> Spectral().runCode(params[program]!!)
            REGEX -> RegexRedux().runCode(getFromAssets(params[program]!!))
            REVCOMP -> RevComp().runCode(getFromAssets(params[program]!!))
            KNUCLEOTIDE -> Knucleotide().runCode(getFromAssets(params[program]!!))
        }
    }

    private val params = mapOf(
        BINARYTREE to 19,
        FANNKUCH to 11,
        FASTA to 25000000,
        SPECTRAL to 5500,
        MANDELBROT to 10000,
        NBODY to 7000000,
        PIDIGITS to 4000,
        REGEX to 1500000,
        REVCOMP to 25000000,
        KNUCLEOTIDE to 10000000,
    )

    private fun getFromAssets(n: Int) = assets.open("input$n.txt")

}