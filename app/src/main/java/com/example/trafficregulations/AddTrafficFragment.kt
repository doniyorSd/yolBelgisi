package com.example.trafficregulations

import android.Manifest
import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.trafficregulations.adapters.SpinnerAdapter
import com.example.trafficregulations.databinding.FragmentAddTrafficBinding
import com.example.trafficregulations.databinding.MyDialogBinding
import com.example.trafficregulations.db.MyDbHelper
import com.example.trafficregulations.models.User
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTrafficFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTrafficFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var currentPhotoPath: String
    private var absolutePath: String = ""
    lateinit var name: String
    lateinit var about: String
    lateinit var mark: String
    var c = 0
    lateinit var bind: FragmentAddTrafficBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var photoURI: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myDbHelper = MyDbHelper(requireContext())
        denyPeremission()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_traffic, container, false)
        bind = FragmentAddTrafficBinding.bind(view)
        bind.back.setOnClickListener {
            findNavController().popBackStack()
        }

        val listSpinner = arrayListOf(
            "Ogohlantiruvchi belgilar",
            "Imtiyozli belgilar",
            "Taqiqlovchi belgilar",
            "Buyuruvchi belgilar",
            "Axborot-ishora belgilar",
            "Servis belgilar",
            "Qoshimcha axborot belgilar"
        )
        bind.spinner.adapter = SpinnerAdapter(listSpinner)

        bind.imgBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val inflate = layoutInflater.inflate(R.layout.my_dialog, null, false)
            dialog.setView(inflate)
            val binding = MyDialogBinding.bind(inflate)
            dialog.show()

            binding.camera.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.resolveActivity(requireActivity().packageManager)
                    val imageFile = createImageFile()

                    imageFile.also {
                        photoURI = FileProvider.getUriForFile(
                            requireContext(),
                            BuildConfig.APPLICATION_ID,
                            it
                        )
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                        startActivityForResult(intent,1)
                    }
//                    getTakeImage.launch(photoURI)
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Kameradan foydalanishga ruxsat berilmagan!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.gallery.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getImageFromGallery()
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "fayldan malumot oqib olishga ruxsat berilmagan!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        bind.save.setOnClickListener {
            name = bind.name.text.toString()
            about = bind.about.text.toString()
            mark = bind.spinner.selectedItem.toString()
            println(absolutePath.isNotEmpty())
            if (name.trim().isNotEmpty() && about.trim()
                    .isNotEmpty() && absolutePath.isNotEmpty()
            ) {
                val allUser = myDbHelper.getAllUser(mark)
                var isNot = true
                allUser.forEach {
                    if (name == it.name) {
                        isNot = false
                        println("22")
                    }
                }
                if (isNot) {
                    myDbHelper.insert(User(name, about, mark, absolutePath, 0))
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Siz kiritgan ogohlantiruvchi belgi qo'shilgan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(requireContext(), "Malumot to'liq kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    private val getTakeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            bind.imgBtn.setImageURI(photoURI)
            val openInputStream = requireContext().contentResolver?.openInputStream(photoURI)
            val file = File(requireContext().filesDir, "${it.hashCode()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            absolutePath = file.absolutePath
        }
    }

    private fun denyPeremission() {
        askPermission(
            permission.CAMERA,
            permission.READ_EXTERNAL_STORAGE,
            permission.WRITE_EXTERNAL_STORAGE
        ) {

        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(requireContext())
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show()
            }
            if (e.hasForeverDenied()) {
                e.goToSettings();
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun getImageFromGallery() {
        getImageContent.launch("image/*")
    }

    private var getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        bind.imgBtn.setImageURI(it)
        val openInputStream = requireContext().contentResolver?.openInputStream(it)
        val file = File(requireContext().filesDir, "${it.hashCode()}.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        openInputStream?.close()
        fileOutputStream.close()
        absolutePath = file.absolutePath
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddTrafficFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTrafficFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("$requestCode $resultCode")
        if (::currentPhotoPath.isInitialized && requestCode == 1) {
            bind.imgBtn.setImageURI(Uri.fromFile(File(currentPhotoPath)))
            absolutePath = currentPhotoPath
        }
    }
}