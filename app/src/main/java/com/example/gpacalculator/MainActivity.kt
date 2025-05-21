package com.example.gpacalculator

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var subjectTable: LinearLayout
    private lateinit var gpaResultText: TextView
    private lateinit var subjectCountEditText: EditText
    private val inputRows = mutableListOf<Triple<EditText, EditText, EditText>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subjectTable = findViewById(R.id.subjectTable)
        gpaResultText = findViewById(R.id.gpaResultText)
        subjectCountEditText = findViewById(R.id.subjectCountEditText)

        val generateButton = findViewById<Button>(R.id.generateButton)
        val calculateButton = findViewById<Button>(R.id.calculateGpaButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        generateButton.setOnClickListener {
            val count = subjectCountEditText.text.toString().toIntOrNull()
            subjectTable.removeAllViews()
            inputRows.clear()

            if (count == null || count <= 0) {
                Toast.makeText(this, "Enter a valid number of subjects", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until count) {
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 12
                    }
                }

                val courseEdit = EditText(this).apply {
                    hint = "Course"
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setBackgroundResource(R.drawable.edittext_background)
                    setPadding(14, 14, 14, 14)
                }

                val marksEdit = EditText(this).apply {
                    hint = "Marks"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setBackgroundResource(R.drawable.edittext_background)
                    setPadding(14, 14, 14, 14)
                }

                val creditEdit = EditText(this).apply {
                    hint = "Credits"
                    inputType = InputType.TYPE_CLASS_NUMBER
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setBackgroundResource(R.drawable.edittext_background)
                    setPadding(14, 14, 14, 14)
                }

                row.addView(courseEdit)
                row.addView(marksEdit)
                row.addView(creditEdit)
                subjectTable.addView(row)
                inputRows.add(Triple(courseEdit, marksEdit, creditEdit))
            }
        }

        calculateButton.setOnClickListener {
            if (inputRows.isEmpty()) {
                Toast.makeText(this, "Please generate the subject table first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var totalPoints = 0.0
            var totalCredits = 0

            for ((course, marks, credit) in inputRows) {
                val courseName = course.text.toString()
                val marksVal = marks.text.toString().toDoubleOrNull()
                val creditVal = credit.text.toString().toIntOrNull()

                if (courseName.isBlank()) {
                    Toast.makeText(this, "Course name cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (marksVal == null || marksVal !in 0.0..100.0) {
                    Toast.makeText(this, "Marks must be between 0 and 100", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (creditVal == null || creditVal <= 0) {
                    Toast.makeText(this, "Credits must be greater than 0", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val gradePoint = when {
                    marksVal >= 90 -> 10
                    marksVal >= 80 -> 9
                    marksVal >= 70 -> 8
                    marksVal >= 60 -> 7
                    marksVal >= 50 -> 6
                    marksVal >= 40 -> 5
                    else -> 0
                }

                totalPoints += gradePoint * creditVal
                totalCredits += creditVal
            }

            val gpa = if (totalCredits > 0) totalPoints / totalCredits else 0.0
            gpaResultText.text = "Your GPA: %.2f".format(gpa)
        }

        resetButton.setOnClickListener {
            subjectCountEditText.text.clear()
            subjectTable.removeAllViews()
            inputRows.clear()
            gpaResultText.text = "Your GPA: "
            Toast.makeText(this, "Reset successful", Toast.LENGTH_SHORT).show()
        }
    }
}
