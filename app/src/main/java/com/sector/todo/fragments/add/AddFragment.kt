package com.sector.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sector.todo.R
import com.sector.todo.data.models.Priority
import com.sector.todo.data.models.ToDoData
import kotlinx.android.synthetic.main.row_layout.view.*
import com.sector.todo.data.viewmodel.ToDoViewModel
import com.sector.todo.databinding.FragmentAddBinding
import com.sector.todo.fragments.SharedViewModel

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.apply {
            spinnerPriorities.onItemSelectedListener = sharedViewModel.listener
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        binding.apply {
            val title = etTitle.text.toString()
            val priority = spinnerPriorities.selectedItem.toString()
            val description = etDescription.text.toString()

            val validation = sharedViewModel.verifyDataFromUser(title, description)

            if (validation) {
                // Insert data to database
                val newData = ToDoData(
                    0,
                    title,
                    sharedViewModel.parsePriority(priority),
                    description
                )
                toDoViewModel.insertData(newData)
                Toast.makeText(
                    requireContext(),
                    "Successfully added!",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate back
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}