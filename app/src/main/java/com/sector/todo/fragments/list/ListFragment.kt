package com.sector.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sector.todo.R
import com.sector.todo.data.viewmodel.ToDoViewModel
import com.sector.todo.databinding.FragmentListBinding
import com.sector.todo.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        // Set menu
        setHasOptionsMenu(true)

        // Setup RecyclerView
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Observe LiveData
            toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
                sharedViewModel?.checkIfDatabaseEmpty(data)
                adapter.setData(data)
            })
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val recyclerView = recyclerView
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    // Show AlertDialog to confirm removal of all items database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_,_ ->
            toDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully removed everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") {_,_ -> }

        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}