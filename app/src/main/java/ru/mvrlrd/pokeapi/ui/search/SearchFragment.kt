package ru.mvrlrd.pokeapi.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.api.load
import kotlinx.coroutines.launch
import ru.mvrlrd.pokeapi.MyApplication
import ru.mvrlrd.pokeapi.databinding.FragmentSearchBinding


const val REQUEST_KEY = "requestKey"
const val QUERY_KEY = "queryKey"
const val TAG = "SearchFragment"

class SearchFragment : Fragment() {
    private lateinit  var searchViewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val searchDialogFragment = SearchDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel =
            (activity?.applicationContext as MyApplication).appComponent.injectSearchVM()
    }

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchActionButton.setOnClickListener {
            parentFragmentManager.let {
                searchDialogFragment.show(it, "SearchingDialogFragment")
            }
        }
        setFragmentResultListener(REQUEST_KEY)
        { key, bundle ->
            val query = bundle.getString(QUERY_KEY)
            searchViewModel.getPokemonByNameOrId(query!!)
        }

        searchViewModel.pokemon.observe(viewLifecycleOwner, Observer {
            binding.nameText.text = it.name
            binding.pokemonWeightText.text = it.getWeight()
            binding.pokemonHeightText.text = it.getHeight()
            binding.pokemonImage.load(it.url)

            //save to favs
            searchViewModel.savePokemon(it)

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}