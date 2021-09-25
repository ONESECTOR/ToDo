package com.sector.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sector.todo.R
import com.sector.todo.data.models.ToDoData
import com.sector.todo.data.viewmodel.ToDoViewModel
import com.sector.todo.databinding.FragmentUpdateBinding
import com.sector.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding

    private val args by navArgs<UpdateFragmentArgs>()

    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.apply {
            etCurrentTitle.setText(args.currentItem.title)
            etCurrentDescription.setText(args.currentItem.description)
            spinnerCurrentPriorities.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
            spinnerCurrentPriorities.onItemSelectedListener = sharedViewModel.listener
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        binding.apply {
            val title = etCurrentTitle.text.toString()
            val description = etCurrentDescription.text.toString()
            val getPriority = spinnerCurrentPriorities.selectedItem.toString()

            val validation = sharedViewModel.verifyDataFromUser(title, description)

            if (validation) {
                // Update current item
                val updatedItem = ToDoData(
                    args.currentItem.id,
                    title,
                    sharedViewModel.parsePriority(getPriority),
                    description
                )
                toDoViewModel.updateData(updatedItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully updated!",
                    Toast.LENGTH_SHORT
                ).show()

                //Navigate back
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill out all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Show AlertDialog to confirm item removal
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_,_ ->
            toDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully removed: '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_,_ -> }

        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()
    }
}