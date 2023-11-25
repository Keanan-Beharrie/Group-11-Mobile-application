package com.example.protecthemapp.fragments

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import com.example.protecthemapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ServicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        // Find the download button for the first PDF and set its click listener
        val downloadButton1 = view.findViewById<View>(R.id.download1)
        downloadButton1.setOnClickListener {
            downloadPdf1()
        }
        val downloadButton2 = view.findViewById<View>(R.id.download2)
        downloadButton2.setOnClickListener {
            downloadPdf2()
        }
        val downloadButton3 = view.findViewById<View>(R.id.download3)
        downloadButton3.setOnClickListener {
            downloadPdf3()
        }
        val downloadButton4 = view.findViewById<View>(R.id.download4)
        downloadButton4.setOnClickListener {
            downloadPdf4()
        }
        val downloadButton5 = view.findViewById<View>(R.id.download5)
        downloadButton5.setOnClickListener {
            downloadPdf5()
        }
        val downloadButton6 = view.findViewById<View>(R.id.download6)
        downloadButton6.setOnClickListener {
            downloadPdf6()
        }
        val downloadButton7 = view.findViewById<View>(R.id.download7)
        downloadButton7.setOnClickListener {
            downloadPdf7()
        }
        val downloadButton8 = view.findViewById<View>(R.id.download8)
        downloadButton8.setOnClickListener {
            downloadPdf8()
        }

        // You can repeat the above for other download buttons

        return view
    }

    private fun copyFileToSharedStorage(pdfFileName: String): Uri? {
        val assetManager = requireContext().assets
        val outputDir = File(requireContext().filesDir, "YourApp")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, pdfFileName)
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        return try {
            inputStream = assetManager.open(pdfFileName)
            outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            FileProvider.getUriForFile(requireContext(), "com.example.protecthemapp.provider", outputFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    private fun downloadPdf1() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Abortion-Care-and-Services-Guide.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf2() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "GBV-A-Survivors-Guide.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf3() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Sexual-Violence-against-Men-and-Boys.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf4() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Sexual-Consent.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf5() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Sexual-Harassment.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf6() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Grooming.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf7() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Sexual-Coercion.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun downloadPdf8() {
        // Define the file name for the PDF file you want to download
        val pdfFileName = "Staying-Safe.pdf"

        val pdfFileUri = copyFileToSharedStorage(pdfFileName)

        if (pdfFileUri != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is available
                Toast.makeText(requireContext(), "No PDF viewer app is available.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the file couldn't be copied
            Toast.makeText(requireContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
}







